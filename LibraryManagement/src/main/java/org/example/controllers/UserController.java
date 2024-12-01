package org.example.controllers;

import org.example.models.UserEntity;
import org.example.services.basics.UserService;

/**
 * Lớp Controller chịu trách nhiệm xử lý các hành động liên quan đến người dùng như đăng ký, đăng
 * nhập, và cập nhật thông tin người dùng. Lớp này hoạt động như một cầu nối giữa giao diện người
 * dùng (UI) và các dịch vụ nền tảng để quản lý dữ liệu người dùng.
 */
public class UserController {

  // Đối tượng UserService được sử dụng để xử lý các logic nghiệp vụ liên quan đến các thao tác của
  // người dùng.
  private final UserService userService;

  /**
   * Constructor khởi tạo đối tượng UserService. Phương thức này được gọi khi tạo một đối tượng
   * UserController để quản lý các thao tác liên quan đến người dùng.
   */
  public UserController() {
    this.userService = UserService.getInstance();
  }

  /**
   * Đăng ký người dùng mới bằng cách gọi dịch vụ UserService để xử lý logic đăng ký.
   *
   * @param user Đối tượng UserEntity chứa thông tin đăng ký của người dùng (ví dụ: tên người dùng,
   *     mật khẩu, email).
   * @return boolean Trả về true nếu đăng ký thành công, false nếu thất bại.
   */
  public boolean registerUser(UserEntity user) {
    return userService.registerUser(user);
  }

  /**
   * Đăng nhập người dùng bằng cách kiểm tra thông tin đăng nhập với dữ liệu đã lưu trữ thông qua
   * UserService.
   *
   * @param userName Tên người dùng mà người dùng nhập vào.
   * @param password Mật khẩu mà người dùng nhập vào.
   * @return UserEntity Trả về đối tượng UserEntity nếu đăng nhập thành công, null nếu thất bại.
   */
  public UserEntity loginUser(String userName, String password) {
    return userService.loginUser(userName, password);
  }

  /**
   * Đăng xuất người dùng bằng cách gọi dịch vụ UserService để xử lý logic đăng xuất.
   *
   * @return boolean Trả về true nếu đăng xuất thành công, false nếu thất bại.
   */
  public boolean logoutUser() {
    return userService.logoutUser();
  }

  /**
   * Thay đổi mật khẩu của người dùng thông qua dịch vụ UserService.
   *
   * @param newPassword Mật khẩu mới mà người dùng muốn thay đổi.
   * @return boolean Trả về true nếu thay đổi mật khẩu thành công, false nếu thất bại.
   */
  public boolean changePassword(String newPassword) {
    return userService.changePassword(newPassword);
  }

  /**
   * Cập nhật email của người dùng thông qua dịch vụ UserService.
   *
   * @param newEmail Email mới mà người dùng muốn cập nhật.
   * @return boolean Trả về true nếu cập nhật email thành công, false nếu thất bại.
   */
  public boolean updateEmail(String newEmail) {
    return userService.updateEmail(newEmail);
  }

  /**
   * Cập nhật số điện thoại của người dùng thông qua dịch vụ UserService.
   *
   * @param newPhoneNumber Số điện thoại mới mà người dùng muốn cập nhật.
   * @return boolean Trả về true nếu cập nhật số điện thoại thành công, false nếu thất bại.
   */
  public boolean updatePhoneNumber(String newPhoneNumber) {
    return userService.updatePhoneNumber(newPhoneNumber);
  }

  /**
   * Cập nhật ảnh hồ sơ của người dùng thông qua dịch vụ UserService.
   *
   * @param newProfileImageDirectory Đường dẫn đến ảnh hồ sơ mới mà người dùng muốn cập nhật.
   * @return boolean Trả về true nếu cập nhật ảnh hồ sơ thành công, false nếu thất bại.
   */
  public boolean updateProfileImage(String newProfileImageDirectory) {
    return userService.updateProfileImage(newProfileImageDirectory);
  }

  /**
   * Lấy thông tin người dùng hiện tại từ dịch vụ UserService.
   *
   * @return UserEntity Trả về đối tượng UserEntity chứa thông tin người dùng, hoặc null nếu không
   *     tìm thấy.
   */
  public UserEntity getUserInfo() {
    return userService.getUserInfo();
  }
}
