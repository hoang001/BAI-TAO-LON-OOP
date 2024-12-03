package org.example.services.advanced;

import java.io.IOException;

import org.example.models.BookEntity;

import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.google.api.services.books.model.Volumes;



public class BooksAPIAdapter implements APIInterface {
    private final Books booksAccessing = BooksAPIService.getBooksService();

    @Override
    public BookEntity getBookEntity(String ISBN) {
        try {
            // 1. Make the API call to get book information
            Volumes volumes = booksAccessing.volumes().list("isbn:" + ISBN).execute();
            if (volumes == null || volumes.getItems() == null || volumes.getItems().isEmpty()) {
                System.err.println("No book found.");
                return null;
            }

            // 2. Check if any results were found
            if (volumes.getTotalItems() > 0) {
                // 3. Get the first volume
                Volume volume = volumes.getItems().get(0);
                VolumeInfo info = volume.getVolumeInfo();

                // 4. Extract relevant information and create a BookEntity
                BookEntity bookEntity = new BookEntity();

                bookEntity.setTitle(info.getTitle());
                bookEntity.setIsbn(ISBN);
                try {
                    bookEntity.setAuthorName(info.getAuthors().get(0));
                } catch (Exception e) {
                    bookEntity.setAuthorName("Unknown Author");
                }
                
                String publisher = info.getPublisher();
                if (publisher == null || publisher.isEmpty()) {
                    bookEntity.setPublisherName("Unknown Publisher");
                } else {
                    bookEntity.setPublisherName(info.getPublisher());
                }
                
                String publishedDate = info.getPublishedDate();
                if (publishedDate == null) {
                    bookEntity.setPublishedDate("Unknown Published Date");
                } else {
                    bookEntity.setPublishedDate(info.getPublishedDate());
                }

                try {
                    bookEntity.setCategory(info.getCategories().get(0));
                } catch (Exception e) {
                    bookEntity.setCategory("Unknown Category");
                }
                
                try {
                    bookEntity.setBookCoverDirectory(info.getImageLinks().getThumbnail());
                } catch (Exception e) {
                    bookEntity.setBookCoverDirectory(
                        "https://i.pinimg.com/originals/49/e5/8d/49e58d5922019b8ec4642a2e2b9291c2.png");
                }
                
                bookEntity.setAvailable(true);
                bookEntity.setQuantity(1);
                
                return bookEntity;
            } else {
                // Handle case where no book was found
                return null; 
            }
        } catch (IOException e) {
            // Handle potential exceptions from the API call
            System.err.println("Error fetching book data: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getLink(String ISBN) {
        try {
            // 1. Make the API call to get book information
            Volume volume = booksAccessing.volumes().list("isbn:" + ISBN).execute().getItems().get(0);
            VolumeInfo info = volume.getVolumeInfo();

            return info.getInfoLink();
        } catch (IOException e) {
            // Handle potential exceptions from the API call
            System.err.println("Error getting book link: " + e.getMessage());
            return null;
        }
    }
}
