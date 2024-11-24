package org.example.models;

import java.time.LocalDateTime;

/**
 * Lớp này đại diện cho thực thể sách đã mượn, lưu trữ thông tin về việc mượn sách của người dùng.
 * Nó mở rộng từ lớp BaseEntity để kế thừa thuộc tính ID.
 */
public class BorrowedBookEntity extends BaseEntity {

    // Thuộc tính của lớp BorrowedBookEntity
    private int bookId;               // ID của sách đã mượn
    private String userName;          // Tên người dùng mượn sách
    private LocalDateTime borrowDate; // Ngày mượn sách
    private LocalDateTime returnDate; // Ngày trả sách

    /**
     * Constructor mặc định cho BorrowedBookEntity.
     * Khởi tạo đối tượng BorrowedBookEntity mà không cần thông tin đầu vào.
     */
    public BorrowedBookEntity() {}

    /**
     * Constructor cho BorrowedBookEntity với các thông tin chi tiết.
     *
     * @param borrowId     ID của việc mượn sách.
     * @param bookId       ID của sách mượn.
     * @param userName     Tên người dùng mượn sách.
     * @param borrowDate   Ngày mượn sách.
     * @param returnDate   Ngày trả sách.
     */
    public BorrowedBookEntity(int borrowId, int bookId, String userName, LocalDateTime borrowDate, LocalDateTime returnDate) {
        super(borrowId);  // Gọi constructor của lớp cha BaseEntity để thiết lập ID
        this.bookId = bookId;
        this.userName = userName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    /**
     * Lấy ID của sách đã mượn.
     * 
     * @return ID của sách đã mượn.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Đặt ID của sách đã mượn.
     * 
     * @param bookId ID của sách cần thiết lập.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Lấy tên người dùng mượn sách.
     * 
     * @return Tên người dùng mượn sách.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Đặt tên người dùng mượn sách.
     * 
     * @param userName Tên người dùng cần thiết lập.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy ngày mượn sách.
     * 
     * @return Ngày mượn sách dưới dạng LocalDateTime.
     */
    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    /**
     * Đặt ngày mượn sách.
     * 
     * @param borrowDate Ngày mượn sách cần thiết lập.
     */
    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * Lấy ngày trả sách.
     * 
     * @return Ngày trả sách dưới dạng LocalDateTime.
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    /**
     * Đặt ngày trả sách.
     * 
     * @param returnDate Ngày trả sách cần thiết lập.
     */
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}
