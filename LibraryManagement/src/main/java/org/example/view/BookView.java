package org.example.view;

import java.time.LocalDate;
import java.util.List;

import org.example.controllers.BorrowedBookController;
import org.example.controllers.ReviewController;
import org.example.models.BookEntity;
import org.example.models.ReviewEntity;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class BookView {

    @FXML
    private Label lblIsbn;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblAuthor;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblPublisher;

    @FXML
    private Label lblQuantity;

    @FXML
    private ListView<String> reviewList;

    @FXML
    private Button btnAddReview;

    @FXML
    private Button btnBorrowBook;

    @FXML
    private Button btnBack;

    @FXML
    private DatePicker datePickerReturnDate;
    
    private BookEntity currentBook;
    private ReviewController reviewController = new ReviewController();
    private BorrowedBookController borrowedBookController = new BorrowedBookController();

    public void setBookData(BookEntity book) {
        this.currentBook = book;

        lblIsbn.setText(book.getIsbn());
        lblTitle.setText(book.getTitle());
        lblAuthor.setText(book.getAuthorName());
        lblCategory.setText(book.getCategory());
        lblPublisher.setText(book.getPublisherName());
        lblQuantity.setText(String.valueOf(book.getQuantity()));

        // Hiển thị đánh giá của cuốn sách
        showReviews(book.getId());
    }

    private void showReviews(int bookId) {
        List<ReviewEntity> reviews = reviewController.getReviewsByBookId(bookId);
        reviewList.getItems().clear();
        for (ReviewEntity review : reviews) {
            reviewList.getItems().add(review.getComment());
        }
    }

    @FXML
    private void handleAddReview() {
        // Mở dialog cho người dùng nhập bình luận và điểm đánh giá
        TextInputDialog commentDialog = new TextInputDialog();
        commentDialog.setTitle("Đánh giá sách");
        commentDialog.setHeaderText("Nhập bình luận của bạn:");
        String comment = commentDialog.showAndWait().orElse("");
        
        if (comment.isEmpty()) {
            showAlert("Lỗi", "Bình luận không thể để trống.", Alert.AlertType.WARNING);
            return;
        }

        // Giả sử rating được lấy từ một control khác (ví dụ: một slider hoặc dialog)
        int rating = 5; // Giá trị mặc định hoặc lấy từ control.

        // Thêm đánh giá
        reviewController.addReview(currentBook.getId(), rating, comment);
        showAlert("Thành công", "Đánh giá đã được thêm thành công.", Alert.AlertType.INFORMATION);
        showReviews(currentBook.getId()); // Cập nhật lại danh sách đánh giá
    }



    @FXML
    private void onBorrowButtonClick() {
        if (currentBook == null) {
            showAlert("Lỗi", "Không có thông tin sách để mượn.", Alert.AlertType.ERROR);
            return;
        }
    
        // Lấy ngày trả từ DatePicker
        LocalDate returnDate = datePickerReturnDate.getValue();
    
        if (returnDate == null) {
            showAlert("Lỗi", "Vui lòng chọn ngày trả sách.", Alert.AlertType.WARNING);
            return;
        }
    
        LocalDate borrowDate = LocalDate.now();
    
        // Kiểm tra ngày trả có hợp lệ không (ngày trả phải sau ngày mượn)
        if (returnDate.isBefore(borrowDate) || returnDate.isEqual(borrowDate)) {
            showAlert("Lỗi", "Ngày trả phải sau ngày hôm nay.", Alert.AlertType.WARNING);
            return;
        }
    
        // Gửi yêu cầu mượn sách qua BorrowedBookController
        boolean result = borrowedBookController.borrowBook(currentBook.getId(), borrowDate, returnDate);
    
        if (result) {
            showAlert("Thành công", "Mượn sách thành công!", Alert.AlertType.INFORMATION);
            // Giảm số lượng sách hiển thị
            currentBook.setQuantity(currentBook.getQuantity() - 1);
            lblQuantity.setText(String.valueOf(currentBook.getQuantity()));
        } else {
            showAlert("Thất bại", "Không thể mượn sách. Hãy kiểm tra lại.", Alert.AlertType.ERROR);
        }
    }
    

    

    @FXML
    private void onBackButtonClick() {
        // Đóng cửa sổ hiện tại
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
