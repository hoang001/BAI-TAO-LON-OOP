package org.example.services.basics;

import org.example.daos.implementations.LogDaoImpl;
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

    public LogService() {
        this.logDao = new LogDaoImpl();
        this.userService = UserService.getInstance();
        this.executorService = Executors.newFixedThreadPool(4); // Sử dụng thread pool cố định với 4 luồng
    }
    
    /**
     * Lấy tất cả danh sách các log.
     */
    public List<LogEntity> getAllLogs() {
        try {
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) || !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Truy vấn danh sách log bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.findAllLogs();
                } catch (SQLException e) {
                    // Xử lý lỗi cơ sở dữ liệu và ghi log
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
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;

        } catch(SecurityException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * lấy thông tin log theo id.
     * @param logId id của log cần lấy thông tin.
     * @return trả về thông tin của log.
     */
    public LogEntity getLogById(int logId) {
        try {
            if (!userService.getLoginUser().getRole().equals(Roles.ADMIN) || !userService.getLoginUser().getRole().equals(Roles.LIBRARIAN)) {
                throw new SecurityException("Bạn không có quyền");
            }
    
            // Truy vấn log theo ID bằng luồng riêng
            Future<LogEntity> futureLog = executorService.submit(() -> {
                try {
                    return logDao.findLogById(logId);
                } catch (SQLException e) {
                    // Xử lý lỗi cơ sở dữ liệu và ghi log
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
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;

        } catch(SecurityException e) {
            System.out.println("Lỗi" + e.getMessage());
            return null;
        }
    }
    
    /**
     * lấy log theo userName.
     * @param userName tên người dùng lấy log.
     * @return trả về danh sách các log.
     */
    public List<LogEntity> getLogsByUserName(String userName) {
        try {
    
            // Truy vấn log theo tên người dùng bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.findLogsByUserName(userName);
                } catch (SQLException e) {
                    // Xử lý lỗi cơ sở dữ liệu và ghi log
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
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;

        }
    }
    
    /**
     * Lấy danh sách các log trong 1 khoảng thời gian.
     * @param startDate thời gian bắt đầu.
     * @param endDate thời gian kết thúc.
     * @return trả về danh sách các log.
     */
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
    
            // Truy vấn log theo khoảng thời gian bằng luồng riêng
            Future<List<LogEntity>> futureLogs = executorService.submit(() -> {
                try {
                    return logDao.findLogsByTimeRange(startDate, endDate);
                } catch (SQLException e) {
                    // Xử lý lỗi cơ sở dữ liệu và ghi log
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
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;

        }
    }
}    