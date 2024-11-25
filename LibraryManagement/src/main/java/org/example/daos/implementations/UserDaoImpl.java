package org.example.daos.implementations;

import org.example.daos.interfaces.UserDao;
import org.example.models.UserEntity;
import org.example.models.UserEntity.Roles;
import org.example.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

/**
 * Cài đặt giao diện UserDAO để thực hiện các thao tác với cơ sở dữ liệu.
 */
public class UserDaoImpl implements UserDao {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public UserDaoImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Đăng ký người dùng mới trong cơ sở dữ liệu.
     * 
     * @param user đối tượng UserEntity chứa thông tin người dùng.
     * @return true nếu đăng ký thành công, false nếu không thành công.
     * @throws SQLException khi xảy ra lỗi trong quá trình kết nối hoặc truy vấn cơ sở dữ liệu.
     */
    @Override
    public boolean registerUser(UserEntity user) throws SQLException {
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String hashedPassword = BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt());

        String sql = "INSERT INTO Users (Username, PasswordHash, Email, FirstName, LastName, PhoneNumber, ProfileImageDirectory, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, hashedPassword);  // Lưu mật khẩu đã mã hóa
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getProfileImageDirectory());
            preparedStatement.setString(8, user.getRole().name());  // Lưu vai trò người dùng (Role)
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    /**
     * Đăng nhập người dùng bằng tên người dùng và mật khẩu.
     * 
     * @param userName tên người dùng.
     * @param password mật khẩu của người dùng.
     * @return đối tượng UserEntity nếu đăng nhập thành công, null nếu không tìm thấy người dùng.
     * @throws SQLException khi xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public UserEntity loginUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE userName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedPasswordHash = resultSet.getString("PasswordHash");
                // Kiểm tra mật khẩu người dùng nhập vào có khớp với mật khẩu đã mã hóa trong cơ sở dữ liệu
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    return new UserEntity(
                            resultSet.getInt("UserID"),
                            resultSet.getString("Username"),
                            resultSet.getString("PasswordHash"),
                            resultSet.getString("Email"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("PhoneNumber"),
                            resultSet.getString("ProfileImageDirectory"),
                            Roles.valueOf(resultSet.getString("Role"))
                    );
                }
            }
        }
        return null;
    }

    /**
     * Thay đổi mật khẩu của người dùng.
     * 
     * @param userId    ID của người dùng.
     * @param newPassword mật khẩu mới.
     * @return true nếu thay đổi mật khẩu thành công, false nếu không thành công.
     * @throws SQLException khi xảy ra lỗi trong quá trình cập nhật cơ sở dữ liệu.
     */
    @Override
    public boolean changePassword(int userId, String newPassword) throws SQLException {
        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE Users SET passwordHash = ? WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newHashedPassword);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    /**
     * Cập nhật email của người dùng.
     * 
     * @param userId    ID của người dùng.
     * @param newEmail email mới.
     * @return true nếu cập nhật email thành công, false nếu không thành công.
     * @throws SQLException khi xảy ra lỗi trong quá trình cập nhật cơ sở dữ liệu.
     */
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

    /**
     * Cập nhật số điện thoại của người dùng.
     * 
     * @param userId        ID của người dùng.
     * @param newPhoneNumber số điện thoại mới.
     * @return true nếu cập nhật số điện thoại thành công, false nếu không thành công.
     * @throws SQLException khi xảy ra lỗi trong quá trình cập nhật cơ sở dữ liệu.
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
     * Cập nhật hình ảnh đại diện của người dùng.
     * 
     * @param userId              ID của người dùng.
     * @param newProfileImageDirectory đường dẫn mới tới hình ảnh đại diện.
     * @return true nếu cập nhật hình ảnh thành công, false nếu không thành công.
     * @throws SQLException khi xảy ra lỗi trong quá trình cập nhật cơ sở dữ liệu.
     */
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

    /**
     * Tìm thông tin người dùng từ cơ sở dữ liệu.
     * 
     * @param userId ID của người dùng cần lấy thông tin.
     * @return đối tượng UserEntity chứa thông tin người dùng, null nếu không tìm thấy.
     * @throws SQLException khi xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu.
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
                        Roles.valueOf(resultSet.getString("Role"))
                    );
                }
            }
        }
        return null;
    }

    /**
     * Kiểm tra xem tên người dùng có bị trùng không.
     * 
     * @param userName tên người dùng cần kiểm tra.
     * @return true nếu tên người dùng đã tồn tại, false nếu không.
     * @throws SQLException khi xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu.
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
     * Kiểm tra xem email có bị trùng không.
     * 
     * @param email email cần kiểm tra.
     * @return true nếu email đã tồn tại, false nếu không.
     * @throws SQLException khi xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu.
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
     * Kiểm tra xem số điện thoại có bị trùng không.
     * 
     * @param phoneNumber số điện thoại cần kiểm tra.
     * @return true nếu số điện thoại đã tồn tại, false nếu không.
     * @throws SQLException khi xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu.
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
