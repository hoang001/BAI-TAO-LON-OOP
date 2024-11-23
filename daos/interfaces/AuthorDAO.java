package org.example.daos.interfaces;

import org.example.models.AuthorEntity;

import java.sql.SQLException;
import java.util.List;

// Giao diện cho các thao tác CRUD đối với Author
public interface AuthorDAO {
    // Thêm tác giả mới
    boolean addAuthor(AuthorEntity author) throws SQLException;

    // Xóa tác giả dựa trên ID
    boolean deleteAuthorById(int authorId) throws SQLException;

    // Tìm tác giả dựa trên ID
    AuthorEntity findAuthorById(int authorId) throws SQLException;

    // Tìm tác giả dựa trên name
    AuthorEntity findAuthorByName(String authorName) throws SQLException;

    // Lấy danh sách tất cả tác giả
    List<AuthorEntity> getAllAuthors() throws SQLException;
}