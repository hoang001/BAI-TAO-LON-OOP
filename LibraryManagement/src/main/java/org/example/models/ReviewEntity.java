package org.example.models;

/**
 * Lớp này đại diện cho một bài đánh giá của người dùng về một cuốn sách.
 * Lớp ReviewEntity chứa các thông tin như tên người dùng, ID sách, xếp hạng và nhận xét.
 */
public class ReviewEntity extends BaseEntity {

    // Thuộc tính của lớp ReviewEntity
    private String userName;    // Tên người dùng đã viết bài đánh giá
    private int bookId;         // ID của cuốn sách được đánh giá
    private int rating;         // Đánh giá của người dùng từ 1 đến 5
    private String comment;     // Nhận xét của người dùng về cuốn sách

    /**
     * Constructor mặc định cho ReviewEntity.
     * Khởi tạo một đối tượng ReviewEntity mà không cần tham số.
     */
    public ReviewEntity() {}

    /**
     * Constructor cho ReviewEntity với các thông tin chi tiết.
     * Sử dụng constructor này để khởi tạo một đối tượng ReviewEntity với đầy đủ thông tin.
     *
     * @param reviewId  ID của bài đánh giá.
     * @param userName  Tên người dùng đã viết bài đánh giá.
     * @param bookId    ID của cuốn sách được đánh giá.
     * @param rating    Đánh giá của người dùng (1 đến 5).
     * @param comment   Nhận xét của người dùng về cuốn sách.
     */
    public ReviewEntity(int reviewId, String userName, int bookId, int rating, String comment) {
        super(reviewId);  // Gọi constructor của lớp cha (BaseEntity) để khởi tạo ID bài đánh giá
        this.userName = userName;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Lấy tên người dùng đã viết bài đánh giá.
     * 
     * @return Tên người dùng.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Đặt tên người dùng đã viết bài đánh giá.
     * 
     * @param userName Tên người dùng mới.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy ID của cuốn sách được đánh giá.
     * 
     * @return ID của cuốn sách.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Đặt ID cho cuốn sách được đánh giá.
     * 
     * @param bookId ID sách mới.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Lấy đánh giá của người dùng (từ 1 đến 5).
     * 
     * @return Đánh giá của người dùng.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Đặt đánh giá cho cuốn sách.
     * 
     * @param rating Đánh giá mới của người dùng.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Lấy nhận xét của người dùng về cuốn sách.
     * 
     * @return Nhận xét của người dùng.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Đặt nhận xét cho cuốn sách.
     * 
     * @param comment Nhận xét mới của người dùng.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
