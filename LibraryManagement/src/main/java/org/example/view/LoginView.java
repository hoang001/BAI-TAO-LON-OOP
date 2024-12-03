package org.example.view;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.example.mainLibrary;
import org.example.daos.implementations.UserDaoImpl;
import org.example.models.UserEntity;
import org.example.models.UserEntity.Roles;
import org.example.services.basics.UserService;

public class LoginView {

    @FXML
    private TextField tFUsername;

    @FXML
    private PasswordField tFPassword;

    @FXML
    private Label lStatus;

    @FXML
    private Button bLogin;

    // Khởi tạo UserDaoImpl để làm việc với cơ sở dữ liệu
    private final UserDaoImpl userDao = new UserDaoImpl();

    // Sử dụng Singleton để lấy UserService
    private final UserService userService = UserService.getInstance(); // Lấy instance duy nhất của UserService

    @FXML
    public void onLoginButtonClick() throws IOException {
        String username = tFUsername.getText();
        String password = tFPassword.getText();
    
        try {
            // Kiểm tra thông tin đăng nhập
            UserEntity user = userDao.loginUser(username, password);
            if (user != null) {
                lStatus.setText("Đăng nhập thành công!");
                lStatus.setStyle("-fx-text-fill: green;");

                // Thiết lập người dùng đang đăng nhập vào UserService
                userService.setLoginUser(user);  // Gọi setLoginUser() để lưu người dùng vào hệ thống
    
                // Kiểm tra vai trò của người dùng
                Roles role = user.getRole();
    
                FXMLLoader loader;
                Pane root;
    
                if (role == Roles.USER) {
                    // Nếu là User, chuyển đến trang homepageforuser.fxml
                    loader = new FXMLLoader(getClass().getResource("/homepageforuser.fxml"));
                    root = loader.load();
    
                    // Lấy controller của HomepageForUser và truyền đối tượng UserEntity
                    HomepageForUser homepageController = loader.getController();
                    homepageController.setUser(user); // Truyền User vào controller
                } else if (role == Roles.ADMIN || role == Roles.LIBRARIAN) {
                    // Nếu là Admin hoặc Librarian, chuyển đến trang homepageforadmin.fxml
                    loader = new FXMLLoader(getClass().getResource("/homepageforadmin.fxml"));
                    root = loader.load();
    
                    // Lấy controller của HomepageForAdmin và truyền đối tượng UserEntity
                    HomepageForAdmin homepageController = loader.getController();
                    homepageController.setAdmin(user); // Truyền User vào controller
                } else {
                    // Trường hợp vai trò không hợp lệ
                    lStatus.setText("Vai trò không hợp lệ!");
                    lStatus.setStyle("-fx-text-fill: red;");
                    return;
                }
    
                // Hiển thị giao diện mới
                Stage stage = (Stage) bLogin.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Homepage");
            } else {
                lStatus.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
                lStatus.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            lStatus.setText("Có lỗi xảy ra. Vui lòng thử lại sau.");
            lStatus.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        // Quay lại trang bắt đầu(StartView)
        try {
            // Lấy Stage hiện tại
            Stage currentStage = (Stage) tFUsername.getScene().getWindow();
            
            // Gọi lại hàm init để khởi động lại ứng dụng
            mainLibrary.init(currentStage);  // Chuyển Stage hiện tại vào để làm mới nó
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
