package org.example.daos.implementations;

import org.example.daos.interfaces.BorrowedBookDao;
import org.example.models.BorrowedBookEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức trong giao diện BorrowedBookDAO.
 * Quản lý các thao tác liên quan đến mượn sách trong cơ sở dữ liệu.
 */
public class BorrowedBookDaoImpl implements BorrowedBookDao {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public BorrowedBookDaoImpl() {
        this.connection = DatabaseConnection.getConnection();  // Lấy kết nối từ DatabaseConnection
    }

    /**
     * Ghi nhận thông tin mượn sách vào cơ sở dữ liệu.
     * 
     * @param bookId    ID của sách được mượn
     * @param userName  Tên người mượn sách
     * @param borrowDate Thời gian mượn sách
     * @return true nếu việc mượn sách thành công, false nếu không
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public boolean borrowBook(int bookId, String userName, LocalDateTime borrowDate) throws SQLException {
        // Câu lệnh SQL để chèn thông tin mượn sách vào bảng BorrowedBooks
        String query = "INSERT INTO BorrowedBooks (bookId, userName, borrowDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, userName);
            preparedStatement.setObject(3, borrowDate);

            // Thực hiện câu lệnh SQL và kiểm tra xem có bản ghi nào được chèn vào hay không
            int result = preparedStatement.executeUpdate();
            return result > 0;  // Nếu có ít nhất 1 bản ghi được chèn thành công, trả về true
        }
    }

    /**
     * Cập nhật thông tin trả sách vào cơ sở dữ liệu.
     * 
     * @param bookId    ID của sách được trả
     * @param userName  Tên người dùng trả sách
     * @return true nếu việc trả sách thành công, false nếu không
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public boolean returnBook(int bookId, String userName) throws SQLException {
        // Lấy thời gian hiện tại
        LocalDateTime returnDate = LocalDateTime.now();
    
        // Câu lệnh SQL để cập nhật ngày trả sách trong bảng BorrowedBooks
        String query = "UPDATE BorrowedBooks SET returnDate = ? WHERE bookId = ? AND userName = ?";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setObject(1, returnDate);  // Đặt thời gian trả sách
            preparedStatement.setInt(2, bookId);         // Đặt ID sách
            preparedStatement.setString(3, userName);    // Đặt tên người dùng
    
            // Thực hiện câu lệnh SQL và kiểm tra kết quả
            int result = preparedStatement.executeUpdate();
            return result > 0;  // Nếu có ít nhất 1 bản ghi được cập nhật thành công, trả về true
        }
    }

    /**
     * Tìm tất cả sách đã mượn của người dùng trong khoảng thời gian cho trước.
     * 
     * @param userName  Tên người mượn sách
     * @param startDate Ngày bắt đầu tìm kiếm
     * @param endDate   Ngày kết thúc tìm kiếm
     * @return danh sách các sách đã mượn của người dùng trong khoảng thời gian
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public List<BorrowedBookEntity> findBorrowedBooksByUser(String userName, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<BorrowedBookEntity> borrowedBooks = new ArrayList<>();

        // Câu lệnh SQL để lấy tất cả sách đã mượn của người dùng trong khoảng thời gian
        String query = "SELECT * FROM BorrowedBooks WHERE userName = ? AND borrowDate BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setString(1, userName);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            // Thực thi câu lệnh SQL và duyệt kết quả
            ResultSet resultSet = preparedStatement.executeQuery();

            // Đọc dữ liệu từ resultSet và tạo đối tượng BorrowedBookEntity để thêm vào danh sách
            while (resultSet.next()) {
                BorrowedBookEntity borrowedBook = new BorrowedBookEntity(
                        resultSet.getInt("borrowedBookId"),        // Lấy borrowedBookId từ bảng BorrowedBooks
                        resultSet.getInt("bookId"),        // Lấy bookId từ bảng Books
                        resultSet.getString("userName"),   // Lấy userName từ bảng BorrowedBooks
                        resultSet.getObject("borrowDate", LocalDateTime.class),
                        resultSet.getObject("returnDate", LocalDateTime.class)
                );
                borrowedBooks.add(borrowedBook);  // Thêm sách vào danh sách
            }
        }
        return borrowedBooks;  // Trả về danh sách các sách đã mượn
    }

    /**
     * Tìm tất cả các sách chưa được trả của người dùng.
     * 
     * @param userName Tên người mượn sách
     * @return danh sách các sách chưa trả của người dùng
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public List<BorrowedBookEntity> findNotReturnedBooksByUser(String userName) throws SQLException {
        List<BorrowedBookEntity> borrowedBooks = new ArrayList<>();

        // Câu lệnh SQL để lấy các sách chưa trả
        String query = "SELECT COUNT(*) " +
                       "FROM BorrowedBooks bb " +
                       "INNER JOIN Books b ON bb.bookId = b.bookId " +
                       "WHERE bb.bookId = ? AND bb.userName = ? AND b.available = false";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho tham số userName trong câu lệnh SQL
            preparedStatement.setString(1, userName);

            // Thực thi câu lệnh SQL và duyệt kết quả
            ResultSet resultSet = preparedStatement.executeQuery();

            // Đọc dữ liệu từ resultSet và tạo đối tượng BorrowedBookEntity
            while (resultSet.next()) {
                BorrowedBookEntity borrowedBook = new BorrowedBookEntity(
                        resultSet.getInt("borrowedBookId"),
                        resultSet.getInt("bookId"),        // Lấy borrowedBookId từ bảng BorrowedBooks
                        resultSet.getString("userName"),   // Lấy userName từ bảng BorrowedBooks
                        resultSet.getObject("borrowDate", LocalDateTime.class),
                        resultSet.getObject("returnDate", LocalDateTime.class)
                );
                borrowedBooks.add(borrowedBook);  // Thêm sách vào danh sách
            }
        }
        return borrowedBooks;  // Trả về danh sách các sách chưa trả hoặc đã quá hạn
    }

    /**
     * Lấy số lượng sách đã mượn của người dùng.
     * 
     * @param userName Tên người mượn sách
     * @return số lượng sách đã mượn
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public int getBorrowedBooksCountByUser(String userName) throws SQLException {
        String query = "SELECT COUNT(DISTINCT bookId) FROM BorrowedBooks WHERE userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);  // Trả về số lượng sách đã mượn
            }
        }
        return 0;  // Trả về 0 nếu không có sách mượn
    }

    /**
     * Kiểm tra xem người dùng đã mượn cuốn sách hay chưa.
     * 
     * @param bookId    ID của sách
     * @param userName  Tên người mượn sách
     * @return true nếu sách đã mượn, false nếu không
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public boolean isBookBorrowedByUser(int bookId, String userName) throws SQLException {
        // Câu lệnh SQL để kiểm tra xem người dùng đã mượn cuốn sách này chưa và sách đó có trạng thái available = false
        String query = "SELECT COUNT(*) " +
                       "FROM BorrowedBooks bb " +
                       "INNER JOIN Books b ON bb.bookId = b.bookId " +
                       "WHERE bb.bookId = ? AND bb.userName = ? AND b.available = false";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setInt(1, bookId);  // Đặt ID sách
            preparedStatement.setString(2, userName);  // Đặt tên người dùng
    
            // Thực thi câu lệnh SQL và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;  // Nếu có ít nhất 1 bản ghi (sách đã mượn và chưa trả), trả về true
            }
        }
        return false;  // Nếu không có bản ghi nào, trả về false
    }

    /**
     * Tìm tất cả sách đã mượn của người dùng.
     * 
     * @param userName Tên người mượn sách
     * @return danh sách các sách đã mượn của người dùng
     * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
     */
    @Override
    public List<BorrowedBookEntity> findAllBorrowedBooksByUser(String userName) throws SQLException {
        List<BorrowedBookEntity> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM BorrowedBooks WHERE userName = ?";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    BorrowedBookEntity book = new BorrowedBookEntity();
                    book.setBookId(resultSet.getInt("bookId"));
                    book.setUserName(resultSet.getString("userName"));
                    book.setBorrowDate(resultSet.getTimestamp("borrowDate").toLocalDateTime());
                    book.setReturnDate(resultSet.getTimestamp("returnDate") != null
                            ? resultSet.getTimestamp("returnDate").toLocalDateTime()
                            : null);
                    borrowedBooks.add(book);  // Thêm sách vào danh sách
                }
            }
        }
    
        return borrowedBooks;  // Trả về danh sách các sách đã mượn
    }
}
