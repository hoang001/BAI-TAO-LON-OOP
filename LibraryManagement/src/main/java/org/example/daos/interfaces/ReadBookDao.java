package org.example.daos.interfaces;

import org.example.models.ReadBookEntity;
import java.sql.SQLException;
import java.util.List;

/**
 * Giao diện cho các phương thức thao tác với dữ liệu sách đã đọc.
 */
public interface ReadBookDao {

    /**
     * Đánh dấu sách là đã đọc.
     *
     * @param bookId ID của sách.
     * @param userName Tên người dùng.
     * @return true nếu đánh dấu thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean markAsRead(int bookId, String userName) throws SQLException;

    /**
     * Tìm danh sách sách đã đọc theo tên người dùng.
     *
     * @param userName Tên người dùng.
     * @return Danh sách ReadBookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<ReadBookEntity> findReadBooks(String userName) throws SQLException;

    /**
     * Kiểm tra sách đã được đánh dấu là đọc chưa.
     *
     * @param bookId ID của sách.
     * @param userName Tên người dùng.
     * @return true nếu sách đã được đánh dấu là đọc, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isBookRead(int bookId, String userName) throws SQLException;

    /**
     * Bỏ đánh dấu sách là đã đọc.
     *
     * @param bookId ID của sách.
     * @param userName Tên người dùng.
     * @return true nếu bỏ đánh dấu thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean unmarkAsRead(int bookId, String userName) throws SQLException;

    /**
     * Tìm số lượng sách đã đọc của người dùng.
     *
     * @param userName Tên người dùng.
     * @return Số lượng sách đã đọc.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    int findReadBooksCountByUser(String userName) throws SQLException;
}
