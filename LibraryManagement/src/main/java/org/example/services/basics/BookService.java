package org.example.services.basics;

import org.example.daos.interfaces.BookDao;
import org.example.models.UserEntity;
import org.example.models.BookEntity;
import org.example.models.LogEntity;

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
    public BookService(BookDao bookDao, UserService userService) {
        this.bookDao = bookDao;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    /**
     * Thêm một cuốn sách mới vào hệ thống.
     *
     * @param BookEntity Thông tin cuốn sách cần thêm.
     * @return True nếu thêm sách thành công, ngược lại False.
     */
    public boolean addBook(BookEntity bookEntity) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.ADMIN &&
                    currentUser.getRole() != UserEntity.Roles.LIBRARYAN)) {
                System.out.println("Bạn không có quyền thêm sách.");
                return false;
            }

            // Kiểm tra xem sách đã tồn tại với ISBN chưa
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> findBookByISBN(bookEntity.getIsbn()));
            List<BookEntity> existingBooks = futureBooks.get(); // Chờ kết quả
            if (existingBooks != null && !existingBooks.isEmpty()) {
                System.out.println("Sách này đã tồn tại với ISBN: " + bookEntity.getIsbn());
                return false;
            }

            // Chuyển đổi từ DTO sang Entity và thêm sách vào cơ sở dữ liệu
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
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> findBookByISBN(isbn));
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
     * @param BookEntity Thông tin cuốn sách cần cập nhật.
     * @return True nếu cập nhật thành công, ngược lại False.
     */
    public boolean updateBook(BookEntity bookEntity) {
        try {
            // Kiểm tra quyền người dùng
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null || (currentUser.getRole() != UserEntity.Roles.ADMIN &&
                    currentUser.getRole() != UserEntity.Roles.LIBRARYAN)) {
                System.out.println("Bạn không có quyền cập nhật sách.");
                return false;
            }

            // Tìm sách theo ISBN
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(bookEntity.getIsbn()));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities.isEmpty()) {
                System.out.println("Không tìm thấy sách với ISBN: " + bookEntity.getIsbn());
                return false;
            }

            // Cập nhật thông tin sách
            Future<Boolean> futureUpdate = executorService.submit(() -> bookDao.updateBook(bookEntity));
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
    public List<BookEntity> findBookByISBN(String isbn) {
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
     * Tìm sách theo tên.
     *
     * @param title Tiêu đề của cuốn sách cần tìm.
     * @return Danh sách các cuốn sách với tiêu đề đã cho.
     */
    public List<BookEntity> findBooksByTitle(String title) {
        try {
            // Tìm sách theo tiêu đề
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByTitle(title));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            if (bookEntities != null) {
                return bookEntities.stream()

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
    public List<BookEntity> findBooksByAuthor(String authorName) {
        try {
            // Tìm sách theo tác giả
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByAuthor(authorName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
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
    public List<BookEntity> findBooksByGenre(String genre) {
        try {
            // Tìm sách theo thể loại
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByGenre(genre));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
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
    public List<BookEntity> findBooksByPublisher(String publisherName) {
        try {
            // Tìm sách theo nhà xuất bản
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByPublisher(publisherName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
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
    public List<BookEntity> getAllBooks() {
        try {
            // Lấy tất cả sách
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.getAllBooks());
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            return bookEntities.stream()
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
}
