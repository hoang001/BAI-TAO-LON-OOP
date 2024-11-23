package org.example.models;// không dùng đa luồng

public class ReviewEntity extends BaseEntity {
    private int userId;
    private int bookId;
    private int rating;
    private String comment;

    public ReviewEntity() {}

    public ReviewEntity(int reviewId, int userId, int bookId, int rating, String comment) {
        super(reviewId);
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
