package org.example.daos.implementations;

import org.example.daos.interfaces.UserDao;
import org.example.models.UserEntity;
import org.example.models.UserEntity.Roles;
import org.example.utils.DatabaseConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

/**
 * Lớp triển khai UserDao để thao tác với cơ sở dữ liệu.
 */
public class UserDaoImpl implements UserDao {

    private final Connection connection;

    /**
     * Khởi tạo một đối tượng UserDaoImpl.
     */
    public UserDaoImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Hàm băm mật khẩu dạng plain text sử dụng thuật toán SHA-256.
     *
     * @param password mật khẩu plain text cần được băm.
     * @return mật khẩu đã băm dưới dạng chuỗi được mã hóa Base64.
     * @throws NoSuchAlgorithmException nếu thuật toán SHA-256 không khả dụng.
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    /**
     * @param password
     * @param hashedPassword
     * @return
     * @throws NoSuchAlgorithmException
     */
    private boolean verifyPassword(String password, String hashedPassword)
            throws NoSuchAlgorithmException {
        String hashedInputPassword = hashPassword(password);
        return hashedInputPassword.equals(hashedPassword);
    }

    /**
     * @param user Đối tượng UserEntity chứa thông tin người dùng cần đăng ký.
     * @return true nếu tạo tài khoản thành công, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public boolean registerUser(UserEntity user) throws SQLException {
        try {
            String hashedPassword = hashPassword(user.getPasswordHash()); // Hash mật khẩu

            String sql = "INSERT INTO Users (Username, PasswordHash, Email, FirstName, LastName, PhoneNumber, ProfileImageDirectory, Role) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getFirstName());
                preparedStatement.setString(5, user.getLastName());
                preparedStatement.setString(6, user.getPhoneNumber());
                preparedStatement.setString(7, user.getProfileImageDirectory());
                preparedStatement.setString(8, user.getRole().name());
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash", e);
        }
    }

    /**
     * @param userName Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Đối tượng đã đăng nhập, ngược lại trả về null.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public UserEntity loginUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPasswordHash = resultSet.getString("PasswordHash");

                    if (verifyPassword(password, storedPasswordHash)) {
                        return new UserEntity(
                                resultSet.getInt("UserID"),
                                resultSet.getString("Username"),
                                resultSet.getString("PasswordHash"),
                                resultSet.getString("Email"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                resultSet.getString("PhoneNumber"),
                                resultSet.getString("ProfileImageDirectory"),
                                Roles.valueOf(resultSet.getString("Role")));
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash", e);
        }
        return null;
    }

    /**
     * @param userId      ID của người dùng.
     * @param newPassword Mật khẩu mới của người dùng.
     * @return true nếu thay đổi mật khẩu thành công, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        try {
            String newHashedPassword = hashPassword(newPassword);
            String sql = "UPDATE Users SET PasswordHash = ? WHERE UserID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newHashedPassword);
                preparedStatement.setInt(2, userId);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash", e);
        }
    }

    /**
     * @param userId   ID của người dùng.
     * @param newEmail Email mới của người dùng.
     * @return true nếu cập nhật email thành công, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public boolean updateEmail(int userId, String newEmail) throws SQLException {
        String sql = "UPDATE Users SET Email = ? WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, userId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * @param userId         ID của người dùng.
     * @param newPhoneNumber Số điện thoại mới của người dùng.
     * @return true nếu cập nhật số điện thoại thành công, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
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

    /**
     * @param userId                   ID của người dùng.
     * @param newProfileImageDirectory Đường dẫn ảnh đại diện mới của người dùng.
     * @return true nếu cập nhật ảnh đại diện thành công, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public boolean updateProfileImage(int userId, String newProfileImageDirectory)
            throws SQLException {
        String sql = "UPDATE Users SET profileImageDirectory = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newProfileImageDirectory);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    /**
     * @param userId ID của người dùng.
     * @return Đối tượng tương ứng với userId.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
    @Override
    public UserEntity findUserInfo(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new UserEntity(
                            resultSet.getInt("UserID"),
                            resultSet.getString("Username"),
                            resultSet.getString("PasswordHash"),
                            resultSet.getString("Email"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("ProfileImageDirectory"),
                            Roles.valueOf(resultSet.getString("Role")));
                }
            }
        }
        return null;
    }

    /**
     * @param userName Tên người dùng cần kiểm tra.
     * @return true nếu tên người dùng tồn tại, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
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

    /**
     * @param email Email cần kiểm tra.
     * @return true nếu email đã đăng kí, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
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

    /**
     * @param phoneNumber Số điện thoại cần kiểm tra.
     * @return true nếu số điện thoại đã đăng kí, ngược lại trả về false.
     * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
     */
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
