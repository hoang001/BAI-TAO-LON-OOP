package org.example.controllers;

import org.example.models.ReviewEntity;
import org.example.services.basics.ReviewService;

import java.util.List;

public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Thêm đánh giá
    public void addReview(int bookId, int rating, String comment) {
            boolean result = reviewService.addReview(bookId, rating, comment);
            if (result) {
                System.out.println("Đánh giá đã được thêm thành công.");
            } else {
                System.out.println("Thêm đánh giá thất bại.");
            }
    }

    // Lấy danh sách đánh giá theo sách
    public void getReviewsByBookId(int bookId) {
            List<ReviewEntity> reviews = reviewService.getReviewsByBookId(bookId);
            if (reviews.isEmpty()) {
                System.out.println("Không có đánh giá nào cho sách này.");
            } else {
                reviews.forEach(System.out::println);
            }
    }

    // Lấy danh sách đánh giá theo người dùng
    public void getReviewsByUserId() {
            List<ReviewEntity> reviews = reviewService.getReviewsByUserId();
            if (reviews.isEmpty()) {
                System.out.println("Người dùng này chưa có đánh giá nào.");
            } else {
                reviews.forEach(System.out::println);
            }
    }

    // Cập nhật đánh giá
    public void updateReview(int reviewId, int rating, String comment) {
            boolean result = reviewService.updateReview(reviewId, rating, comment);
            if (result) {
                System.out.println("Cập nhật đánh giá thành công.");
            } else {
                System.out.println("Cập nhật đánh giá thất bại.");
            }
    }

    // Xóa đánh giá
    public void deleteReview(int reviewId) {
            boolean result = reviewService.deleteReview(reviewId);
            if (result) {
                System.out.println("Xóa đánh giá thành công.");
            } else {
                System.out.println("Xóa đánh giá thất bại.");
            }
    }
}

