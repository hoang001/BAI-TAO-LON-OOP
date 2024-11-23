package org.example.models;// không dùng đa luồng

public class ReviewEntity extends BaseEntity {
    private String userName;
    private int bookId;
    private int rating;
    private String comment;

    public ReviewEntity() {}

    public ReviewEntity(int reviewId, String userName, int bookId, int rating, String comment) {
        super(reviewId);
        this.userName = userName;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
