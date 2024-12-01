package org.example.daos.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.daos.interfaces.ReadBookDao;
import org.example.models.ReadBookEntity;
import org.example.utils.DatabaseConnection;

/** Lớp triển khai ReadBookDAO để thực hiện các thao tác CRUD đối với sách đã đọc. */
public class ReadBookDaoImpl implements ReadBookDao {
  private final Connection connection;

  /** Constructor để khởi tạo kết nối cơ sở dữ liệu. */
  public ReadBookDaoImpl() {
    this.connection = DatabaseConnection.getConnection();
  }

  /**
   * Đánh dấu một cuốn sách là đã đọc.
   *
   * @param bookId ID của cuốn sách.
   * @param userName Tên người dùng.
   * @return true nếu đánh dấu thành công, ngược lại false.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public boolean markAsRead(int bookId, String userName) throws SQLException {
    String query = "INSERT INTO readBooks (bookId, userName) VALUES (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, bookId);
      preparedStatement.setString(2, userName);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Lấy danh sách các cuốn sách đã đọc theo tên người dùng.
   *
   * @param userName Tên người dùng.
   * @return Danh sách các đối tượng ReadBookEntity.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public List<ReadBookEntity> findReadBooks(String userName) throws SQLException {
    String query =
        "SELECT readId, userName, bookId, isbn, title, "
            + "authorId, b.publisherId, b.publicationYear, b.categoryId, b.bookCoverDirectory "
            + "FROM ReadBooks rb "
            + "JOIN Books b ON rb.bookId = b.bookId "
            + "WHERE rb.userName = ?";

    List<ReadBookEntity> readBooks = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          ReadBookEntity readBook =
              new ReadBookEntity(
                  resultSet.getInt("readId"),
                  resultSet.getInt("bookId"),
                  resultSet.getString("userName"));
          readBooks.add(readBook);
        }
      }
    }
    return readBooks;
  }

  /**
   * Kiểm tra xem một cuốn sách đã được đọc bởi người dùng chưa.
   *
   * @param bookId ID của cuốn sách.
   * @param userName Tên người dùng.
   * @return true nếu sách đã được đọc, ngược lại false.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public boolean isBookRead(int bookId, String userName) throws SQLException {
    String query = "SELECT COUNT(*) FROM readBooks WHERE bookId = ? AND userName = ?";
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
   * Bỏ đánh dấu một cuốn sách là đã đọc.
   *
   * @param bookId ID của cuốn sách.
   * @param userName Tên người dùng.
   * @return true nếu bỏ đánh dấu thành công, ngược lại false.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public boolean unmarkAsRead(int bookId, String userName) throws SQLException {
    String query = "DELETE FROM readBooks WHERE bookId = ? AND userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, bookId);
      preparedStatement.setString(2, userName);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Tìm số lượng sách đã đọc theo tên người dùng.
   *
   * @param userName Tên người dùng.
   * @return Số lượng sách đã đọc.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public int findReadBooksCountByUser(String userName) throws SQLException {
    String query = "SELECT COUNT(DISTINCT bookId) FROM readBooks WHERE userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, userName);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    }
    return 0;
  }
}
