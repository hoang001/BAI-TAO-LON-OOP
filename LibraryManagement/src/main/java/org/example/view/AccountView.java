package org.example.view;

import java.io.IOException;

import org.example.MainLibrary;
import org.example.models.UserEntity;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AccountView {

    @FXML
    private Label lUsername;

    @FXML
    private Label lFullName;

    @FXML
    private Label lEmail;

    @FXML
    private Label lRole;

    @FXML
    private Button bLogout;

    @FXML
    private Button bUpdateAccount;

    @FXML
    private Button bGoBack;

    @FXML
    private ImageView profileImageView;

    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
        updateUI();
    }

    private void updateUI() {
        if (user != null) {
            lUsername.setText(user.getUserName());
            lFullName.setText(user.getFirstName() + " " + user.getLastName());
            lEmail.setText(user.getEmail());
            lRole.setText(user.getRole().toString());

            // Thử tải ảnh đại diện người dùng
            String profileImageUrl = user.getProfileImageDirectory();
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                try {
                    Image profileImage = new Image(profileImageUrl, 150, 150, true, true);
                    profileImageView.setImage(profileImage);
                } catch (Exception e) {
                    // Nếu không tải được, giữ ảnh mặc định (không thay đổi gì)
                    System.err.println("Không thể tải ảnh đại diện người dùng, sử dụng ảnh mặc định.");
                }
            }
        }
    }

    @FXML
    public void onUpdateAccountButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/accountupdate.fxml"));
            Parent updateAccountRoot = loader.load();

            AccountUpdate accountUpdate = loader.getController();
            accountUpdate.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Cập nhật tài khoản");
            stage.setScene(new Scene(updateAccountRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    @FXML
    public void onLogOutButtonClick() {
        try {
            Stage currentStage = (Stage) bLogout.getScene().getWindow();
            MainLibrary.init(currentStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onGoBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforuser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) bLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
