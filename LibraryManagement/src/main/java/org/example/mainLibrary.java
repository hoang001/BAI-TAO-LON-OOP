package org.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLibrary extends Application {
    public static Stage stageSender;

    @Override
    public void start(Stage stage) throws IOException {
        stageSender = stage;
        init(stage); // Hiển thị giao diện StartView
    }

    public static void init(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainLibrary.class.getResource("/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("OOP LIBRARY");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
