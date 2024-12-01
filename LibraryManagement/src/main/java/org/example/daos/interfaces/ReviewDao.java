package org.example.daos.interfaces;

import java.sql.SQLException;
import java.util.List;
import org.example.models.ReviewEntity;

/** Giao diện cho các phương thức thao tác với dữ liệu đánh giá. */
public interface ReviewDao {

  /**
   * Thêm đánh giá mới.
   *
   * @param userName Tên người dùng đánh giá.
   * @param bookId ID của sách được đánh giá.
   * @param rating Đánh giá (số sao).
   * @param comment Bình luận.
   * @return true nếu thêm đánh giá thành công, ngược lại false.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  boolean addReview(String userName, int bookId, int rating, String comment) throws SQLException;

  /**
   * Tìm danh sách đánh giá theo ID sách.
   *
   * @param bookId ID của sách.
   * @return Danh sách ReviewEntity nếu tìm thấy, ngược lại null.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  List<ReviewEntity> findReviewsByBookId(int bookId) throws SQLException;

  /**
   * Tìm danh sách đánh giá theo ISBN của sách.
   *
   * @param isbn ISBN của sách.
   * @return Danh sách ReviewEntity nếu tìm thấy, ngược lại null.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  List<ReviewEntity> findReviewsByIsbn(String isbn) throws SQLException;

  /**
   * Tìm danh sách đánh giá theo tên người dùng.
   *
   * @param userName Tên người dùng.
   * @return Danh sách ReviewEntity nếu tìm thấy, ngược lại null.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  List<ReviewEntity> findReviewsByUserName(String userName) throws SQLException;

  /**
   * Cập nhật đánh giá.
   *
   * @param reviewId ID của đánh giá.
   * @param rating Đánh giá mới (số sao).
   * @param comment Bình luận mới.
   * @return true nếu cập nhật thành công, ngược lại false.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  boolean updateReview(int reviewId, int rating, String comment) throws SQLException;

  /**
   * Xóa đánh giá.
   *
   * @param reviewId ID của đánh giá.
   * @param userName Tên người dùng đã đăng đánh giá.
   * @return true nếu xóa thành công, ngược lại false.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  boolean deleteReview(int reviewId, String userName) throws SQLException;

  /**
   * Tìm điểm đánh giá trung bình theo ISBN của sách.
   *
   * @param isbn ISBN của sách.
   * @return Điểm đánh giá trung bình.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  Double findAverageRatingByIsbn(String isbn) throws SQLException;

  /**
   * Tìm điểm đánh giá trung bình theo ID sách.
   *
   * @param bookId ID của sách.
   * @return Điểm đánh giá trung bình.
   * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  Double findAverageRatingById(int bookId) throws SQLException;
}
