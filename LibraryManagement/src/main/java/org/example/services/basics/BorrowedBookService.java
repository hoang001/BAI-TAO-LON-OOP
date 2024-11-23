package org.example.services.basics;

import org.example.daos.interfaces.BorrowedBookDao;
import org.example.models.BorrowedBookEntity;
import org.example.models.BookEntity;
import org.example.models.UserEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * Lớp BorrowedBookService chịu trách nhiệm quản lý các thao tác liên quan đến sách đã mượn,
 * bao gồm mượn sách, trả sách, tìm sách chưa trả, lấy danh sách sách đã mượn theo khoảng thời gian,
 * và các thao tác khác liên quan đến sách mượn của người dùng.
 */
public class BorrowedBookService {

    // Đối tượng DAO để thao tác với cơ sở dữ liệu sách đã mượn
    private final BorrowedBookDao borrowedBookDao;

    // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
    private final UserService userService;

    // Dịch vụ quản lý sách, được sử dụng để kiểm tra tình trạng sách
    private final BookService bookService;

    // Dịch vụ ghi log, lưu lại các hoạt động liên quan đến sách đã mượn
    private final LogService logService;

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    /**
     * Khởi tạo lớp BorrowedBookService với các phụ thuộc cần thiết.
     *
     * @param borrowedBookDao Đối tượng BorrowedBookDao để thao tác với cơ sở dữ liệu.
     * @param userService     Đối tượng UserService để xác thực người dùng.
     * @param bookService     Đối tượng BookService để kiểm tra tình trạng sách.
     * @param logService      Đối tượng LogService để ghi nhật ký hoạt động.
     */
    public BorrowedBookService(BorrowedBookDao borrowedBookDao, UserService userService, 
                               BookService bookService, LogService logService) {
        this.borrowedBookDao = borrowedBookDao;
        this.userService = userService;
        this.bookService = bookService;
        this.logService = logService;
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    /**
     * Mượn một cuốn sách.
     *
     * @param bookId     ID của sách cần mượn.
     * @param borrowDate Ngày mượn sách.
     * @return True nếu mượn sách thành công, ngược lại False.
     */
    public boolean borrowBook(int bookId, LocalDateTime borrowDate) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi mượn sách.");
            }

            // Kiểm tra xem sách còn bản sao không
            Future<BookEntity> futureBook = executorService.submit(() -> bookService.findBookEntityById(bookId));
            BookEntity book = futureBook.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (book == null || bookService.countAvailableBooksByISBN(book.getIsbn()) == 0) {
                throw new IllegalStateException("Không có sách này để mượn.");
            }

            // Ghi nhận mượn sách
            Future<Boolean> futureBorrow = executorService.submit(() -> borrowedBookDao.borrowBook(bookId, currentUser.getUserName(), borrowDate));
            boolean result = futureBorrow.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (result) {
                // Cập nhật trạng thái sách
                Future<Boolean> futureUpdate = executorService.submit(() -> bookService.updateBookAvailability(bookId, false));
                if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                }
                logService.addLog(new LogEntity(LocalDateTime.now(), currentUser.getUserName(), "Mượn sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận mượn sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi mượn sách: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Trả một cuốn sách.
     *
     * @param bookId     ID của sách cần trả.
     * @param returnDate Ngày trả sách.
     * @return True nếu trả sách thành công, ngược lại False.
     */
    public boolean returnBook(int bookId, LocalDateTime returnDate) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi trả sách.");
            }

            // Ghi nhận trả sách
            Future<Boolean> futureReturn = executorService.submit(() -> borrowedBookDao.returnBook(bookId, currentUser.getUserName()));
            boolean result = futureReturn.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (result) {
                // Cập nhật trạng thái sách
                Future<Boolean> futureUpdate = executorService.submit(() -> bookService.updateBookAvailability(bookId, true));
                if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                }
                logService.addLog(new LogEntity(LocalDateTime.now(), currentUser.getUserName(), "Trả sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận trả sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi trả sách: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy danh sách các sách chưa trả của người dùng.
     *
     * @return Danh sách các đối tượng BorrowedBookEntity hoặc null nếu có lỗi.
     */
    public List<BorrowedBookEntity> findNotReturnedBooksByUser() {
        try {
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem sách chưa trả.");
            }

            // Tìm danh sách sách chưa trả
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDao.findNotReturnedBooksByUser(currentUser.getUserName()));
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi lấy danh sách sách chưa trả: " + e.getMessage()));
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
    public List<BorrowedBookEntity> findBorrowedBooksByUserAndDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem sách đã mượn.");
            }

            // Tìm danh sách sách mượn trong khoảng thời gian
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDao.findBorrowedBooksByUser(currentUser.getUserName(), startDate, endDate));
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi tìm sách đã mượn: " + e.getMessage()));
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
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy số lượng sách đã mượn");
            }

            // Lấy số lượng sách đã mượn
            Future<Integer> futureCount = executorService.submit(() -> borrowedBookDao.getBorrowedBooksCountByUser(currentUser.getUserName()));
            return futureCount.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi lấy số lượng sách đã mượn: " + e.getMessage()));
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
            // Kiểm tra người dùng đã đăng nhập
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy danh sách sách đã mượn.");
            }

            // Thực thi trong một luồng riêng biệt
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDao.findAllBorrowedBooksByUser(currentUser.getUserName()));
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getUserName() : "unknown", 
                "Lỗi khi lấy tất cả sách đã mượn: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }
}
