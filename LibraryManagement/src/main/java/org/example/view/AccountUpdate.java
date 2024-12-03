package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.controllers.UserController;
import org.example.models.UserEntity;

public class AccountUpdate {

    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private PasswordField pfNewPassword;  // Mật khẩu mới
    @FXML
    private PasswordField pfConfirmPassword;  // Xác nhận mật khẩu mới
    @FXML
    private Button bSave;
    @FXML
    private Button bCancel;

    private UserEntity user;  // Đối tượng UserEntity chứa thông tin người dùng
    private final UserController userController;  // Đối tượng UserController xử lý logic người dùng

    // Khởi tạo UserController khi tạo AccountUpdate
    public AccountUpdate() {
        this.userController = new UserController();
    }

    // Nhận thông tin người dùng từ màn hình trước (AccountView)
    public void setUser(UserEntity user) {
        this.user = user;
        updateUI();
    }

    // Cập nhật giao diện người dùng với thông tin hiện có
    private void updateUI() {
        if (user != null) {
            tfUsername.setText(user.getUserName());
            tfFirstName.setText(user.getFirstName());
            tfLastName.setText(user.getLastName());
            tfEmail.setText(user.getEmail());
            tfPhoneNumber.setText(user.getPhoneNumber());
        }
    }

    @FXML
    public void onSaveButtonClick() {
        try {
            // Lấy giá trị từ các trường nhập liệu
            String newEmail = tfEmail.getText().trim();
            String newPhoneNumber = tfPhoneNumber.getText().trim();
    
            boolean hasChanges = false;
    
            // Cập nhật email nếu khác giá trị cũ và không rỗng
            if (!newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
                if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    showAlert("Lỗi", "Email không hợp lệ!", Alert.AlertType.ERROR);
                    return;
                }
                boolean emailUpdated = userController.updateEmail(newEmail);
                if (emailUpdated) {
                    user = userController.getUserInfo(); // Cập nhật lại thông tin người dùng
                    hasChanges = true;
                } else {
                    showAlert("Lỗi", "Không thể cập nhật email!", Alert.AlertType.ERROR);
                    return;
                }
            }
    
            // Cập nhật số điện thoại nếu khác giá trị cũ và không rỗng
            if (!newPhoneNumber.isEmpty() && !newPhoneNumber.equals(user.getPhoneNumber())) {
                if (!newPhoneNumber.matches("^[0-9]{10,15}$")) {
                    showAlert("Lỗi", "Số điện thoại không hợp lệ!", Alert.AlertType.ERROR);
                    return;
                }
                boolean phoneUpdated = userController.updatePhoneNumber(newPhoneNumber);
                if (phoneUpdated) {
                    user = userController.getUserInfo(); // Cập nhật lại thông tin người dùng
                    hasChanges = true;
                } else {
                    showAlert("Lỗi", "Không thể cập nhật số điện thoại!", Alert.AlertType.ERROR);
                    return;
                }
            }
    
            // Hiển thị thông báo kết quả
            if (hasChanges) {
                showAlert("Thành công", "Thông tin đã được cập nhật!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Thông báo", "Không có thay đổi nào được thực hiện.", Alert.AlertType.INFORMATION);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi khi cập nhật thông tin!", Alert.AlertType.ERROR);
        }
    }
    
    

    @FXML
    public void onChangePasswordButtonClick() {
        try {
            String newPassword = pfNewPassword.getText().trim();
            String confirmPassword = pfConfirmPassword.getText().trim();
    
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập mật khẩu mới và xác nhận mật khẩu!", Alert.AlertType.ERROR);
                return;
            }
    
            if (!newPassword.equals(confirmPassword)) {
                showAlert("Lỗi", "Mật khẩu mới và xác nhận mật khẩu không khớp!", Alert.AlertType.ERROR);
                return;
            }
    
            boolean passwordChanged = userController.changePassword(newPassword);
            if (passwordChanged) {
                showAlert("Thành công", "Mật khẩu đã được thay đổi!", Alert.AlertType.INFORMATION);
                pfNewPassword.clear();
                pfConfirmPassword.clear();
            } else {
                showAlert("Lỗi", "Không thể thay đổi mật khẩu!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi trong quá trình thay đổi mật khẩu!", Alert.AlertType.ERROR);
        }
    }
    

    @FXML
    public void onCancelButtonClick() {
        closeWindow();
    }

    private void closeWindow() {
        javafx.stage.Stage stage = (javafx.stage.Stage) bCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
