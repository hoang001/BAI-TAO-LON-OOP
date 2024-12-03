package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import org.example.mainLibrary;
import org.example.models.UserEntity;

public class HomepageForAdmin {

    private UserEntity admin;

    @FXML
    private VBox vBox;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Button btnManageBooks;

    @FXML
    private Button btnLogOut;

    // Phương thức để nhận thông tin UserEntity
    public void setAdmin(UserEntity admin) {
        this.admin = admin;
    }

    // Nút "Quản lý người dùng"
    @FXML
    public void onManageUsersButtonClick(ActionEvent event) {
        try {
            // Tải file FXML cho giao diện quản lý người dùng
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usermanagement.fxml"));
            Pane root = loader.load();
    
            // Chuyển sang giao diện quản lý người dùng
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý Người Dùng");
        } catch (IOException e) {
            System.out.println("Không thể chuyển đến trang quản lý người dùng.");
            e.printStackTrace();
        }
    }
    


    // Nút "Quản lý sách"
    @FXML
    public void onManageBooksButtonClick(ActionEvent event) {
        try {
            // Tải file FXML cho giao diện quản lý sách
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/managelibrary.fxml"));
            Pane root = loader.load();

            // Chuyển sang giao diện quản lý sách
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý Sách");
        } catch (IOException e) {
            System.out.println("Không thể chuyển đến trang quản lý sách.");
            e.printStackTrace();
        }
    }


    // Nút "Đăng xuất"
    @FXML
    public void onLogOutButtonClick(ActionEvent event) {
        // Quay lại trang bắt đầu (StartView)
        try {
            // Lấy Stage hiện tại
            Stage currentStage = (Stage) btnLogOut.getScene().getWindow();

            // Gọi lại hàm init để khởi động lại ứng dụng
            mainLibrary.init(currentStage);  // Chuyển Stage hiện tại vào để làm mới nó

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
