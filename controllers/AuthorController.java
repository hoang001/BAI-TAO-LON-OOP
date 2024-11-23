package org.example.controllers;

import org.example.models.AuthorEntity;
import org.example.services.basics.AuthorService;

import java.util.List;

// Lớp điều khiển để xử lý các yêu cầu từ người dùng liên quan đến Author
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Gọi dịch vụ để thêm tác giả
    public boolean addAuthor(AuthorEntity author) {
        return authorService.addAuthor(author);
    }

    // Gọi dịch vụ để xóa tác giả theo ID
    public boolean deleteAuthorById(int authorId) {
        return authorService.deleteAuthorById(authorId);
    }

    // Gọi dịch vụ để tìm tác giả dựa trên ID
    public AuthorEntity findAuthorById(int authorId) {
        return authorService.findAuthorById(authorId);
    }

    // Gọi dịch vụ để tìm tác giả dựa trên tên
    public AuthorEntity findAuthorByName(String authorName) {
        return authorService.findAuthorByName(authorName);
    }

    // Gọi dịch vụ để lấy danh sách tất cả các tác giả
    public List<AuthorEntity> getAllAuthors() {
        return authorService.getAllAuthors();
    }
}