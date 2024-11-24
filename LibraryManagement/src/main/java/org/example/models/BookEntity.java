package org.example.models;

/**
 * Lớp này đại diện cho thực thể sách, bao gồm các thông tin liên quan đến sách như ISBN, tên sách, 
 * tác giả, nhà xuất bản, năm xuất bản, thể loại, đường dẫn bìa sách và trạng thái sẵn có của sách.
 * Lớp này mở rộng từ BaseEntity để kế thừa thuộc tính ID của sách.
 */
public class BookEntity extends BaseEntity {

    // Thuộc tính của lớp BookEntity
    private String isbn;               // ISBN của sách
    private String title;              // Tên sách
    private String authorName;         // Tên tác giả
    private String publisherName;      // Tên nhà xuất bản
    private int publicationYear;       // Năm xuất bản
    private String categoryName;       // Tên thể loại sách
    private String bookCoverDirectory; // Đường dẫn đến bìa sách
    private boolean available;         // Trạng thái có sẵn của sách
    private int quantity;

    /**
     * Constructor mặc định cho BookEntity.
     * Khởi tạo đối tượng BookEntity mà không cần thông tin đầu vào.
     */
    public BookEntity() {}

    /**
     * Constructor cho BookEntity với các thông tin chi tiết về sách.
     *
     * @param bookId             ID của sách.
     * @param isbn               ISBN của sách.
     * @param title              Tên sách.
     * @param authorName         Tên tác giả của sách.
     * @param publisherName      Tên nhà xuất bản của sách.
     * @param publicationYear    Năm xuất bản sách.
     * @param categoryName       Thể loại của sách.
     * @param bookCoverDirectory Đường dẫn bìa sách.
     * @param available          Trạng thái có sẵn của sách.
     * @param quantity
     */
    public BookEntity(int bookId, String isbn, String title, String authorName, String publisherName, int publicationYear, String categoryName, String bookCoverDirectory, boolean available, int quantity) {
        super(bookId);  // Gọi constructor của lớp cha BaseEntity để thiết lập ID
        this.isbn = isbn;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.publicationYear = publicationYear;
        this.categoryName = categoryName;
        this.bookCoverDirectory = bookCoverDirectory;
        this.available = available;
        this.quantity = quantity;
    }

    /**
     * Lấy ISBN của sách.
     * 
     * @return ISBN của sách.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Đặt ISBN cho sách.
     * 
     * @param isbn ISBN của sách cần thiết lập.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Lấy tên sách.
     * 
     * @return Tên sách.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Đặt tên sách.
     * 
     * @param title Tên sách cần thiết lập.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Lấy tên tác giả của sách.
     * 
     * @return Tên tác giả của sách.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Đặt tên tác giả của sách.
     * 
     * @param authorName Tên tác giả cần thiết lập.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Lấy tên nhà xuất bản của sách.
     * 
     * @return Tên nhà xuất bản.
     */
    public String getPublisherName() {
        return publisherName;
    }

    /**
     * Đặt tên nhà xuất bản của sách.
     * 
     * @param publisherName Tên nhà xuất bản cần thiết lập.
     */
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * Lấy năm xuất bản của sách.
     * 
     * @return Năm xuất bản sách.
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Đặt năm xuất bản của sách.
     * 
     * @param publicationYear Năm xuất bản cần thiết lập.
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Lấy tên thể loại sách.
     * 
     * @return Tên thể loại của sách.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Đặt tên thể loại sách.
     * 
     * @param categoryName Tên thể loại cần thiết lập.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Lấy đường dẫn đến bìa sách.
     * 
     * @return Đường dẫn đến bìa sách.
     */
    public String getBookCoverDirectory() {
        return bookCoverDirectory;
    }

    /**
     * Đặt đường dẫn đến bìa sách.
     * 
     * @param bookCoverDirectory Đường dẫn bìa sách cần thiết lập.
     */
    public void setBookCoverDirectory(String bookCoverDirectory) {
        this.bookCoverDirectory = bookCoverDirectory;
    }

    /**
     * Lấy trạng thái có sẵn của sách (đang có sẵn hay đã mượn).
     * 
     * @return Trạng thái có sẵn của sách.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Đặt trạng thái có sẵn của sách (có thể mượn hay không).
     * 
     * @param available Trạng thái có sẵn của sách cần thiết lập.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
