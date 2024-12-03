package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.models.BookEntity;
import org.example.services.basics.BookService;

import java.io.IOException;
import java.util.List;

public class ManageLibrary {

    @FXML
    private TableView<BookEntity> tableBooks;

    @FXML
    private TableColumn<BookEntity, String> colIsbn;

    @FXML
    private TableColumn<BookEntity, String> colTitle;

    @FXML
    private TableColumn<BookEntity, String> colAuthor;

    @FXML
    private TableColumn<BookEntity, String> colPublisher;

    @FXML
    private TableColumn<BookEntity, Integer> colQuantity;

    @FXML
    private Button btnAddBook;

    @FXML
    private Button btnDeleteBook;

    @FXML
    private Button btnBack;

    @FXML
    private Label lblStatus;

    private final BookService bookService = new BookService();
    private ObservableList<BookEntity> bookList;

    @FXML
    public void initialize() {
        // Cấu hình cột TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisherName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Tải danh sách sách từ cơ sở dữ liệu
        loadAllBooks();
    }

    public void loadAllBooks() {
        try {
            List<BookEntity> books = bookService.getAllBooks();
            bookList = FXCollections.observableArrayList(books);
            tableBooks.setItems(bookList);
            lblStatus.setText("Đã tải danh sách sách.");
        } catch (Exception e) {
            lblStatus.setText("Lỗi khi tải danh sách sách.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddBookButtonClick(ActionEvent event) {
        try {
            // Chuyển đến trang thêm sách
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addbook.fxml"));
            Pane root = loader.load();

            Stage stage = (Stage) btnAddBook.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Thêm Sách");
        } catch (IOException e) {
            lblStatus.setText("Không thể chuyển đến trang thêm sách.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteBookButtonClick(ActionEvent event) {
        try {
            // Chuyển đến trang xóa sách
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deletebook.fxml"));
            Pane root = loader.load();

            Stage stage = (Stage) btnDeleteBook.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Xóa Sách");
        } catch (IOException e) {
            lblStatus.setText("Không thể chuyển đến trang xóa sách.");
            e.printStackTrace();
        }
    }

        @FXML
    public void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforadmin.fxml"));
            Pane root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("homepage");
        } catch (IOException e) {
            showError("Không thể quay lại trang homepage.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: red;");
    }
}
