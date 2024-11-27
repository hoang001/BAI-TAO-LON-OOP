package org.example.services.basics;

import org.example.daos.implementations.UserDaoImpl;
import org.example.daos.implementations.LogDaoImpl;
import org.example.models.UserEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private static UserService instance;
    private UserDaoImpl userDao;
    private LogDaoImpl logDao;
    private UserEntity loginUser;

    // Constructor riêng tư để ngăn chặn khởi tạo từ bên ngoài
    private UserService() {
        this.userDao = new UserDaoImpl();
        this.logDao = new LogDaoImpl();
        this.loginUser = null;
    }

    // Phương thức trả về instance duy nhất của UserService
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // Phương thức trả về người dùng đang đăng nhập
    public UserEntity getLoginUser() {
        return loginUser;
    }

    // Phương thức thiết lập người dùng đang đăng nhập
    public void setLoginUser(UserEntity loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * Tạo người dùng mới.
     * @param user người dùng mới được thêm vào.
     * @return trả về kết quả người dùng mới đã được thêm vào hay chưa.
     */
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
                logDao.addLog(new LogEntity(LocalDateTime.now(), user.getUserName(), "Đăng ký thành công"));
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

    /**
     * Đăng nhập.
     * @param userName tên người dùng.
     * @param password mật khẩu người dùng.
     * @return trả về thông tin người dùng.
     */
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
            logDao.addLog(new LogEntity(LocalDateTime.now(), userName, "Đăng nhập thành công"));
            return user;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình đăng nhập: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Đăng xuất
     * @return trả về người dùng có đăng xuất thành công hay không.
     */
    public boolean logoutUser() {
        try {
            if (loginUser != null) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Đăng xuất thành công"));
                loginUser = null;
                return true;
            }
            throw new IllegalStateException("Không có người dùng nào đang đăng nhập");
        } catch (IllegalStateException e) {
            // Xử lý lỗi khi không có người dùng nào đăng nhập
            System.out.println("Lỗi đăng xuất: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // Xử lý lỗi liên quan đến cơ sở dữ liệu (SQL)
            System.out.println("Lỗi cơ sở dữ liệu khi thêm log: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thay đổi mật khẩu.
     * @param newPassword mật khẩu mới.
     * @return trả về có thay đổi thành công hay không.
     */
    public boolean changePassword(String newPassword) {
        try {
            // Kiểm tra nếu người dùng chưa đăng nhập
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi thay đổi mật khẩu.");
            }
            
            // Kiểm tra mật khẩu mới hợp lệ
            if (newPassword == null || newPassword.trim().isEmpty()) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thất bại: Chưa nhập đủ thông tin"));
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
            
            // Thực hiện thay đổi mật khẩu trong cơ sở dữ liệu
            boolean result = userDao.changePassword(loginUser.getId(), newPassword);
            
            // Ghi log nếu thay đổi mật khẩu thành công
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thành công"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thất bại: Không thay đổi được mật khẩu"));
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            // Ghi log khi gặp lỗi do điều kiện không hợp lệ (mật khẩu trống, chưa đăng nhập, ...)
            System.out.println("Lỗi thay đổi mật khẩu: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thất bại: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi trong quá trình ghi log
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            return false;
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // Xử lý lỗi cơ sở dữ liệu trong quá trình thay đổi mật khẩu
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Lỗi cơ sở dữ liệu trong quá trình thay đổi mật khẩu: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi trong quá trình ghi log
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật email.
     * @param newEmail email mới.
     * @return trả về đã cập nhật được email chưa.
     */
    public boolean updateEmail(String newEmail) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật email");
            }
    
            // Kiểm tra xem email mới có hợp lệ hay không
            if (newEmail == null || newEmail.trim().isEmpty()) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại: Chưa nhập đủ thông tin"));
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
    
            // Kiểm tra email có hợp lệ hay không
            if (!isValidEmail(newEmail)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại: Email không hợp lệ"));
                throw new IllegalArgumentException("Email không hợp lệ");
            }
    
            // Kiểm tra chiều dài email có vượt quá 100 ký tự hay không
            if (newEmail.length() > 100) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại: Email vượt quá giới hạn 100 ký tự"));
                throw new IllegalArgumentException("Email vượt quá giới hạn 100 ký tự");
            }
    
            // Kiểm tra xem email có bị trùng không
            if (userDao.isEmailTaken(newEmail)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại: Email đã được đăng ký"));
                throw new IllegalStateException("Email đã được đăng ký");
            }
    
            // Thực hiện cập nhật email vào cơ sở dữ liệu
            boolean result = userDao.updateEmail(loginUser.getId(), newEmail);
    
            // Ghi log nếu cập nhật thành công hoặc thất bại
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thành công"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại"));
            }
    
            return result;
        } catch (IllegalArgumentException e) {
            // Xử lý các ngoại lệ do người dùng nhập không hợp lệ (email không hợp lệ, thiếu thông tin, ...).
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi trong quá trình ghi log
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cập nhật email: " + e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // Xử lý lỗi cơ sở dữ liệu trong quá trình cập nhật email hoặc ghi log
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Lỗi cơ sở dữ liệu trong quá trình cập nhật email: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi trong quá trình ghi log
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật số điện thoại
     * @param newPhoneNumber số điện thoại mới.
     * @return trả về có cập nhật thành công hay không.
     */
    public boolean updatePhoneNumber(String newPhoneNumber) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật số điện thoại");
            }
    
            // Kiểm tra xem số điện thoại mới có hợp lệ hay không
            if (newPhoneNumber == null || newPhoneNumber.trim().isEmpty()) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại: Chưa nhập đủ thông tin"));
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
    
            // Kiểm tra số điện thoại có hợp lệ không (ví dụ, theo định dạng)
            if (!isValidPhoneNumber(newPhoneNumber)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại: Số điện thoại không hợp lệ"));
                throw new IllegalArgumentException("Số điện thoại không hợp lệ");
            }
    
            // Kiểm tra chiều dài số điện thoại
            if (newPhoneNumber.length() > 20) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại: Số điện thoại vượt quá giới hạn 20 ký tự"));
                throw new IllegalArgumentException("Số điện thoại vượt quá giới hạn 20 ký tự");
            }
    
            // Kiểm tra xem số điện thoại có bị trùng không
            if (userDao.isPhoneNumberTaken(newPhoneNumber)) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại: Số điện thoại đã được đăng ký"));
                throw new IllegalStateException("Số điện thoại đã được đăng ký");
            }
    
            // Cập nhật số điện thoại mới
            boolean result = userDao.updatePhoneNumber(loginUser.getId(), newPhoneNumber);
    
            // Ghi log khi cập nhật thành công hoặc thất bại
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thành công"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại"));
            }
    
            return result;
        } catch (IllegalArgumentException e) {
            // Xử lý các lỗi liên quan đến nhập liệu không hợp lệ
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi ghi log nếu có
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // Xử lý lỗi trong cơ sở dữ liệu (cập nhật số điện thoại hoặc ghi log)
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Lỗi cơ sở dữ liệu trong quá trình cập nhật số điện thoại: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi ghi log nếu có
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật số điện thoại: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật ảnh đại diện.
     * @param newProfileImageDirectory ảnh đại diện mới.
     * @return trả về ảnh đại diện đã được cập nhật thành công chưa.
     */
    public boolean updateProfileImage(String newProfileImageDirectory) {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật ảnh đại diện");
            }
    
            // Kiểm tra xem đường dẫn ảnh có hợp lệ hay không
            if (newProfileImageDirectory == null || newProfileImageDirectory.trim().isEmpty()) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại: Chưa nhập đủ thông tin"));
                throw new IllegalArgumentException("Chưa nhập đủ thông tin");
            }
    
            // Cập nhật ảnh đại diện
            boolean result = userDao.updateProfileImage(loginUser.getId(), newProfileImageDirectory);
    
            // Ghi log khi cập nhật thành công hoặc thất bại
            if (result) {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thành công"));
            } else {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại"));
            }
    
            return result;
    
        } catch (IllegalArgumentException e) {
            // Xử lý các lỗi liên quan đến nhập liệu không hợp lệ
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi ghi log nếu có
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cập nhật ảnh đại diện: " + e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            // Xử lý lỗi trong cơ sở dữ liệu (cập nhật ảnh đại diện hoặc ghi log)
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Lỗi cơ sở dữ liệu trong quá trình cập nhật ảnh đại diện: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý lỗi ghi log nếu có
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật ảnh đại diện: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy thông tin người dùng.
     * @return trả về thông tin người dùng.
     */
    public UserEntity getUserInfo() {
        try {
            // Kiểm tra xem người dùng đã đăng nhập chưa
            if (loginUser == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi xem thông tin tài khoản");
            }
    
            // Lấy thông tin người dùng từ cơ sở dữ liệu
            UserEntity userInfo = userDao.findUserInfo(loginUser.getId());
    
            // Ghi log thành công khi lấy thông tin tài khoản
            logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Xem thông tin tài khoản thành công"));
            return userInfo;
    
        } catch (IllegalStateException e) {
            // Ghi log thất bại khi người dùng chưa đăng nhập
            System.out.println("Lỗi: " + e.getMessage());
            return null;
    
        } catch (SQLException e) {
            // Xử lý lỗi cơ sở dữ liệu khi tìm kiếm thông tin người dùng
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Lỗi cơ sở dữ liệu trong quá trình lấy thông tin tài khoản: " + e.getMessage()));
            } catch (SQLException logException) {
                // Xử lý ngoại lệ ghi log nếu có
                System.out.println("Lỗi ghi log: " + logException.getMessage());
            }
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy thông tin tài khoản: " + e.getMessage());
            return null;
    
        }
    }

    /**
     * Kiểm tra tính hợp lệ của email.
     * @param email email kiểm tra.
     * @return trả về email có đúng cấu trúc hay không.
     */
    public boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
    }

    /**
     * kiểm trả cấu trúc số điẹne thoại.
     * @param phoneNumber số điện thoại kiểm tra.
     * @return trả về trạng thái kiểm tra.
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}    