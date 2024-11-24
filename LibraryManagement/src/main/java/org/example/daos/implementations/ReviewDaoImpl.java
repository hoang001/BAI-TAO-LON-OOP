package org.example.daos.implementations;

import org.example.daos.interfaces.ReviewDao;
import org.example.models.ReviewEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai ReviewDAO để thực hiện các thao tác CRUD đối với đánh giá.
 */
public class ReviewDaoImpl implements ReviewDao {
    private Connection connection;

    /**
     * Constructor để khởi tạo kết nối cơ sở dữ liệu.
     *
     * @param connection đối tượng Connection để kết nối cơ sở dữ liệu.
     */
    public ReviewDaoImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Thêm đánh giá cho một cuốn sách.
     *
     * @param userName   Tên của người dùng.
     * @param bookId   ID của cuốn sách.
     * @param rating   Đánh giá của người dùng.
     * @param comment  Bình luận của người dùng.
     * @return true nếu thêm đánh giá thành công, ngược lại false.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public boolean addReview(String userName, int bookId, int rating, String comment) throws SQLException {
        String query = "INSERT INTO Reviews (userId, bookId, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, comment);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * Lấy danh sách đánh giá theo ID sách.
     *
     * @param bookId ID của cuốn sách.
     * @return Danh sách các đối tượng ReviewEntity.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<ReviewEntity> findReviewsByBookId(int bookId) throws SQLException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE bookId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ReviewEntity reviewEntity = new ReviewEntity();
                    reviewEntity.setId(resultSet.getInt("reviewId"));
                    reviewEntity.setUserName(resultSet.getString("userName"));
                    reviewEntity.setBookId(resultSet.getInt("bookId"));
                    reviewEntity.setRating(resultSet.getInt("rating"));
                    reviewEntity.setComment(resultSet.getString("comment"));
                    reviewEntities.add(reviewEntity);
                }
            }
        }
        return reviewEntities;
    }

    /**
     * Lấy danh sách đánh giá theo ID người dùng.
     *
     * @param userName Tên của người dùng.
     * @return Danh sách các đối tượng ReviewEntity.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<ReviewEntity> findReviewsByUserName(String userName) throws SQLException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ReviewEntity reviewEntity = new ReviewEntity();
                    reviewEntity.setId(resultSet.getInt("reviewId"));
                    reviewEntity.setUserName(resultSet.getString("userName"));
                    reviewEntity.setBookId(resultSet.getInt("bookId"));
                    reviewEntity.setRating(resultSet.getInt("rating"));
                    reviewEntity.setComment(resultSet.getString("comment"));
                    reviewEntities.add(reviewEntity);
                }
            }
        }
        return reviewEntities;
    }

    /**
     * Cập nhật đánh giá.
     *
     * @param reviewId ID của đánh giá.
     * @param rating   Đánh giá mới của người dùng.
     * @param comment  Bình luận mới của người dùng.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public boolean updateReview(int reviewId, int rating, String comment) throws SQLException {
        String query = "UPDATE Reviews SET rating = ?, comment = ? WHERE reviewId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, rating);
            preparedStatement.setString(2, comment);
            preparedStatement.setInt(3, reviewId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * Xóa đánh giá.
     *
     * @param reviewId ID của đánh giá.
     * @return true nếu xóa thành công, ngược lại false.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public boolean deleteReview(int reviewId, String userName) throws SQLException {
        String query = "DELETE FROM Reviews WHERE reviewId = ? and userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reviewId);
            preparedStatement.setString(2, userName);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}
