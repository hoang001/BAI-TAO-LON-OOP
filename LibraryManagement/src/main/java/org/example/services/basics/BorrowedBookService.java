package org.example.services.basics;

import org.example.daos.implementations.BorrowedBookDaoImpl;
import org.example.daos.interfaces.BorrowedBookDao;
import org.example.daos.implementations.BookDaoImpl;
import org.example.daos.interfaces.BookDao;
import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.interfaces.LogDao;
import org.example.models.BorrowedBookEntity;
import org.example.models.BookEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import org.example.models.UserEntity.Roles;

/**
 * Lớp BorrowedbookDao chịu trách nhiệm quản lý các thao tác liên quan đến sách đã mượn,
 * bao gồm mượn sách, trả sách, tìm sách chưa trả, lấy danh sách sách đã mượn theo khoảng thời gian,
 * và các thao tác khác liên quan đến sách mượn của người dùng.
 */
public class BorrowedBookService {

    // Đối tượng DAO để thao tác với cơ sở dữ liệu sách đã mượn
    private final BorrowedBookDao borrowedBookDao;

    // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
    private final UserService userService;

    // Dịch vụ quản lý sách, được sử dụng để kiểm tra tình trạng sách
    private final BookDao bookDao;

    // Dịch vụ ghi log, lưu lại các hoạt động liên quan đến sách đã mượn
    private final LogDao logDao;

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    public BorrowedBookService() {
        this.borrowedBookDao = new BorrowedBookDaoImpl();
        this.userService = UserService.getInstance();
        this.bookDao = new BookDaoImpl();
        this.logDao = new LogDaoImpl();
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    /**
     * Mượn một cuốn sách và cập nhật trạng thái của sách.
     *
     * @param bookId     ID của sách cần mượn.
     * @param borrowDate Ngày mượn sách.
     * @return True nếu mượn sách thành công, ngược lại False.
     */
    public boolean borrowBook(int bookId, LocalDate borrowDate, LocalDate returnDate) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi mượn sách");
            }

            // Kiểm tra xem người dùng có sách quá hạn không
            Future<List<BorrowedBookEntity>> futureOverdueBooks = executorService.submit(() -> borrowedBookDao.findOverdueBooksByUser(userService.getLoginUser().getUserName(), LocalDate.now()));
            List<BorrowedBookEntity> overdueBooks = futureOverdueBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (!overdueBooks.isEmpty()) {
                throw new IllegalStateException("Bạn có sách quá hạn. Vui lòng trả sách trước khi mượn sách mới.");
            }

            // Kiểm tra xem sách còn bản sao không
            Future<BookEntity> futureBook = executorService.submit(() -> bookDao.findBookById(bookId));
            BookEntity book = futureBook.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (book == null || !book.isAvailable()) {
                throw new IllegalStateException("Không có sách này để mượn.");
            }

            // Lấy số lượng sách đã mượn mà chưa trả
            int borrowedCount = borrowedBookDao.findBorrowedBooksCountByIsbn(book.getIsbn());

            // Ghi nhận mượn sách
            Future<Boolean> futureBorrow = executorService.submit(() -> borrowedBookDao.borrowBook(bookId, userService.getLoginUser().getUserName(), borrowDate, returnDate));
            boolean result = futureBorrow.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (result) {
                // Cập nhật trạng thái sách nếu chỉ còn 1 quyển
                if (book.getQuantity() - borrowedCount == 1) {
                    Future<Boolean> futureUpdate = executorService.submit(() -> bookDao.updateBookAvailability(bookId, false));
                    if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                    }
                }
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Mượn sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận mượn sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            return false;
        }
    }

    /**
     * Trả một cuốn sách và cập nhật trạng thái của sách.
     *
     * @param bookId     ID của sách cần trả.
     * @param returnDate Ngày trả sách.
     * @return True nếu trả sách thành công, ngược lại False.
     */
    public boolean returnBook(int bookId, LocalDate returnDate) {
        try {
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) && !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Không có quyền thêm sách"));
                throw new SecurityException("Bạn không có quyền");
            }
            // Ghi nhận trả sách
            Future<Boolean> futureReturn = executorService.submit(() -> borrowedBookDao.returnBook(bookId, userService.getLoginUser().getUserName()));
            boolean result = futureReturn.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
    
            if (result) {
                // Cập nhật trạng thái sách luôn là available
                Future<Boolean> futureUpdate = executorService.submit(() -> bookDao.updateBookAvailability(bookId, true));
                if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                }
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Trả sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận trả sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
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
        } catch (SQLException logException) {
            System.out.println("Lỗi khi ghi log: " + logException.getMessage());
        }
        return false;
    }    
    

    /**
     * Lấy danh sách các sách chưa trả của người dùng.
     *
     * @return Danh sách các đối tượng BorrowedBookEntity hoặc null nếu có lỗi.
     */
    public List<BorrowedBookEntity> getNotReturnedBooksByUser() {
        try {
            // Tìm danh sách sách chưa trả
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> {
                try {
                    List<BorrowedBookEntity> books = borrowedBookDao.findNotReturnedBooksByUser(userService.getLoginUser().getUserName());
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách chưa trả thành công"));
                    return books;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình tìm sách chưa trả: " + e.getMessage());
                    return null;
                }
            });
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return null;
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }    

    /**
     * Lấy danh sách các sách đã mượn trong một khoảng thời gian.
     *
     * @param startDate Ngày bắt đầu.
     * @param endDate   Ngày kết thúc.
     * @return Danh sách các đối tượng BorrowedBookEntity hoặc null nếu có lỗi.
     */
    public List<BorrowedBookEntity> getBorrowedBooksByUserAndDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            // Tìm danh sách sách mượn trong khoảng thời gian
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> {
                try {
                    List<BorrowedBookEntity> books = borrowedBookDao.findBorrowedBooksByUser(userService.getLoginUser().getUserName(), startDate, endDate);
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm thấy sách mượn trong khoảng thời gian thành công"));
                    return books;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình tìm sách mượn: " + e.getMessage());
                    return null;
                }
            });
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return null;
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }    
    

    /**
     * Lấy số lượng sách đã mượn của người dùng.
     *
     * @return Số lượng sách đã mượn.
     */
    public int getBorrowedBooksCountByUser() {
        try {
            // Lấy số lượng sách đã mượn
            Future<Integer> futureCount = executorService.submit(() -> {
                try {
                    int count = borrowedBookDao.findBorrowedBooksCountByUser(userService.getLoginUser().getUserName());
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lấy số lượng sách đã mượn thành công"));
                    return count;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy số lượng sách đã mượn: " + e.getMessage());
                    return 0;
                }
            });
            return futureCount.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return 0;
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return 0;
        }
    }    

    /**
     * Lấy tất cả các sách đã mượn của người dùng.
     *
     * @return Danh sách các đối tượng BorrowedBookEntity hoặc null nếu có lỗi.
     */
    public List<BorrowedBookEntity> getAllBorrowedBooksByUser() {
        try {
            // Thực thi trong một luồng riêng biệt
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> {
                try {
                    List<BorrowedBookEntity> books = borrowedBookDao.findAllBorrowedBooksByUser(userService.getLoginUser().getUserName());
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lấy danh sách sách đã mượn thành công"));
                    return books;
                } catch (SQLException e) {
                    logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy danh sách sách đã mượn: " + e.getMessage());
                    return null;
                }
            });
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException |  TimeoutException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return null;
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;

        }
    }
}
