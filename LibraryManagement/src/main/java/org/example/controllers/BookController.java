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

    // Gọi dịch vụ để xóa sách dựa trên ISBN
    public void deleteBookByISBN(String isbn) {
        bookService.deleteBookByISBN(isbn);
    }

    public void deleteBookByID(int bookId) {
        bookService.deleteBookByID(bookId);
    }

    public void updateBook(BookEntity BookEntity) {
        bookService.updateBook(BookEntity);
    }

    public BookEntity findBookEntityByID(int bookId) {
        return bookService.findBookEntityById(bookId);
    }

    // Gọi dịch vụ để tìm sách dựa trên ISBN
    public List<BookEntity> findBookByISBN(String isbn) {
        return bookService.findBookByISBN(isbn);
    }

    // Gọi dịch vụ để tìm sách theo tiêu đề
    public List<BookEntity> findBooksByTitle(String title) {
        return bookService.findBooksByTitle(title);
    }

    // Gọi dịch vụ để tìm sách theo tác giả
    public List<BookEntity> findBooksByAuthor(String authorName) {
        return bookService.findBooksByAuthor(authorName);
    }

    // Gọi dịch vụ để tìm sách theo thể loại
    public List<BookEntity> findBooksByGenre(String genre) {
        return bookService.findBooksByGenre(genre);
    }

    // Gọi dịch vụ để tìm sách theo nhà xuất bản
    public List<BookEntity> findBooksByYPublisher(String publisherName) {
        return bookService.findBooksByPublisher(publisherName);
    }

    // Gọi dịch vụ để lấy danh sách tất cả sách
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    }
}