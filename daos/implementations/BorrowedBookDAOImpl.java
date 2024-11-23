package org.example.daos.implementations;

import org.example.daos.interfaces.BorrowedBookDAO;
import org.example.models.BorrowedBookEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Triển khai các phương thức trong giao diện BorrowedBookDAO
public class BorrowedBookDAOImpl implements BorrowedBookDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public BorrowedBookDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Phương thức ghi nhận mượn sách
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

    // Phương thức cập nhật khi trả sách
    @Override
    public boolean returnBook(int bookId, String userName) throws SQLException {
        // Lấy thời gian hiện tại
        LocalDateTime returnDate = LocalDateTime.now();
    
        // Câu lệnh SQL để cập nhật ngày trả sách trong bảng BorrowedBooks
        String query = "UPDATE BorrowedBooks SET returnDate = ? WHERE bookId = ? AND userName = ?";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setObject(1, returnDate);  // Gán thời gian hiện tại vào returnDate
            preparedStatement.setInt(2, bookId);  // Gán bookId
            preparedStatement.setString(3, userName);  // Gán userName
    
            // Thực hiện câu lệnh SQL và kiểm tra kết quả
            int result = preparedStatement.executeUpdate();
            return result > 0;  // Nếu có ít nhất 1 bản ghi được cập nhật thành công, trả về true
        }
    }


    // Phương thức lấy danh sách sách đã mượn của người dùng trong khoảng thời gian cho trước
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
                borrowedBooks.add(borrowedBook);
            }
        }
        return borrowedBooks;  // Trả về danh sách các sách đã mượn
    }

    // Phương thức lấy danh sách sách chưa trả của người dùng
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
                borrowedBooks.add(borrowedBook); // Thêm sách mượn vào danh sách
            }
        }
        return borrowedBooks;  // Trả về danh sách các sách chưa trả hoặc đã quá hạn
    }

    @Override
    public int getBorrowedBooksCountByUser(String userName) throws SQLException {
        String query = "SELECT COUNT(DISTINCT bookId) FROM BorrowedBooks WHERE userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    // Phương thức kiểm tra xem một cuốn sách có được mượn bởi người dùng hay không
    @Override
    public boolean isBookBorrowedByUser(int bookId, String userName) throws SQLException {
        // Câu lệnh SQL để kiểm tra xem người dùng đã mượn cuốn sách này chưa và sách đó có trạng thái available = false
        String query = "SELECT COUNT(*) " +
                       "FROM BorrowedBooks bb " +
                       "INNER JOIN Books b ON bb.bookId = b.bookId " +
                       "WHERE bb.bookId = ? AND bb.userName = ? AND b.available = false";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu lệnh SQL
            preparedStatement.setInt(1, bookId);  // Gán bookId
            preparedStatement.setString(2, userName);  // Gán userName
    
            // Thực thi câu lệnh SQL và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Nếu có ít nhất 1 bản ghi (tức là sách đã mượn và chưa trả và sách không có sẵn), trả về true
                return resultSet.getInt(1) > 0;
            }
        }
        return false;  // Nếu không có bản ghi nào, trả về false
    }

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
                    borrowedBooks.add(book);
                }
            }
        }
    
        return borrowedBooks;
    }

}
