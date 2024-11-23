package org.example.models;

public class ReadBookEntity extends BaseEntity{
    private int bookId;
    private String userName;

    public ReadBookEntity() {}

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
    
    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}