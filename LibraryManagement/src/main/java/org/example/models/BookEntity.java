package org.example.models;

/**
 * Class đại diện cho thực thể sách, mở rộng từ BaseEntity.
 */
public class BookEntity extends BaseEntity {
    private String isbn;
    private String title;
    private String authorName;
    private String publisherName;
    private int publicationYear;
    private String categoryName;
    private String bookCoverDirectory;
    private boolean available;

    /**
     * Constructor mặc định cho BookEntity.
     */
    public BookEntity() {}

    /**
     * Constructor có tham số cho BookEntity.
     *
     * @param bookId ID của sách.
     * @param isbn ISBN của sách.
     * @param title Tên sách.
     * @param authorName ID của tác giả.
     * @param publisherName ID của nhà xuất bản.
     * @param publicationYear Năm xuất bản.
     * @param categoryName ID của thể loại.
     * @param bookCoverDirectory Đường dẫn bìa sách.
     * @param available Trạng thái sẵn có của sách.
     */
    public BookEntity(int bookId, String isbn, String title, String authorName, String publisherName, int publicationYear, String categoryName, String bookCoverDirectory, boolean available) {
        super(bookId);
        this.isbn = isbn;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.publicationYear = publicationYear;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setcategoryName(String categoryName) {
        this.categoryName = categoryName;
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
