package org.example.services.advanced;

import org.example.models.BookEntity;

// Target Adapter để chương trình tương tác với BooksAPI Service
public interface APIInterface {
    // BooksAPIAdapter
    public BookEntity getBookEntity(String ISBN);

    // BooksAPIAdapter
    public String getLink(String ISBN);
}
