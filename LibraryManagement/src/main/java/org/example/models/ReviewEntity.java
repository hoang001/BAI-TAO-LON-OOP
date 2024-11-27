package org.example.models;

/**
 * Lớp đại diện cho đánh giá sách của người dùng.
 */
public class ReviewEntity extends BaseEntity {
    private String userName;
    private int bookId;
    private int rating;
    private String comment;

    /**
     * Khởi tạo một đối tượng ReviewEntity mặc định.
     */
    public ReviewEntity() {}

    /**
     * Khởi tạo một đối tượng ReviewEntity với các thông tin chi tiết.
     *
     * @param reviewId ID của đánh giá
     * @param userName Tên người dùng
     * @param bookId   ID của sách
     * @param rating   Đánh giá (số sao)
     * @param comment  Bình luận
     */
    public ReviewEntity(int reviewId, String userName, 
    int bookId, int rating, String comment) {
        super(reviewId);
        this.userName = userName;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Lấy tên người dùng.
     *
     * @return Tên người dùng
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Thiết lập tên người dùng.
     *
     * @param userName Tên người dùng
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy ID của sách.
     *
     * @return ID của sách
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Thiết lập ID của sách.
     *
     * @param bookId ID của sách
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Lấy đánh giá (số sao).
     *
     * @return Đánh giá
     */
    public int getRating() {
        return rating;
    }

    /**
     * Thiết lập đánh giá (số sao).
     *
     * @param rating Đánh giá
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Lấy bình luận.
     *
     * @return Bình luận
     */
    public String getComment() {
        return comment;
    }

    /**
     * Thiết lập bình luận.
     *
     * @param comment Bình luận
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
