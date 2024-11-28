package org.example.services.basics;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.implementations.ReadBookDaoImpl;
import org.example.daos.interfaces.LogDao;
import org.example.daos.interfaces.ReadBookDao;
import org.example.models.LogEntity;
import org.example.models.ReadBookEntity;

/**
 * Lớp ReadBookService chịu trách nhiệm quản lý các chức năng liên quan đến sách đã đọc, bao gồm
 * đánh dấu sách đã đọc, kiểm tra sách đã đọc, lấy danh sách sách đã đọc, và xóa đánh dấu sách đã
 * đọc.
 */
public class ReadBookService {

  // Đối tượng DAO để thao tác với cơ sở dữ liệu liên quan đến sách đã đọc
  private final ReadBookDao readBookDao;

  // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
  private final UserService userService;

  // Dịch vụ ghi log, lưu lại các hoạt động liên quan đến sách đã đọc
  private final LogDao logDao;

  // ExecutorService để quản lý các luồng xử lý đồng thời
  private final ExecutorService executorService;

  /**
   * Constructor của lớp ReadBookService.
   * Khởi tạo các đối tượng DAO và dịch vụ cần thiết.
   */
  public ReadBookService() {
    this.readBookDao = new ReadBookDaoImpl();
    this.userService = UserService.getInstance();
    this.logDao = new LogDaoImpl();
    this.executorService = Executors.newFixedThreadPool(4);
  }

  /**
   * Đánh dấu sách đã đọc.
   *
   * @param bookId ID của sách cần đánh dấu.
   * @return True nếu đánh dấu thành công, ngược lại False.
   */
  public boolean markAsRead(int bookId) {
    try {

      // Kiểm tra giá trị đầu vào
      if (bookId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: ID sách không hợp lệ"));
        throw new IllegalArgumentException("ID sách không được để trống và phải là số dương");
      }

      // Thực hiện đánh dấu sách đã đọc bằng luồng riêng
      Future<Boolean> future =
          executorService.submit(
              () -> {
                try {
                  return readBookDao.markAsRead(bookId, userService.getLoginUser().getUserName());
                } catch (SQLException e) {
                  logDao.addLog(
                      new LogEntity(
                          LocalDateTime.now(),
                          userService.getLoginUser().getUserName(),
                          "Lỗi cơ sở dữ liệu: " + e.getMessage()));
                  System.out.println(
                      "Lỗi cơ sở dữ liệu trong quá trình đánh dấu sách đã đọc: " + e.getMessage());
                  return false;
                }
              });
      boolean result = future.get();

      // Ghi log nếu thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Đánh dấu sách " + bookId + " đã đọc"));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Không thể đánh dấu sách " + bookId + " đã đọc"));
      }

      return result;
    } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }
      return false;
    } catch (IllegalStateException e) {
      // Ghi log thất bại khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return false;

    } catch (SQLException logException) {
      System.out.println("Lỗi khi ghi log: " + logException.getMessage());
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

      // Truy vấn danh sách sách đã đọc bằng luồng riêng
      Future<List<ReadBookEntity>> future =
          executorService.submit(
              () -> {
                try {
                  return readBookDao.findReadBooks(userService.getLoginUser().getUserName());
                } catch (SQLException e) {
                  System.out.println(
                      "Lỗi cơ sở dữ liệu trong quá trình lấy danh sách sách đã đọc: "
                          + e.getMessage());
                  return null;
                }
              });
      return future.get();
    } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi: " + e.getMessage()));
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
   * Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa.
   *
   * @param bookId ID của sách cần kiểm tra.
   * @return True nếu sách đã được đánh dấu là đã đọc, ngược lại False.
   */
  public boolean isBookRead(int bookId) {
    try {

      // Kiểm tra giá trị đầu vào
      if (bookId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: ID sách không hợp lệ"));
        throw new IllegalArgumentException("ID sách không được để trống và phải là số dương");
      }

      // Thực hiện kiểm tra sách đã đọc bằng luồng riêng
      Future<Boolean> future =
          executorService.submit(
              () -> {
                try {
                  return readBookDao.isBookRead(bookId, userService.getLoginUser().getUserName());
                } catch (SQLException e) {
                  System.out.println(
                      "Lỗi cơ sở dữ liệu trong quá trình kiểm tra sách đã đọc: " + e.getMessage());
                  return false;
                }
              });
      boolean isRead = future.get();

      // Ghi log kết quả kiểm tra
      logDao.addLog(
          new LogEntity(
              LocalDateTime.now(),
              userService.getLoginUser().getUserName(),
              "Kiểm tra sách " + bookId + " đã đọc: " + isRead));
      return isRead;
    } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }
      return false;
    } catch (IllegalStateException e) {
      // Ghi log thất bại khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return false;

    } catch (SQLException logException) {
      System.out.println("Lỗi khi ghi log: " + logException.getMessage());
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

      // Kiểm tra giá trị đầu vào
      if (bookId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: ID sách không hợp lệ"));
        throw new IllegalArgumentException("ID sách không được để trống và phải là số dương");
      }

      // Thực hiện xóa đánh dấu sách đã đọc bằng luồng riêng
      Future<Boolean> future =
          executorService.submit(
              () -> {
                try {
                  return readBookDao.unmarkAsRead(bookId, userService.getLoginUser().getUserName());
                } catch (SQLException e) {
                  System.out.println(
                      "Lỗi cơ sở dữ liệu trong quá trình xóa đánh dấu sách đã đọc: "
                          + e.getMessage());
                  return false;
                }
              });
      boolean result = future.get();

      // Ghi log nếu thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Xóa đánh dấu sách " + bookId + " đã đọc"));
      }
      return result;
    } catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }
      return false;
    } catch (IllegalStateException e) {
      // Ghi log thất bại khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return false;

    } catch (SQLException logException) {
      System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      return false;
    }
  }
}
