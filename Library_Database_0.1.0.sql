DROP DATABASE IF EXISTS Library;
CREATE DATABASE Library;
USE Library;

CREATE TABLE Authors (
    AuthorID INT AUTO_INCREMENT NOT NULL,
    AuthorName VARCHAR(50) NOT NULL,
    DateOfBirth DATETIME,
    Nationality VARCHAR(50),
    PRIMARY KEY (AuthorID)
);

CREATE TABLE Publishers (
    PublisherID INT AUTO_INCREMENT NOT NULL,
    Name VARCHAR(100),
    Address VARCHAR(200),
    ContactEmail VARCHAR(50),
    ContactPhone VARCHAR(20),
    PRIMARY KEY (PublisherID),
    UNIQUE (Name, Address, ContactEmail, ContactPhone)
);

CREATE TABLE Categories (
    CategoryID INT AUTO_INCREMENT NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Description TEXT NOT NULL,
    PRIMARY KEY (CategoryID),
    UNIQUE (Category)
);

CREATE TABLE Books (
    BookID INT AUTO_INCREMENT NOT NULL,
    ISBN VARCHAR(13) NOT NULL,
    Title VARCHAR(255) NOT NULL,
    AuthorID INT NOT NULL,
    PublisherID INT NOT NULL,
    PublicationYear INT NOT NULL,
    CategoryID INT NOT NULL,
    BookCoverDirectory VARCHAR(100),
    PRIMARY KEY (BookID),
    UNIQUE (ISBN),
    FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID),
    FOREIGN KEY (PublisherID) REFERENCES Publishers(PublisherID),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
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
    PRIMARY KEY (UserID),
    UNIQUE (Email),
    UNIQUE (PhoneNumber),
    UNIQUE (Username)
);

CREATE TABLE BorrowedBooks (
    BorrowID INT AUTO_INCREMENT NOT NULL,
    BookID INT NOT NULL,
    BorrowDate DATETIME NOT NULL,
    ReturnDate DATETIME NOT NULL,
    PRIMARY KEY (BorrowID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);

CREATE TABLE ReadBooks (
    ReadID INT AUTO_INCREMENT NOT NULL,
    BookID INT NOT NULL,
    PRIMARY KEY (ReadID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);

CREATE TABLE PersonalBookshelf (
    PersonalBookshelfID INT AUTO_INCREMENT NOT NULL,
    UserID INT NOT NULL,
    BorrowID INT NOT NULL,
    ReadID INT NOT NULL,
    PRIMARY KEY (PersonalBookshelfID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BorrowID) REFERENCES BorrowedBooks(BorrowID),
    FOREIGN KEY (ReadID) REFERENCES ReadBooks(ReadID)
);