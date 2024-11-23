package org.example.models;

/**
 * Class đại diện cho thực thể sách đã đọc, mở rộng từ BaseEntity.
 */
public class ReadBookEntity extends BaseEntity {
    private int bookId;
    private String userName;

    /**
     * Constructor mặc định cho ReadBookEntity.
     */
    public ReadBookEntity() {}

    /**
     * Constructor cho ReadBookEntity với các thông tin chi tiết.
     *
     * @param readId  ID của việc đọc sách.
     * @param bookId  ID của sách.
     * @param userName  Tên người dùng.
     */
    public ReadBookEntity(int readId, int bookId, String userName) {
        super(readId);
        this.bookId = bookId;
        this.userName = userName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
