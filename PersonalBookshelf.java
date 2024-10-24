import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class PersonalBookshelf {
    private String username;
    private final HashMap<Book, LocalDateTime> borrowedBooks = new HashMap<>();
    private final ArrayList<Book> readBooks = new ArrayList<>();

    public PersonalBookshelf() {
    }

    public PersonalBookshelf(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void borrowBook(Book book) {
        borrowedBooks.put(book, LocalDateTime.now());        
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
        readBooks.add(book);
    }
}
