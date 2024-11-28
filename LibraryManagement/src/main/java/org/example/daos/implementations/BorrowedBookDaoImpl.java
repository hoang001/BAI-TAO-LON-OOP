package org.example.daos.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.daos.interfaces.BorrowedBookDao;
import org.example.models.BorrowedBookEntity;
import org.example.utils.DatabaseConnection;

/**
 * Triển khai các phương thức trong giao diện BorrowedBookDAO. Quản lý các thao tác liên quan đến
 * mượn sách trong cơ sở dữ liệu.
 */
public class BorrowedBookDaoImpl implements BorrowedBookDao {
  private final Connection connection;

  /** Hàm khởi tạo để thiết lập kết nối cơ sở dữ liệu. */
  public BorrowedBookDaoImpl() {
    this.connection = DatabaseConnection.getConnection();
  }

  /**
   * Ghi nhận thông tin mượn sách vào cơ sở dữ liệu.
   *
   * @param bookId ID của sách được mượn
   * @param userName Tên người mượn sách
   * @param borrowDate Thời gian mượn sách
   * @return true nếu việc mượn sách thành công, false nếu không
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public boolean borrowBook(int bookId, String userName, LocalDate borrowDate, LocalDate returnDate)
      throws SQLException {
    String query =
        "INSERT INTO borrowedBooks (bookId, userName, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, bookId);
      preparedStatement.setString(2, userName);
      preparedStatement.setObject(3, borrowDate);
      preparedStatement.setObject(4, returnDate);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Cập nhật thông tin trả sách vào cơ sở dữ liệu.
   *
   * @param bookId ID của sách được trả
   * @param userName Tên người dùng trả sách
   * @return true nếu việc trả sách thành công, false nếu không
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public boolean returnBook(int bookId, String userName) throws SQLException {
    LocalDate returnDate = LocalDate.now();
    String query = "UPDATE borrowedBooks SET returnDate = ? WHERE bookId = ? AND userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setObject(1, returnDate);
      preparedStatement.setInt(2, bookId);
      preparedStatement.setString(3, userName);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Lấy tất cả sách đã mượn của người dùng trong khoảng thời gian cho trước.
   *
   * @param userName Tên người mượn sách
   * @param startDate Ngày bắt đầu tìm kiếm
   * @param endDate Ngày kết thúc tìm kiếm
   * @return danh sách các sách đã mượn của người dùng trong khoảng thời gian
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public List<BorrowedBookEntity> findBorrowedBooksByUser(
      String userName, LocalDate startDate, LocalDate endDate) throws SQLException {
    List<BorrowedBookEntity> borrowedBooks = new ArrayList<>();
    String query = "SELECT * FROM borrowedBooks WHERE userName = ? AND borrowDate BETWEEN ? AND ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      preparedStatement.setObject(2, startDate);
      preparedStatement.setObject(3, endDate);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        BorrowedBookEntity borrowedBook =
            new BorrowedBookEntity(
                resultSet.getInt("borrowedBookId"),
                resultSet.getInt("bookId"),
                resultSet.getString("userName"),
                resultSet.getObject("borrowDate", LocalDate.class),
                resultSet.getObject("returnDate", LocalDate.class));
        borrowedBooks.add(borrowedBook);
      }
    }
    return borrowedBooks;
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
    String query =
        "SELECT COUNT(*) "
            + "FROM borrowedBooks "
            + "INNER JOIN books USING(bookId) "
            + "WHERE borrowedBooks.bookId = ? AND borrowedBooks.userName = ? "
            + "AND books.available = false";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        BorrowedBookEntity borrowedBook =
            new BorrowedBookEntity(
                resultSet.getInt("borrowedBookId"),
                resultSet.getInt("bookId"),
                resultSet.getString("userName"),
                resultSet.getObject("borrowDate", LocalDate.class),
                resultSet.getObject("returnDate", LocalDate.class));
        borrowedBooks.add(borrowedBook);
      }
    }
    return borrowedBooks;
  }

  /**
   * Lấy danh sách các sách mà người dùng đã mượn quá hạn.
   *
   * @param userName Tên người dùng
   * @param returnDate Ngày hiện tại
   * @return danh sách các sách đã quá hạn của người dùng
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public List<BorrowedBookEntity> findOverdueBooksByUser(String userName, LocalDate returnDate)
      throws SQLException {
    List<BorrowedBookEntity> overdueBooks = new ArrayList<>();
    String query = "SELECT * FROM borrowedBooks WHERE userName = ? AND now()>?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      preparedStatement.setObject(2, returnDate);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          BorrowedBookEntity overdueBook =
              new BorrowedBookEntity(
                  resultSet.getInt("borrowedBookId"),
                  resultSet.getInt("bookId"),
                  resultSet.getString("userName"),
                  resultSet.getObject("borrowDate", LocalDate.class),
                  resultSet.getObject("returnDate", LocalDate.class));
          overdueBooks.add(overdueBook);
        }
      }
    }
    return overdueBooks;
  }

  /**
   * Lấy số lượng sách đã mượn của người dùng.
   *
   * @param userName Tên người mượn sách
   * @return số lượng sách đã mượn
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public int findBorrowedBooksCountByUser(String userName) throws SQLException {
    String query = "SELECT COUNT(DISTINCT bookId) FROM borrowedBooks WHERE userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    }
    return 0;
  }

  /**
   * Kiểm tra xem người dùng đã mượn cuốn sách hay chưa.
   *
   * @param bookId ID của sách
   * @param userName Tên người mượn sách
   * @return true nếu sách đã mượn, false nếu không
   * @throws SQLException Nếu có lỗi trong quá trình truy vấn cơ sở dữ liệu
   */
  @Override
  public boolean isBookBorrowedByUser(int bookId, String userName) throws SQLException {
    String query =
        "SELECT COUNT(*) "
            + "FROM borrowedBooks "
            + "INNER JOIN books b USING(bookId) "
            + "WHERE borrowedBooks.bookId = ? AND borrowedBooks.userName = ? "
            + "AND books.available = false";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, bookId);
      preparedStatement.setString(2, userName);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1) > 0;
      }
    }
    return false;
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
    String query = "SELECT * FROM borrowedBooks WHERE userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          BorrowedBookEntity book = new BorrowedBookEntity();
          book.setBookId(resultSet.getInt("bookId"));
          book.setUserName(resultSet.getString("userName"));
          book.setBorrowDate(resultSet.getDate("borrowDate").toLocalDate());
          book.setReturnDate(
              resultSet.getDate("returnDate") != null
                  ? resultSet.getDate("returnDate").toLocalDate()
                  : null);
          borrowedBooks.add(book);
        }
      }
    }
    return borrowedBooks;
  }

  /**
   * Tìm số lượng sách mượn theo ISBN.
   *
   * @param isbn ISBN của sách.
   * @return Số lượng sách đang được mượn.
   * @throws SQLException nếu có lỗi truy vấn cơ sở dữ liệu.
   */
  @Override
  public int findBorrowedBooksCountByIsbn(String isbn) throws SQLException {
    String query =
        "SELECT COUNT(bookId) AS borrowed_count"
            + "FROM borrowedBooks "
            + "INNER JOIN books USING(bookId) "
            + "WHERE isbn = ? "
            + "AND returnDate > now()";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, isbn);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt("borrowed_count");
      }
    }
    return 0;
  }
}
