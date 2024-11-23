package org.example.daos.interfaces;

import org.example.models.BookEntity;

import java.sql.SQLException;
import java.util.List;

// Giao diện cho các thao tác CRUD đối với BookEntity
public interface BookDAO {
    // Thêm sách mới
    boolean addBook(BookEntity book) throws SQLException;

    // Xóa sách theo ID
    boolean deleteBookByID(int bookId) throws SQLException;

    // Xóa sách theo ISBN
    boolean deleteBookByISBN(String isbn) throws SQLException;

    // Cập nhật tài liệu
    boolean updateBook(BookEntity book) throws SQLException;

    // Cập nhật trạng thái sách
    boolean updateBookAvailability(int bookId, boolean available) throws SQLException;

    // Tìm sách theo ISBN
    List<BookEntity> findBookByISBN(String isbn) throws SQLException;

    // Tìm sách theo tiêu đề
    List<BookEntity> findBooksByTitle(String title) throws SQLException;

    // Tìm sách theo tác giả
    List<BookEntity> findBooksByAuthor(String authorName) throws SQLException;

    // Tìm sách theo thể loại
    List<BookEntity> findBooksByGenre(String genre) throws SQLException;

    // Tìm sách theo năm xuất bản
    List<BookEntity> findBooksByYear(int year) throws SQLException;

    // Tìm sách theo nhà xuất bản
    List<BookEntity> findBooksByPublisher(String publisherName) throws SQLException;

    // Lấy tất cả sách
    List<BookEntity> getAllBooks() throws SQLException;

    // Đếm số lượng sách đang ở trạng thái true theo ISBN
    int countAvailableBooksByISBN(String isbn) throws SQLException;

    // Tìm sách theo ID
    BookEntity findBookById(int bookId) throws SQLException;
}
