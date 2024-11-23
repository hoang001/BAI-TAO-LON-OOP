package org.example.daos.implementations;

import org.example.daos.interfaces.UserDAO;
import org.example.models.UserEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public UserDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean registerUser(UserEntity user) throws SQLException {
        String sql = "INSERT INTO Users (userName, passwordHash, email, firstName, lastName, phoneNumber, profileImageDirectory) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getProfileImageDirectory());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public UserEntity loginUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE userName = ? AND passwordHash = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserEntity(
                        resultSet.getInt("userId"),
                        resultSet.getString("userName"),
                        resultSet.getString("passwordHash"),
                        resultSet.getString("email"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("profileImageDirectory")
                );
            }
        }
        return null;
    }

    @Override
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET passwordHash = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean updateEmail(int userId, String newEmail) throws SQLException {
        String sql = "UPDATE Users SET email = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException {
        String sql = "UPDATE Users SET phoneNumber = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPhoneNumber);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean updateProfileImage(int userId, String newProfileImageDirectory) throws SQLException {
        String sql = "UPDATE Users SET profileImageDirectory = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newProfileImageDirectory);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public UserEntity getUserInfo(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new UserEntity(
                            resultSet.getInt("userId"),
                            resultSet.getString("userName"),
                            resultSet.getString("passwordHash"),
                            resultSet.getString("email"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("phoneNumber"),
                            resultSet.getString("profileImageDirectory")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean isUsernameTaken(String userName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isEmailTaken(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPhoneNumberTaken(String phoneNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE phoneNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phoneNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
