package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import org.example.mainLibrary;
import org.example.models.UserEntity;

public class HomepageForUser {

    private UserEntity user;

    @FXML
    private VBox vBox;

    @FXML
    private Button btnLibrary;

    @FXML
    private Button btnMyBooks;

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnLogOut;

    // Phương thức để nhận thông tin UserEntity
    public void setUser(UserEntity user) {
        this.user = user;
    }

    // Các sự kiện khi nhấn nút
    @FXML
    public void onLibraryButtonClick(ActionEvent event) {
        try {
            // Tải file FXML cho giao diện thư viện
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library.fxml"));
            Pane root = loader.load();
    
            // Lấy controller của LibraryView và khởi tạo dữ liệu
            LibraryView libraryView = loader.getController();
            libraryView.loadAllBooks(); // Tải danh sách sách từ cơ sở dữ liệu
    
            // Chuyển sang giao diện thư viện
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Thư viện tổng");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển đến thư viện tổng.");
        }
    }
    

    @FXML
    public void onMyBooksButtonClick(ActionEvent event) throws IOException {        
        // Load màn hình tủ sách từ file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookshelf.fxml"));
        Pane bookshelf = loader.load(); // Thay đổi từ VBox thành Pane

        // Chuyển sang màn hình tủ sách
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(bookshelf);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void onAccountButtonClick(ActionEvent event) {
        try {
            // Tải file account.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/account.fxml"));
            Pane root = loader.load();

            // Lấy controller của AccountView và truyền thông tin người dùng
            AccountView accountView = loader.getController();
            accountView.setUser(user); // user là thuộc tính UserEntity đã được lưu trong class này

            // Chuyển sang giao diện tài khoản
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Thông tin tài khoản");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển đến trang tài khoản.");
        }
    }


    // Nút đăng xuất
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
