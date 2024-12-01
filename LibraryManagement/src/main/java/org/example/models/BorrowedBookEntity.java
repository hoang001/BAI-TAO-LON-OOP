package org.example.models;

import java.time.LocalDate;

/** Lớp đại diện cho sách được mượn bởi người dùng. */
public class BorrowedBookEntity extends BaseEntity {

  private int bookId;
  private String userName;
  private LocalDate borrowDate;
  private LocalDate returnDate;

  /** Khởi tạo một đối tượng BorrowedBookEntity mặc định. */
  public BorrowedBookEntity() {}

  /**
   * Khởi tạo một đối tượng BorrowedBookEntity với các thông tin chi tiết.
   *
   * @param borrowId ID của mượn sách
   * @param bookId ID của sách
   * @param userName Tên người dùng mượn sách
   * @param borrowDate Ngày mượn sách
   * @param returnDate Ngày trả sách
   */
  public BorrowedBookEntity(int borrowId, int bookId, String userName,
      LocalDate borrowDate, LocalDate returnDate) {
    super(borrowId);
    this.bookId = bookId;
    this.userName = userName;
    this.borrowDate = borrowDate;
    this.returnDate = returnDate;
  }

  /**
   * Lấy ID của sách.
   *
   * @return ID của sách
   */
  public int getBookId() {
    return bookId;
  }

  /**
   * Thiết lập ID của sách.
   *
   * @param bookId ID của sách
   */
  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  /**
   * Lấy tên người dùng mượn sách.
   *
   * @return Tên người dùng mượn sách
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Thiết lập tên người dùng mượn sách.
   *
   * @param userName Tên người dùng mượn sách
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Lấy ngày mượn sách.
   *
   * @return Ngày mượn sách
   */
  public LocalDate getBorrowDate() {
    return borrowDate;
  }

  /**
   * Thiết lập ngày mượn sách.
   *
   * @param borrowDate Ngày mượn sách
   */
  public void setBorrowDate(LocalDate borrowDate) {
    this.borrowDate = borrowDate;
  }

  /**
   * Lấy ngày trả sách.
   *
   * @return Ngày trả sách
   */
  public LocalDate getReturnDate() {
    return returnDate;
  }

  /**
   * Thiết lập ngày trả sách.
   *
   * @param returnDate Ngày trả sách
   */
  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }
}
