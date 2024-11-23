package org.example.services.basics;

import org.example.dtos.BookDTO;
import org.example.daos.interfaces.BookDao;
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

/**
 * Lớp BookService chịu trách nhiệm quản lý các chức năng liên quan đến sách.
 * Các chức năng bao gồm thêm, xóa, cập nhật, tìm kiếm và lấy thông tin sách.
 */
public class BookService {
    
    // Đối tượng DAO để thao tác với cơ sở dữ liệu sách
    private final BookDao bookDao;
    
    // Dịch vụ quản lý tác giả, nhà xuất bản, thể loại, và người dùng
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;
    private final UserService userService;

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    /**
     * Khởi tạo lớp BookService với các phụ thuộc cần thiết.
     *
     * @param bookDao        Đối tượng BookDao để thao tác với cơ sở dữ liệu sách.
     * @param authorService  Dịch vụ quản lý tác giả.
     * @param publisherService Dịch vụ quản lý nhà xuất bản.
     * @param categoryService Dịch vụ quản lý thể loại.
     * @param userService    Dịch vụ quản lý người dùng.
     */
    public BookService(BookDao bookDao, AuthorService authorService, PublisherService publisherService,
                       CategoryService categoryService, UserService userService) {
        this.bookDao = bookDao;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    /**
     * Thêm một cuốn sách mới vào hệ thống.
     *
     * @param bookDTO Thông tin cuốn sách cần thêm.
     * @return True nếu thêm sách thành công, ngược lại False.
     */
    public boolean addBook(BookDTO bookDTO) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.ADMIN &&
                    currentUser.getRole() != UserEntity.Roles.LIBRARYAN)) {
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

            // Chuyển đổi từ DTO sang Entity và thêm sách vào cơ sở dữ liệu
            BookEntity bookEntity = convertToEntity(bookDTO);
            Future<Boolean> future = executorService.submit(() -> bookDao.addBook(bookEntity));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một cuốn sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách cần xóa.
     * @return True nếu xóa sách thành công, ngược lại False.
     */
    public boolean deleteBookByISBN(String isbn) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || currentUser.getRole() != UserEntity.Roles.ADMIN) {
                System.out.println("Chỉ ADMIN mới có quyền xóa sách.");
                return false;
            }

            // Tìm sách theo ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> findBookEntityByISBN(isbn));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities.isEmpty()) {
                System.out.println("Sách không tồn tại");
                return false;
            }

            // Xóa sách
            Future<Boolean> futureDelete = executorService.submit(() -> bookDao.deleteBookByIsbn(isbn));
            return futureDelete.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một cuốn sách theo ID.
     *
     * @param bookId ID của cuốn sách cần xóa.
     * @return True nếu xóa sách thành công, ngược lại False.
     */
    public boolean deleteBookByID(int bookId) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || currentUser.getRole() != UserEntity.Roles.ADMIN) {
                System.out.println("Chỉ ADMIN mới có quyền xóa sách.");
                return false;
            }

            // Xóa sách theo ID
            Future<Boolean> future = executorService.submit(() -> bookDao.deleteBookById(bookId));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin của một cuốn sách.
     *
     * @param bookDTO Thông tin cuốn sách cần cập nhật.
     * @return True nếu cập nhật thành công, ngược lại False.
     */
    public boolean updateBook(BookDTO bookDTO) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.ADMIN &&
                    currentUser.getRole() != UserEntity.Roles.LIBRARYAN)) {
                System.out.println("Bạn không có quyền cập nhật sách.");
                return false;
            }

            // Tìm sách theo ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(bookDTO.getIsbn()));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities.isEmpty()) {
                System.out.println("Không tìm thấy sách với ISBN: " + bookDTO.getIsbn());
                return false;
            }

            // Cập nhật thông tin sách
            BookEntity updatedBook = convertToEntity(bookDTO);
            Future<Boolean> futureUpdate = executorService.submit(() -> bookDao.updateBook(updatedBook));
            return futureUpdate.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái có sẵn của một cuốn sách.
     *
     * @param bookId   ID của cuốn sách cần cập nhật.
     * @param available Trạng thái có sẵn của cuốn sách.
     * @return True nếu cập nhật thành công, ngược lại False.
     */
    public boolean updateBookAvailability(int bookId, boolean available) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.ADMIN &&
                    currentUser.getRole() != UserEntity.Roles.LIBRARYAN)) {
                System.out.println("Bạn không có quyền cập nhật trạng thái sách.");
                return false;
            }

            // Cập nhật trạng thái sách
            Future<Boolean> future = executorService.submit(() -> bookDao.updateBookAvailability(bookId, available));
            return future.get(); // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm một cuốn sách theo ID.
     *
     * @param bookId ID của cuốn sách cần tìm.
     * @return Đối tượng BookEntity nếu tìm thấy, ngược lại null.
     */
    public BookEntity findBookEntityById(int bookId) {
        try {
            // Tìm sách theo ID
            Future<BookEntity> future = executorService.submit(() -> bookDao.findBookById(bookId));
            return future.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tìm một cuốn sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách cần tìm.
     * @return Danh sách các cuốn sách với ISBN đã cho.
     */
    public List<BookEntity> findBookEntityByISBN(String isbn) {
        try {
            // Tìm sách theo ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(isbn));
            return futureBooks.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tìm một cuốn sách theo ISBN và trả về danh sách BookDTO.
     *
     * @param isbn ISBN của cuốn sách cần tìm.
     * @return Danh sách BookDTO của sách có ISBN đã cho.
     */
    public List<BookDTO> findBookDTOByISBN(String isbn) {
        try {
            // Tìm sách theo ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(isbn));
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

    /**
     * Tìm sách theo tên.
     *
     * @param title Tiêu đề của cuốn sách cần tìm.
     * @return Danh sách các cuốn sách với tiêu đề đã cho.
     */
    public List<BookDTO> findBooksByTitle(String title) {
        try {
            // Tìm sách theo tiêu đề
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByTitle(title));
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

    /**
     * Tìm sách theo tên tác giả.
     *
     * @param authorName Tên tác giả của sách cần tìm.
     * @return Danh sách các cuốn sách của tác giả đã cho.
     */
    public List<BookDTO> findBooksByAuthor(String authorName) {
        try {
            // Tìm sách theo tác giả
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByAuthor(authorName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tìm sách theo thể loại.
     *
     * @param genre Thể loại của sách cần tìm.
     * @return Danh sách các cuốn sách của thể loại đã cho.
     */
    public List<BookDTO> findBooksByGenre(String genre) {
        try {
            // Tìm sách theo thể loại
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByGenre(genre));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tìm sách theo tên nhà xuất bản.
     *
     * @param publisherName Tên nhà xuất bản của sách cần tìm.
     * @return Danh sách các cuốn sách của nhà xuất bản đã cho.
     */
    public List<BookDTO> findBooksByPublisher(String publisherName) {
        try {
            // Tìm sách theo nhà xuất bản
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByPublisher(publisherName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy tất cả các cuốn sách.
     *
     * @return Danh sách tất cả các cuốn sách.
     */
    public List<BookDTO> getAllBooks() {
        try {
            // Lấy tất cả sách
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.getAllBooks());
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Đếm số lượng sách có sẵn theo ISBN.
     *
     * @param isbn ISBN của sách cần kiểm tra.
     * @return Số lượng sách có sẵn với ISBN đã cho.
     */
    public int countAvailableBooksByISBN(String isbn) {
        try {
            // Đếm sách có sẵn theo ISBN
            Future<Integer> futureCount = executorService.submit(() -> bookDao.countAvailableBooksByIsbn(isbn));
            return futureCount.get();  // Chờ kết quả
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Chuyển đối tượng BookEntity thành BookDTO.
     *
     * @param book Đối tượng BookEntity cần chuyển đổi.
     * @return Đối tượng BookDTO tương ứng.
     */
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

    /**
     * Chuyển đối tượng BookDTO thành BookEntity.
     *
     * @param bookDTO Đối tượng BookDTO cần chuyển đổi.
     * @return Đối tượng BookEntity tương ứng.
     */
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
