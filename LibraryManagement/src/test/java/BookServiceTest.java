import org.example.models.BookEntity;
import org.example.services.basics.BookService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private final BookService bookService = new BookService();


    /** 
     * Test both the addBook method due to addBook(BookEntity)
     * ís included in addBook(String).
     */
    @Test
    void testAddBook() {
        // Test adding a book using the addBook(String ISBN) method
        String isbn = "9781618130327"; // Example ISBN 
        assertTrue(bookService.addBook(isbn));

        // Clean up: Delete the added book
        bookService.deleteBookByIsbn(isbn); 
    }
    
}