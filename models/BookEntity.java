package org.example.models;

public class BookEntity extends BaseEntity{
    private String isbn;
    private String title;
    private int authorId;
    private int publisherId;
    private int publicationYear;
    private int categoryId;
    private String bookCoverDirectory;
    private boolean available;

    public BookEntity() {}
    public BookEntity(int bookId, String isbn, String title, int authorId, int publisherId, int publicationYear, int categoryId, String bookCoverDirectory, boolean available) {
        super(bookId);
        this.isbn = isbn;
        this.title = title;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.publicationYear = publicationYear;
        this.categoryId = categoryId;
        this.bookCoverDirectory = bookCoverDirectory;
        this.available = available;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBookCoverDirectory() {
        return bookCoverDirectory;
    }

    public void setBookCoverDirectory(String bookCoverDirectory) {
        this.bookCoverDirectory = bookCoverDirectory;
    }

    public boolean getBooStatus() {
        return available;
    }
    
    public void setavailable(boolean available) {
        this.available = available;
    }
}
