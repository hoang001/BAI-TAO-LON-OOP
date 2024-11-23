package org.example.services.basics;

import org.example.dtos.BookDTO;
import org.example.daos.interfaces.LogDAO;
import org.example.models.LogEntity;
import org.example.models.UserEntity.Roles;
import org.example.models.UserEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public class LogService {
    private final LogDAO logDAO;
    private final UserService userService;
    private final ExecutorService executorService; // Thread pool chung

    // Constructor để khởi tạo LogDAO
    public LogService(LogDAO logDAO, UserService userService) {
        this.logDAO = logDAO;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    // Thêm một log mới
    public boolean addLog(LogEntity logEntity) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để thêm log");
            }

            Future<Boolean> future = executorService.submit(() -> logDAO.addLog(logEntity));
            return future.get(5, TimeUnit.SECONDS); // Cung cấp timeout cho get()
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình thêm log: " + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi thêm log: " + e.getMessage());
            return false;
        }
    }

    // Lấy tất cả các log
    public List<LogEntity> getAllLogs() {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy tất cả các log");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền");
            }

            Future<List<LogEntity>> futureLogs = executorService.submit(() -> logDAO.getAllLogs());
            return futureLogs.get(5, TimeUnit.SECONDS); // Cung cấp timeout cho get()
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy tất cả các log: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy tất cả các log: " + e.getMessage());
            return null;
        }
    }

    // Lấy log theo ID
    public LogEntity getLogById(int logId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo ID");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền");
            }

            Future<LogEntity> futureLog = executorService.submit(() -> logDAO.getLogById(logId));
            return futureLog.get(5, TimeUnit.SECONDS); // Cung cấp timeout cho get()
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo ID: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo ID: " + e.getMessage());
            return null;
        }
    }

    // Lấy log theo userName
    public List<LogEntity> getLogsByUserName(String userName) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo userName");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền");
            }

            Future<List<LogEntity>> futureLogs = executorService.submit(() -> logDAO.getLogsByUserName(userName));
            return futureLogs.get(5, TimeUnit.SECONDS); // Cung cấp timeout cho get()
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo userName: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo userName: " + e.getMessage());
            return null;
        }
    }

    // Lấy log theo thời gian
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo thời gian");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền");
            }

            Future<List<LogEntity>> futureLogs = executorService.submit(() -> logDAO.getLogsByTimeRange(startDate, endDate));
            return futureLogs.get(5, TimeUnit.SECONDS); // Cung cấp timeout cho get()
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo thời gian: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo thời gian: " + e.getMessage());
            return null;
        }
    }

    // Đảm bảo shutdown executor khi không còn sử dụng
    public void shutdownExecutor() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
