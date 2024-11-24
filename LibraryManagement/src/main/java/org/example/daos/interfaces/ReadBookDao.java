package org.example.daos.interfaces;

import org.example.models.ReadBookEntity;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface cho các thao tác quản lý sách đã đọc của người dùng.
 * Cung cấp các phương thức để đánh dấu sách đã đọc, lấy danh sách sách đã đọc,
 * kiểm tra trạng thái sách đã đọc và xóa đánh dấu sách đã đọc.
 */
public interface ReadBookDao {

    /**
     * Đánh dấu sách là đã đọc cho người dùng.
     *
     * @param bookId   ID của sách cần đánh dấu là đã đọc.
     * @param userName Tên người dùng thực hiện thao tác.
     * @return true nếu đánh dấu thành công, false nếu có lỗi hoặc sách đã được đánh dấu trước đó.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean markAsRead(int bookId, String userName) throws SQLException;

    /**
     * Tìm danh sách sách đã đọc của người dùng.
     *
     * @param userName Tên người dùng cần lấy danh sách sách đã đọc.
     * @return Danh sách các đối tượng ReadBookEntity chứa thông tin sách đã đọc.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<ReadBookEntity> findReadBooks(String userName) throws SQLException;

    /**
     * Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa.
     *
     * @param bookId   ID của sách cần kiểm tra.
     * @param userName Tên người dùng cần kiểm tra.
     * @return true nếu sách đã được đánh dấu là đã đọc, false nếu chưa.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isBookRead(int bookId, String userName) throws SQLException;

    /**
     * Xóa đánh dấu sách đã đọc của người dùng.
     *
     * @param bookId   ID của sách cần xóa đánh dấu đã đọc.
     * @param userName Tên người dùng thực hiện thao tác.
     * @return true nếu xóa đánh dấu thành công, false nếu có lỗi hoặc sách chưa được đánh dấu.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean unmarkAsRead(int bookId, String userName) throws SQLException;

    /**
     * Tìm số lượng sách đã đọc của người dùng.
     *
     * @param userName Tên người dùng cần lấy số lượng sách đã đọc.
     * @return Số lượng sách đã đọc của người dùng.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    int findReadBooksCountByUser(String userName) throws SQLException;
}
