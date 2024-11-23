package org.example.controllers;

import org.example.models.ReadBookEntity;
import org.example.services.basics.ReadBookService;
import java.util.List;

public class ReadBookController {
    private final ReadBookService readBookService;

    public ReadBookController(ReadBookService readBookService) {
        this.readBookService = readBookService;
    }

    // Đánh dấu sách là đã đọc
    public boolean markBookAsRead(int readBookId) {
        return readBookService.markAsRead(readBookId);  // Trả về kết quả success/failure
    }

    // Lấy danh sách sách đã đọc
    public List<ReadBookEntity> getAllReadBooks() {
        return readBookService.getReadBooksByUser();  // Trả về danh sách sách đã đọc
    }

    // Kiểm tra sách đã đọc hay chưa
    public boolean checkIfBookIsRead(int readBookId) {
        return readBookService.isBookRead(readBookId);  // Trả về kết quả true/false
    }

    // Xóa đánh dấu sách đã đọc
    public boolean unmarkBookAsRead(int readBookId) {
        return readBookService.unmarkAsRead(readBookId);  // Trả về kết quả success/failure
    }
}
