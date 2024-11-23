package org.example.daos.interfaces;

import org.example.models.ReviewEntity;

import java.sql.SQLException;
import java.util.List;

public interface ReviewDAO {
    boolean addReview(int userId, int documentId, int rating, String comment) throws SQLException;
    List<ReviewEntity> getReviewsByBookId(int bookId) throws SQLException;
    List<ReviewEntity> getReviewsByUserId(int userId) throws SQLException;
    boolean updateReview(int reviewId, int rating, String comment) throws SQLException;
    boolean deleteReview(int reviewId) throws SQLException;
}

