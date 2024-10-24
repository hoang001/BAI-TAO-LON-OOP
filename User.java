public class User {
    private String username;
    private String passwordHash;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageDirectory;
    private final PersonalBookshelf personalBookshelf = new PersonalBookshelf();

    public User() {
    }

    public User(String username, String passwordHash, String email, String firstName, String lastName, String phoneNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageDirectory() {
        return profileImageDirectory;
    }

    public void setProfileImageDirectory(String profileImageDirectory) {
        this.profileImageDirectory = profileImageDirectory;
    }

    public PersonalBookshelf getPersonalBookshelf() {
        return personalBookshelf;
    }

    public void borrowBook(Book book) {
        this.personalBookshelf.borrowBook(book);
    }

    public void returnBook(Book book) {
        this.personalBookshelf.returnBook(book);
    }
}
