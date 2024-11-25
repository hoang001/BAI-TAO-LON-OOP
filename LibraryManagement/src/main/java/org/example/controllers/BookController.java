package org.example.controllers;

import org.example.models.BookEntity;
import org.example.services.basics.BookService;

import java.util.List;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến sách.
 * Lớp này giúp xử lý các yêu cầu từ người dùng, bao gồm thêm, xóa, cập nhật, và tìm kiếm sách
 * thông qua các phương thức gọi dịch vụ của BookService.
 */
public class BookController {

    // Đối tượng BookService để thực hiện các thao tác với sách.
    private final BookService bookService;

    /**
     * Constructor khởi tạo đối tượng BookService.
     * Phương thức này được gọi khi tạo đối tượng BookController để quản lý các thao tác với sách.
     */
    public BookController() {
        this.bookService = new BookService();  // Khởi tạo BookService để xử lý các yêu cầu liên quan đến sách.
    }

    /**
     * Thêm một cuốn sách mới vào hệ thống.
     * 
     * @param book Đối tượng BookEntity đại diện cho cuốn sách cần thêm.
     */
    public void addBook(BookEntity book) {
        bookService.addBook(book);  // Gọi phương thức của BookService để thêm sách.
    }

    /**
     * Xóa sách dựa trên mã Isbn.
     * 
     * @param Isbn Mã ISBN của sách cần xóa.
     */
    public void deleteBookByIsbn(String Isbn) {
        bookService.deleteBookByIsbn(Isbn);  // Gọi phương thức của BookService để xóa sách theo ISBN.
    }

    /**
     * Xóa sách dựa trên ID sách.
     * 
     * @param bookId ID của sách cần xóa.
     */
    public void deleteBookById(int bookId) {
        bookService.deleteBookById(bookId);  // Gọi phương thức của BookService để xóa sách theo ID.
    }

    /**
     * Cập nhật thông tin sách.
     * 
     * @param bookEntity Đối tượng BookEntity chứa thông tin sách cần cập nhật.
     */
    public void updateBook(BookEntity bookEntity) {
        bookService.updateBook(bookEntity);  // Gọi phương thức của BookService để cập nhật sách.
    }

    /**
     * Lấy thông tin sách theo ID sách.
     * 
     * @param bookId ID của sách cần lấy thông tin.
     * @return BookEntity Đối tượng BookEntity chứa thông tin sách.
     */
    public BookEntity getBookByID(int bookId) {
        return bookService.getBookById(bookId);  // Gọi phương thức của BookService để lấy sách theo ID.
    }

    /**
     * Lấy thông tin sách theo mã ISBN.
     * 
     * @param Isbn Mã ISBN của sách cần lấy thông tin.
     * @return BookEntity Đối tượng BookEntity chứa thông tin sách.
     */
    public BookEntity getBookByIsbn(String Isbn) {
        return bookService.getBookByIsbn(Isbn);  // Gọi phương thức của BookService để lấy sách theo ISBN.
    }

    /**
     * Lấy danh sách sách theo tiêu đề.
     * 
     * @param title Tiêu đề của sách cần tìm.
     * @return List<BookEntity> Danh sách các cuốn sách có tiêu đề trùng với tham số title.
     */
    public List<BookEntity> getBooksByTitle(String title) {
        return bookService.getBooksByTitle(title);  // Gọi phương thức của BookService để lấy sách theo tiêu đề.
    }

    /**
     * Lấy danh sách sách theo tên tác giả.
     * 
     * @param authorName Tên tác giả của sách cần tìm.
     * @return List<BookEntity> Danh sách các cuốn sách của tác giả.
     */
    public List<BookEntity> getBooksByAuthor(String authorName) {
        return bookService.getBooksByAuthor(authorName);  // Gọi phương thức của BookService để lấy sách theo tác giả.
    }

    /**
     * Lấy danh sách sách theo thể loại.
     * 
     * @param genre Thể loại của sách cần tìm.
     * @return List<BookEntity> Danh sách các cuốn sách thuộc thể loại genre.
     */
    public List<BookEntity> getBooksByGenre(String genre) {
        return bookService.getBooksByGenre(genre);  // Gọi phương thức của BookService để lấy sách theo thể loại.
    }

    /**
     * Lấy danh sách sách theo tên nhà xuất bản.
     * 
     * @param publisherName Tên nhà xuất bản của sách cần tìm.
     * @return List<BookEntity> Danh sách các cuốn sách của nhà xuất bản.
     */
    public List<BookEntity> getBooksByPublisher(String publisherName) {
        return bookService.getBooksByPublisher(publisherName);  // Gọi phương thức của BookService để lấy sách theo nhà xuất bản.
    }

    /**
     * Lấy tất cả sách trong hệ thống.
     * 
     * @return List<BookEntity> Danh sách tất cả các cuốn sách.
     */
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();  // Gọi phương thức của BookService để lấy tất cả sách.
    }
}