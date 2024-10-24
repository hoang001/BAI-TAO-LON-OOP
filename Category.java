import java.util.HashMap;

public class Category {
    private String name;
    private String description;
    private final HashMap<Book, Category> BooksOfCategory = new HashMap<>();


    // ------------------------------------------------------------------------------------------------
    // Constructors are private because every Category should be initialized beforehand.
    // ------------------------------------------------------------------------------------------------


    private Category() {
        this.name = null;
        this.description = null;
    }

    private Category(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addBook(Book book) {
        BooksOfCategory.put(book, this);
    }

    public void removeBook(Book book) {
        BooksOfCategory.remove(book);
    }

    public int getCountBooksOfCategory() {
        return BooksOfCategory.size();
    }

    @Override
    public String toString() {
        return null;
    }
}
