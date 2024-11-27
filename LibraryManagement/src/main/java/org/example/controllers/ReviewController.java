package org.example.controllers;

import org.example.models.ReviewEntity;
import org.example.services.basics.ReviewService;

import java.util.List;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến việc đánh giá sách,
 * bao gồm thêm mới, cập nhật, xóa và lấy thông tin đánh giá theo sách hoặc theo người dùng.
 * Lớp này tương tác với ReviewService để thực hiện các thao tác đánh giá.
 */
public class ReviewController {

    // Đối tượng ReviewService được sử dụng để thực hiện các chức năng đánh giá sách.
    private final ReviewService reviewService;

    /**
     * Constructor khởi tạo đối tượng ReviewService.
     * Phương thức này được gọi khi tạo đối tượng ReviewController để quản lý các thao tác đánh giá.
     */
    public ReviewController() {
        this.reviewService = new ReviewService();  // Khởi tạo ReviewService để xử lý các yêu cầu đánh giá.
    }

    /**
     * Thêm một đánh giá mới cho một cuốn sách.
     * 
     * @param bookId ID của cuốn sách mà người dùng muốn đánh giá.
     * @param rating Điểm đánh giá của người dùng cho sách (ví dụ: từ 1 đến 5).
     * @param comment Bình luận của người dùng về cuốn sách.
     * @return boolean Trả về true nếu thêm đánh giá thành công, false nếu thất bại.
     */
    public boolean addReview(int bookId, int rating, String comment) {
        return reviewService.addReview(bookId, rating, comment);  // Gọi phương thức của ReviewService để thêm đánh giá.
    }

    /**
     * Lấy danh sách các đánh giá của một cuốn sách cụ thể.
     * 
     * @param bookId ID của cuốn sách mà người dùng muốn xem các đánh giá.
     * @return List<ReviewEntity> Danh sách các đánh giá của cuốn sách, hoặc danh sách trống nếu không có đánh giá.
     */
    public List<ReviewEntity> getReviewsByBookId(int bookId) {
        return reviewService.getReviewsByBookId(bookId);  // Gọi phương thức của ReviewService để lấy các đánh giá của sách theo ID.
    }

    /**
     * Lấy danh sách các đánh giá của người dùng.
     * 
     * @return List<ReviewEntity> Danh sách các đánh giá của người dùng, hoặc danh sách trống nếu không có đánh giá.
     */
    public List<ReviewEntity> getReviewsByUserId() {
        return reviewService.getReviewsByUserId();  // Gọi phương thức của ReviewService để lấy các đánh giá của người dùng.
    }

    /**
     * Cập nhật đánh giá của người dùng cho một cuốn sách.
     * 
     * @param reviewId ID của đánh giá mà người dùng muốn cập nhật.
     * @param rating Điểm đánh giá mới của người dùng cho sách.
     * @param comment Bình luận mới của người dùng về cuốn sách.
     * @return boolean Trả về true nếu cập nhật đánh giá thành công, false nếu thất bại.
     */
    public boolean updateReview(int reviewId, int rating, String comment) {
        return reviewService.updateReview(reviewId, rating, comment);  // Gọi phương thức của ReviewService để cập nhật đánh giá.
    }

    /**
     * Xóa một đánh giá của người dùng cho một cuốn sách.
     * 
     * @param reviewId ID của đánh giá mà người dùng muốn xóa.
     * @return boolean Trả về true nếu xóa đánh giá thành công, false nếu thất bại.
     */
    public boolean deleteReview(int reviewId) {
        return reviewService.deleteReview(reviewId);  // Gọi phương thức của ReviewService để xóa đánh giá.
    }

    /**
     * Lấy điểm trung bình theo ISBN.
     * @param isbn tham số đầu vào.
     * @return điểm trung bình.
     */
    public Double getAverageRatingByIsbn(String isbn) {
        return reviewService.getAverageRatingByIsbn(isbn);
    }

    /**
     * Lấy điểm trung bình theo ID.
     * @param bookId tham số đầu vào.
     * @return điểm trung bình.
     */
    public Double getAverageRatingById(int bookId) {
        return reviewService.getAverageRatingById(bookId);
    }
}
