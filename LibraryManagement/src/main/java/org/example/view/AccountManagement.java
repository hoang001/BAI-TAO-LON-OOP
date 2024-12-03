package org.example.view;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.example.models.BookEntity;
import org.example.services.basics.BookService;

/**
 * Lớp điều khiển quản lý các chức năng liên quan đến sách trong User Management.
 */
public class AccountManagement {

    // Khai báo các bảng và cột trong FXML
    @FXML 
    private TableView<BookEntity> tblBorrowedBooks;

    @FXML 
    private TableView<BookEntity> tblReturnedBooks;

    @FXML 
    private TableColumn<BookEntity, String> colBorrowedBookTitle;

    @FXML 
    private TableColumn<BookEntity, String> colBorrowedBookAuthor;

    @FXML 
    private TableColumn<BookEntity, String> colReturnedBookTitle;

    @FXML 
    private TableColumn<BookEntity, String> colReturnedBookAuthor;

    @FXML 
    private Button btnBack;
    
    private BookService bookService;

    // Constructor
    public AccountManagement() {
        this.bookService = new BookService();
    }

    /**
     * Hàm khởi tạo bảng khi trang được tải.
     */
    @FXML
    public void initialize() {
        // Thiết lập cột cho bảng "Sách Đang Mượn"
        colBorrowedBookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBorrowedBookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Thiết lập cột cho bảng "Sách Đã Trả"
        colReturnedBookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colReturnedBookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Lấy danh sách sách đã mượn và đã trả từ controller
        loadBooks();
    }

    /**
     * Tải thông tin sách đã mượn và đã trả từ BookService.
     */
    private void loadBooks() {
        List<BookEntity> borrowedBooks = bookService.getAllBooks(); // Giả sử bạn đã có danh sách các sách mượn từ backend
        tblBorrowedBooks.getItems().setAll(borrowedBooks);

        // Tải sách đã trả (thêm logic nếu cần thiết để phân biệt sách đã trả)
        List<BookEntity> returnedBooks = bookService.getAllBooks(); // Hoặc từ danh sách khác cho sách đã trả
        tblReturnedBooks.getItems().setAll(returnedBooks);
    }

    /**
     * Xử lý khi người dùng nhấn nút "Quay Lại" để quay về trang chính.
     */
    @FXML
    public void onBackButtonClick(ActionEvent event) {
        // Logic để quay lại trang HomepageForUser
        // Cần sử dụng một đoạn mã để chuyển tiếp về trang trước
        // Ví dụ sử dụng FXMLLoader để load lại trang HomepageForUser
        try {
            // Tạo một FXMLLoader mới để chuyển tới trang HomepageForUser.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/HomepageForUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể quay lại trang chính.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Xử lý khi người dùng nhấn vào một cuốn sách trong bảng "Sách Đang Mượn" và đánh dấu là đã trả.
     */
    @FXML
    public void onReturnBook(MouseEvent event) {
        BookEntity selectedBook = tblBorrowedBooks.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            boolean result = bookService.deleteBookById(selectedBook.getId());
            if (result) {
                showAlert("Thành Công", "Sách đã được trả.", Alert.AlertType.INFORMATION);
                loadBooks(); // Cập nhật lại bảng sau khi trả sách
            } else {
                showAlert("Lỗi", "Không thể trả sách.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Hiển thị thông báo cho người dùng.
     * 
     * @param title Tiêu đề của thông báo.
     * @param message Nội dung thông báo.
     * @param alertType Loại thông báo (thành công, lỗi, vv).
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
