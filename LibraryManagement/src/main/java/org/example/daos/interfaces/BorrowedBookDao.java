package org.example.daos.interfaces;

import org.example.models.BorrowedBookEntity;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Giao diện cho các phương thức thao tác với dữ liệu sách mượn.
 */
public interface BorrowedBookDao {

    /**
     * Mượn sách.
     *
     * @param borrowedBookId ID của sách mượn.
     * @param userName Tên người dùng mượn sách.
     * @param borrowDate Ngày mượn sách.
     * @param returnDate Ngày trả sách.
     * @return true nếu mượn sách thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean borrowBook(int borrowedBookId, String userName, LocalDate borrowDate, LocalDate returnDate) throws SQLException;

    /**
     * Trả sách.
     *
     * @param borrowedBookId ID của sách mượn.
     * @param userName Tên người dùng trả sách.
     * @return true nếu trả sách thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean returnBook(int borrowedBookId, String userName) throws SQLException;

    /**
     * Tìm danh sách sách mượn theo tên người dùng trong khoảng thời gian.
     *
     * @param userName Tên người dùng.
     * @param startDate Ngày bắt đầu.
     * @param endDate Ngày kết thúc.
     * @return Danh sách BorrowedBookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findBorrowedBooksByUser(String userName, LocalDate startDate, LocalDate endDate) throws SQLException;

    /**
     * Tìm danh sách sách chưa trả theo tên người dùng.
     *
     * @param userName Tên người dùng.
     * @return Danh sách BorrowedBookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findNotReturnedBooksByUser(String userName) throws SQLException;

    /**
     * Tìm danh sách sách quá hạn theo tên người dùng.
     *
     * @param userName Tên người dùng.
     * @param currentDate Ngày hiện tại.
     * @return Danh sách BorrowedBookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findOverdueBooksByUser(String userName, LocalDate currentDate) throws SQLException;

    /**
     * Kiểm tra sách có đang được mượn bởi người dùng không.
     *
     * @param borrowedBookId ID của sách mượn.
     * @param userName Tên người dùng.
     * @return true nếu sách đang được mượn bởi người dùng, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isBookBorrowedByUser(int borrowedBookId, String userName) throws SQLException;

    /**
     * Tìm số lượng sách mượn của người dùng.
     *
     * @param userName Tên người dùng.
     * @return Số lượng sách mượn.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    int findBorrowedBooksCountByUser(String userName) throws SQLException;

    /**
     * Tìm tất cả sách mượn của người dùng.
     *
     * @param userName Tên người dùng.
     * @return Danh sách tất cả BorrowedBookEntity của người dùng.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BorrowedBookEntity> findAllBorrowedBooksByUser(String userName) throws SQLException;

    /**
     * Tìm số lượng sách mượn theo ISBN.
     *
     * @param isbn ISBN của sách.
     * @return Số lượng sách mượn.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    int findBorrowedBooksCountByIsbn(String isbn) throws SQLException;
}
