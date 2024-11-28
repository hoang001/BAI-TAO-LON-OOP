package org.example.daos.implementations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import org.example.daos.interfaces.UserDao;
import org.example.models.UserEntity;
import org.example.models.UserEntity.Roles;
import org.example.utils.DatabaseConnection;

/** Lớp triển khai UserDao để thao tác với cơ sở dữ liệu. */
public class UserDaoImpl implements UserDao {

  private final Connection connection;

  /** Khởi tạo một đối tượng UserDaoImpl. */
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
   * Xác thực mật khẩu.
   *
   * @param password Mật khẩu người dùng nhập vào.
   * @param hashedPassword Mật khẩu đã được mã hóa lưu trữ trong hệ thống.
   * @return true nếu mật khẩu khớp, false nếu không khớp.
   * @throws NoSuchAlgorithmException nếu thuật toán mã hóa không hợp lệ.
   */
  private boolean verifyPassword(String password, String hashedPassword)
      throws NoSuchAlgorithmException {
    String hashedInputPassword = hashPassword(password);
    return hashedInputPassword.equals(hashedPassword);
  }

  /**
   * Đăng ký người dùng mới.
   *
   * @param user Đối tượng UserEntity chứa thông tin người dùng cần đăng ký.
   * @return true nếu tạo tài khoản thành công, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean registerUser(UserEntity user) throws SQLException {
    try {
      String hashedPassword = hashPassword(user.getPasswordHash());
      String sql =
          "INSERT INTO users (userName, passwordHash, email, firstName, "
              + "lastName, phoneNumber, profileImageDirectory, role) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
   * Đăng nhập người dùng.
   *
   * @param userName Tên đăng nhập của người dùng.
   * @param password Mật khẩu của người dùng.
   * @return Đối tượng UserEntity nếu đăng nhập thành công, ngược lại trả về null.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public UserEntity loginUser(String userName, String password) throws SQLException {
    String sql = "SELECT * FROM users WHERE userName = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, userName);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          String storedPasswordHash = resultSet.getString("passwordHash");
          if (verifyPassword(password, storedPasswordHash)) {
            return new UserEntity(
                resultSet.getInt("userId"),
                resultSet.getString("userName"),
                resultSet.getString("passwordHash"),
                resultSet.getString("email"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("phoneNumber"),
                resultSet.getString("profileImageDirectory"),
                Roles.valueOf(resultSet.getString("role")));
          }
        }
      }
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Không tìm thấy thuật toán hash", e);
    }
    return null;
  }

  /**
   * Thay đổi mật khẩu người dùng.
   *
   * @param userId ID của người dùng.
   * @param newPassword Mật khẩu mới của người dùng.
   * @return true nếu thay đổi mật khẩu thành công, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean changePassword(int userId, String newPassword) throws SQLException {
    try {
      String newHashedPassword = hashPassword(newPassword);
      String sql = "UPDATE users SET passwordHash = ? WHERE userId = ?";
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
   * Cập nhật email của người dùng.
   *
   * @param userId ID của người dùng.
   * @param newEmail Email mới của người dùng.
   * @return true nếu cập nhật email thành công, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean updateEmail(int userId, String newEmail) throws SQLException {
    String sql = "UPDATE users SET email = ? WHERE userId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, newEmail);
      preparedStatement.setInt(2, userId);
      return preparedStatement.executeUpdate() > 0;
    }
  }

  /**
   * Cập nhật số điện thoại của người dùng.
   *
   * @param userId ID của người dùng.
   * @param newPhoneNumber Số điện thoại mới của người dùng.
   * @return true nếu cập nhật số điện thoại thành công, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean updatePhoneNumber(int userId, String newPhoneNumber) throws SQLException {
    String sql = "UPDATE users SET phoneNumber = ? WHERE userId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, newPhoneNumber);
      preparedStatement.setInt(2, userId);
      return preparedStatement.executeUpdate() > 0;
    }
  }

  /**
   * Cập nhật ảnh đại diện của người dùng.
   *
   * @param userId ID của người dùng.
   * @param newProfileImageDirectory Đường dẫn ảnh đại diện mới của người dùng.
   * @return true nếu cập nhật ảnh đại diện thành công, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean updateProfileImage(int userId, String newProfileImageDirectory)
      throws SQLException {
    String sql = "UPDATE users SET profileImageDirectory = ? WHERE userId = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, newProfileImageDirectory);
      preparedStatement.setInt(2, userId);
      return preparedStatement.executeUpdate() > 0;
    }
  }

  /**
   * Tìm thông tin người dùng dựa trên ID.
   *
   * @param userId ID của người dùng.
   * @return Đối tượng UserEntity tương ứng với userId, hoặc null nếu không tìm thấy.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public UserEntity findUserInfo(int userId) throws SQLException {
    String sql = "SELECT * FROM users WHERE userId = ?";
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
              resultSet.getString("profileImageDirectory"),
              Roles.valueOf(resultSet.getString("role")));
        }
      }
    }
    return null;
  }

  /**
   * Kiểm tra xem tên người dùng có tồn tại hay không.
   *
   * @param userName Tên người dùng cần kiểm tra.
   * @return true nếu tên người dùng tồn tại, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean isUsernameTaken(String userName) throws SQLException {
    String sql = "SELECT COUNT(*) FROM users WHERE userName = ?";
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
   * Kiểm tra xem email có được đăng ký hay không.
   *
   * @param email Email cần kiểm tra.
   * @return true nếu email đã đăng ký, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean isEmailTaken(String email) throws SQLException {
    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
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
   * Kiểm tra xem số điện thoại có được đăng ký hay không.
   *
   * @param phoneNumber Số điện thoại cần kiểm tra.
   * @return true nếu số điện thoại đã đăng ký, ngược lại trả về false.
   * @throws SQLException nếu có lỗi trong quá trình thao tác với cơ sở dữ liệu.
   */
  @Override
  public boolean isPhoneNumberTaken(String phoneNumber) throws SQLException {
    String sql = "SELECT COUNT(*) FROM users WHERE phoneNumber = ?";
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
