package org.example.controllers;

import org.example.models.BorrowedBookEntity;
import org.example.services.basics.BorrowedBookService;

import java.time.LocalDate;
import java.util.List;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến sách mượn.
 * Các chức năng bao gồm ghi nhận mượn sách, trả sách, lấy danh sách sách chưa trả,
 * lấy danh sách sách mượn trong khoảng thời gian và lấy số lượng sách mượn của người dùng.
 * Lớp này tương tác với BorrowedBookService để thực hiện các thao tác liên quan đến sách mượn.
 */
public class BorrowedBookController {

    // Đối tượng BorrowedBookService được sử dụng để thực hiện các chức năng liên quan đến sách mượn.
    private final BorrowedBookService borrowedBookService;

    /**
     * Constructor khởi tạo đối tượng BorrowedBookService.
     * Phương thức này được gọi khi tạo đối tượng BorrowedBookController để quản lý các thao tác liên quan đến sách mượn.
     */
    public BorrowedBookController() {
        this.borrowedBookService = new BorrowedBookService();  // Khởi tạo BorrowedBookService để xử lý các yêu cầu liên quan đến sách mượn.
    }

    /**
     * Ghi nhận việc mượn sách.
     * 
     * @param borrowedBookId ID của sách mượn.
     * @param borrowDate Thời gian mượn sách.
     * @return boolean Trả về true nếu mượn sách thành công, false nếu thất bại.
     */
    public boolean borrowBook(int borrowedBookId, LocalDate borrowDate, LocalDate returnDate) {
        return borrowedBookService.borrowBook(borrowedBookId, borrowDate, returnDate);  // Gọi phương thức của BorrowedBookService để ghi nhận mượn sách.
    }

    /**
     * Ghi nhận việc trả sách.
     * 
     * @param borrowedBookId ID của sách đã mượn.
     * @param returnDate Thời gian trả sách.
     * @return boolean Trả về true nếu trả sách thành công, false nếu thất bại.
     */
    public boolean returnBook(int borrowedBookId, LocalDate returnDate) {
        return borrowedBookService.returnBook(borrowedBookId, returnDate);  // Gọi phương thức của BorrowedBookService để ghi nhận trả sách.
    }

    /**
     * Lấy danh sách các sách chưa trả của người dùng.
     * 
     * @return List<BorrowedBookEntity> Danh sách các sách chưa trả của người dùng.
     */
    public List<BorrowedBookEntity> listNotReturnedBooks() {
        return borrowedBookService.getNotReturnedBooksByUser();  // Gọi phương thức của BorrowedBookService để lấy danh sách sách chưa trả.
    }

    /**
     * Lấy danh sách các sách mượn của người dùng trong khoảng thời gian nhất định.
     * 
     * @param startDate Thời gian bắt đầu của khoảng thời gian.
     * @param endDate Thời gian kết thúc của khoảng thời gian.
     * @return List<BorrowedBookEntity> Danh sách sách mượn của người dùng trong khoảng thời gian từ startDate đến endDate.
     */
    public List<BorrowedBookEntity> listBorrowedBooksByDateRange(LocalDate startDate, LocalDate endDate) {
        return borrowedBookService.getBorrowedBooksByUserAndDateRange(startDate, endDate);  // Gọi phương thức của BorrowedBookService để lấy danh sách sách mượn trong khoảng thời gian.
    }

    /**
     * Lấy số lượng sách đã mượn của người dùng.
     * 
     * @return int Số lượng sách đã mượn của người dùng.
     */
    public int getBorrowedBooksCountByUser() {
        return borrowedBookService.getBorrowedBooksCountByUser();  // Gọi phương thức của BorrowedBookService để lấy số lượng sách đã mượn của người dùng.
    }
}
