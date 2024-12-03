package org.example.view;

import org.example.mainLibrary;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartView implements Initializable {
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = mainLibrary.stageSender; // Lấy Stage chính
    }

    public void onLoginButtonClick() throws IOException {
        // Chuyển sang giao diện LoginView
        FXMLLoader fxmlLoader = new FXMLLoader(mainLibrary.class.getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 350);
        stage.setTitle("Đăg nhập");
        stage.setScene(scene);
        stage.show();
    }

    public void onSignUpButtonClick() throws IOException {
        // Chuyển sang giao diện SignUpView
        FXMLLoader fxmlLoader = new FXMLLoader(mainLibrary.class.getResource("/signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Đăng ký tài khoản");
        stage.setScene(scene);
        stage.show();
    }
}
