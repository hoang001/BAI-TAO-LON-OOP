package org.example.controllers;

import org.example.models.UserEntity;
import org.example.services.basics.UserService;

// Lớp điều khiển để xử lý các yêu cầu từ người dùng liên quan đến User
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Gọi dịch vụ để đăng ký người dùng mới và trả về kết quả
    public boolean registerUser(UserEntity user) {
        return userService.registerUser(user);  // Trả về true nếu thành công, false nếu thất bại
    }

    // Gọi dịch vụ để đăng nhập người dùng và trả về UserEntity nếu thành công, hoặc null nếu thất bại
    public UserEntity loginUser(String userName, String password) {
        return userService.loginUser(userName, password);  // Trả về UserEntity nếu đăng nhập thành công, null nếu thất bại
    }

    // Gọi dịch vụ để đăng xuất người dùng và trả về kết quả
    public boolean logoutUser() {
        return userService.logoutUser();  // Trả về true nếu đăng xuất thành công, false nếu thất bại
    }

    // Gọi dịch vụ để thay đổi mật khẩu người dùng và trả về kết quả
    public boolean changePassword(String newPassword) {
        return userService.changePassword(newPassword);  // Trả về true nếu thay đổi mật khẩu thành công, false nếu thất bại
    }

    // Gọi dịch vụ để cập nhật email người dùng và trả về kết quả
    public boolean updateEmail(String newEmail) {
        return userService.updateEmail(newEmail);  // Trả về true nếu cập nhật email thành công, false nếu thất bại
    }

    // Gọi dịch vụ để cập nhật số điện thoại người dùng và trả về kết quả
    public boolean updatePhoneNumber(String newPhoneNumber) {
        return userService.updatePhoneNumber(newPhoneNumber);  // Trả về true nếu cập nhật số điện thoại thành công, false nếu thất bại
    }

    // Gọi dịch vụ để cập nhật ảnh hồ sơ người dùng và trả về kết quả
    public boolean updateProfileImage(String newProfileImageDirectory) {
        return userService.updateProfileImage(newProfileImageDirectory);  // Trả về true nếu cập nhật ảnh hồ sơ thành công, false nếu thất bại
    }

    // Lấy thông tin người dùng
    public UserEntity getUserInfo() {
        return userService.getUserInfo();  // Trả về UserEntity hoặc null nếu không tìm thấy
    }
}
