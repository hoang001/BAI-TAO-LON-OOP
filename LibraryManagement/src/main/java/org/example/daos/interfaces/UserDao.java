package org.example.daos.interfaces;

import org.example.models.UserEntity;
import java.sql.SQLException;

/**
 * Giao diện cho các phương thức thao tác với dữ liệu người dùng.
 */
public interface UserDao {

    /**
     * Đăng ký người dùng mới.
     *
     * @param user Đối tượng UserEntity chứa thông tin người dùng cần đăng ký.
     * @return true nếu đăng ký thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean registerUser(UserEntity user) throws SQLException;

    /**
     * Đăng nhập người dùng.
     *
     * @param userName Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Đối tượng UserEntity chứa thông tin người dùng nếu đăng nhập thành công, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    UserEntity loginUser(String userName, String password) throws SQLException;

    /**
     * Đổi mật khẩu của người dùng.
     *
     * @param userId ID của người dùng.
     * @param newPassword Mật khẩu mới của người dùng.
     * @return true nếu thay đổi thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean changePassword(int userId, String newPassword) throws SQLException;

    /**
     * Cập nhật email của người dùng.
     *
     * @param userId ID của người dùng.
     * @param newEmail Email mới của người dùng.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updateEmail(int userId, String newEmail) throws SQLException;

    /**
     * Cập nhật số điện thoại của người dùng.
     *
     * @param userId ID của người dùng.
     * @param newPhoneNumber Số điện thoại mới của người dùng.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException;

    /**
     * Cập nhật ảnh đại diện của người dùng.
     *
     * @param userId ID của người dùng.
     * @param newProfileImageDirectory Đường dẫn ảnh đại diện mới của người dùng.
     * @return true nếu cập nhật thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean updateProfileImage(int userId, String newProfileImageDirectory) throws SQLException;

    /**
     * Tìm thông tin người dùng.
     *
     * @param userId ID của người dùng.
     * @return Đối tượng UserEntity chứa thông tin người dùng nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    UserEntity findUserInfo(int userId) throws SQLException;

    /**
     * Kiểm tra tên người dùng đã tồn tại chưa.
     *
     * @param userName Tên người dùng cần kiểm tra.
     * @return true nếu tên người dùng đã tồn tại, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isUsernameTaken(String userName) throws SQLException;

    /**
     * Kiểm tra email đã tồn tại chưa.
     *
     * @param email Email cần kiểm tra.
     * @return true nếu email đã tồn tại, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isEmailTaken(String email) throws SQLException;

    /**
     * Kiểm tra số điện thoại đã tồn tại chưa.
     *
     * @param phoneNumber Số điện thoại cần kiểm tra.
     * @return true nếu số điện thoại đã tồn tại, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean isPhoneNumberTaken(String phoneNumber) throws SQLException;
}
