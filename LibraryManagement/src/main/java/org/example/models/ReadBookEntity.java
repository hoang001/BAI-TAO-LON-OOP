package org.example.models;

/**
 * Lớp này đại diện cho thực thể sách đã đọc, mở rộng từ BaseEntity.
 * Lớp này lưu trữ thông tin về sách mà người dùng đã đọc, bao gồm ID sách và tên người dùng.
 */
public class ReadBookEntity extends BaseEntity {
    
    // Thuộc tính của lớp ReadBookEntity
    private int bookId;       // ID của sách đã đọc
    private String userName;  // Tên người dùng đã đọc sách

    /**
     * Constructor mặc định cho ReadBookEntity.
     * Khởi tạo đối tượng ReadBookEntity mà không cần thông tin đầu vào.
     */
    public ReadBookEntity() {}

    /**
     * Constructor cho ReadBookEntity với các thông tin chi tiết.
     * 
     * @param readId    ID của việc đọc sách, kế thừa từ lớp BaseEntity.
     * @param bookId    ID của sách đã đọc.
     * @param userName  Tên người dùng đã đọc sách.
     */
    public ReadBookEntity(int readId, int bookId, String userName) {
        super(readId);  // Gọi constructor của lớp cha (BaseEntity) để khởi tạo ID
        this.bookId = bookId;
        this.userName = userName;
    }

    /**
     * Lấy ID của sách đã đọc.
     * 
     * @return ID của sách đã đọc.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Đặt ID cho sách đã đọc.
     * 
     * @param bookId ID của sách mới.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Lấy tên người dùng đã đọc sách.
     * 
     * @return Tên người dùng.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Đặt tên người dùng đã đọc sách.
     * 
     * @param userName Tên người dùng mới.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
