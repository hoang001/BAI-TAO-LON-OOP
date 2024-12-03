package org.example.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.daos.implementations.BorrowedBookDaoImpl;
import org.example.daos.implementations.ReadBookDaoImpl;
import org.example.models.BorrowedBookEntity;
import org.example.models.ReadBookEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Bookshelf {

    @FXML
    private TableView<BorrowedBookEntity> borrowedBooksTable;

    @FXML
    private TableView<ReadBookEntity> readBooksTable;

    @FXML
    private TableColumn<BorrowedBookEntity, String> bookTitleColumn;
    
    @FXML
    private TableColumn<BorrowedBookEntity, LocalDate> borrowDateColumn;

    @FXML
    private TableColumn<BorrowedBookEntity, LocalDate> returnDateColumn;

    @FXML
    private TableColumn<ReadBookEntity, String> readBookTitleColumn;
    
    @FXML
    private TableColumn<ReadBookEntity, LocalDate> readDateColumn;

    private BorrowedBookDaoImpl borrowedBookDao = new BorrowedBookDaoImpl();
    private ReadBookDaoImpl readBookDao = new ReadBookDaoImpl();

    private String userName;

    public Bookshelf() {}

    public Bookshelf(String userName) {
        this.userName = userName;
    }

    @FXML
    public void initialize() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        readBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        readDateColumn.setCellValueFactory(new PropertyValueFactory<>("readDate"));

        loadBorrowedBooks();
        loadReadBooks();
    }

    private void loadBorrowedBooks() {
        try {
            List<BorrowedBookEntity> borrowedBooks = borrowedBookDao.findAllBorrowedBooksByUser(userName);
            borrowedBooksTable.getItems().setAll(borrowedBooks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadReadBooks() {
        try {
            List<ReadBookEntity> readBooks = readBookDao.findReadBooks(userName);
            readBooksTable.getItems().setAll(readBooks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackButtonClick(ActionEvent event) throws IOException {
        // Tải lại file FXML của HomepageForUser
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforuser.fxml"));
        GridPane homepageForUser = loader.load(); // Thay đổi từ VBox sang GridPane

        Stage stage = (Stage) borrowedBooksTable.getScene().getWindow();
        Scene scene = new Scene(homepageForUser); // Sử dụng GridPane làm root node
        stage.setScene(scene);
        stage.show();
    }

}
