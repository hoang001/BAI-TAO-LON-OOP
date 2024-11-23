package org.example.services.basics;

import org.example.daos.interfaces.ReviewDAO;
import org.example.models.ReviewEntity;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final UserService userService;

    public ReviewService(ReviewDAO reviewDAO, UserService userService) {
        this.reviewDAO = reviewDAO;
        this.userService = userService;
    }

    public boolean addReview(int bookId, int rating, String comment) {
        try {
            // Kiểm tra người dùng đăng nhập
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi thêm nhận xét.");
            }

            // Kiểm tra đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không được để trống và phải là số dương.");
            }
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5.");
            }
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Nhận xét không được để trống.");
            }

            // Thêm nhận xét
            return reviewDAO.addReview(userService.getLoginUser().getId(), bookId, rating, comment);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }

    public List<ReviewEntity> getReviewsByBookId(int bookId) {
        try {
            // Kiểm tra ID sách
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không được để trống và phải là số dương.");
            }

            return reviewDAO.getReviewsByBookId(bookId);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ReviewEntity> getReviewsByUserId() {
        try {
            // Kiểm tra người dùng đăng nhập
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để xem nhận xét.");
            }

            return reviewDAO.getReviewsByUserId(userService.getLoginUser().getId());
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean updateReview(int reviewId, int rating, String comment) {
        try {
            // Kiểm tra đầu vào
            if (reviewId <= 0) {
                throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương.");
            }
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5.");
            }
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Nhận xét không được để trống.");
            }

            return reviewDAO.updateReview(reviewId, rating, comment);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReview(int reviewId) {
        try {
            // Kiểm tra ID nhận xét
            if (reviewId <= 0) {
                throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương.");
            }

            return reviewDAO.deleteReview(reviewId);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }
}
