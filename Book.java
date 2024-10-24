public class Book {
    private String title;
    private int publicationYear;
    private String ISBN;
    private Category category;
    private Author author;
    private Publisher publisher;
    private String BookCoverDirectory;

    // Constructor, getters, and setters...
    public Book() {
        this.title = null;
        this.publicationYear = 0;
        this.ISBN = null;
        this.category = null;
        this.author = null;
        this.publisher = null;
    }

    public Book(String title, int publicationYear, String ISBN) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.ISBN = ISBN;
    }

    public Book(String title, int publicationYear, String ISBN, Category category) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.ISBN = ISBN;
        this.category = category;
    }

    public Book(String title, int publicationYear, String ISBN, Category category, Author author, Publisher publisher) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.ISBN = ISBN;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (this.category != null) {
            this.category.removeBook(this);
        }

        this.category = category;
        category.addBook(this);
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getBookCoverDirectory() {
        return BookCoverDirectory;
    }

    public void setBookCoverDirectory(String bookCoverDirectory) {
        this.BookCoverDirectory = bookCoverDirectory;
    }

    @Override
    public String toString() {
        return null;
    }
}