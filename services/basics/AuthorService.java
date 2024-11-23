package org.example.services.basics;

import org.example.daos.interfaces.AuthorDAO;
import org.example.models.AuthorEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.util.List;

public class AuthorService {

    private AuthorDAO authorDAO;
    private UserService userService;
    private LogService logService;

    public AuthorService(AuthorDAO authorDAO, UserService userService, LogService logService) {
        this.authorDAO = authorDAO;
        this.userService = userService;
        this.logService = logService;
    }

    // Kiểm tra xem người dùng có đăng nhập không
    private boolean isUserLoggedIn() {
        if (userService.getLoginUser() == null) {
            System.out.println("Bạn cần đăng nhập để thực hiện hành động này.");
            return false;
        }
        return true;
    }

    // Thêm tác giả mới
    public boolean addAuthor(AuthorEntity author) {
        try {
            // Kiểm tra đăng nhập
            if (!isUserLoggedIn()) {
                return false;
            }

            // Kiểm tra điều kiện AuthorName không được rỗng và hợp lệ
            if (author.getName() == null || author.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên tác giả không được để trống.");
            }
            // Kiểm tra nếu Nationality có dữ liệu hợp lệ
            if (author.getNationality() == null || author.getNationality().trim().isEmpty()) {
                throw new IllegalArgumentException("Quốc tịch tác giả không được để trống.");
            }

            // Kiểm tra ngày sinh hợp lệ
            if (author.getDateOfBirth() != null) {
                throw new IllegalArgumentException("Ngày sinh không thể là ngày trong tương lai.");
            }

            // Thêm tác giả vào cơ sở dữ liệu
            boolean result = authorDAO.addAuthor(author);

            // Nếu thành công, thêm log
            if (result) {
                logService.addLog(new LogEntity(java.time.LocalDateTime.now(), userService.getLoginUser().getName(),
                        "Thêm tác giả: " + author.getName()));
            }

            return result;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tác giả: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        }
    }

    // Xóa tác giả theo ID
    public boolean deleteAuthorById(int authorId) {
        try {
            // Kiểm tra đăng nhập
            if (!isUserLoggedIn()) {
                return false;
            }

            if (authorId <= 0) {
                throw new IllegalArgumentException("ID tác giả không hợp lệ.");
            }

            // Xóa tác giả khỏi cơ sở dữ liệu
            boolean result = authorDAO.deleteAuthorById(authorId);

            // Nếu thành công, thêm log
            if (result) {
                logService.addLog(new LogEntity(java.time.LocalDateTime.now(), userService.getLoginUser().getName(),
                        "Xóa tác giả ID: " + authorId));
            }

            return result;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa tác giả: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return false;
        }
    }

    // Tìm tác giả theo ID
    public AuthorEntity findAuthorById(int authorId) {
        try {
            // Kiểm tra đăng nhập
            if (!isUserLoggedIn()) {
                return null;
            }

            if (authorId <= 0) {
                throw new IllegalArgumentException("ID tác giả không hợp lệ.");
            }

            // Tìm tác giả theo ID
            AuthorEntity author = authorDAO.findAuthorById(authorId);

            // Thêm log khi tìm tác giả
            logService.addLog(new LogEntity(java.time.LocalDateTime.now(), userService.getLoginUser().getName(),
                    "Xem thông tin tác giả ID: " + authorId));

            return author;
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm tác giả theo ID: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return null;
        }
    }

    // Tìm tác giả theo tên
    public AuthorEntity findAuthorByName(String authorName) {
        try {
            // Kiểm tra đăng nhập
            if (!isUserLoggedIn()) {
                return null;
            }

            if (authorName == null || authorName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên tác giả không thể để trống.");
            }

            // Tìm tác giả theo tên
            AuthorEntity author = authorDAO.findAuthorByName(authorName);

            // Thêm log khi tìm tác giả
            logService.addLog(new LogEntity(java.time.LocalDateTime.now(), userService.getLoginUser().getName(),
                    "Xem thông tin tác giả tên: " + authorName));

            return author;
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm tác giả theo tên: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi dữ liệu đầu vào: " + e.getMessage());
            return null;
        }
    }

    // Lấy tất cả tác giả
    public List<AuthorEntity> getAllAuthors() {
        try {
            // Kiểm tra đăng nhập
            if (!isUserLoggedIn()) {
                return null;
            }

            // Lấy tất cả tác giả
            List<AuthorEntity> authors = authorDAO.getAllAuthors();

            // Thêm log khi lấy danh sách tác giả
            logService.addLog(new LogEntity(java.time.LocalDateTime.now(), userService.getLoginUser().getName(),
                    "Lấy danh sách tất cả các tác giả"));

            return authors;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách tác giả: " + e.getMessage());
            return null;
        }
    }
}
