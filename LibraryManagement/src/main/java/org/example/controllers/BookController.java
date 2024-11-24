package org.example.controllers;

import org.example.models.BookEntity;
import org.example.services.basics.BookService;

import java.util.List;

// Lớp điều khiển để xử lý các yêu cầu từ người dùng liên quan đến Book
public class BookController {
    private final BookService bookService;

    // Khởi tạo BookController với BookService
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Gọi dịch vụ để thêm sách mới
    public void addBook(BookEntity book) {
        bookService.addBook(book);
    }

    // Gọi dịch vụ để xóa sách dựa trên Isbn
    public void deleteBookByIsbn(String Isbn) {
        bookService.deleteBookByIsbn(Isbn);
    }

    public void deleteBookByID(int bookId) {
        bookService.deleteBookById(bookId);
    }

    public void updateBook(BookEntity BookEntity) {
        bookService.updateBook(BookEntity);
    }

    public BookEntity getBookByID(int bookId) {
        return bookService.getBookById(bookId);
    }

    // Gọi dịch vụ để tìm sách dựa trên Isbn
    public BookEntity getBookByIsbn(String Isbn) {
        return bookService.getBookByIsbn(Isbn);
    }

    // Gọi dịch vụ để tìm sách theo tiêu đề
    public List<BookEntity> getBooksByTitle(String title) {
        return bookService.getBooksByTitle(title);
    }

    // Gọi dịch vụ để tìm sách theo tác giả
    public List<BookEntity> getBooksByAuthor(String authorName) {
        return bookService.getBooksByAuthor(authorName);
    }

    // Gọi dịch vụ để tìm sách theo thể loại
    public List<BookEntity> getBooksByGenre(String genre) {
        return bookService.getBooksByGenre(genre);
    }

    // Gọi dịch vụ để tìm sách theo nhà xuất bản
    public List<BookEntity> getBooksByYPublisher(String publisherName) {
        return bookService.getBooksByPublisher(publisherName);
    }

    // Gọi dịch vụ để lấy danh sách tất cả sách
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    }
}
