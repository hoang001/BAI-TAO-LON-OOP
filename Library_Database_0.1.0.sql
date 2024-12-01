DROP DATABASE IF EXISTS Library;
CREATE DATABASE Library;
USE Library;

CREATE TABLE Books (
    BookID INT AUTO_INCREMENT NOT NULL,
    ISBN VARCHAR(13) NOT NULL,
    Title VARCHAR(255) NOT NULL,
    AuthorName VARCHAR,
    PublisherName VARCHAR,
    PublishedDate DATE,
    Category VARCHAR,
    BookCoverDirectory VARCHAR(100),
    Available BOOLEAN NOT NULL,
    Quantity INT NOT NULL,
    PRIMARY KEY (BookID),
    UNIQUE (ISBN)
);

CREATE TABLE Users (
    UserID INT AUTO_INCREMENT NOT NULL,
    Username VARCHAR(20) NOT NULL,
    PasswordHash VARCHAR(64) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    PhoneNumber VARCHAR(20) NOT NULL,
    ProfileImageDirectory VARCHAR(100),
    Role ENUM('ADMIN', 'LIBRARIAN', 'USER') NOT NULL,
    PRIMARY KEY (UserID),
    UNIQUE (Email),
    UNIQUE (PhoneNumber),
    UNIQUE (Username)
);

CREATE TABLE BorrowedBooks (
    BorrowID INT AUTO_INCREMENT NOT NULL,
    Username VARCHAR(20) NOT NULL,
    BookID INT NOT NULL,
    BorrowDate DATE NOT NULL,
    ReturnDate DATE NOT NULL,
    PRIMARY KEY (BorrowID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (Username) REFERENCES Users(Username)
);

CREATE TABLE ReadBooks (
    ReadID INT AUTO_INCREMENT NOT NULL,
    Username VARCHAR(20) NOT NULL,
    BookID INT NOT NULL,
    PRIMARY KEY (ReadID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (Username) REFERENCES Users(Username)
);

CREATE TABLE Logs (
    LogID INT AUTO_INCREMENT NOT NULL,
    Timestamp DATETIME NOT NULL,
    Username VARCHAR(20) NOT NULL,
    ActionDetails TEXT,
    PRIMARY KEY (LogID),
    FOREIGN KEY (Username) REFERENCES Users(Username)
);

CREATE TABLE Reviews (
    ReviewID INT AUTO_INCREMENT NOT NULL,
    Username VARCHAR(20) NOT NULL,
    BookID INT NOT NULL,
    Rating INT NOT NULL,
    Comment TEXT,
    PRIMARY KEY (ReviewID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);