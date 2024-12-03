package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.controllers.UserController;
import org.example.models.UserEntity;

import java.io.IOException;
import java.util.List;

public class UserManagement {

    @FXML
    private TableView<UserEntity> tblUsers;

    @FXML
    private Button btnGoBack;

    @FXML
    private TableColumn<UserEntity, String> colUsername;

    @FXML
    private TableColumn<UserEntity, String> colFirstName;

    @FXML
    private TableColumn<UserEntity, String> colLastName;

    @FXML
    private TableColumn<UserEntity, String> colEmail;

    @FXML
    private TableColumn<UserEntity, String> colPhoneNumber;

    @FXML
    private TableColumn<UserEntity, String> colRole;

    private final UserController userController;

    public UserManagement() {
        this.userController = new UserController();
    }

    @FXML
    public void initialize() {
        // Liên kết các cột với thuộc tính của lớp UserEntity
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Nạp dữ liệu vào bảng
        loadUserData();
    }

    private void loadUserData() {
        // Giả sử UserService có phương thức lấy danh sách tất cả người dùng
        List<UserEntity> users = userController.getAllUsers(); 
        ObservableList<UserEntity> userObservableList = FXCollections.observableArrayList(users);
        tblUsers.setItems(userObservableList);
    }


    @FXML
    public void onGoBackButtonClick() {
        try {
            // Tải lại trang "homepageforadmin.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforadmin.fxml"));
            Parent root = loader.load();

            // Lấy Stage hiện tại và thay đổi Scene
            Stage stage = (Stage) btnGoBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
