package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.services.basics.BookService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DeleteBook {

    @FXML
    private TextField tfIsbn;

    @FXML
    private TextField tfBookId;

    @FXML
    private Label lblStatus;

    private final BookService bookService = new BookService();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // Thread pool để xử lý bất đồng bộ

    @FXML
    public void onDeleteBookByIsbn() {
        String isbn = tfIsbn.getText().trim();

        if (isbn.isEmpty()) {
            lblStatus.setText("Vui lòng nhập ISBN.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        // Xử lý bất đồng bộ khi xóa sách
        executorService.submit(() -> {
            try {
                boolean result = bookService.deleteBookByIsbn(isbn);
                updateStatus(result, "Xóa sách thành công!", "Không thể xóa sách. ISBN không tồn tại.");
            } catch (Exception e) {
                updateStatus(false, "", "Đã xảy ra lỗi trong quá trình xóa sách.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void onDeleteBookById() {
        String bookIdText = tfBookId.getText().trim();

        if (bookIdText.isEmpty()) {
            lblStatus.setText("Vui lòng nhập ID sách.");
            lblStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);

            // Xử lý bất đồng bộ khi xóa sách theo ID
            executorService.submit(() -> {
                try {
                    boolean result = bookService.deleteBookById(bookId);
                    updateStatus(result, "Xóa sách thành công!", "Không thể xóa sách. ID không tồn tại.");
                } catch (Exception e) {
                    updateStatus(false, "", "Đã xảy ra lỗi trong quá trình xóa sách.");
                    e.printStackTrace();
                }
            });
        } catch (NumberFormatException e) {
            lblStatus.setText("ID sách phải là số.");
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Cập nhật trạng thái của giao diện (trên JavaFX Application Thread).
     *
     * @param success      Kết quả thành công hay thất bại.
     * @param successMsg   Thông báo khi thành công.
     * @param failureMsg   Thông báo khi thất bại.
     */
    private void updateStatus(boolean success, String successMsg, String failureMsg) {
        javafx.application.Platform.runLater(() -> {
            if (success) {
                lblStatus.setText(successMsg);
                lblStatus.setStyle("-fx-text-fill: green;");
                tfIsbn.clear();
                tfBookId.clear();
            } else {
                lblStatus.setText(failureMsg);
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });
    }

    /**
     * Đóng ExecutorService khi không sử dụng nữa.
     */
    public void shutdownExecutor() {
        executorService.shutdown();
    }
}
