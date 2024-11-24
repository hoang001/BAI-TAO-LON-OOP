package org.example.daos.interfaces;

import org.example.models.BorrowedBookEntity;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Giao diện cho các thao tác quản lý lịch sử mượn sách của người dùng.
 */
public interface BorrowedBookDao {

    /**
     * Ghi nhận việc mượn sách của người dùng.
     *
     * @param borrowedBookId ID của cuốn sách được mượn.
     * @param userName tên người dùng.
     * @param borrowDate ngày mượn sách.
     * @return true nếu ghi nhận mượn sách thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean borrowBook(int borrowedBookId, String userName, LocalDateTime borrowDate) throws SQLException;

    /**
     * Cập nhật khi người dùng trả sách.
     *
     * @param borrowedBookId ID của cuốn sách trả lại.
     * @param userName tên người dùng.
     * @return true nếu trả sách thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean returnBook(int borrowedBookId, String userName) throws SQLException;

    /**
     * Lấy danh sách các sách đã mượn của người dùng trong khoảng thời gian cho trước.
     *
     * @param userName tên người dùng.
     * @param startDate ngày bắt đầu khoảng thời gian.
     * @param endDate ngày kết thúc khoảng thời gian.
     * @return danh sách các cuốn sách đã mượn trong khoảng thời gian cho trước.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findBorrowedBooksByUser(String userName, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

    /**
     * Lấy danh sách các sách chưa trả của người dùng.
     *
     * @param userName tên người dùng.
     * @return danh sách các cuốn sách chưa trả.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findNotReturnedBooksByUser(String userName) throws SQLException;

    /**
     * Kiểm tra xem một cuốn sách đã được người dùng mượn chưa.
     *
     * @param borrowedBookId ID của cuốn sách.
     * @param userName tên người dùng.
     * @return true nếu người dùng đã mượn cuốn sách, false nếu chưa mượn.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean isBookBorrowedByUser(int borrowedBookId, String userName) throws SQLException;

    /**
     * Lấy số lượng sách đã mượn của người dùng.
     *
     * @param userName tên người dùng.
     * @return số lượng sách đã mượn.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    int findBorrowedBooksCountByUser(String userName) throws SQLException;

    /**
     * Lấy danh sách tất cả các sách mà người dùng đã mượn.
     *
     * @param userName tên người dùng.
     * @return danh sách tất cả các cuốn sách đã mượn.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findAllBorrowedBooksByUser(String userName) throws SQLException;
}
