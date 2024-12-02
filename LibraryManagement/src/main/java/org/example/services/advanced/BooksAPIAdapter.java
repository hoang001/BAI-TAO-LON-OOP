package org.example.services.advanced;

import java.io.IOException;

import org.example.models.BookEntity;

import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;



public class BooksAPIAdapter implements APIInterface {
    private final Books booksAccessing = BooksAPIService.getBooksService();

    @Override
    public BookEntity getBookEntity(String ISBN) {
        

        try {
            // 1. Make the API call to get book information
            Volumes volumes = booksAccessing.volumes().list("isbn:" + ISBN).execute();

            // 2. Check if any results were found
            if (volumes.getTotalItems() > 0) {
                // 3. Get the first volume
                Volume volume = volumes.getItems().get(0); 

                // 4. Extract relevant information and create a BookEntity
                BookEntity bookEntity = new BookEntity();

                bookEntity.setTitle(volume.getVolumeInfo().getTitle());
                bookEntity.setIsbn(ISBN);
                bookEntity.setAuthorName(volume.getVolumeInfo().getAuthors().get(0));
                bookEntity.setPublisherName(volume.getVolumeInfo().getPublisher());
                bookEntity.setPublishedDate(volume.getVolumeInfo().getPublishedDate());
                bookEntity.setCategory(volume.getVolumeInfo().getCategories().get(0));
                bookEntity.setBookCoverDirectory(volume.getVolumeInfo().getImageLinks().getThumbnail());
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
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getLink(String ISBN) {
        try {
            // 1. Make the API call to get book information
            Volume volume = booksAccessing.volumes().list("isbn:" + ISBN).execute().getItems().get(0);

            return volume.getVolumeInfo().getInfoLink();
        } catch (IOException e) {
            // Handle potential exceptions from the API call
            System.err.println("Error getting book link: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
