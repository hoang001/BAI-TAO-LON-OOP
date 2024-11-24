package org.example.daos.interfaces;

import org.example.models.BookEntity;
import java.sql.SQLException;
import java.util.List;

/**
 * Giao diện cho các thao tác CRUD đối với BookEntity.
 */
public interface BookDao {

    /**
     * Thêm một cuốn sách mới.
     *
     * @param book đối tượng BookEntity chứa thông tin sách.
     * @return true nếu thêm sách thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean addBook(BookEntity book) throws SQLException;

    /**
     * Xóa một cuốn sách theo ID.
     *
     * @param bookId ID của cuốn sách cần xóa.
     * @return true nếu xóa sách thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean deleteBookById(int bookId) throws SQLException;

    /**
     * Xóa một cuốn sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách cần xóa.
     * @return true nếu xóa sách thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean deleteBookByIsbn(String isbn) throws SQLException;

    /**
     * Cập nhật thông tin sách.
     *
     * @param book đối tượng BookEntity chứa thông tin sách cập nhật.
     * @return true nếu cập nhật thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean updateBook(BookEntity book) throws SQLException;

    /**
     * Cập nhật trạng thái của cuốn sách (có sẵn hay không).
     *
     * @param bookId ID của cuốn sách.
     * @param available trạng thái có sẵn của sách.
     * @return true nếu cập nhật trạng thái thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    boolean updateBookAvailability(int bookId, boolean available) throws SQLException;

    boolean updateBookQuantity(String isbn, int quantity) throws SQLException;

    /**
     * Tìm sách theo ISBN.
     *
     * @param isbn ISBN của cuốn sách.
     * @return danh sách các cuốn sách có ISBN trùng khớp.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    BookEntity findBookByIsbn(String isbn) throws SQLException;

    /**
     * Tìm sách theo tiêu đề.
     *
     * @param title tiêu đề của cuốn sách.
     * @return danh sách các cuốn sách có tiêu đề trùng khớp.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByTitle(String title) throws SQLException;

    /**
     * Tìm sách theo tác giả.
     *
     * @param authorName tên tác giả của cuốn sách.
     * @return danh sách các cuốn sách có tác giả trùng khớp.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByAuthor(String authorName) throws SQLException;

    /**
     * Tìm sách theo thể loại.
     *
     * @param genre thể loại của cuốn sách.
     * @return danh sách các cuốn sách thuộc thể loại trùng khớp.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByGenre(String genre) throws SQLException;

    /**
     * Tìm sách theo tên nhà xuất bản.
     *
     * @param publisherName tên nhà xuất bản của cuốn sách.
     * @return danh sách các cuốn sách có nhà xuất bản trùng khớp.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BookEntity> findBooksByPublisher(String publisherName) throws SQLException;

    /**
     * Lấy tất cả các cuốn sách trong cơ sở dữ liệu.
     *
     * @return danh sách tất cả các cuốn sách.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    List<BookEntity> findAllBooks() throws SQLException;

    // /**
    //  * Đếm số lượng sách có sẵn (đang ở trạng thái true) theo ISBN.
    //  *
    //  * @param isbn ISBN của cuốn sách.
    //  * @return số lượng sách có sẵn.
    //  * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
    //  */
    // int countAvailableBooksByIsbn(String isbn) throws SQLException;

    /**
     * Tìm sách theo ID.
     *
     * @param bookId ID của cuốn sách.
     * @return đối tượng BookEntity chứa thông tin sách, null nếu không tìm thấy.
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu.
     */
    BookEntity findBookById(int bookId) throws SQLException;

    boolean isBookInDatabase(String isbn) throws SQLException;
}
