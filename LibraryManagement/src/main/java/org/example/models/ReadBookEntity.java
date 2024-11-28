package org.example.models;

/** Lớp đại diện cho sách đã đọc của người dùng. */
public class ReadBookEntity extends BaseEntity {
  private int bookId;
  private String userName;

  /** Khởi tạo một đối tượng ReadBookEntity mặc định. */
  public ReadBookEntity() {}

  /**
   * Khởi tạo một đối tượng ReadBookEntity với các thông tin chi tiết.
   *
   * @param readId ID của sách đã đọc
   * @param bookId ID của sách
   * @param userName Tên người dùng
   */
  public ReadBookEntity(int readId, int bookId, String userName) {
    super(readId);
    this.bookId = bookId;
    this.userName = userName;
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
   * Lấy tên người dùng.
   *
   * @return Tên người dùng
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Thiết lập tên người dùng.
   *
   * @param userName Tên người dùng
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }
}
