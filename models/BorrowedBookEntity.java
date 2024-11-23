package org.example.models;

import java.time.LocalDateTime;

public class BorrowedBookEntity extends BaseEntity {
    private int bookId;
    private String userName;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    public BorrowedBookEntity() {}

    public BorrowedBookEntity(int borrowId, int bookId, String userName, LocalDateTime borrowDate, LocalDateTime returnDate) {
        super(borrowId);
        this.bookId = bookId;
        this.userName = userName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
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

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userName) {
        this.userName = userName;
    }
}