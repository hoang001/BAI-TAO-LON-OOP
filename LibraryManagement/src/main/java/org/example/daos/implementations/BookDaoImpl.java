package org.example.daos.implementations;

import org.example.daos.interfaces.BookDao;
import org.example.models.BookEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO Books (isbn, title, authorId, publisherId, publicationYear, categoryId, bookCoverDirectory) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthorName());
            preparedStatement.setString(4, book.getPublisherName());
            preparedStatement.setInt(5, book.getPublicationYear());
            preparedStatement.setString(6, book.getCategoryName());
            preparedStatement.setString(7, book.getBookCoverDirectory());
            int result = preparedStatement.executeUpdate();
            return result > 0; // Trả về true nếu có ít nhất 1 bản ghi được thêm vào cơ sở dữ liệu
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
            return result > 0; // Trả về true nếu có ít nhất 1 bản ghi bị xóa
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
            return result > 0; // Trả về true nếu có ít nhất 1 bản ghi bị xóa
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
        String sql = "UPDATE Books SET title = ?, authorId = ?, publisherId = ?, publicationYear = ?, categoryId = ?, bookCoverDirectory = ? WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(7, book.getIsbn());
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthorName());
            preparedStatement.setString(3, book.getPublisherName());
            preparedStatement.setInt(4, book.getPublicationYear());
            preparedStatement.setString(5, book.getCategoryName());
            preparedStatement.setString(6, book.getBookCoverDirectory());
            int result = preparedStatement.executeUpdate();
            return result > 0; // Trả về true nếu có ít nhất 1 bản ghi được cập nhật
        }
    }

    /**
     * Cập nhật trạng thái khả dụng của sách.
     * 
     * @param bookId ID của sách cần cập nhật.
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
            return rowsUpdated > 0; // Trả về true nếu có ít nhất 1 bản ghi được cập nhật
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
                        resultSet.getInt("PublicationYear"),
                        resultSet.getString("CategoryName"),
                        resultSet.getString("BookCoverDirectory"),
                        resultSet.getBoolean("Available")
                );
            }
        }
        return null; // Nếu không tìm thấy sách, trả về null
    }

    /**
     * Tìm sách theo ISBN.
     * 
     * @param isbn ISBN của sách cần tìm.
     * @return Danh sách sách tìm được.
     * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
     */
    @Override
    public List<BookEntity> findBookByIsbn(String isbn) throws SQLException {
        String query = "SELECT * FROM Books WHERE isbn = ?";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                books.add(new BookEntity(
                    resultSet.getInt("BookID"),
                    resultSet.getString("ISBN"),
                    resultSet.getString("Title"),
                    resultSet.getString("AuthorName"),
                    resultSet.getString("PublisherName"),
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
            return books; // Trả về danh sách sách tìm được
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
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách sách tìm được
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
        String query = "SELECT * FROM Books WHERE authorId = (SELECT authorId FROM Authors WHERE name LIKE ?)";
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
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách sách tìm được
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
        String query = "SELECT * FROM Books WHERE categoryId = (SELECT categoryId FROM Categories WHERE name LIKE ?)";
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
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách sách tìm được
    }

    /**
     * Tìm sách theo năm xuất bản.
     * 
     * @param year Năm xuất bản của sách cần tìm.
     * @return Danh sách sách tìm được.
     * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
     */
    @Override
    public List<BookEntity> findBooksByYear(int year) throws SQLException {
        String query = "SELECT * FROM Books WHERE publicationYear = ?";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                    resultSet.getInt("BookID"),
                    resultSet.getString("ISBN"),
                    resultSet.getString("Title"),
                    resultSet.getString("AuthorName"),
                    resultSet.getString("PublisherName"),
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách sách tìm được
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
        String query = "SELECT * FROM Books WHERE publisherName = (SELECT publisherName FROM Publishers WHERE name LIKE ?)";
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
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách sách tìm được
    }

    /**
     * Lấy tất cả sách trong cơ sở dữ liệu.
     * 
     * @return Danh sách tất cả sách.
     * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
     */
    @Override
    public List<BookEntity> getAllBooks() throws SQLException {
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
                    resultSet.getInt("PublicationYear"),
                    resultSet.getString("CategoryName"),
                    resultSet.getString("BookCoverDirectory"),
                    resultSet.getBoolean("Available")
                ));
            }
        }
        return books; // Trả về danh sách tất cả sách
    }

    /**
     * Đếm số lượng sách có sẵn theo ISBN.
     * 
     * @param isbn ISBN của sách cần đếm.
     * @return Số lượng sách có sẵn.
     * @throws SQLException Nếu có lỗi khi thực hiện câu lệnh SQL.
     */
    @Override
    public int countAvailableBooksByIsbn(String isbn) throws SQLException {
        String query = "SELECT COUNT(*) FROM Books WHERE ISBN = ? AND Available = TRUE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0; // Nếu không có sách nào có sẵn, trả về 0
    }
}