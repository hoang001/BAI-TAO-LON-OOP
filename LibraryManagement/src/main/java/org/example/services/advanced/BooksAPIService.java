package org.example.services.advanced;

// GGBooksAPI Library
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;

public abstract class BooksAPIService {
    private static final String APPLICATION_NAME = "Library_Management";
    private static final String API_KEY = "AIzaSyDGjxnNso58E9KMG4dW-zjiHp2KmUbtSaw";


    /**
     * Lấy Books của GGAPI (như một dạng interface để tương tác với database của GGBooks API)
     * 
     * @return Books
     */
    public static Books getBooksService() {
        return new Books.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                .build();
    }
}
