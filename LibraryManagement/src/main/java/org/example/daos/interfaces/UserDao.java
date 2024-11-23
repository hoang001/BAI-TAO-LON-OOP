package org.example.daos.interfaces;

import org.example.models.UserEntity;
import java.sql.SQLException;

/**
 * Giao diện cho các thao tác CRUD đối với người dùng.
 */
public interface UserDao {

    /**
     * Đăng ký người dùng mới.
     *
     * @param user đối tượng UserEntity chứa thông tin người dùng cần đăng ký.
     * @return true nếu đăng ký thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean registerUser(UserEntity user) throws SQLException;

    /**
     * Đăng nhập người dùng.
     *
     * @param userName tên người dùng.
     * @param password mật khẩu của người dùng.
     * @return đối tượng UserEntity chứa thông tin người dùng nếu đăng nhập thành công, null nếu không thành công.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    UserEntity loginUser(String userName, String password) throws SQLException;

    /**
     * Thay đổi mật khẩu người dùng.
     *
     * @param userId    ID của người dùng cần thay đổi mật khẩu.
     * @param newPassword mật khẩu mới.
     * @return true nếu thay đổi mật khẩu thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean changePassword(int userId, String newPassword) throws SQLException;

    /**
     * Cập nhật email người dùng.
     *
     * @param userId    ID của người dùng cần cập nhật email.
     * @param newEmail email mới của người dùng.
     * @return true nếu cập nhật email thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updateEmail(int userId, String newEmail) throws SQLException;

    /**
     * Cập nhật số điện thoại người dùng.
     *
     * @param userId        ID của người dùng cần cập nhật số điện thoại.
     * @param newPhoneNumber số điện thoại mới của người dùng.
     * @return true nếu cập nhật số điện thoại thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException;

    /**
     * Cập nhật ảnh hồ sơ người dùng.
     *
     * @param userId                ID của người dùng cần cập nhật ảnh hồ sơ.
     * @param newProfileImageDirectory đường dẫn tới ảnh hồ sơ mới.
     * @return true nếu cập nhật ảnh hồ sơ thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean updateProfileImage(int userId, String newProfileImageDirectory) throws SQLException;

    /**
     * Lấy thông tin của người dùng.
     *
     * @param userId ID của người dùng cần lấy thông tin.
     * @return đối tượng UserEntity chứa thông tin người dùng.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    UserEntity getUserInfo(int userId) throws SQLException;

    /**
     * Kiểm tra xem username đã tồn tại hay chưa.
     *
     * @param userName tên người dùng cần kiểm tra.
     * @return true nếu username đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isUsernameTaken(String userName) throws SQLException;

    /**
     * Kiểm tra xem email đã tồn tại hay chưa.
     *
     * @param email email cần kiểm tra.
     * @return true nếu email đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isEmailTaken(String email) throws SQLException;

    /**
     * Kiểm tra xem số điện thoại đã tồn tại hay chưa.
     *
     * @param phoneNumber số điện thoại cần kiểm tra.
     * @return true nếu số điện thoại đã tồn tại, false nếu chưa tồn tại.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean isPhoneNumberTaken(String phoneNumber) throws SQLException;
}
