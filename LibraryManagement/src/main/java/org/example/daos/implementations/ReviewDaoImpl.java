package org.example.daos.implementations;

import org.example.daos.interfaces.ReviewDao;
import org.example.models.ReviewEntity;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai giao diện ReviewDao.
 */
public class ReviewDaoImpl implements ReviewDao {

    private final Connection connection;

    /**
     * Hàm khởi tạo để thiết lập kết nối cơ sở dữ liệu.
     */
    public ReviewDaoImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Thêm một đánh giá mới.
     *
     * @param userName Tên người dùng đánh giá.
     * @param bookId   ID của sách được đánh giá.
     * @param rating   Đánh giá (số sao).
     * @param comment  Bình luận.
     * @return true nếu đánh giá được thêm thành công; false nếu không.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public boolean addReview(String userName, int bookId, int rating, String comment) throws SQLException {
        String query = "INSERT INTO Reviews (userName, bookId, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, comment);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * Tìm đánh giá theo ID sách.
     *
     * @param bookId ID của sách.
     * @return Danh sách các đối tượng ReviewEntity.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
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
     * Tìm đánh giá theo ISBN.
     *
     * @param isbn ISBN của sách.
     * @return Danh sách các đối tượng ReviewEntity.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public List<ReviewEntity> findReviewsByIsbn(String isbn) throws SQLException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        String query = "SELECT * FROM Reviews " +
            "INNER JOIN books USING(bookId) " +
            "WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
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
     * Tìm đánh giá theo tên người dùng.
     *
     * @param userName Tên người dùng.
     * @return Danh sách các đối tượng ReviewEntity.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public List<ReviewEntity> findReviewsByUserName(String userName) throws SQLException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE userName = ?";
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
     * Cập nhật một đánh giá.
     *
     * @param reviewId ID của đánh giá.
     * @param rating   Đánh giá mới (số sao).
     * @param comment  Bình luận mới.
     * @return true nếu đánh giá được cập nhật thành công; false nếu không.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
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
     * Xóa một đánh giá.
     *
     * @param reviewId ID của đánh giá.
     * @param userName Tên người dùng.
     * @return true nếu đánh giá được xóa thành công; false nếu không.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public boolean deleteReview(int reviewId, String userName) throws SQLException {
        String query = "DELETE FROM Reviews WHERE reviewId = ? AND userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reviewId);
            preparedStatement.setString(2, userName);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * Tìm đánh giá trung bình theo ISBN.
     *
     * @param isbn ISBN của sách.
     * @return Đánh giá trung bình hoặc null nếu không tìm thấy.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public Double findAverageRatingByIsbn(String isbn) throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating " +
            "FROM reviews " +
            "INNER JOIN books USING(bookId) " +
            "WHERE books.isbn = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("average_rating");
            }
        }
        return null;
    }

    /**
     * Tìm đánh giá trung bình theo ID sách.
     *
     * @param bookId ID của sách.
     * @return Đánh giá trung bình hoặc null nếu không tìm thấy.
     * @throws SQLException nếu xảy ra lỗi truy cập cơ sở dữ liệu.
     */
    @Override
    public Double findAverageRatingById(int bookId) throws SQLException {
        String query = "SELECT AVG(reviews.rating) AS average_rating " +
            "FROM reviews " +
            "INNER JOIN books ON reviews.book_id = books.book_id " +
            "WHERE books.book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("average_rating");
            }
        }
        return null;
    }
}
