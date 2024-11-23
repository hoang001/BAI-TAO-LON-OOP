package org.example.daos.interfaces;

import org.example.models.ReviewEntity;
import java.sql.SQLException;
import java.util.List;

/**
 * Giao diện cho các thao tác CRUD đối với đánh giá của người dùng.
 */
public interface ReviewDao {

    /**
     * Thêm một đánh giá mới cho tài liệu.
     *
     * @param userName     Tên của người dùng thực hiện đánh giá.
     * @param bookId     ID của sách được đánh giá.
     * @param rating     điểm đánh giá (thường từ 1 đến 5).
     * @param comment    nội dung bình luận của người dùng.
     * @return true nếu thêm đánh giá thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean addReview(String userName, int bookId, int rating, String comment) throws SQLException;

    /**
     * Lấy danh sách các đánh giá của một tài liệu (hoặc sách).
     *
     * @param bookId ID của tài liệu (hoặc sách) cần lấy đánh giá.
     * @return danh sách các đối tượng ReviewEntity chứa thông tin đánh giá.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<ReviewEntity> getReviewsByBookId(int bookId) throws SQLException;

    /**
     * Lấy danh sách các đánh giá của một người dùng.
     *
     * @param userId ID của người dùng cần lấy các đánh giá của họ.
     * @return danh sách các đối tượng ReviewEntity chứa thông tin đánh giá của người dùng.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<ReviewEntity> getReviewsByUserName(String userName) throws SQLException;

    /**
     * Cập nhật một đánh giá đã tồn tại.
     *
     * @param reviewId ID của đánh giá cần cập nhật.
     * @param rating   điểm đánh giá mới.
     * @param comment  nội dung bình luận mới.
     * @return true nếu cập nhật đánh giá thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updateReview(int reviewId, int rating, String comment) throws SQLException;

    /**
     * Xóa một đánh giá.
     *
     * @param reviewId ID của đánh giá cần xóa.
     * @return true nếu xóa đánh giá thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean deleteReview(int reviewId) throws SQLException;
}
