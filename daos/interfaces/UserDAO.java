package org.example.daos.interfaces;

import org.example.models.UserEntity;

import java.sql.SQLException;

// Giao diện cho các thao tác CRUD đối với User
public interface UserDAO {
    // Đăng ký người dùng mới
    boolean registerUser(UserEntity user) throws SQLException;

    // Đăng nhập người dùng
    UserEntity loginUser(String userName, String password) throws SQLException;

    // Thay đổi mật khẩu người dùng
    boolean changePassword(int userId, String newPassword) throws SQLException;

    // Cập nhật email người dùng
    boolean updateEmail(int userId, String newEmail) throws SQLException;

    // Cập nhật số điện thoại người dùng
    boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException;

    // Cập nhật ảnh hồ sơ người dùng
    boolean updateProfileImage(int userId, String newProfileImageDirectory) throws SQLException;

    // Lấy thông tin của người dùng
    UserEntity getUserInfo(int userId) throws SQLException;

    // Kiểm tra xem username đã tồn tại hay chưa
    boolean isUsernameTaken(String userName) throws SQLException;

    boolean isEmailTaken(String email) throws SQLException;

    boolean isPhoneNumberTaken(String phoneNumber) throws SQLException;
}
