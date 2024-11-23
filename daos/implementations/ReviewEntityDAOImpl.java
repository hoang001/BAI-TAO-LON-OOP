package org.example.daos.implementations;

import org.example.daos.interfaces.ReviewDAO;
import org.example.models.ReviewEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewEntityDAOImpl implements ReviewDAO {
    private Connection connection;

    public ReviewEntityDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addReview(int userId, int bookId, int rating, String comment) throws SQLException {
        String query = "INSERT INTO ReviewEntitys (user_id, document_id, rating, comment, created_at) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, comment);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public List<ReviewEntity> getReviewsByBookId(int bookId) throws SQLException {
        List<ReviewEntity> ReviewEntitys = new ArrayList<>();
        String query = "SELECT * FROM Reviews WHERE book_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ReviewEntity ReviewEntity = new ReviewEntity();
                    ReviewEntity.setId(resultSet.getInt("ReviewEntity_id"));
                    ReviewEntity.setUserId(resultSet.getInt("user_id"));
                    ReviewEntity.setBookId(resultSet.getInt("document_id"));
                    ReviewEntity.setRating(resultSet.getInt("rating"));
                    ReviewEntity.setComment(resultSet.getString("comment"));
                    ReviewEntitys.add(ReviewEntity);
                }
            }
        }
        return ReviewEntitys;
    }

    @Override
    public List<ReviewEntity> getReviewsByUserId(int userId) throws SQLException {
        List<ReviewEntity> ReviewEntitys = new ArrayList<>();
        String query = "SELECT * FROM ReviewEntitys WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ReviewEntity ReviewEntity = new ReviewEntity();
                    ReviewEntity.setId(resultSet.getInt("ReviewEntity_id"));
                    ReviewEntity.setUserId(resultSet.getInt("user_id"));
                    ReviewEntity.setBookId(resultSet.getInt("document_id"));
                    ReviewEntity.setRating(resultSet.getInt("rating"));
                    ReviewEntity.setComment(resultSet.getString("comment"));
                    ReviewEntitys.add(ReviewEntity);
                }
            }
        }
        return ReviewEntitys;
    }

    @Override
    public boolean updateReview(int reviewId, int rating, String comment) throws SQLException {
        String query = "UPDATE ReviewEntitys SET rating = ?, comment = ? WHERE ReviewEntity_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, rating);
            preparedStatement.setString(2, comment);
            preparedStatement.setInt(3, reviewId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteReview(int reviewId) throws SQLException {
        String query = "DELETE FROM ReviewEntitys WHERE ReviewEntity_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reviewId);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}