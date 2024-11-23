package org.example.daos.implementations;

import org.example.daos.interfaces.AuthorDAO;
import org.example.models.AuthorEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAOImpl implements AuthorDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public AuthorDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean addAuthor(AuthorEntity author) throws SQLException {
        String sql = "INSERT INTO Authors (name, dateOfBirth, nationality) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setDate(2, Date.valueOf(author.getDateOfBirth()));
            preparedStatement.setString(3, author.getNationality());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean deleteAuthorById(int authorId) throws SQLException {
        String sql = "DELETE FROM Authors WHERE authorId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public AuthorEntity findAuthorById(int authorId) throws SQLException {
        String sql = "SELECT * FROM Authors WHERE authorId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate(); // Chuyển java.sql.Date sang LocalDate
                String nationality = resultSet.getString("nationality");
                return new AuthorEntity(authorId, name, dateOfBirth, nationality);
            }
        }
        return null;
    }

    @Override
    public AuthorEntity findAuthorByName(String authorName) throws SQLException {
        String sql = "SELECT * FROM Authors WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("authorId");
                LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
                String nationality = resultSet.getString("nationality");
                return new AuthorEntity(id, authorName, dateOfBirth, nationality);
            }
        }
        return null;
    }

    @Override
    public List<AuthorEntity> getAllAuthors() throws SQLException {
        List<AuthorEntity> authors = new ArrayList<>();
        String sql = "SELECT * FROM Authors";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("authorId");
                String name = resultSet.getString("name");
                LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
                String nationality = resultSet.getString("nationality");
                authors.add(new AuthorEntity(id, name, dateOfBirth, nationality));
            }
        }
        return authors;
    }
}
