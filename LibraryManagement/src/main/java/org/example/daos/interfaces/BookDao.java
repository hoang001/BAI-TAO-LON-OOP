package org.example.daos.interfaces;

import org.example.models.BookEntity;
import java.sql.SQLException;
import java.util.List;

/**
 * Giao diện cho các phương thức thao tác với dữ liệu sách.
 */
public interface BookDao {

    /**
     * Thêm sách mới vào cơ sở dữ liệu.
     *
     * @param book Đối tượng BookEntity chứa thông tin sách cần thêm.
     * @return true nếu thêm sách thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean addBook(BookEntity book) throws SQLException;

    /**
     * Xóa sách theo ID.
     *
     * @param bookId ID của sách cần xóa.
     * @return true nếu xóa sách thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean deleteBookById(int bookId) throws SQLException;

    /**
     * Xóa sách theo ISBN.
     *
     * @param isbn ISBN của sách cần xóa.
     * @return true nếu xóa sách thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean deleteBookByIsbn(String isbn) throws SQLException;

    /**
     * Cập nhật thông tin sách.
     *
     * @param book Đối tượng BookEntity chứa thông tin sách cần cập nhật.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updateBook(BookEntity book) throws SQLException;

    /**
     * Cập nhật trạng thái có sẵn của sách.
     *
     * @param bookId ID của sách.
     * @param available Trạng thái có sẵn của sách.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updateBookAvailability(int bookId, boolean available) throws SQLException;

    /**
     * Cập nhật số lượng sách.
     *
     * @param isbn ISBN của sách.
     * @param quantity Số lượng sách cần cập nhật.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updateBookQuantity(String isbn, int quantity) throws SQLException;

    /**
     * Tìm sách theo ISBN.
     *
     * @param isbn ISBN của sách.
     * @return Đối tượng BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    BookEntity findBookByIsbn(String isbn) throws SQLException;

    /**
     * Tìm sách theo tiêu đề.
     *
     * @param title Tiêu đề sách.
     * @return Danh sách BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByTitle(String title) throws SQLException;

    /**
     * Tìm sách theo tên tác giả.
     *
     * @param authorName Tên tác giả.
     * @return Danh sách BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByAuthor(String authorName) throws SQLException;

    /**
     * Tìm sách theo thể loại.
     *
     * @param genre Thể loại sách.
     * @return Danh sách BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByGenre(String genre) throws SQLException;

    /**
     * Tìm sách theo nhà xuất bản.
     *
     * @param publisherName Tên nhà xuất bản.
     * @return Danh sách BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByPublisher(String publisherName) throws SQLException;

    /**
     * Tìm tất cả sách.
     *
     * @return Danh sách tất cả BookEntity trong cơ sở dữ liệu.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<BookEntity> findAllBooks() throws SQLException;

    /**
     * Tìm sách theo ID.
     *
     * @param bookId ID của sách.
     * @return Đối tượng BookEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    BookEntity findBookById(int bookId) throws SQLException;

    /**
     * Kiểm tra sách có tồn tại trong cơ sở dữ liệu không.
     *
     * @param isbn ISBN của sách.
     * @return true nếu sách tồn tại, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isBookInDatabase(String isbn) throws SQLException;
}
