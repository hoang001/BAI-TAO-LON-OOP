package org.example.controllers;

import java.util.List;
import org.example.models.BookEntity;
import org.example.services.basics.BookService;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến sách. Lớp này giúp xử lý các yêu
 * cầu từ người dùng, bao gồm thêm, xóa, cập nhật, và tìm kiếm sách thông qua các phương thức gọi
 * dịch vụ của BookService.
 */
public class BookController {

  // Đối tượng BookService để thực hiện các thao tác với sách.
  private final BookService bookService;

  /**
   * Constructor khởi tạo đối tượng BookService. Phương thức này được gọi khi tạo đối tượng
   * BookController để quản lý các thao tác với sách.
   */
  public BookController() {
    this.bookService = new BookService();
  }

  /**
   * Thêm một cuốn sách mới vào hệ thống.
   *
   * @param book Đối tượng BookEntity đại diện cho cuốn sách cần thêm.
   */
  public boolean addBook(BookEntity book) {
    return bookService.addBook(book);
  }

  /**
   * Xóa sách dựa trên mã Isbn.
   *
   * @param isbn Mã ISBN của sách cần xóa.
   */
  public boolean deleteBookByIsbn(String isbn) {
    return bookService.deleteBookByIsbn(isbn);
  }

  /**
   * Xóa sách dựa trên ID sách.
   *
   * @param bookId ID của sách cần xóa.
   */
  public boolean deleteBookById(int bookId) {
    return bookService.deleteBookById(bookId);
  }

  /**
   * Cập nhật thông tin sách.
   *
   * @param bookEntity Đối tượng BookEntity chứa thông tin sách cần cập nhật.
   */
  public boolean updateBook(BookEntity bookEntity) {
    return bookService.updateBook(bookEntity);
  }

  /**
   * Thêm số lượng sách theo ISBN.
   *
   * @param isbn ISBN của sách cần thêm số lượng.
   * @param quantity Số lượng sách cần thêm.
   * @return Trả về true nếu thêm thành công, false nếu thất bại.
   */
  public boolean addBookQuantity(String isbn, int quantity) {
    return bookService.addBookQuantity(isbn, quantity);
  }

  /**
   * Lấy thông tin sách theo ID sách.
   *
   * @param bookId ID của sách cần lấy thông tin.
   * @return BookEntity Đối tượng BookEntity chứa thông tin sách.
   */
  public BookEntity getBookById(int bookId) {
    return bookService.getBookById(bookId);
  }

  /**
   * Lấy thông tin sách theo mã ISBN.
   *
   * @param isbn Mã ISBN của sách cần lấy thông tin.
   * @return BookEntity Đối tượng BookEntity chứa thông tin sách.
   */
  public BookEntity getBookByIsbn(String isbn) {
    return bookService.getBookByIsbn(isbn);
  }

  /**
   * Lấy danh sách sách theo tiêu đề.
   *
   * @param title Tiêu đề của sách cần tìm.
   * @return Danh sách các cuốn sách có tiêu đề trùng với tham số title.
   */
  public List<BookEntity> getBooksByTitle(String title) {
    return bookService.getBooksByTitle(title);
  }

  /**
   * Lấy danh sách sách theo tên tác giả.
   *
   * @param authorName Tên tác giả của sách cần tìm.
   * @return Danh sách các cuốn sách của tác giả.
   */
  public List<BookEntity> getBooksByAuthor(String authorName) {
    return bookService.getBooksByAuthor(authorName);
  }

  /**
   * Lấy danh sách sách theo thể loại.
   *
   * @param genre Thể loại của sách cần tìm.
   * @return Danh sách các cuốn sách thuộc thể loại genre.
   */
  public List<BookEntity> getBooksByGenre(String genre) {
    return bookService.getBooksByGenre(genre);
  }

  /**
   * Lấy danh sách sách theo tên nhà xuất bản.
   *
   * @param publisherName Tên nhà xuất bản của sách cần tìm.
   * @return Danh sách các cuốn sách của nhà xuất bản.
   */
  public List<BookEntity> getBooksByPublisher(String publisherName) {
    return bookService.getBooksByPublisher(publisherName);
  }

  /**
   * Lấy tất cả sách trong hệ thống.
   *
   * @return Danh sách tất cả các cuốn sách.
   */
  public List<BookEntity> getAllBooks() {
    return bookService.getAllBooks();
  }
}
