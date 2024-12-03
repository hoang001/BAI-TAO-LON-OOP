package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.example.mainLibrary;
import org.example.daos.implementations.UserDaoImpl;
import org.example.daos.interfaces.UserDao;
import org.example.models.UserEntity;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpView {

    @FXML
    private TextField tFUsername;

    @FXML
    private PasswordField tFPassword;

    @FXML
    private PasswordField tFConfirmPassword;

    @FXML
    private TextField tFFirstName;

    @FXML
    private TextField tFLastName;

    @FXML
    private TextField tFEmail;

    @FXML
    private TextField tFPhoneNumber;

    @FXML
    private Button bSignUp;

    private final UserDao userDao = new UserDaoImpl(); // Sử dụng UserDaoImpl để thao tác với CSDL

    @FXML
    public void onSignUpButtonClick() {
        // Lấy thông tin từ form
        String username = tFUsername.getText();
        String password = tFPassword.getText();
        String confirmPassword = tFConfirmPassword.getText();
        String firstName = tFFirstName.getText();
        String lastName = tFLastName.getText();
        String email = tFEmail.getText();
        String phoneNumber = tFPhoneNumber.getText();

        // Kiểm tra tính hợp lệ
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Vui lòng điền đầy đủ thông tin!", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Mật khẩu xác nhận không khớp!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Kiểm tra xem username, email, và số điện thoại có bị trùng không
            if (userDao.isUsernameTaken(username)) {
                showAlert("Tên người dùng đã tồn tại!", Alert.AlertType.ERROR);
                return;
            }

            if (userDao.isEmailTaken(email)) {
                showAlert("Email đã được sử dụng!", Alert.AlertType.ERROR);
                return;
            }

            if (userDao.isPhoneNumberTaken(phoneNumber)) {
                showAlert("Số điện thoại đã được sử dụng!", Alert.AlertType.ERROR);
                return;
            }

            // Tạo đối tượng UserEntity và gán vai trò mặc định "USER"
            UserEntity newUser = new UserEntity();
            newUser.setUserName(username);
            newUser.setPasswordHash(password); // Giả sử mật khẩu được lưu dưới dạng hash, cần băm trước
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            // Role mặc định là USER (đã được định nghĩa trong UserEntity)

            // Gọi hàm registerUser để lưu vào CSDL
            boolean isRegistered = userDao.registerUser(newUser);

            if (isRegistered) {
                showAlert("Đăng ký thành công!", Alert.AlertType.INFORMATION);
                switchToHomepageView(newUser); // Chuyển đến View
            } else {
                showAlert("Đăng ký thất bại. Thử lại sau!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Lỗi kết nối cơ sở dữ liệu!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Chuyển đến trang View sau khi đăng ký thành công.
     */
    private void switchToHomepageView(UserEntity user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforuser.fxml"));
            Pane homepageViewRoot = loader.load();
    
            // Lấy controller của HomepageView và truyền đối tượng UserEntity
            HomepageForUser homepageController = loader.getController();
            homepageController.setUser(user);
    
            // Hiển thị giao diện mới
            Stage stage = (Stage) bSignUp.getScene().getWindow();
            stage.setScene(new Scene(homepageViewRoot));
            stage.setTitle("Homepage");
        } catch (IOException e) {
            showAlert("Không thể chuyển đến Homepage!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    

    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        // Quay lại trang chủ (StartView)
        try {
            // Lấy Stage hiện tại
            Stage currentStage = (Stage) tFUsername.getScene().getWindow();

            // Gọi lại hàm init để khởi động lại ứng dụng
            mainLibrary.init(currentStage); // Chuyển Stage hiện tại vào để làm mới nó

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /**
     * Hiển thị thông báo cho người dùng.
     * @param message Nội dung thông báo.
     * @param alertType Loại thông báo (ERROR, INFORMATION, v.v.).
     */
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle("Thông báo");
        alert.showAndWait();
    } 
}
