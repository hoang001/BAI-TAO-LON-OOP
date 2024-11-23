package org.example.models;
import java.time.LocalDate;

public class AuthorEntity extends PersonEntity {
    private LocalDate dateOfBirth;
    private String nationality;

    public AuthorEntity() {
    }

    public AuthorEntity(int authorId, String authorName, LocalDate dateOfBirth, String nationality) {
        super(authorId, authorName);
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
