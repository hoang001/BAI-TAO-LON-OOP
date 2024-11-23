package org.example.daos.interfaces;

import org.example.models.PublisherEntity;

import java.sql.SQLException;
import java.util.List;

// Giao diện cho các thao tác CRUD đối với Publisher
public interface PublisherDAO {

    // Thêm nhà xuất bản
    boolean addPublisher(PublisherEntity publisher) throws SQLException;

    // Xóa nhà xuất bản theo ID
    boolean deletePublisherById(int publisherId) throws SQLException;

    // Tìm nhà xuất bản dựa trên ID
    PublisherEntity findPublisherById(int publisherId) throws SQLException;

    PublisherEntity findPublisherByName(String publisherName) throws SQLException;

    // Lấy danh sách tất cả các nhà xuất bản
    List<PublisherEntity> getAllPublishers() throws SQLException;
}
