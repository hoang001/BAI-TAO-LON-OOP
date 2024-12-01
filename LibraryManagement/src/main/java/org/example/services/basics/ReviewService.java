package org.example.services.basics;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.daos.implementations.BookDaoImpl;
import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.implementations.ReviewDaoImpl;
import org.example.daos.interfaces.BookDao;
import org.example.daos.interfaces.LogDao;
import org.example.daos.interfaces.ReviewDao;
import org.example.models.LogEntity;
import org.example.models.ReviewEntity;

/**
 * Lớp ReviewService chịu trách nhiệm quản lý các chức năng liên quan đến nhận xét. Bao gồm thêm,
 * xem, cập nhật và xóa nhận xét của người dùng đối với sách.
 */
public class ReviewService {

  // Đối tượng DAO để thao tác với cơ sở dữ liệu nhận xét
  private final ReviewDao reviewDao;

  // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
  private final UserService userService;

  private final BookDao bookDao;

  private final LogDao logDao;

  /**
   * Constructor của lớp ReviewService.
   * Khởi tạo các đối tượng DAO và dịch vụ cần thiết.
   */
  public ReviewService() {
    this.reviewDao = new ReviewDaoImpl();
    this.userService = UserService.getInstance();
    this.logDao = new LogDaoImpl();
    this.bookDao = new BookDaoImpl();
  }

  /**
   * Thêm một nhận xét mới cho sách.
   *
   * @param bookId ID của sách cần nhận xét.
   * @param rating Đánh giá (từ 1 đến 5).
   * @param comment Nội dung nhận xét.
   * @return True nếu thêm nhận xét thành công, ngược lại False.
   */
  public boolean addReview(int bookId, int rating, String comment) {
    try {
      // Kiểm tra ID sách hợp lệ
      if (bookId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: ID sách không hợp lệ"));
        throw new IllegalArgumentException("ID sách không được để trống và phải là số dương");
      }

      // Kiểm tra đánh giá hợp lệ
      if (rating < 1 || rating > 5) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: Đánh giá không hợp lệ"));
        throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5");
      }

      // Kiểm tra nhận xét hợp lệ
      if (comment == null || comment.trim().isEmpty()) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: Nhận xét không hợp lệ"));
        throw new IllegalArgumentException("Nhận xét không được để trống");
      }

      // Thực hiện thêm đánh giá trong cơ sở dữ liệu
      boolean result =
          reviewDao.addReview(userService.getLoginUser().getUserName(), bookId, rating, comment);

      // Ghi log nếu thêm đánh giá thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thành công"));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại"));
      }
      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Thêm đánh giá thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi thêm đánh giá: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi không hợp lệ
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi thêm đánh giá
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu trong qua trình thêm đánh giá: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cơ sở dữ liệu trong quá trình thêm đánh giá: " + e.getMessage());
      return false;
    }
  }

  /**
   * Lấy danh sách nhận xét của một sách cụ thể.
   *
   * @param bookId ID của sách cần lấy nhận xét.
   * @return Danh sách các đối tượng ReviewEntity hoặc danh sách trống nếu có lỗi.
   */
  public List<ReviewEntity> getReviewsByBookId(int bookId) {
    try {
      // Kiểm tra ID sách hợp lệ
      if (bookId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi lấy nhận xét: ID sách không hợp lệ"));
        throw new IllegalArgumentException("ID sách không được để trống và phải là số dương");
      }

      // Lấy danh sách nhận xét từ cơ sở dữ liệu dựa trên ID sách
      List<ReviewEntity> reviews = reviewDao.findReviewsByBookId(bookId);

      // Ghi log khi lấy danh sách nhận xét thành công
      logDao.addLog(
          new LogEntity(
              LocalDateTime.now(),
              userService.getLoginUser().getUserName(),
              "Lấy danh sách nhận xét thành công cho sách ID: " + bookId));

      // Trả về danh sách nhận xét
      return reviews;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do ID sách không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi ID không hợp lệ: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi lấy danh sách đánh giá theo ID của sách: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    } catch (SQLException e) {
      // Ghi log khi gặp lỗi cơ sở dữ liệu
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi cơ sở dữ liệu: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    }
  }

  /**
   * Lấy danh sách nhận xét theo ISBN.
   *
   * @param isbn Mã ISBN của sách.
   * @return Danh sách các nhận xét về sách, hoặc danh sách rỗng nếu có lỗi.
   */
  public List<ReviewEntity> getReviewsByIsbn(String isbn) {
    try {
      // Kiểm tra xem sách với ISBN có tồn tại trong cơ sở dữ liệu không
      if (!bookDao.isBookInDatabase(isbn)) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi lấy nhận xét: ISBN không tồn tại"));
        throw new IllegalArgumentException("ISBN không tồn tại");
      }

      // Lấy danh sách nhận xét từ cơ sở dữ liệu dựa trên ISBN
      List<ReviewEntity> reviews = reviewDao.findReviewsByIsbn(isbn);

      // Ghi log khi lấy danh sách nhận xét thành công
      logDao.addLog(
          new LogEntity(
              LocalDateTime.now(),
              userService.getLoginUser().getUserName(),
              "Lấy danh sách nhận xét thành công cho sách ISBN: " + isbn));

      // Trả về danh sách nhận xét
      return reviews;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do ISBN không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi ISBN không hợp lệ: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi lấy danh sách đánh giá theo ISBN của sách: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    } catch (SQLException e) {
      // Ghi log khi gặp lỗi cơ sở dữ liệu
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi cơ sở dữ liệu: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    }
  }

  /**
   * Lấy danh sách nhận xét của người dùng hiện tại.
   *
   * @return Danh sách các đối tượng ReviewEntity hoặc danh sách trống nếu có lỗi.
   */
  public List<ReviewEntity> getReviewsByUserId() {
    try {
      // Lấy danh sách nhận xét từ cơ sở dữ liệu dựa trên tên người dùng đang đăng nhập
      List<ReviewEntity> reviews =
          reviewDao.findReviewsByUserName(userService.getLoginUser().getUserName());

      // Ghi log khi lấy danh sách nhận xét thành công
      logDao.addLog(
          new LogEntity(
              LocalDateTime.now(),
              userService.getLoginUser().getUserName(),
              "Lấy danh sách nhận xét thành công"));

      // Trả về danh sách nhận xét
      return reviews;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi ID không hợp lệ: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi lấy danh sách đánh giá theo ID người dùng: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi người dùng không hợp lệ
      System.out.println("Lỗi: " + e.getMessage());
      return null;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi lấy nhận xét
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu khi lấy nhận xét: " + e.getMessage()));
      } catch (SQLException logError) {
        System.out.println("Lỗi ghi log khi gặp lỗi cơ sở dữ liệu: " + logError.getMessage());
      }
      // Ghi thông báo lỗi ra console
      System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
      // Trả về danh sách rỗng trong trường hợp lỗi
      return Collections.emptyList();
    }
  }

  /**
   * Cập nhật nhận xét của người dùng.
   *
   * @param reviewId ID của nhận xét cần cập nhật.
   * @param rating Đánh giá mới (từ 1 đến 5).
   * @param comment Nội dung nhận xét mới.
   * @return True nếu cập nhật thành công, ngược lại False.
   */
  public boolean updateReview(int reviewId, int rating, String comment) {
    try {
      // Kiểm tra ID đánh giá hợp lệ
      if (reviewId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thất bại: ID đánh giá không hợp lệ"));
        throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương");
      }

      // Kiểm tra đánh giá hợp lệ
      if (rating < 1 || rating > 5) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thất bại: Đánh giá không hợp lệ"));
        throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5");
      }

      // Kiểm tra nhận xét hợp lệ
      if (comment == null || comment.trim().isEmpty()) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thất bại: Nhận xét không hợp lệ"));
        throw new IllegalArgumentException("Nhận xét không được để trống");
      }

      // Thực hiện cập nhật đánh giá trong cơ sở dữ liệu
      boolean result = reviewDao.updateReview(reviewId, rating, comment);

      // Ghi log nếu cập nhật đánh giá thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thành công: ID đánh giá " + reviewId));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thất bại: Không thể cập nhật ID đánh giá " + reviewId));
      }

      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Cập nhật đánh giá thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cập nhật đánh giá: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi trạng thái không hợp lệ
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi cập nhật đánh giá
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu khi cập nhật đánh giá: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cơ sở dữ liệu khi cập nhật đánh giá: " + e.getMessage());
      return false;
    }
  }

  /**
   * Xóa nhận xét của người dùng.
   *
   * @param reviewId ID của nhận xét cần xóa.
   * @return True nếu xóa thành công, ngược lại False.
   */
  public boolean deleteReview(int reviewId) {
    try {
      // Kiểm tra ID nhận xét hợp lệ
      if (reviewId <= 0) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Xóa đánh giá thất bại: ID nhận xét không hợp lệ"));
        throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương");
      }

      // Thực hiện xóa nhận xét trong cơ sở dữ liệu
      boolean result = reviewDao.deleteReview(reviewId, userService.getLoginUser().getUserName());

      // Ghi log nếu xóa nhận xét thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Xóa đánh giá thành công: ID nhận xét " + reviewId));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Xóa đánh giá thất bại: Không thể xóa ID nhận xét " + reviewId));
      }
      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Xóa đánh giá thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi xóa đánh giá: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi trạng thái không hợp lệ
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi xóa đánh giá
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi cơ sở dữ liệu khi xóa đánh giá: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cơ sở dữ liệu khi xóa đánh giá: " + e.getMessage());
      return false;
    }
  }

  /**
   * Lấy điểm trung bình theo ISBN.
   *
   * @param isbn Mã ISBN của sách.
   * @return Điểm trung bình của sách dựa trên mã ISBN, hoặc null nếu có lỗi.
   */
  public Double getAverageRatingByIsbn(String isbn) {
    try {
      return reviewDao.findAverageRatingByIsbn(isbn);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Lấy điểm trung bình theo ID.
   *
   * @param bookId ID của sách.
   * @return Điểm trung bình của sách dựa trên ID, hoặc null nếu có lỗi.
   */
  public Double getAverageRatingById(int bookId) {
    try {
      return reviewDao.findAverageRatingById(bookId);
    } catch (SQLException e) {
      return null;
    }
  }
}
