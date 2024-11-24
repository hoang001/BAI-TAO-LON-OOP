package org.example.services.basics;

import org.example.daos.interfaces.UserDao;
import org.example.daos.interfaces.LogDao;
import org.example.daos.implementations.UserDaoImpl;
import org.example.daos.implementations.LogDaoImpl;
import org.example.models.UserEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private final UserDao userDao;
    private final LogDao logDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
        this.logDao = new LogDaoImpl();
    }

    private UserEntity loginUser = null;

    public UserEntity getLoginUser() {
        return loginUser;
    }

    public boolean registerUser(UserEntity user) {
        try {
            if (user.getUserName() == null || user.getPasswordHash() == null || user.getEmail() == null ||
                    user.getFirstName() == null || user.getLastName() == null || user.getPhoneNumber() == null ||
                    user.getUserName().trim().isEmpty() || user.getPasswordHash().trim().isEmpty() || user.getEmail().trim().isEmpty() ||
                    user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty() || user.getPhoneNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
            if (!isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email không hợp lệ");
            }
            if (!isValidPhoneNumber(user.getPhoneNumber())) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ");
            }
            if (user.getUserName().length() > 20) {
                throw new IllegalArgumentException("Tên người dùng vượt quá giới hạn 20 ký tự");
            }
            if (user.getPasswordHash().length() > 64) {
                throw new IllegalArgumentException("Mật khẩu vượt quá giới hạn 64 ký tự");
            }
            if (user.getEmail().length() > 100) {
                throw new IllegalArgumentException("Email vượt quá giới hạn 100 ký tự");
            }
            if (user.getFirstName().length() > 50) {
                throw new IllegalArgumentException("Tên đầu vượt quá giới hạn 50 ký tự");
            }
            if (user.getLastName().length() > 50) {
                throw new IllegalArgumentException("Tên cuối vượt quá giới hạn 50 ký tự");
            }
            if (user.getPhoneNumber().length() > 20) {
                throw new IllegalArgumentException("Số điện thoại vượt quá giới hạn 20 ký tự");
            }
            if (user.getProfileImageDirectory() != null && user.getProfileImageDirectory().length() > 100) {
                throw new IllegalArgumentException("Đường dẫn ảnh đại diện vượt quá giới hạn 100 ký tự");
            }
            if (userDao.isUsernameTaken(user.getUserName())) {
                throw new IllegalStateException("Tên người dùng đã tồn tại");
            }
            if (userDao.isEmailTaken(user.getEmail())) {
                throw new IllegalStateException("Email đã được đăng kí");
            }
            if (userDao.isPhoneNumberTaken(user.getPhoneNumber())) {
                throw new IllegalStateException("Số điện thoại đã được đăng kí");
            }
    
            boolean result = userDao.registerUser(user);
            if (result) {
                // logDao.addLog(new LogEntity(LocalDateTime.now(), user.getUserName(), "Đăng ký thành công"));
            }
            return result;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi đăng ký: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình đăng ký: " + e.getMessage());
            return false;
        }
    }      
    
    public UserEntity loginUser(String userName, String password) {
        try {
            if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
            if (!userDao.isUsernameTaken(userName)) {
                throw new IllegalStateException("Tên đăng nhập không tồn tại");
            }
    
            UserEntity user = userDao.loginUser(userName, password);
            if (user == null) {
                throw new IllegalStateException("Tên đăng nhập hoặc mật khẩu không đúng");
            }
    
            loginUser = user;
            // logDao.addLog(new LogEntity(LocalDateTime.now(), userName, "Đăng nhập thành công"));
            return user;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình đăng nhập: " + e.getMessage());
            return null;
        }
    }
    
    public boolean logoutUser() {
        try {
            if (loginUser != null) {
                loginUser = null;
                return true;
            }
            throw new IllegalStateException("Không có người dùng nào đang đăng nhập");
        } catch (IllegalStateException e) {
            System.out.println("Lỗi đăng xuất: " + e.getMessage());
            return false;
        }
    }
    
    public boolean changePassword(String newPassword) {
        try {
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi thay đổi mật khẩu.");
            }
            if (newPassword == null || newPassword.isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
    
            boolean result = userDao.changePassword(loginUser.getId(), newPassword);
            if (result) {
                // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thành công"));
            }
            return result;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi thay đổi mật khẩu: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateEmail(String newEmail) {
        try {
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật email");
            }
            if (newEmail == null || newEmail.isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
            if (!isValidEmail(newEmail)) {
                throw new IllegalArgumentException("Email không hợp lệ");
            }
            if (newEmail.length() > 100) {
                throw new IllegalArgumentException("Email vượt quá giới hạn 100 ký tự");
            }
            if (userDao.isEmailTaken(newEmail)) {
                throw new IllegalStateException("Email đã được đăng kí");
            }
    
            boolean result = userDao.updateEmail(loginUser.getId(), newEmail);
            if (result) {
                // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thành công"));
            }
            return result;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi cập nhật email: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật email: " + e.getMessage());
            return false;
        }
    }    
    
    public boolean updatePhoneNumber(String newPhoneNumber) {
        try {
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật số điện thoại");
            }
            if (newPhoneNumber == null || newPhoneNumber.isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
            if (!isValidPhoneNumber(newPhoneNumber)) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ");
            }
            if (newPhoneNumber.length() > 20) {
                throw new IllegalArgumentException("Số điện thoại vượt quá giới hạn 20 ký tự");
            }
            if (userDao.isPhoneNumberTaken(newPhoneNumber)) {
                throw new IllegalStateException("Số điện thoại đã được đăng kí");
            }
    
            boolean result = userDao.updatePhoneNumber(loginUser.getId(), newPhoneNumber);
            if (result) {
                // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thành công"));
            }
            return result;
        } catch (IllegalArgumentException | IllegalStateException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại"));
            System.out.println("Lỗi cập nhật số điện thoại: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại"));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật số điện thoại: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateProfileImage(String newProfileImageDirectory) {
        try {
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật ảnh nền");
            }
            if (newProfileImageDirectory == null || newProfileImageDirectory.isEmpty()) {
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
    
            boolean result = userDao.updateProfileImage(loginUser.getId(), newProfileImageDirectory);
            if (result) {
                // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thành công"));
            }
            return result;
        } catch (IllegalArgumentException | IllegalStateException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại"));
            System.out.println("Lỗi cập nhật ảnh nền: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại"));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật ảnh nền: " + e.getMessage());
            return false;
        }
    }
    
    public UserEntity getUserInfo() {
        try {
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem thông tin tài khoản");
            }
    
            UserEntity userInfo = userDao.findUserInfo(loginUser.getId());
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Xem thông tin tài khoản"));
            return userInfo;
        } catch (IllegalStateException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Xem thông tin tài khoản thất bại"));
            System.out.println("Lỗi lấy thông tin tài khoản: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            // logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Xem thông tin tài khoản thất bại"));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy thông tin tài khoản: " + e.getMessage());
            return null;
        }
    }

    public boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}    