package org.example.services.basics;

import org.example.daos.interfaces.ReadBookDAO;
import org.example.models.ReadBookEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class ReadBookService {
    private final ReadBookDAO readBookDAO;
    private final UserService userService;
    private final LogService logService;
    private final ExecutorService executorService;

    public ReadBookService(ReadBookDAO readBookDAO, UserService userService, LogService logService) {
        this.readBookDAO = readBookDAO;
        this.userService = userService;
        this.logService = logService;
        this.executorService = Executors.newFixedThreadPool(4); // Thread pool chung
    }

    // Đánh dấu sách đã đọc
    public boolean markAsRead(int bookId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi đánh dấu sách đã đọc");
            }

            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            String userName = userService.getLoginUser().getName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            Future<Boolean> future = executorService.submit(() -> readBookDAO.markAsRead(bookId, userName));
            boolean result = future.get(); // Chờ kết quả

            if (result) {
                logService.addLog(new LogEntity(LocalDateTime.now(), userName, "Đánh dấu sách " + bookId + " đã đọc"));
            }
            return result;
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return false;
        }
    }

    // Lấy danh sách sách đã đọc
    public List<ReadBookEntity> getReadBooksByUser() {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi lấy danh sách sách đã đọc");
            }

            String userName = userService.getLoginUser().getName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            Future<List<ReadBookEntity>> future = executorService.submit(() -> readBookDAO.getReadBooks(userName));
            return future.get(); // Chờ kết quả
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return null;
        }
    }

    // Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa
    public boolean isBookRead(int bookId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi kiểm tra sách đã đánh dấu sách đã đọc hay chưa");
            }

            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            String userName = userService.getLoginUser().getName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            Future<Boolean> future = executorService.submit(() -> readBookDAO.isBookRead(bookId, userName));
            boolean isRead = future.get(); // Chờ kết quả

            logService.addLog(new LogEntity(LocalDateTime.now(), userName, "Kiểm tra sách " + bookId + " đã đọc: " + isRead));
            return isRead;
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return false;
        }
    }

    // Xóa đánh dấu sách đã đọc
    public boolean unmarkAsRead(int bookId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi xóa đánh dấu sách đã đọc");
            }

            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            String userName = userService.getLoginUser().getName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            Future<Boolean> future = executorService.submit(() -> readBookDAO.unmarkAsRead(bookId, userName));
            boolean result = future.get(); // Chờ kết quả

            if (result) {
                logService.addLog(new LogEntity(LocalDateTime.now(), userName, "Xóa đánh dấu sách " + bookId + " đã đọc"));
            }
            return result;
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return false;
        }
    }
}
