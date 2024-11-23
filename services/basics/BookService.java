package org.example.services.basics;

import org.example.dtos.BookDTO;
import org.example.daos.interfaces.BookDAO;
import org.example.models.UserEntity;
import org.example.models.AuthorEntity;
import org.example.models.BookEntity;
import org.example.models.CategoryEntity;
import org.example.models.LogEntity;
import org.example.models.PublisherEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class BookService {
    private final BookDAO bookDAO;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ExecutorService executorService;

    // Constructor
    public BookService(BookDAO bookDAO, AuthorService authorService, PublisherService publisherService,
                       CategoryService categoryService, UserService userService) {
        this.bookDAO = bookDAO;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    // Thêm sách
    public boolean addBook(BookDTO bookDTO) {
        try {
            // Kiểm tra quyền
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.admin &&
                    currentUser.getRole() != UserEntity.Roles.libraryan)) {
                System.out.println("Bạn không có quyền thêm sách.");
                return false;
            }

            // Kiểm tra xem sách đã tồn tại với ISBN chưa
            Future<List<BookDTO>> futureBooks = executorService.submit(() -> findBookDTOByISBN(bookDTO.getIsbn()));
            List<BookDTO> existingBooks = futureBooks.get(); // Chờ kết quả
            if (existingBooks != null && !existingBooks.isEmpty()) {
                System.out.println("Sách này đã tồn tại với ISBN: " + bookDTO.getIsbn());
                return false;
            }

            BookEntity bookEntity = convertToEntity(bookDTO);
            Future<Boolean> future = executorService.submit(() -> bookDAO.addBook(bookEntity));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sách theo ISBN (xóa tất cả sách có ISBN)
    public boolean deleteBookByISBN(String isbn) {
        try {
            // Kiểm tra quyền
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || currentUser.getRole() != UserEntity.Roles.admin) {
                System.out.println("Chỉ admin mới có quyền xóa sách.");
                return false;
            }

            // Tìm tất cả các sách với ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> findBookEntityByISBN(isbn));
            List<BookEntity> bookEntities = futureBooks.get();  // Chờ kết quả

            if (bookEntities.isEmpty()) {
                System.out.println("Sách không tồn tại");
                return false;
            }

            // Xóa tất cả các sách với ISBN
            Future<Boolean> futureDelete = executorService.submit(() -> bookDAO.deleteBookByISBN(isbn));
            return futureDelete.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sách theo ID
    public boolean deleteBookByID(int bookId) {
        try {
            // Kiểm tra quyền
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || currentUser.getRole() != UserEntity.Roles.admin) {
                System.out.println("Chỉ admin mới có quyền xóa sách.");
                return false;
            }

            Future<Boolean> future = executorService.submit(() -> bookDAO.deleteBookByID(bookId));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật sách
    public boolean updateBook(BookDTO bookDTO) {
        try {
            // Kiểm tra quyền
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.admin &&
                    currentUser.getRole() != UserEntity.Roles.libraryan)) {
                System.out.println("Bạn không có quyền cập nhật sách.");
                return false;
            }

            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBookByISBN(bookDTO.getIsbn()));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities.isEmpty()) {
                System.out.println("Không tìm thấy sách với ISBN: " + bookDTO.getIsbn());
                return false;
            }

            BookEntity updatedBook = convertToEntity(bookDTO);
            Future<Boolean> futureUpdate = executorService.submit(() -> bookDAO.updateBook(updatedBook));
            return futureUpdate.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái sách
    public boolean updateBookAvailability(int bookId, boolean available) {
        try {
            // Kiểm tra quyền
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.admin &&
                    currentUser.getRole() != UserEntity.Roles.libraryan)) {
                System.out.println("Bạn không có quyền cập nhật trạng thái sách.");
                return false;
            }

            Future<Boolean> future = executorService.submit(() -> bookDAO.updateBookAvailability(bookId, available));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm sách theo ID (Entity)
    public BookEntity findBookEntityById(int bookId) {
        try {
            Future<BookEntity> future = executorService.submit(() -> bookDAO.findBookById(bookId));
            return future.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm sách theo ISBN (Entity)
    public List<BookEntity> findBookEntityByISBN(String isbn) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBookByISBN(isbn));
            return futureBooks.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm sách theo ISBN (DTO)
    public List<BookDTO> findBookDTOByISBN(String isbn) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBookByISBN(isbn));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            List<BookDTO> bookDTOs = new ArrayList<>();
            for (BookEntity bookEntity : bookEntities) {
                bookDTOs.add(convertToDTO(bookEntity));
            }
            return bookDTOs;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm sách theo tiêu đề
    public List<BookDTO> findBooksByTitle(String title) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBooksByTitle(title));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            if (bookEntities != null) {
                return bookEntities.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Tìm sách theo tác giả
    public List<BookDTO> findBooksByAuthor(String authorName) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBooksByAuthor(authorName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm sách theo thể loại
    public List<BookDTO> findBooksByGenre(String genre) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBooksByGenre(genre));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm sách theo nhà xuất bản
    public List<BookDTO> findBooksByPublisher(String publisherName) {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.findBooksByPublisher(publisherName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy tất cả sách
    public List<BookDTO> getAllBooks() {
        try {
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDAO.getAllBooks());
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tính số lượng sách có sẵn theo ISBN
    public int countAvailableBooksByISBN(String isbn) {
        try {
            Future<Integer> futureCount = executorService.submit(() -> bookDAO.countAvailableBooksByISBN(isbn));
            return futureCount.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Chuyển đổi từ BookEntity sang BookDTO
    private BookDTO convertToDTO(BookEntity book) {
        AuthorEntity author = authorService.findAuthorById(book.getAuthorId());
        PublisherEntity publisher = publisherService.findPublisherById(book.getPublisherId());
        CategoryEntity category = categoryService.findCategoryById(book.getCategoryId());
        return new BookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                author.getName(),
                publisher.getPublisherName(),
                book.getPublicationYear(),
                category.getCategoryName(),
                book.getBookCoverDirectory()
        );
    }

    // Chuyển từ DTO sang Entity
    private BookEntity convertToEntity(BookDTO bookDTO) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn(bookDTO.getIsbn());
        bookEntity.setTitle(bookDTO.getTitle());
        bookEntity.setAuthorId(authorService.findAuthorByName(bookDTO.getAuthorName()).getId());
        bookEntity.setPublisherId(publisherService.findPublisherByName(bookDTO.getPublisherName()).getId());
        bookEntity.setPublicationYear(bookDTO.getPublicationYear());
        bookEntity.setCategoryId(categoryService.findCategoryByName(bookDTO.getCategory()).getId());
        bookEntity.setBookCoverDirectory(bookDTO.getBookCoverDirectory());
        return bookEntity;
    }
}
