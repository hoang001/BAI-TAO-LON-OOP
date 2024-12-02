package org.example.daos.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.daos.interfaces.BookDao;
import org.example.models.BookEntity;
import org.example.utils.DatabaseConnection;

/**
 * Cài đặt interface BookDAO để thực hiện các thao tác với bảng Books trong cơ sở dữ liệu.
 */
public class BookDaoImpl implements BookDao {

  private final Connection connection;

  // Constructor để khởi tạo kết nối cơ sở dữ liệu
  public BookDaoImpl() {
    this.connection = DatabaseConnection.getConnection();
  }

  /**
   * Thêm một cuốn sách mới vào cơ sở dữ liệu.
   *
   * @param book Thông tin sách cần thêm.
   * @return true nếu thêm thành công, false nếu thất bại.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public boolean addBook(BookEntity book) throws SQLException {
    String sql = "INSERT INTO Books (isbn, title, authorName, publisherName, publishedDate, Category, bookCoverDirectory, available, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, book.getIsbn());
      preparedStatement.setString(2, book.getTitle());
      preparedStatement.setString(3, book.getAuthorName());
      preparedStatement.setString(4, book.getPublisherName());
      preparedStatement.setString(5, book.getPublishedDate());
      preparedStatement.setString(6, book.getCategory());
      preparedStatement.setString(7, book.getBookCoverDirectory());
      preparedStatement.setBoolean(8, book.isAvailable());
      preparedStatement.setInt(9, book.getQuantity());
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Xóa sách theo ID.
   *
   * @param bookId ID của sách cần xóa.
   * @return true nếu xóa thành công, false nếu thất bại.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public boolean deleteBookById(int bookId) throws SQLException {
    String sql = "DELETE FROM Books WHERE bookId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, bookId);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Xóa sách theo ISBN.
   *
   * @param isbn ISBN của sách cần xóa.
   * @return true nếu xóa thành công, false nếu thất bại.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public boolean deleteBookByIsbn(String isbn) throws SQLException {
    String sql = "DELETE FROM Books WHERE isbn = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, isbn);
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Cập nhật thông tin sách trong cơ sở dữ liệu.
   *
   * @param book Thông tin sách cần cập nhật.
   * @return true nếu cập nhật thành công, false nếu thất bại.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public boolean updateBook(BookEntity book) throws SQLException {
    String sql = "UPDATE Books SET title = ?, authorName = ?, publisherName = ?, publishedDate = ?, category = ?, bookCoverDirectory = ?, available = ?, quantity = ? WHERE isbn = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(7, book.getIsbn());
      preparedStatement.setString(1, book.getTitle());
      preparedStatement.setString(2, book.getAuthorName());
      preparedStatement.setString(3, book.getPublisherName());
      preparedStatement.setString(4, book.getPublishedDate());
      preparedStatement.setString(5, book.getCategory());
      preparedStatement.setString(6, book.getBookCoverDirectory());
      preparedStatement.setBoolean(8, book.isAvailable());
      preparedStatement.setInt(9, book.getQuantity());
      int result = preparedStatement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Cập nhật trạng thái khả dụng của sách.
   *
   * @param bookId    ID của sách cần cập nhật.
   * @param available Trạng thái khả dụng của sách (true nếu có sẵn, false nếu không có sẵn).
   * @return true nếu cập nhật thành công, false nếu thất bại.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public boolean updateBookAvailability(int bookId, boolean available) throws SQLException {
    String query = "UPDATE Books SET available = ? WHERE bookId = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setBoolean(1, available);
      statement.setInt(2, bookId);
      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    }
  }


  /**
   * Cập nhật số lượng sách theo ISBN.
   *
   * @param isbn     ISBN của cuốn sách.
   * @param quantity Số lượng sách muốn cập nhật (có thể là số dương để tăng hoặc số âm để giảm).
   * @return true nếu cập nhật số lượng thành công, false nếu có lỗi.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public boolean updateBookQuantity(String isbn, int quantity) throws SQLException {
    String query = "UPDATE Books SET quantity = quantity + ? WHERE isbn = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, quantity);
      statement.setString(2, isbn);
      int result = statement.executeUpdate();
      return result > 0;
    }
  }

  /**
   * Tìm sách theo ID.
   *
   * @param bookId ID của sách cần tìm.
   * @return Sách tìm thấy nếu có, null nếu không tìm thấy.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public BookEntity findBookById(int bookId) throws SQLException {
    String query = "SELECT * FROM Books WHERE BookID = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, bookId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        );
      }
    }
    return null;
  }

  /**
   * Tìm sách theo ISBN.
   *
   * @param isbn ISBN của sách cần tìm.
   * @return Danh sách sách tìm được.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public BookEntity findBookByIsbn(String isbn) throws SQLException {
    String query = "SELECT * FROM Books WHERE isbn = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, isbn);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        );
      }
      return null;
    }
  }

  /**
   * Tìm sách theo tiêu đề.
   *
   * @param title Tiêu đề của sách cần tìm.
   * @return Danh sách sách tìm được.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public List<BookEntity> findBooksByTitle(String title) throws SQLException {
    String query = "SELECT * FROM Books WHERE title LIKE ?";
    List<BookEntity> books = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, "%" + title + "%");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        ));
      }
    }
    return books;
  }

  /**
   * Tìm sách theo tác giả.
   *
   * @param authorName Tên tác giả.
   * @return Danh sách sách của tác giả.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public List<BookEntity> findBooksByAuthor(String authorName) throws SQLException {
    String query = "SELECT * FROM Books WHERE authorName LIKE ?";
    List<BookEntity> books = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, "%" + authorName + "%");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        ));
      }
    }
    return books;
  }

  /**
   * Tìm sách theo thể loại.
   *
   * @param genre Thể loại sách cần tìm.
   * @return Danh sách sách tìm được.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public List<BookEntity> findBooksByGenre(String genre) throws SQLException {
    String query = "SELECT * FROM Books WHERE category LIKE ?";
    List<BookEntity> books = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, "%" + genre + "%");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        ));
      }
    }
    return books;
  }

  /**
   * Tìm sách theo nhà xuất bản.
   *
   * @param publisherName Tên nhà xuất bản.
   * @return Danh sách sách tìm được.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public List<BookEntity> findBooksByPublisher(String publisherName) throws SQLException {
    String query = "SELECT * FROM Books WHERE publisherName LIKE ?";
    List<BookEntity> books = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, "%" + publisherName + "%");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        books.add(new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        ));
      }
    }
    return books;
  }

  /**
   * Lấy tất cả sách trong cơ sở dữ liệu.
   *
   * @return Danh sách tất cả sách.
   * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
   */
  @Override
  public List<BookEntity> findAllBooks() throws SQLException {
    String query = "SELECT * FROM Books";
    List<BookEntity> books = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        books.add(new BookEntity(
            resultSet.getInt("BookID"),
            resultSet.getString("ISBN"),
            resultSet.getString("Title"),
            resultSet.getString("AuthorName"),
            resultSet.getString("PublisherName"),
            resultSet.getString("PublishedDate"),
            resultSet.getString("Category"),
            resultSet.getString("BookCoverDirectory"),
            resultSet.getBoolean("Available"),
            resultSet.getInt("Quantity")
        ));
      }
    }
    return books;
  }

  /**
   * Kiểm tra sách có tồn tại trong cơ sở dữ liệu theo ISBN.
   *
   * @param isbn ISBN của cuốn sách cần kiểm tra.
   * @return true nếu sách tồn tại trong cơ sở dữ liệu, ngược lại false.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public boolean isBookInDatabase(String isbn) throws SQLException {
    String query = "SELECT COUNT(*) FROM books WHERE isbn = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, isbn);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1) > 0;
        }
      }
    }
    return false;
  }
}
