package org.example.controllers;

import org.example.models.BorrowedBookEntity;
import org.example.services.basics.BorrowedBookService;

import java.time.LocalDateTime;
import java.util.List;

public class BorrowedBookController {
    private final BorrowedBookService borrowedBookService;

    // Khởi tạo BorrowedBookController với BorrowedBookService
    public BorrowedBookController(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    // Gọi dịch vụ để ghi nhận mượn sách
    public boolean borrowBook(int borrowedBookId, LocalDateTime borrowDate) {
        return borrowedBookService.borrowBook(borrowedBookId, borrowDate);
    }

    // Gọi dịch vụ để ghi nhận trả sách
    public boolean returnBook(int borrowedBookId, LocalDateTime returnDate) {
        return  borrowedBookService.returnBook(borrowedBookId, returnDate);
    }

    // Lấy danh sách các sách chưa trả của người dùng
    public List<BorrowedBookEntity> listNotReturnedBooks() {
        // Gọi dịch vụ để lấy danh sách sách chưa trả
        return borrowedBookService.findNotReturnedBooksByUser();
    }

    // Lấy danh sách các sách mượn của người dùng trong khoảng thời gian
    public List<BorrowedBookEntity> listBorrowedBooksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Gọi dịch vụ để lấy danh sách sách mượn trong khoảng thời gian
        return borrowedBookService.findBorrowedBooksByUserAndDateRange(startDate, endDate);
    }

    // Lấy số lượng sách đã đọc
    public int getBorrowedBooksCountByUser() {
        return borrowedBookService.getBorrowedBooksCountByUser();
    }
}
