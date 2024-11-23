package org.example.daos.interfaces;

import org.example.models.ReadBookEntity;

import java.sql.SQLException;
import java.util.List;

public interface ReadBookDAO {
    // Đánh dấu sách đã đọc
    boolean markAsRead(int bookId, String userName) throws SQLException;

    // Lấy danh sách sách đã đọc
    List<ReadBookEntity> getReadBooks(String userName) throws SQLException;

    // Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa
    boolean isBookRead(int bookId, String userName) throws SQLException;

    // Xóa đánh dấu sách đã đọc
    boolean unmarkAsRead(int bookId, String userName) throws SQLException;

    // Lấy số lượng sách đã đọc
    int getReadBooksCountByUser(String userName) throws SQLException;
}
