package org.example.services.basics;

import org.example.daos.interfaces.BorrowedBookDAO;
import org.example.models.BorrowedBookEntity;
import org.example.models.BookEntity;
import org.example.models.UserEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public class BorrowedBookService {
    private final BorrowedBookDAO borrowedBookDAO;
    private final UserService userService;
    private final BookService bookService;
    private final LogService logService;
    private final ExecutorService executorService; // Executor duy nhất cho toàn bộ lớp

    // Constructor
    public BorrowedBookService(BorrowedBookDAO borrowedBookDAO, UserService userService, BookService bookService, LogService logService) {
        this.borrowedBookDAO = borrowedBookDAO;
        this.userService = userService;
        this.bookService = bookService;
        this.logService = logService;
        this.executorService = Executors.newFixedThreadPool(4); // Tạo thread pool với 4 luồng
    }

    // Mượn sách
    public boolean borrowBook(int bookId, LocalDateTime borrowDate) {
        try {
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
            Future<Boolean> futureBorrow = executorService.submit(() -> borrowedBookDAO.borrowBook(bookId, currentUser.getName(), borrowDate));
            boolean result = futureBorrow.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (result) {
                Future<Boolean> futureUpdate = executorService.submit(() -> bookService.updateBookAvailability(bookId, false));
                if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                }
                logService.addLog(new LogEntity(LocalDateTime.now(), currentUser.getName(), "Mượn sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận mượn sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
                "Lỗi khi mượn sách: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    // Trả sách
    public boolean returnBook(int bookId, LocalDateTime returnDate) {
        try {
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi trả sách.");
            }

            // Ghi nhận trả sách
            Future<Boolean> futureReturn = executorService.submit(() -> borrowedBookDAO.returnBook(bookId, currentUser.getName()));
            boolean result = futureReturn.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây

            if (result) {
                // Cập nhật trạng thái sách
                Future<Boolean> futureUpdate = executorService.submit(() -> bookService.updateBookAvailability(bookId, true));
                if (!futureUpdate.get(5, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Không thể cập nhật trạng thái sách.");
                }
                logService.addLog(new LogEntity(LocalDateTime.now(), currentUser.getName(), "Trả sách ID: " + bookId + " thành công"));
            } else {
                throw new IllegalStateException("Không thể ghi nhận trả sách.");
            }
            return result;
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
                "Lỗi khi trả sách: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    // Lấy danh sách sách chưa trả của người dùng
    public List<BorrowedBookEntity> findNotReturnedBooksByUser() {
        try {
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem sách chưa trả.");
            }

            // Tìm danh sách sách chưa trả
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDAO.findNotReturnedBooksByUser(currentUser.getName()));
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
                "Lỗi khi lấy danh sách sách chưa trả: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }

    // Tìm sách đã mượn của người dùng trong khoảng thời gian
    public List<BorrowedBookEntity> findBorrowedBooksByUserAndDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem sách đã mượn.");
            }

            // Tìm danh sách sách mượn trong khoảng thời gian
            Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDAO.findBorrowedBooksByUser(currentUser.getName(), startDate, endDate));
            return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
                "Lỗi khi tìm sách đã mượn: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }

    // Lấy số lượng sách đã mượn của người dùng
    public int getBorrowedBooksCountByUser() {
        try {
            UserEntity currentUser = userService.getLoginUser();
            if (currentUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy số lượng sách đã mượn");
            }

            // Lấy số lượng sách đã mượn
            Future<Integer> futureCount = executorService.submit(() -> borrowedBookDAO.getBorrowedBooksCountByUser(currentUser.getName()));
            return futureCount.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
        } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), 
                userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
                "Lỗi khi lấy số lượng sách đã mượn: " + e.getMessage()));
            System.out.println("Lỗi: " + e.getMessage());
            return 0;
        }
    }
    // Lấy tất cả sách đã mượn của người dùng
public List<BorrowedBookEntity> getAllBorrowedBooksByUser() {
    try {
        UserEntity currentUser = userService.getLoginUser();
        if (currentUser == null) {
            throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy danh sách sách đã mượn.");
        }

        // Thực thi trong một luồng riêng biệt
        Future<List<BorrowedBookEntity>> futureBooks = executorService.submit(() -> borrowedBookDAO.findAllBorrowedBooksByUser(currentUser.getName()));
        return futureBooks.get(5, TimeUnit.SECONDS); // Thêm timeout 5 giây
    } catch (InterruptedException | ExecutionException | IllegalStateException | TimeoutException e) {
        logService.addLog(new LogEntity(LocalDateTime.now(), 
            userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", 
            "Lỗi khi lấy tất cả sách đã mượn: " + e.getMessage()));
        System.out.println("Lỗi: " + e.getMessage());
        return null;
    }
}

}