package org.example.controllers;

import java.time.LocalDate;
import java.util.List;
import org.example.models.BorrowedBookEntity;
import org.example.services.basics.BorrowedBookService;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến sách mượn. Các chức năng bao gồm
 * ghi nhận mượn sách, trả sách, lấy danh sách sách chưa trả, lấy danh sách sách mượn trong khoảng
 * thời gian và lấy số lượng sách mượn của người dùng. Lớp này tương tác với BorrowedBookService để
 * thực hiện các thao tác liên quan đến sách mượn.
 */
public class BorrowedBookController {

  // Đối tượng BorrowedBookService được sử dụng để thực hiện các chức năng liên quan đến sách mượn.
  private final BorrowedBookService borrowedBookService;

  /**
   * Constructor khởi tạo đối tượng BorrowedBookService. Phương thức này được gọi khi tạo đối tượng
   * BorrowedBookController để quản lý các thao tác liên quan đến sách mượn.
   */
  public BorrowedBookController() {
    this.borrowedBookService = new BorrowedBookService();
  }

  /**
   * Ghi nhận việc mượn sách.
   *
   * @param borrowedBookId ID của sách mượn.
   * @param borrowDate Thời gian mượn sách.
   * @return boolean Trả về true nếu mượn sách thành công, false nếu thất bại.
   */
  public boolean borrowBook(int borrowedBookId, LocalDate borrowDate, LocalDate returnDate) {
    return borrowedBookService.borrowBook(borrowedBookId, borrowDate, returnDate);
  }

  /**
   * Ghi nhận việc trả sách.
   *
   * @param borrowedBookId ID của sách đã mượn.
   * @return boolean Trả về true nếu trả sách thành công, false nếu thất bại.
   */
  public boolean returnBook(int borrowedBookId) {
    return borrowedBookService.returnBook(borrowedBookId);
  }

  /**
   * Lấy danh sách các sách chưa trả của người dùng.
   *
   * @return Danh sách các sách chưa trả của người dùng.
   */
  public List<BorrowedBookEntity> getNotReturnedBooksByUser() {
    return borrowedBookService.getNotReturnedBooksByUser();
  }

  /**
   * Lấy danh sách các sách mượn của người dùng trong khoảng thời gian nhất định.
   *
   * @param startDate Thời gian bắt đầu của khoảng thời gian.
   * @param endDate Thời gian kết thúc của khoảng thời gian.
   * @return Danh sách sách mượn của người dùng trong khoảng thời gian từ
   *     startDate đến endDate.
   */
  public List<BorrowedBookEntity> getBooksBorrowedInDateRange(
      LocalDate startDate, LocalDate endDate) {
    return borrowedBookService.getBorrowedBooksByUserAndDateRange(startDate, endDate);
  }

  /**
   * Lấy số lượng sách đã mượn của người dùng.
   *
   * @return int Số lượng sách đã mượn của người dùng.
   */
  public int getBorrowedBooksCountByUser() {
    return borrowedBookService.getBorrowedBooksCountByUser();
  }
}
