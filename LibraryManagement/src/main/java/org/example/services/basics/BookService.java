package org.example.services.basics;

import org.example.daos.implementations.BookDaoImpl;
import org.example.daos.interfaces.BookDao;
import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.interfaces.LogDao;
import org.example.models.UserEntity.Roles;
import org.example.models.BookEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    private final LogDao logDao; 

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    public BookService() {
        this.bookDao = new BookDaoImpl();
        this.userService = UserService.getInstance();
        this.logDao = new LogDaoImpl();
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    /**
     * Thêm một cuốn sách mới vào hệ thống và cập nhật trạng thái của sách.
     *
     * @param bookEntity Thông tin cuốn sách cần thêm.
     * @return True nếu thêm sách thành công, ngược lại False.
     */
    public boolean addBook(BookEntity bookEntity) {
        try {
            // Kiểm tra quyền người dùng
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền thêm sách"));
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Kiểm tra xem sách đã tồn tại với ISBN chưa
            if (isBookInDatabase(bookEntity.getIsbn())) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Sách đã tồn tại với ISBN: " + bookEntity.getIsbn()));
                throw new IllegalArgumentException("Sách đã tồn tại với ISBN: " + bookEntity.getIsbn());
            }
    
            // Thêm sách vào cơ sở dữ liệu
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    boolean result = bookDao.addBook(bookEntity);
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Thêm sách thành công: " + bookEntity.getIsbn()));
                    return result;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình thêm sách: " + e.getMessage());
                    return false;
                }
            });
            boolean result = future.get(); // Chờ kết quả
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Sách đã được thêm vào cơ sở dữ liệu"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể thêm sách vào cơ sở dữ liệu"));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi bảo mật: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (IllegalArgumentException e) { 
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
        }
        return false;
    }        

    /**
     * Xóa một cuốn sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách cần xóa.
     * @return True nếu xóa sách thành công, ngược lại False.
     */
    public boolean deleteBookByIsbn(String isbn) {
        try {
            // Kiểm tra quyền người dùng
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền xóa sách"));
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Tìm sách theo ISBN
            Future<BookEntity> futureBooks = executorService.submit(() -> getBookByIsbn(isbn));
            BookEntity bookEntity = futureBooks.get(); // Chờ kết quả
    
            if (bookEntity == null) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Xóa sách thất bại: Không tìm thấy sách với ISBN: " + isbn));
                throw new NoSuchElementException("Sách không tồn tại với ISBN: " + isbn);
            }
    
            // Xóa sách
            Future<Boolean> futureDelete = executorService.submit(() -> {
                try {
                    boolean result = bookDao.deleteBookByIsbn(isbn);
                    if (result) {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Xóa sách thành công với ISBN: " + isbn));
                    } else {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể xóa sách với ISBN: " + isbn));
                    }
                    return result;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình xóa sách: " + e.getMessage());
                    return false;
                }
            });
            boolean result = futureDelete.get(); // Chờ kết quả
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Sách đã được xóa khỏi cơ sở dữ liệu"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể xóa sách với ISBN: " + isbn));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi bảo mật: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (NoSuchElementException e) { 
            System.out.println("Lỗi: " + e.getMessage());
        }  catch (IllegalArgumentException e) { 
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
        }
        return false;
    }       

    /**
     * Xóa một cuốn sách theo ID.
     *
     * @param bookId ID của cuốn sách cần xóa.
     * @return True nếu xóa sách thành công, ngược lại False.
     */
    public boolean deleteBookById(int bookId) {
        try {
            // Kiểm tra quyền người dùng
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền xóa sách"));
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Xóa sách theo ID
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    boolean result = bookDao.deleteBookById(bookId);
                    if (result) {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Xóa sách thành công với ID: " + bookId));
                    } else {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể xóa sách với ID: " + bookId));
                    }
                    return result;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình xóa sách: " + e.getMessage());
                    return false;
                }
            });
            boolean result = future.get(); // Chờ kết quả
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Sách đã được xóa khỏi cơ sở dữ liệu"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể xóa sách với ID: " + bookId));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi bảo mật: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        }  catch (IllegalArgumentException e) { 
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
        }
        return false;
    }
    

    /**
     * Cập nhật thông tin của một cuốn sách.
     *
     * @param book Thông tin cuốn sách cần cập nhật.
     * @return True nếu cập nhật thành công, ngược lại False.
     */
    public boolean updateBook(BookEntity book) {
        try {
            // Kiểm tra quyền người dùng
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền cập nhật sách"));
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Tìm sách theo ISBN
            Future<BookEntity> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(book.getIsbn()));
            BookEntity bookEntity = futureBooks.get(); // Chờ kết quả
    
            if (bookEntity == null) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không tìm thấy sách với ISBN: " + book.getIsbn()));
                throw new NoSuchElementException("Sách không tồn tại với ISBN: " + book.getIsbn());
            }
    
            // Cập nhật thông tin sách
            Future<Boolean> futureUpdate = executorService.submit(() -> {
                try {
                    boolean result = bookDao.updateBook(book);
                    if (result) {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Cập nhật sách thành công với ISBN: " + book.getIsbn()));
                    } else {
                        logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể cập nhật sách với ISBN: " + book.getIsbn()));
                    }
                    return result;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật sách: " + e.getMessage());
                    return false;
                }
            });
            boolean result = futureUpdate.get(); // Chờ kết quả
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Sách đã được cập nhật trong cơ sở dữ liệu"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không thể cập nhật sách với ISBN: " + book.getIsbn()));
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi bảo mật: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        }  catch (IllegalArgumentException e) { 
            System.out.println("Lỗi: " + e.getMessage());
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
        }
        return false;
    }
    
    /**
     * Thêm số lượng sách và cập nhật trạng thái của sách.
     * @param isbn isbn của sách cần thêm.
     * @param quantity số lượng sách thêm.
     * @return trả về trạng thái thêm của sách.
     */
    public boolean addBookQuantity(String isbn, int quantity) {
        try {
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền thêm sách"));
                throw new SecurityException("Bạn không có quyền");
            }
            // Lấy số lượng hiện tại của sách
            int currentQuantity = getBookByIsbn(isbn).getQuantity();
    
            // Tính toán số lượng mới
            int newQuantity = currentQuantity + quantity;
    
            // Cập nhật số lượng sách
            boolean updateQuantityResult = bookDao.updateBookQuantity(isbn, newQuantity);
    
            if (updateQuantityResult) {
                // Lấy ID của sách dựa trên ISBN
                int bookId = getBookByIsbn(isbn).getId();
    
                // Cập nhật trạng thái sách
                boolean available = newQuantity > 0;
                boolean updateAvailabilityResult = bookDao.updateBookAvailability(bookId, available);
                if (updateAvailabilityResult) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Cập nhật số lượng và trạng thái sách thành công với ISBN: " + isbn));
                } else {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Cập nhật trạng thái sách thất bại với ISBN: " + isbn));
                }
                return updateAvailabilityResult;
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Cập nhật số lượng sách thất bại với ISBN: " + isbn));
                return false;
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            System.out.println("Lỗi bảo mật: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi bảo mật: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (IllegalArgumentException e) { 
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật số lượng sách: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi cập nhật số lượng sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
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
    public BookEntity getBookById(int bookId) {
        try {
            // Tìm sách theo ID
            Future<BookEntity> future = executorService.submit(() -> bookDao.findBookById(bookId));
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Đang tìm sách với ID: " + bookId));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            BookEntity bookEntity = future.get();  // Chờ kết quả
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách với ID: " + bookId));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return bookEntity;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return null;
    }
    
    

    /**
     * Tìm một cuốn sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách cần tìm.
     * @return Danh sách các cuốn sách với ISBN đã cho.
     */
    public BookEntity getBookByIsbn(String isbn) {
        try {
            // Tìm sách theo ISBN
            Future<BookEntity> futureBooks = executorService.submit(() -> bookDao.findBookByIsbn(isbn));
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Đang tìm sách với ISBN: " + isbn));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            BookEntity bookEntity = futureBooks.get();  // Chờ kết quả
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách với ISBN: " + isbn));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return bookEntity;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return null;
    }    

    /**
     * Tìm sách theo tên.
     *
     * @param title Tiêu đề của cuốn sách cần tìm.
     * @return Danh sách các cuốn sách với tiêu đề đã cho.
     */
    public List<BookEntity> getBooksByTitle(String title) {
        try {
            // Tìm sách theo tiêu đề
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByTitle(title));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            if (bookEntities != null) {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách với tiêu đề: " + title));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return bookEntities.stream().collect(Collectors.toList());
            } else {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không tìm thấy sách với tiêu đề: " + title));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }
    

    /**
     * Tìm sách theo tên tác giả.
     *
     * @param authorName Tên tác giả của sách cần tìm.
     * @return Danh sách các cuốn sách của tác giả đã cho.
     */
    public List<BookEntity> getBooksByAuthor(String authorName) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi tìm sách.");
            }

            // Thêm ký tự wildcard cho tên tác giả
            String searchAuthorName = "%" + authorName + "%";

            // Tìm sách theo tác giả
            Future<List<BookEntity>> futureBooks = executorService.submit(
                () -> bookDao.findBooksByAuthor(searchAuthorName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities != null && !bookEntities.isEmpty()) {
                try {
                    logDao.addLog(
                        new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(),
                            "Tìm thấy sách của tác giả: " + authorName));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return bookEntities.stream().collect(Collectors.toList());
            } else {
                try {
                    logDao.addLog(
                        new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(),
                            "Không tìm thấy sách của tác giả: " + authorName));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(
                    new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(),
                        "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }


    /**
     * Tìm sách theo thể loại.
     *
     * @param genre Thể loại của sách cần tìm.
     * @return Danh sách các cuốn sách của thể loại đã cho.
     */
    public List<BookEntity> getBooksByGenre(String genre) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi tìm sách.");
            }

            // Thêm ký tự wildcard cho thể loại
            String searchGenre = "%" + genre + "%";

            // Tìm sách theo thể loại
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByGenre(searchGenre));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities != null && !bookEntities.isEmpty()) {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách với thể loại: " + genre));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return bookEntities.stream().collect(Collectors.toList());
            } else {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không tìm thấy sách với thể loại: " + genre));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Tìm sách theo tên nhà xuất bản.
     *
     * @param publisherName Tên nhà xuất bản của sách cần tìm.
     * @return Danh sách các cuốn sách của nhà xuất bản đã cho.
     */
    public List<BookEntity> getBooksByPublisher(String publisherName) {
        try {
            // Thêm ký tự wildcard cho tên nhà xuất bản
            String searchPublisherName = "%" + publisherName + "%";

            // Tìm sách theo nhà xuất bản
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findBooksByPublisher(searchPublisherName));
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả

            if (bookEntities != null && !bookEntities.isEmpty()) {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách của nhà xuất bản: " + publisherName));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return bookEntities.stream().collect(Collectors.toList());
            } else {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không tìm thấy sách của nhà xuất bản: " + publisherName));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }


    /**
     * Lấy tất cả các cuốn sách.
     *
     * @return Danh sách tất cả các cuốn sách.
     */
    public List<BookEntity> getAllBooks() {
        try {
            // Lấy tất cả sách
            Future<List<BookEntity>> futureBooks = executorService.submit(() -> bookDao.findAllBooks());
            List<BookEntity> bookEntities = futureBooks.get(); // Chờ kết quả
            if (bookEntities != null) {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lấy tất cả sách thành công"));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return bookEntities.stream().collect(Collectors.toList());
            } else {
                try {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không tìm thấy sách nào"));
                } catch (SQLException logException) {
                    System.out.println("Lỗi khi ghi log: " + logException.getMessage());
                }
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi lấy sách: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     *
     * @param isbn
     * @return
     */
    public boolean isBookInDatabase(String isbn) {
        try {
            return bookDao.isBookInDatabase(isbn);
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra sách trong cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }
}
