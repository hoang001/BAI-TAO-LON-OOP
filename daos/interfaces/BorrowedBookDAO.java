package org.example.daos.interfaces;

import org.example.models.BorrowedBookEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// Giao diện cho các thao tác quản lý lịch sử mượn sách
public interface BorrowedBookDAO {
    // Ghi nhận mượn sách
    boolean borrowBook(int borrowedBookId, String userName, LocalDateTime borrowDate) throws SQLException;

    // Cập nhật khi trả sách
    boolean returnBook(int borrowedBookId, String userName) throws SQLException;

    // Lấy danh sách các sách đã mượn của người dùng trong khoảng thời gian
    List<BorrowedBookEntity> findBorrowedBooksByUser(String userName, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

    // Lấy danh sách sách chưa trả của người dùng
    List<BorrowedBookEntity> findNotReturnedBooksByUser(String userName) throws SQLException;

    // Kiểm tra xem một người dùng đã mượn một cuốn sách chưa
    boolean isBookBorrowedByUser(int borrowedBookId, String userName) throws SQLException;

    // Lấy số lượng sách đã mượn của người dùng
    int getBorrowedBooksCountByUser(String userName) throws SQLException;

    // Lấy danh sách tất cả các sách mà người dùng đã mượn
    List<BorrowedBookEntity> findAllBorrowedBooksByUser(String userName) throws SQLException;
}
