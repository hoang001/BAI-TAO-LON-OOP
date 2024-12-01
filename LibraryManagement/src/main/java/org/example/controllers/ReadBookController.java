package org.example.controllers;

import java.util.List;
import org.example.models.ReadBookEntity;
import org.example.services.basics.ReadBookService;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến việc đánh dấu sách đã đọc, bao
 * gồm đánh dấu sách là đã đọc, lấy danh sách sách đã đọc, kiểm tra sách đã đọc hay chưa và xóa đánh
 * dấu sách đã đọc. Lớp này tương tác với ReadBookService để thực hiện các thao tác trên sách đã
 * đọc.
 */
public class ReadBookController {

  // Đối tượng ReadBookService được sử dụng để thực hiện các chức năng liên quan đến sách đã đọc.
  private final ReadBookService readBookService;

  /**
   * Constructor khởi tạo đối tượng ReadBookService. Phương thức này được gọi khi tạo đối tượng
   * ReadBookController để quản lý các thao tác đánh dấu sách đã đọc.
   */
  public ReadBookController() {
    this.readBookService = new ReadBookService();
  }

  /**
   * Đánh dấu một cuốn sách là đã đọc.
   *
   * @param readBookId ID của cuốn sách mà người dùng muốn đánh dấu là đã đọc.
   * @return boolean Trả về true nếu đánh dấu sách là đã đọc thành công, false nếu thất bại.
   */
  public boolean markBookAsRead(int readBookId) {
    return readBookService.markAsRead(readBookId);
  }

  /**
   * Lấy danh sách tất cả các cuốn sách mà người dùng đã đọc.
   *
   * @return Danh sách các sách đã đọc của người dùng.
   */
  public List<ReadBookEntity> getAllReadBooks() {
    return readBookService.getReadBooksByUser();
  }

  /**
   * Kiểm tra xem một cuốn sách có được đánh dấu là đã đọc hay không.
   *
   * @param readBookId ID của cuốn sách cần kiểm tra.
   * @return boolean Trả về true nếu sách đã được đánh dấu là đã đọc, false nếu chưa.
   */
  public boolean checkIfBookIsRead(int readBookId) {
    return readBookService.isBookRead(readBookId);
  }

  /**
   * Xóa đánh dấu "đã đọc" của một cuốn sách.
   *
   * @param readBookId ID của cuốn sách mà người dùng muốn xóa đánh dấu "đã đọc".
   * @return boolean Trả về true nếu xóa đánh dấu thành công, false nếu thất bại.
   */
  public boolean unmarkBookAsRead(int readBookId) {
    return readBookService.unmarkAsRead(readBookId);
  }
}
