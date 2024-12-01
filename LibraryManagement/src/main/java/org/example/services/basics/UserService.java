package org.example.services.basics;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.implementations.UserDaoImpl;
import org.example.models.LogEntity;
import org.example.models.UserEntity;

/** * Lớp UserService quản lý các thao tác liên quan đến người dùng. */
public class UserService {
  private static UserService instance;
  private final UserDaoImpl userDao;
  private final LogDaoImpl logDao;
  private UserEntity loginUser;

  // Constructor riêng tư để ngăn chặn khởi tạo từ bên ngoài
  private UserService() {
    this.userDao = new UserDaoImpl();
    this.logDao = new LogDaoImpl();
    this.loginUser = null;
  }

  /**
   * Lấy thể hiện (instance) duy nhất của UserService.
   *
   * @return Thể hiện duy nhất của UserService.
   */
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
   *
   * @param user người dùng mới được thêm vào.
   * @return trả về kết quả người dùng mới đã được thêm vào hay chưa.
   */
  public boolean registerUser(UserEntity user) {
    try {
      // Kiểm tra xem các trường thông tin người dùng có null hoặc rỗng hay không
      if (user.getUserName() == null
          || user.getPasswordHash() == null
          || user.getEmail() == null
          || user.getFirstName() == null
          || user.getLastName() == null
          || user.getPhoneNumber() == null
          || user.getUserName().trim().isEmpty()
          || user.getPasswordHash().trim().isEmpty()
          || user.getEmail().trim().isEmpty()
          || user.getFirstName().trim().isEmpty()
          || user.getLastName().trim().isEmpty()
          || user.getPhoneNumber().trim().isEmpty()) {
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Kiểm tra định dạng email
      if (isValidEmail(user.getEmail())) {
        throw new IllegalArgumentException("Email không hợp lệ");
      }

      // Kiểm tra định dạng số điện thoại
      if (isValidPhoneNumber(user.getPhoneNumber())) {
        throw new IllegalArgumentException("Số điện thoại không hợp lệ");
      }

      // Kiểm tra độ dài của tên người dùng
      if (user.getUserName().length() > 20) {
        throw new IllegalArgumentException("Tên người dùng vượt quá giới hạn 20 ký tự");
      }

      // Kiểm tra độ dài của mật khẩu
      if (user.getPasswordHash().length() > 64) {
        throw new IllegalArgumentException("Mật khẩu vượt quá giới hạn 64 ký tự");
      }

      // Kiểm tra độ dài của email
      if (user.getEmail().length() > 100) {
        throw new IllegalArgumentException("Email vượt quá giới hạn 100 ký tự");
      }

      // Kiểm tra độ dài của tên đầu
      if (user.getFirstName().length() > 50) {
        throw new IllegalArgumentException("Tên đầu vượt quá giới hạn 50 ký tự");
      }

      // Kiểm tra độ dài của tên cuối
      if (user.getLastName().length() > 50) {
        throw new IllegalArgumentException("Tên cuối vượt quá giới hạn 50 ký tự");
      }

      // Kiểm tra độ dài của số điện thoại
      if (user.getPhoneNumber().length() > 20) {
        throw new IllegalArgumentException("Số điện thoại vượt quá giới hạn 20 ký tự");
      }

      // Kiểm tra độ dài của đường dẫn ảnh đại diện
      if (user.getProfileImageDirectory() != null
          && user.getProfileImageDirectory().length() > 100) {
        throw new IllegalArgumentException("Đường dẫn ảnh đại diện vượt quá giới hạn 100 ký tự");
      }

      // Kiểm tra xem tên người dùng đã tồn tại trong cơ sở dữ liệu hay chưa
      if (userDao.isUsernameTaken(user.getUserName())) {
        throw new IllegalStateException("Tên người dùng đã tồn tại");
      }

      // Kiểm tra xem email đã được đăng ký chưa
      if (userDao.isEmailTaken(user.getEmail())) {
        throw new IllegalStateException("Email đã được đăng kí");
      }

      // Kiểm tra xem số điện thoại đã được đăng ký chưa
      if (userDao.isPhoneNumberTaken(user.getPhoneNumber())) {
        throw new IllegalStateException("Số điện thoại đã được đăng kí");
      }

      // Đăng ký người dùng mới vào cơ sở dữ liệu
      boolean result = userDao.registerUser(user);
      if (result) {
        // Thêm log khi đăng ký thành công
        logDao.addLog(new LogEntity(LocalDateTime.now(), user.getUserName(), "Đăng ký thành công"));
      }
      return result;
    } catch (IllegalArgumentException | IllegalStateException e) {
      // Xử lý ngoại lệ liên quan đến kiểm tra đầu vào
      System.out.println("Lỗi đăng ký: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý ngoại lệ liên quan đến cơ sở dữ liệu
      System.out.println("Lỗi cơ sở dữ liệu trong quá trình đăng ký: " + e.getMessage());
      return false;
    }
  }

  /**
   * Đăng nhập.
   *
   * @param userName tên người dùng.
   * @param password mật khẩu người dùng.
   * @return trả về thông tin người dùng.
   */
  public UserEntity loginUser(String userName, String password) {
    try {
      // Kiểm tra xem tên đăng nhập và mật khẩu có null hoặc rỗng hay không
      if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Kiểm tra xem tên đăng nhập có tồn tại trong cơ sở dữ liệu không
      if (!userDao.isUsernameTaken(userName)) {
        throw new IllegalStateException("Tên đăng nhập không tồn tại");
      }

      // Kiểm tra tên đăng nhập và mật khẩu trong cơ sở dữ liệu
      UserEntity user = userDao.loginUser(userName, password);
      if (user == null) {
        throw new IllegalStateException("Tên đăng nhập hoặc mật khẩu không đúng");
      }

      // Lưu thông tin người dùng đã đăng nhập
      loginUser = user;
      // Ghi lại log khi đăng nhập thành công
      logDao.addLog(new LogEntity(LocalDateTime.now(), userName, "Đăng nhập thành công"));
      return user;
    } catch (IllegalArgumentException | IllegalStateException e) {
      // Xử lý ngoại lệ liên quan đến kiểm tra đầu vào
      System.out.println("Lỗi đăng nhập: " + e.getMessage());
      return null;
    } catch (SQLException e) {
      // Xử lý ngoại lệ liên quan đến cơ sở dữ liệu
      System.out.println("Lỗi cơ sở dữ liệu trong quá trình đăng nhập: " + e.getMessage());
      return null;
    }
  }

  /**
   * Đăng xuất người dùng.
   *
   * @return trả về true nếu đăng xuất thành công, ngược lại trả về false.
   */
  public boolean logoutUser() {
    try {
      // Kiểm tra xem có người dùng nào đang đăng nhập hay không
      if (loginUser != null) {
        // Thêm log khi đăng xuất thành công
        logDao.addLog(
            new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Đăng xuất thành công"));
        // Đặt lại biến loginUser về null để thể hiện rằng không có người dùng nào đang đăng nhập
        loginUser = null;
        return true;
      }
      // Ném ngoại lệ nếu không có người dùng nào đang đăng nhập
      throw new IllegalStateException("Không có người dùng nào đang đăng nhập");
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ nếu không có người dùng nào đang đăng nhập
      System.out.println("Lỗi đăng xuất: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý ngoại lệ liên quan đến cơ sở dữ liệu khi thêm log
      System.out.println("Lỗi cơ sở dữ liệu khi thêm log: " + e.getMessage());
      return false;
    }
  }

  /**
   * Thay đổi mật khẩu.
   *
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
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Thay đổi mật khẩu thất bại: Chưa nhập đủ thông tin"));
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Thực hiện thay đổi mật khẩu trong cơ sở dữ liệu
      boolean result = userDao.changePassword(loginUser.getId(), newPassword);

      // Ghi log nếu thay đổi mật khẩu thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Thay đổi mật khẩu thành công"));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Thay đổi mật khẩu thất bại: Không thay đổi được mật khẩu"));
      }
      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ (mật khẩu trống, chưa đăng nhập, ...)
      System.out.println("Lỗi thay đổi mật khẩu: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Thay đổi mật khẩu thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi thay đổi mật khẩu
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Lỗi cơ sở dữ liệu trong quá trình thay đổi mật khẩu: " + e.getMessage()));
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
   *
   * @param newEmail email mới.
   * @return trả về đã cập nhật được email chưa.
   */
  public boolean updateEmail(String newEmail) {
    try {
      // Kiểm tra nếu người dùng chưa đăng nhập
      if (loginUser == null) {
        throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật email");
      }

      // Kiểm tra email mới hợp lệ
      if (newEmail == null || newEmail.trim().isEmpty()) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật email thất bại: Chưa nhập đủ thông tin"));
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Kiểm tra định dạng email
      if (isValidEmail(newEmail)) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật email thất bại: Email không hợp lệ"));
        throw new IllegalArgumentException("Email không hợp lệ");
      }

      // Kiểm tra độ dài của email
      if (newEmail.length() > 100) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật email thất bại: Email vượt quá giới hạn 100 ký tự"));
        throw new IllegalArgumentException("Email vượt quá giới hạn 100 ký tự");
      }

      // Kiểm tra xem email đã được đăng ký chưa
      if (userDao.isEmailTaken(newEmail)) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật email thất bại: Email đã được đăng ký"));
        throw new IllegalStateException("Email đã được đăng ký");
      }

      // Thực hiện cập nhật email trong cơ sở dữ liệu
      boolean result = userDao.updateEmail(loginUser.getId(), newEmail);

      // Ghi log nếu cập nhật email thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thành công"));
      } else {
        logDao.addLog(
            new LogEntity(LocalDateTime.now(), loginUser.getUserName(), "Cập nhật email thất bại"));
      }
      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật email thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cập nhật email: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi email đã được đăng ký
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi cập nhật email
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Lỗi cơ sở dữ liệu trong quá trình cập nhật email: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cơ sở dữ liệu trong quá trình cập nhật email: " + e.getMessage());
      return false;
    }
  }

  /**
   * Cập nhật số điện thoại.
   *
   * @param newPhoneNumber số điện thoại mới.
   * @return trả về true nếu cập nhật thành công, ngược lại false.
   */
  public boolean updatePhoneNumber(String newPhoneNumber) {
    try {
      // Kiểm tra nếu người dùng chưa đăng nhập
      if (loginUser == null) {
        throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật số điện thoại");
      }

      // Kiểm tra số điện thoại mới hợp lệ
      if (newPhoneNumber == null || newPhoneNumber.trim().isEmpty()) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật số điện thoại thất bại: Chưa nhập đủ thông tin"));
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Kiểm tra định dạng số điện thoại
      if (isValidPhoneNumber(newPhoneNumber)) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật số điện thoại thất bại: Số điện thoại không hợp lệ"));
        throw new IllegalArgumentException("Số điện thoại không hợp lệ");
      }

      // Kiểm tra độ dài của số điện thoại
      if (newPhoneNumber.length() > 20) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật số điện thoại thất bại: Số điện thoại vượt quá giới hạn 20 ký tự"));
        throw new IllegalArgumentException("Số điện thoại vượt quá giới hạn 20 ký tự");
      }

      // Kiểm tra xem số điện thoại đã được đăng ký chưa
      if (userDao.isPhoneNumberTaken(newPhoneNumber)) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật số điện thoại thất bại: Số điện thoại đã được đăng ký"));
        throw new IllegalStateException("Số điện thoại đã được đăng ký");
      }

      // Thực hiện cập nhật số điện thoại trong cơ sở dữ liệu
      boolean result = userDao.updatePhoneNumber(loginUser.getId(), newPhoneNumber);

      // Ghi log nếu cập nhật số điện thoại thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thành công"));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Cập nhật số điện thoại thất bại"));
      }

      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật số điện thoại thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi số điện thoại đã được đăng ký
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi cập nhật số điện thoại
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Lỗi cơ sở dữ liệu trong quá trình cập nhật số điện thoại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println(
          "Lỗi cơ sở dữ liệu trong quá trình cập nhật số điện thoại: " + e.getMessage());
      return false;
    }
  }

  /**
   * Cập nhật ảnh đại diện.
   *
   * @param newProfileImageDirectory ảnh đại diện mới.
   * @return trả về ảnh đại diện đã được cập nhật thành công chưa.
   */
  public boolean updateProfileImage(String newProfileImageDirectory) {
    try {
      // Kiểm tra nếu người dùng chưa đăng nhập
      if (loginUser == null) {
        throw new IllegalStateException("Bạn cần đăng nhập trước khi cập nhật ảnh đại diện");
      }

      // Kiểm tra đường dẫn ảnh đại diện mới hợp lệ
      if (newProfileImageDirectory == null || newProfileImageDirectory.trim().isEmpty()) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật ảnh đại diện thất bại: Chưa nhập đủ thông tin"));
        throw new IllegalArgumentException("Chưa nhập đủ thông tin");
      }

      // Thực hiện cập nhật ảnh đại diện trong cơ sở dữ liệu
      boolean result = userDao.updateProfileImage(loginUser.getId(), newProfileImageDirectory);

      // Ghi log nếu cập nhật ảnh đại diện thành công
      if (result) {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thành công"));
      } else {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(), loginUser.getUserName(), "Cập nhật ảnh đại diện thất bại"));
      }
      return result;
    } catch (IllegalArgumentException e) {
      // Ghi log khi gặp lỗi do điều kiện không hợp lệ
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Cập nhật ảnh đại diện thất bại: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println("Lỗi cập nhật ảnh đại diện: " + e.getMessage());
      return false;
    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return false;
    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi cập nhật ảnh đại diện
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Lỗi cơ sở dữ liệu trong quá trình cập nhật ảnh đại diện: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println(
          "Lỗi cơ sở dữ liệu trong quá trình cập nhật ảnh đại diện: " + e.getMessage());
      return false;
    }
  }

  /**
   * Lấy thông tin người dùng.
   *
   * @return trả về thông tin người dùng.
   */
  public UserEntity getUserInfo() {
    try {
      // Kiểm tra nếu người dùng chưa đăng nhập
      if (loginUser == null) {
        throw new IllegalStateException("Bạn cần đăng nhập trước khi xem thông tin tài khoản");
      }

      // Lấy thông tin người dùng từ cơ sở dữ liệu
      UserEntity userInfo = userDao.findUserInfo(loginUser.getId());

      // Ghi log khi xem thông tin tài khoản thành công
      logDao.addLog(
          new LogEntity(
              LocalDateTime.now(), loginUser.getUserName(), "Xem thông tin tài khoản thành công"));
      return userInfo;

    } catch (IllegalStateException e) {
      // Xử lý ngoại lệ khi người dùng chưa đăng nhập
      System.out.println("Lỗi: " + e.getMessage());
      return null;

    } catch (SQLException e) {
      // Xử lý lỗi liên quan đến cơ sở dữ liệu khi lấy thông tin tài khoản
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                loginUser.getUserName(),
                "Lỗi cơ sở dữ liệu trong quá trình lấy thông tin tài khoản: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi ghi log: " + logException.getMessage());
      }
      System.out.println(
          "Lỗi cơ sở dữ liệu trong quá trình lấy thông tin tài khoản: " + e.getMessage());
      return null;
    }
  }

  /**
   * Kiểm tra tính hợp lệ của email.
   *
   * @param email email kiểm tra.
   * @return false email hợp lệ, ngược lại trả về true.
   */
  public boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return !matcher.matches();
  }

  /**
   * Kiểm tra tính hợp lệ của số điện thoại.
   *
   * @param phoneNumber Số điện thoại kiểm tra.
   * @return false nếu số điện thoại hợp lệ, ngược lại trả về true.
   */
  public boolean isValidPhoneNumber(String phoneNumber) {
    String phoneNumberRegex = "^[0-9]+$";
    Pattern pattern = Pattern.compile(phoneNumberRegex);
    Matcher matcher = pattern.matcher(phoneNumber);
    return !matcher.matches();
  }
}
