package org.example.models;

/**
 * Lớp đại diện cho một cuốn sách trong hệ thống.
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
    private int quantity;

    /**
     * Khởi tạo một đối tượng BookEntity mặc định.
     * Khởi tạo đối tượng BookEntity mà không cần thông tin đầu vào.
     */
    public BookEntity() {}

    /**
     * Khởi tạo một đối tượng BookEntity với các thông tin chi tiết về sách.
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
     * @param quantity           Số lượng sách có sẵn.
     */
    public BookEntity(int bookId, String isbn, String title, String authorName, 
    String publisherName, int publicationYear, String categoryName, 
    String bookCoverDirectory, boolean available, int quantity) {
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

    /**
     * Lấy số lượng sách có sẵn.
     *
     * @return Số lượng sách có sẵn.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Đặt số lượng sách có sẵn.
     *
     * @param quantity Số lượng sách cần thiết lập.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
