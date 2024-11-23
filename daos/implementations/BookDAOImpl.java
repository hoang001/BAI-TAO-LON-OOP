package org.example.daos.implementations;

import org.example.daos.interfaces.BookDAO;
import org.example.models.BookEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public BookDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    // Phương thức thêm sách
    public boolean addBook(BookEntity book) throws SQLException {
        String sql = "INSERT INTO Books (isbn, title, authorId, publisherId, publicationYear, categoryId, bookCoverDirectory) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setInt(3, book.getAuthorId());
            preparedStatement.setInt(4, book.getPublisherId());
            preparedStatement.setInt(5, book.getPublicationYear());
            preparedStatement.setInt(6, book.getCategoryId());
            preparedStatement.setString(7, book.getBookCoverDirectory());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    // Phương thức xóa sách theo ID
    public boolean deleteBookByID(int bookId) throws SQLException {
        String sql = "DELETE FROM Books WHERE bookId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    // Phương thức xóa sách theo ISBN
    public boolean deleteBookByISBN(String isbn) throws SQLException {
        String sql = "DELETE FROM Books WHERE isbn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    // Phương thức cập nhật thông tin sách
    public boolean updateBook(BookEntity book) throws SQLException {
        String sql = "UPDATE Books SET title = ?, authorId = ?, publisherId = ?, publicationYear = ?, categoryId = ?, bookCoverDirectory = ? WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(7, book.getIsbn());
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getAuthorId());
            preparedStatement.setInt(3, book.getPublisherId());
            preparedStatement.setInt(4, book.getPublicationYear());
            preparedStatement.setInt(5, book.getCategoryId());
            preparedStatement.setString(6, book.getBookCoverDirectory());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

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
                        resultSet.getInt("AuthorID"),
                        resultSet.getInt("PublisherID"),
                        resultSet.getInt("PublicationYear"),
                        resultSet.getInt("CategoryID"),
                        resultSet.getString("BookCoverDirectory"),
                        resultSet.getBoolean("Available")
                );
            }
        }
        return null; // Nếu không tìm thấy sách, trả về null
    }


    // Tìm sách bằng ISBN
    @Override
    public List<BookEntity> findBookByISBN(String isbn) throws SQLException {
        String query = "SELECT * FROM Books WHERE isbn = ?";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
            return null;
        }
    }

    // Tìm sách theo tiêu đề
    @Override
    public List<BookEntity> findBooksByTitle(String title) throws SQLException {
        String query = "SELECT * FROM Books WHERE title LIKE ?";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    // Tìm sách theo tác giả
    @Override
    public List<BookEntity> findBooksByAuthor(String authorName) throws SQLException {
        String query = "SELECT * FROM Books WHERE authorId = (SELECT authorId FROM Authors WHERE name LIKE ?)";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + authorName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    // Tìm sách theo thể loại
    @Override
    public List<BookEntity> findBooksByGenre(String genre) throws SQLException {
        String query = "SELECT * FROM Books WHERE categoryId = (SELECT categoryId FROM Categories WHERE name LIKE ?)";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + genre + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    // Tìm sách theo năm xuất bản
    @Override
    public List<BookEntity> findBooksByYear(int year) throws SQLException {
        String query = "SELECT * FROM Books WHERE publicationYear = ?";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    // Tìm sách theo nhà xuất bản
    @Override
    public List<BookEntity> findBooksByPublisher(String publisherName) throws SQLException {
        String query = "SELECT * FROM Books WHERE publisherName = (SELECT publisherName FROM Publishers WHERE name LIKE ?)";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + publisherName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    // Lấy tất cả sách trong cơ sở dữ liệu
    @Override
    public List<BookEntity> getAllBooks() throws SQLException {
        String query = "SELECT * FROM Books";
        List<BookEntity> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                books.add(new BookEntity(
                        resultSet.getInt("bookId"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getInt("authorId"),
                        resultSet.getInt("publisherId"),
                        resultSet.getInt("publicationYear"),
                        resultSet.getInt("categoryId"),
                        resultSet.getString("bookCoverDirectory"),
                        resultSet.getBoolean("available")
                ));
            }
        }
        return books;
    }

    @Override
    public int countAvailableBooksByISBN(String isbn) throws SQLException {
        String query = "SELECT COUNT(*) FROM Books WHERE ISBN = ? AND Available = TRUE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }

}
