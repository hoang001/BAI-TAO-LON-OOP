import org.example.services.basics.BookService;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BookServiceTest {

    private final BookService bookService = new BookService();

    @Test
    void testAddBook() {
        // Test adding a book using the addBook(String ISBN) method
        String isbn = "9781618130327"; // Example ISBN 
        assertTrue(bookService.addBook(isbn));

        // Clean up: Delete the added book
        bookService.deleteBookByIsbn(isbn); 
    }


}