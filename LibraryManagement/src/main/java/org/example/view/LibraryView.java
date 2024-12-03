package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.models.BookEntity;
import org.example.services.basics.BookService;
import org.example.services.advanced.SearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryView {

    @FXML
    private TableView<BookEntity> tableBooks;

    @FXML
    private TableColumn<BookEntity, String> colIsbn;

    @FXML
    private TableColumn<BookEntity, String> colTitle;

    @FXML
    private TableColumn<BookEntity, String> colAuthor;

    @FXML
    private TableColumn<BookEntity, String> colCategory;

    @FXML
    private TableColumn<BookEntity, String> colPublisher;

    @FXML
    private TableColumn<BookEntity, Integer> colQuantity;

    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox<String> cbSearchCriteria;

    @FXML
    private Button btnSearch;

    @FXML
    private Label lblStatus;

    @FXML
    private ListView<String> suggestionsList;

    private final BookService bookService = new BookService();
    private final SearchService searchService = new SearchService();

    private ObservableList<BookEntity> bookList;

    @FXML
    public void initialize() {
        // Đặt các tiêu chí tìm kiếm
        ObservableList<String> searchCriteria = FXCollections.observableArrayList(
            "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản"
        );
        cbSearchCriteria.setItems(searchCriteria);
    
        // Cấu hình cột TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisherName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    
        // Xử lý khi nhấn vào một hàng trong bảng
        tableBooks.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nhấn đúp chuột
                BookEntity selectedBook = tableBooks.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    openBookView(selectedBook); // Chuyển đến trang chi tiết sách
                }
            }
        });
    
        // Tải danh sách sách
        loadAllBooks();
    }
    
    @FXML
    public void onSearchTextChanged() {
        String keyword = tfSearch.getText().trim();
        String criteria = cbSearchCriteria.getValue();
    
        if (keyword.isEmpty() || criteria == null) {
            suggestionsList.setVisible(false);
            return;
        }
    
        List<String> suggestions = new ArrayList<>();
        try {
            switch (criteria) {
                case "Tiêu đề" -> suggestions = bookService.getBooksByTitle(keyword)
                        .stream()
                        .map(BookEntity::getTitle)
                        .collect(Collectors.toList());
                case "Tác giả" -> suggestions = bookService.getBooksByAuthor(keyword)
                        .stream()
                        .map(BookEntity::getAuthorName)
                        .distinct()
                        .collect(Collectors.toList());
                case "Thể loại" -> suggestions = bookService.getBooksByGenre(keyword)
                        .stream()
                        .map(BookEntity::getCategoryName)
                        .distinct()
                        .collect(Collectors.toList());
                case "Nhà xuất bản" -> suggestions = bookService.getBooksByPublisher(keyword)
                        .stream()
                        .map(BookEntity::getPublisherName)
                        .distinct()
                        .collect(Collectors.toList());
            }
    
            if (!suggestions.isEmpty()) {
                suggestionsList.setItems(FXCollections.observableArrayList(suggestions));
                suggestionsList.setVisible(true);
            } else {
                suggestionsList.setVisible(false);
            }
        } catch (Exception e) {
            suggestionsList.setVisible(false);
            e.printStackTrace();
        }
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
    public void onSearchButtonClick() {
        String keyword = tfSearch.getText().trim();
        String criteria = cbSearchCriteria.getValue();
    
        // Ẩn danh sách gợi ý
        suggestionsList.setVisible(false);
        suggestionsList.setItems(FXCollections.observableArrayList());
    
        if (keyword.isEmpty() || criteria == null) {
            lblStatus.setText("Vui lòng nhập từ khóa và chọn tiêu chí tìm kiếm.");
            return;
        }
    
        try {
            List<BookEntity> results = new ArrayList<>();
    
            // Xác định tiêu chí tìm kiếm
            switch (criteria) {
                case "Tiêu đề":
                    results = bookService.getBooksByTitle(keyword);
                    break;
                case "Tác giả":
                    results = bookService.getBooksByAuthor(keyword);
                    break;
                case "Thể loại":
                    results = bookService.getBooksByGenre(keyword);
                    break;
                case "Nhà xuất bản":
                    results = bookService.getBooksByPublisher(keyword);
                    break;
                default:
                    lblStatus.setText("Tiêu chí không hợp lệ.");
                    return;
            }
    
            // Kiểm tra kết quả và cập nhật giao diện
            if (results.isEmpty()) {
                lblStatus.setText("Không tìm thấy kết quả nào.");
                bookList = FXCollections.observableArrayList(); // Làm rỗng bảng
            } else {
                bookList = FXCollections.observableArrayList(results); // Cập nhật danh sách sách
                lblStatus.setText("Đã tìm thấy " + results.size() + " kết quả.");
            }
    
            // Hiển thị danh sách lên bảng
            tableBooks.setItems(bookList);
    
        } catch (Exception e) {
            lblStatus.setText("Lỗi khi tìm kiếm.");
            e.printStackTrace();
        }
    }
    
    

    public void openBookView(BookEntity selectedBook) {
        try {
            // Tải FXML của BookView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/book.fxml"));
            Parent bookViewRoot = loader.load();

            // Lấy controller của BookView để truyền dữ liệu
            BookView bookViewController = loader.getController();
            bookViewController.setBookData(selectedBook); // Truyền thông tin sách vào controller

            // Tạo Stage mới và thiết lập Scene
            Stage stage = new Stage();
            stage.setTitle("Chi tiết sách");
            stage.setScene(new Scene(bookViewRoot));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            lblStatus.setText("Lỗi khi mở trang chi tiết sách.");
        }
    }

    

    @FXML
    public void onBackButtonClick() {
        try {
            // Tải file FXML của trang HomepageForUser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homepageforuser.fxml"));
            Pane root = loader.load();

            // Cài đặt scene và stage cho giao diện mới
            Stage stage = (Stage) btnSearch.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
