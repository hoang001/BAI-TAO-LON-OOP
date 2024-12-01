package org.example.services.advanced;

import org.example.models.BookEntity;

public interface APIInterface {
    // BooksAPIAdapter
    public BookEntity getBookEntity(String ISBN);
}
