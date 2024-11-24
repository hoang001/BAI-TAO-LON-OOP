package org.example.daos.interfaces;

import org.example.models.UserEntity;
import java.sql.SQLException;

/**
 * Interface cho các thao tác CRUD đối với người dùng.
 * Cung cấp các phương thức để đăng ký, đăng nhập, thay đổi mật khẩu, và cập nhật thông tin người dùng.
 */
public interface UserDao {

    /**
     * Đăng ký người dùng mới vào hệ thống.
     *
     * @param user Đối tượng UserEntity chứa thông tin người dùng cần đăng ký.
     * @return true nếu đăng ký thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean registerUser(UserEntity user) throws SQLException;

    /**
     * Đăng nhập người dùng bằng tên người dùng và mật khẩu.
     *
     * @param userName Tên người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Đối tượng UserEntity chứa thông tin người dùng nếu đăng nhập thành công, null nếu không thành công.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    UserEntity loginUser(String userName, String password) throws SQLException;

    /**
     * Thay đổi mật khẩu của người dùng.
     *
     * @param userId ID của người dùng cần thay đổi mật khẩu.
     * @param newPassword Mật khẩu mới.
     * @return true nếu thay đổi mật khẩu thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean changePassword(int userId, String newPassword) throws SQLException;

    /**
     * Cập nhật email của người dùng.
     *
     * @param userId ID của người dùng cần cập nhật email.
     * @param newEmail Email mới của người dùng.
     * @return true nếu cập nhật email thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updateEmail(int userId, String newEmail) throws SQLException;

    /**
     * Cập nhật số điện thoại của người dùng.
     *
     * @param userId ID của người dùng cần cập nhật số điện thoại.
     * @param newPhoneNumber Số điện thoại mới của người dùng.
     * @return true nếu cập nhật số điện thoại thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException;

    /**
     * Cập nhật ảnh hồ sơ của người dùng.
     *
     * @param userId ID của người dùng cần cập nhật ảnh hồ sơ.
     * @param newProfileImageDirectory Đường dẫn tới ảnh hồ sơ mới.
     * @return true nếu cập nhật ảnh hồ sơ thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updateProfileImage(int userId, String newProfileImageDirectory) throws SQLException;

    /**
     * Lấy thông tin người dùng dựa trên ID.
     *
     * @param userId ID của người dùng cần lấy thông tin.
     * @return Đối tượng UserEntity chứa thông tin người dùng.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    UserEntity findUserInfo(int userId) throws SQLException;

    /**
     * Kiểm tra xem tên người dùng đã tồn tại hay chưa.
     *
     * @param userName Tên người dùng cần kiểm tra.
     * @return true nếu tên người dùng đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isUsernameTaken(String userName) throws SQLException;

    /**
     * Kiểm tra xem email đã được sử dụng hay chưa.
     *
     * @param email Email cần kiểm tra.
     * @return true nếu email đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isEmailTaken(String email) throws SQLException;

    /**
     * Kiểm tra xem số điện thoại đã được sử dụng hay chưa.
     *
     * @param phoneNumber Số điện thoại cần kiểm tra.
     * @return true nếu số điện thoại đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isPhoneNumberTaken(String phoneNumber) throws SQLException;
}
