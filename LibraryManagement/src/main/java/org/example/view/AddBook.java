package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.models.BookEntity;
import org.example.services.basics.BookService;

import java.io.IOException;

public class AddBook {

    @FXML
    private TextField tfIsbn, tfTitle, tfAuthor, tfPublisher, tfCategory, tfPublicationYear, tfQuantity, tfAdditionalQuantity;

    @FXML
    private Button btnCheckIsbn, btnAddBook, btnBack;

    @FXML
    private Label lblStatus;

    private final BookService bookService = new BookService();

    @FXML
    public void initialize() {
        resetForm();
    }

    @FXML
    public void onCheckIsbnButtonClick() {
        String isbn = tfIsbn.getText().trim();
    
        if (isbn.isEmpty()) {
            lblStatus.setText("Vui lòng nhập ISBN.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
    
        try {
            BookEntity existingBook = bookService.getBookByIsbn(isbn);
    
            if (existingBook != null) {
                // Điền thông tin sách nếu ISBN tồn tại
                tfTitle.setText(existingBook.getTitle());
                tfAuthor.setText(existingBook.getAuthorName());
                tfPublisher.setText(existingBook.getPublisherName());
                tfCategory.setText(existingBook.getCategoryName());
                tfPublicationYear.setText(String.valueOf(existingBook.getPublicationYear()));
                tfQuantity.setText(String.valueOf(existingBook.getQuantity()));
    
                tfAdditionalQuantity.setDisable(false); // Cho phép nhập số lượng thêm
                lblStatus.setText("ISBN đã tồn tại. Bạn có thể cập nhật số lượng.");
                lblStatus.setStyle("-fx-text-fill: green;");
            } else {
                // Nếu ISBN chưa tồn tại, cho phép nhập thông tin mới
                enableForm();
                lblStatus.setText("ISBN chưa tồn tại. Vui lòng nhập thông tin sách.");
                lblStatus.setStyle("-fx-text-fill: green;");
            }
    
        } catch (Exception e) {
            lblStatus.setText("Lỗi khi kiểm tra ISBN.");
            lblStatus.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onAddBookButtonClick() {
        String isbn = tfIsbn.getText().trim();
    
        if (isbn.isEmpty()) {
            lblStatus.setText("Vui lòng kiểm tra ISBN trước.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }
    
        try {
            BookEntity book = bookService.getBookByIsbn(isbn);
    
            if (book != null) {
                // Nếu sách đã tồn tại, chỉ cập nhật số lượng
                int additionalQuantity = Integer.parseInt(tfAdditionalQuantity.getText().trim());
                if (additionalQuantity <= 0) {
                    lblStatus.setText("Số lượng phải lớn hơn 0.");
                    lblStatus.setStyle("-fx-text-fill: red;");
                    return;
                }
    
                boolean result = bookService.addBookQuantity(isbn, additionalQuantity);
    
                if (result) {
                    lblStatus.setText("Cập nhật số lượng thành công.");
                    lblStatus.setStyle("-fx-text-fill: green;");
                    resetForm();
                } else {
                    lblStatus.setText("Cập nhật số lượng thất bại.");
                    lblStatus.setStyle("-fx-text-fill: red;");
                }
            } else {
                // Nếu sách chưa tồn tại, thêm mới
                BookEntity newBook = new BookEntity(
                    isbn,
                    tfTitle.getText().trim(),
                    tfAuthor.getText().trim(),
                    tfPublisher.getText().trim(),
                    tfCategory.getText().trim(),
                    Integer.parseInt(tfPublicationYear.getText().trim()),
                    Integer.parseInt(tfQuantity.getText().trim())
                );
    
                boolean result = bookService.addBook(newBook);
    
                if (result) {
                    lblStatus.setText("Thêm sách mới thành công.");
                    lblStatus.setStyle("-fx-text-fill: green;");
                    resetForm();
                } else {
                    lblStatus.setText("Thêm sách mới thất bại.");
                    lblStatus.setStyle("-fx-text-fill: red;");
                }
            }
    
        } catch (NumberFormatException e) {
            lblStatus.setText("Vui lòng nhập dữ liệu hợp lệ.");
            lblStatus.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            lblStatus.setText("Lỗi khi thêm sách.");
            lblStatus.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }
    
    private void enableForm() {
        tfTitle.setDisable(false);
        tfAuthor.setDisable(false);
        tfPublisher.setDisable(false);
        tfCategory.setDisable(false);
        tfPublicationYear.setDisable(false);
        tfQuantity.setDisable(false);
        tfAdditionalQuantity.setDisable(true);
    }
    

    @FXML
    public void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manageLibrary.fxml"));
            Pane root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quản lý sách");
        } catch (IOException e) {
            showError("Không thể quay lại trang quản lý sách.");
            e.printStackTrace();
        }
    }

    private void resetForm() {
        tfTitle.clear();
        tfAuthor.clear();
        tfPublisher.clear();
        tfCategory.clear();
        tfPublicationYear.clear();
        tfQuantity.clear();
        tfAdditionalQuantity.clear();
        tfTitle.setDisable(true);
        tfAuthor.setDisable(true);
        tfPublisher.setDisable(true);
        tfCategory.setDisable(true);
        tfPublicationYear.setDisable(true);
        tfQuantity.setDisable(true);
        tfAdditionalQuantity.setDisable(true);
        lblStatus.setText("");
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: red;");
    }

}
