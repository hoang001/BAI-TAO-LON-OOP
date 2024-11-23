package org.example.services.basics;

import org.example.daos.interfaces.LogDao;
import org.example.models.LogEntity;
import org.example.models.UserEntity.Roles;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * Lớp LogService chịu trách nhiệm quản lý các thao tác liên quan đến nhật ký (log).
 * Bao gồm thêm log, lấy log theo ID, theo người dùng, hoặc theo khoảng thời gian.
 */
public class LogService {

    // Đối tượng DAO để thao tác với cơ sở dữ liệu nhật ký
    private final LogDao logDao;

    // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
    private final UserService userService;

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    /**
     * Khởi tạo lớp LogService với các phụ thuộc cần thiết.
     *
     * @param logDao      Đối tượng LogDao để thao tác với cơ sở dữ liệu.
     * @param userService Đối tượng UserService để xác thực người dùng.
     */
    public LogService(LogDao logDao, UserService userService) {
        this.logDao = logDao;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(4); // Sử dụng thread pool cố định với 4 luồng
    }

    /**
     * Thêm một log mới.
     *
     * @param logEntity Đối tượng LogEntity chứa thông tin nhật ký cần thêm.
     * @return True nếu thêm thành công, ngược lại False.
     */
    public boolean addLog(LogEntity logEntity) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để thêm log");
            }

            // Thực hiện thêm log bằng luồng riêng
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    return logDao.addLog(logEntity);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình thêm log: " + e.getMessage());
                    return false;
                }
            });
            return future.get(5, TimeUnit.SECONDS); // Chờ kết quả từ luồng, giới hạn 5 giây
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình thêm log: " + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi thêm log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy tất cả các log trong hệ thống.
     *
     * @return Danh sách các đối tượng LogEntity hoặc null nếu có lỗi.
     */
    public List<LogEntity> getAllLogs() {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy tất cả các log");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN)) {
                throw new SecurityException("Bạn không có quyền");
            }

            // Truy vấn danh sách log bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.getAllLogs();
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy tất cả log: " + e.getMessage());
                    return null;
                }
            });
            return futureLogs.get(5, TimeUnit.SECONDS); // Chờ kết quả từ luồng, giới hạn 5 giây
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy tất cả các log: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy tất cả các log: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy log theo ID.
     *
     * @param logId ID của log cần lấy.
     * @return Đối tượng LogEntity nếu tìm thấy, ngược lại null.
     */
    public LogEntity getLogById(int logId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo ID");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN)) {
                throw new SecurityException("Bạn không có quyền");
            }

            // Truy vấn log theo ID bằng luồng riêng
            Future<LogEntity> futureLog = executorService.submit(() -> {
                try {
                    return logDao.getLogById(logId);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy log theo ID: " + e.getMessage());
                    return null;
                }
            });
            return futureLog.get(5, TimeUnit.SECONDS); // Chờ kết quả từ luồng, giới hạn 5 giây
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo ID: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy log theo tên người dùng.
     *
     * @param userName Tên người dùng cần lấy log.
     * @return Danh sách các đối tượng LogEntity hoặc null nếu có lỗi.
     */
    public List<LogEntity> getLogsByUserName(String userName) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo userName");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN)) {
                throw new SecurityException("Bạn không có quyền");
            }

            // Truy vấn log theo tên người dùng bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.getLogsByUserName(userName);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy log theo userName: " + e.getMessage());
                    return null;
                }
            });
            return futureLogs.get(5, TimeUnit.SECONDS); // Chờ kết quả từ luồng, giới hạn 5 giây
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo userName: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo userName: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy log theo khoảng thời gian.
     *
     * @param startDate Thời gian bắt đầu.
     * @param endDate   Thời gian kết thúc.
     * @return Danh sách các đối tượng LogEntity hoặc null nếu có lỗi.
     */
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để lấy log theo thời gian");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN)) {
                throw new SecurityException("Bạn không có quyền");
            }

            // Truy vấn log theo khoảng thời gian bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.getLogsByTimeRange(startDate, endDate);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy log theo thời gian: " + e.getMessage());
                    return null;
                }
            });
            return futureLogs.get(5, TimeUnit.SECONDS); // Chờ kết quả từ luồng, giới hạn 5 giây
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi trong quá trình lấy log theo thời gian: " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("Thời gian chờ quá lâu khi lấy log theo thời gian: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tắt ExecutorService khi không còn cần thiết để giải phóng tài nguyên.
     */
    public void shutdownExecutor() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
