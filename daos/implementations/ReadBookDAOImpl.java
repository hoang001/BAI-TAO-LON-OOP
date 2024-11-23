package org.example.daos.implementations;

import org.example.daos.interfaces.ReadBookDAO;
import org.example.models.ReadBookEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadBookDAOImpl implements ReadBookDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public ReadBookDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean markAsRead(int bookId, String userName) throws SQLException {
        String query = "INSERT INTO ReadBooks (bookId, userName) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, userName);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public List<ReadBookEntity> getReadBooks(String userName) throws SQLException {
        String query = "SELECT rb.readId, rb.userName, b.bookId, b.isbn, b.title, b.authorId, b.publisherId, b.publicationYear, b.categoryId, b.bookCoverDirectory " +
                "FROM ReadBooks rb " +
                "JOIN Books b ON rb.bookId = b.bookId " +
                "WHERE rb.userName = ?";

        List<ReadBookEntity> readBooks = new ArrayList<>();
        // Thiết lập kết nối và thực thi câu truy vấn
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);  // Set userName vào truy vấn

            // Thực thi truy vấn và xử lý kết quả
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Tạo đối tượng ReadBookEntity từ kết quả truy vấn
                    ReadBookEntity readBook = new ReadBookEntity(
                            resultSet.getInt("readId"),
                            resultSet.getInt("booId"),       // Lấy readId từ bảng ReadBooks
                            resultSet.getString("userName")   // Lấy userName từ bảng ReadBooks
                    );
                    readBooks.add(readBook);
                }
            }
        }
        return readBooks;
    }

    @Override
    public boolean isBookRead(int bookId, String userName) throws SQLException {
        String query = "SELECT COUNT(*) FROM ReadBooks WHERE bookId = ? AND userName = ?";
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

    @Override
    public boolean unmarkAsRead(int bookId, String userName) throws SQLException {
        String query = "DELETE FROM ReadBooks WHERE bookId = ? AND userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setString(2, userName);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public int getReadBooksCountByUser(String userName) throws SQLException {
        String query = "SELECT COUNT(DISTINCT bookId) FROM ReadBooks WHERE userName = ?";
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
