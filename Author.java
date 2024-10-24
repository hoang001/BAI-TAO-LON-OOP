import java.time.LocalDateTime;

public class Author {
    private String name;
    private String nationality;
    private LocalDateTime dateOfBirth;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, String nationality, LocalDateTime dateOfBirth) {
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return null;
    }
}
