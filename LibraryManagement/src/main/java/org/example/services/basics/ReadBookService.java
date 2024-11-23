package org.example.services.basics;

import org.example.daos.interfaces.ReadBookDao;
import org.example.models.ReadBookEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

/**
 * Lớp ReadBookService chịu trách nhiệm quản lý các chức năng liên quan đến sách đã đọc,
 * bao gồm đánh dấu sách đã đọc, kiểm tra sách đã đọc, lấy danh sách sách đã đọc, và xóa đánh dấu sách đã đọc.
 */
public class ReadBookService {

    // Đối tượng DAO để thao tác với cơ sở dữ liệu liên quan đến sách đã đọc
    private final ReadBookDao readBookDao;

    // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
    private final UserService userService;

    // Dịch vụ ghi log, lưu lại các hoạt động liên quan đến sách đã đọc
    private final LogService logService;

    // ExecutorService để quản lý các luồng xử lý đồng thời
    private final ExecutorService executorService;

    /**
     * Khởi tạo lớp ReadBookService với các phụ thuộc cần thiết.
     * 
     * @param readBookDao Đối tượng ReadBookDao để thao tác với cơ sở dữ liệu.
     * @param userService Đối tượng UserService để xác thực người dùng.
     * @param logService Đối tượng LogService để ghi nhật ký hoạt động.
     */
    public ReadBookService(ReadBookDao readBookDao, UserService userService, LogService logService) {
        this.readBookDao = readBookDao;
        this.userService = userService;
        this.logService = logService;
        this.executorService = Executors.newFixedThreadPool(4); // Sử dụng thread pool cố định với 4 luồng
    }

    /**
     * Đánh dấu sách đã đọc.
     * 
     * @param bookId ID của sách cần đánh dấu.
     * @return True nếu đánh dấu thành công, ngược lại False.
     */
    public boolean markAsRead(int bookId) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi đánh dấu sách đã đọc");
            }

            // Kiểm tra giá trị đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            // Lấy tên người dùng hiện tại
            String userName = userService.getLoginUser().getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            // Thực hiện đánh dấu sách đã đọc bằng luồng riêng
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    return readBookDao.markAsRead(bookId, userName);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình đánh dấu sách đã đọc: " + e.getMessage());
                    return false;
                }
            });
            boolean result = future.get(); // Chờ kết quả từ luồng

            // Ghi log nếu thành công
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

    /**
     * Lấy danh sách sách đã đọc của người dùng hiện tại.
     * 
     * @return Danh sách các đối tượng ReadBookEntity hoặc null nếu có lỗi.
     */
    public List<ReadBookEntity> getReadBooksByUser() {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi lấy danh sách sách đã đọc");
            }

            // Lấy tên người dùng hiện tại
            String userName = userService.getLoginUser().getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            // Truy vấn danh sách sách đã đọc bằng luồng riêng
            Future<List<ReadBookEntity>> future = executorService.submit(() -> {
                try {
                    return readBookDao.getReadBooks(userName);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy danh sách sách đã đọc: " + e.getMessage());
                    return null;
                }
            });
            return future.get(); // Chờ kết quả từ luồng
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return null;
        }
    }

    /**
     * Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa.
     * 
     * @param bookId ID của sách cần kiểm tra.
     * @return True nếu sách đã được đánh dấu là đã đọc, ngược lại False.
     */
    public boolean isBookRead(int bookId) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi kiểm tra sách đã đánh dấu sách đã đọc hay chưa");
            }

            // Kiểm tra giá trị đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            // Lấy tên người dùng hiện tại
            String userName = userService.getLoginUser().getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            // Thực hiện kiểm tra sách đã đọc bằng luồng riêng
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    return readBookDao.isBookRead(bookId, userName);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình kiểm tra sách đã đọc: " + e.getMessage());
                    return false;
                }
            });
            boolean isRead = future.get(); // Chờ kết quả từ luồng

            // Ghi log kết quả kiểm tra
            logService.addLog(new LogEntity(LocalDateTime.now(), userName, "Kiểm tra sách " + bookId + " đã đọc: " + isRead));
            return isRead;
        } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            logService.addLog(new LogEntity(LocalDateTime.now(), "SYSTEM", "Lỗi: " + e.getMessage()));
            return false;
        }
    }

    /**
     * Xóa đánh dấu sách đã đọc.
     * 
     * @param bookId ID của sách cần xóa đánh dấu.
     * @return True nếu xóa thành công, ngược lại False.
     */
    public boolean unmarkAsRead(int bookId) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập trước khi xóa đánh dấu sách đã đọc");
            }

            // Kiểm tra giá trị đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không hợp lệ");
            }

            // Lấy tên người dùng hiện tại
            String userName = userService.getLoginUser().getUserName();
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không hợp lệ");
            }

            // Thực hiện xóa đánh dấu sách đã đọc bằng luồng riêng
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    return readBookDao.unmarkAsRead(bookId, userName);
                } catch (SQLException e) {
                    System.out.println("Lỗi cơ sở dữ liệu trong quá trình xóa đánh dấu sách đã đọc: " + e.getMessage());
                    return false;
                }
            });
            boolean result = future.get(); // Chờ kết quả từ luồng

            // Ghi log nếu thành công
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
