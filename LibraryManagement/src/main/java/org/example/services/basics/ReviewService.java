package org.example.services.basics;

import org.example.daos.interfaces.ReviewDao;
import org.example.models.ReviewEntity;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Lớp ReviewService chịu trách nhiệm quản lý các chức năng liên quan đến nhận xét.
 * Bao gồm thêm, xem, cập nhật và xóa nhận xét của người dùng đối với sách.
 */
public class ReviewService {

    // Đối tượng DAO để thao tác với cơ sở dữ liệu nhận xét
    private final ReviewDao reviewDao;

    // Dịch vụ quản lý người dùng, được sử dụng để xác thực người dùng hiện tại
    private final UserService userService;

    /**
     * Khởi tạo lớp ReviewService.
     * 
     * @param reviewDao Đối tượng ReviewDao để thao tác với cơ sở dữ liệu.
     * @param userService Đối tượng UserService để quản lý thông tin người dùng.
     */
    public ReviewService(ReviewDao reviewDao, UserService userService) {
        this.reviewDao = reviewDao;
        this.userService = userService;
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
            // Kiểm tra người dùng đã đăng nhập hay chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi thêm nhận xét.");
            }

            // Kiểm tra giá trị đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không được để trống và phải là số dương.");
            }
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5.");
            }
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Nhận xét không được để trống.");
            }

            // Thực hiện thêm nhận xét vào cơ sở dữ liệu
            return reviewDao.addReview(userService.getLoginUser().getUserName(), bookId, rating, comment);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
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
            // Kiểm tra giá trị đầu vào
            if (bookId <= 0) {
                throw new IllegalArgumentException("ID sách không được để trống và phải là số dương.");
            }

            // Truy vấn danh sách nhận xét từ cơ sở dữ liệu
            return reviewDao.getReviewsByBookId(bookId);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
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
            // Kiểm tra người dùng đã đăng nhập hay chưa
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để xem nhận xét.");
            }

            // Truy vấn danh sách nhận xét từ cơ sở dữ liệu
            return reviewDao.getReviewsByUserName(userService.getLoginUser().getUserName());
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
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
            // Kiểm tra giá trị đầu vào
            if (reviewId <= 0) {
                throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương.");
            }
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Đánh giá phải nằm trong khoảng từ 1 đến 5.");
            }
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Nhận xét không được để trống.");
            }

            // Thực hiện cập nhật nhận xét
            return reviewDao.updateReview(reviewId, rating, comment);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
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
            // Kiểm tra giá trị đầu vào
            if (reviewId <= 0) {
                throw new IllegalArgumentException("ID nhận xét không được để trống và phải là số dương.");
            }

            // Thực hiện xóa nhận xét
            return reviewDao.deleteReview(reviewId);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }
}
