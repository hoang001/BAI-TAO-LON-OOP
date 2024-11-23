package org.example.dtos;

public class BookDTO {
    private int bookId;
    private String isbn;
    private String title;
    private String authorName;
    private String publisherName;
    private int publicationYear;
    private String category;
    private String bookCoverDirectory;

    // Phương thức khởi tạo không tham số
    public BookDTO() {}

    // Phương thức khởi tạo có tham số
    public BookDTO(int bookId, String isbn, String title, String authorName, String publisherName, int publicationYear, String category, String bookCoverDirectory) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.publicationYear = publicationYear;
        this.category = category;
        this.bookCoverDirectory = bookCoverDirectory;
    }

    // Các phương thức getter và setter
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookCoverDirectory() {
        return bookCoverDirectory;
    }

    public void setBookCoverDirectory(String bookCoverDirectory) {
        this.bookCoverDirectory = bookCoverDirectory;
    }
}
