package org.example.controllers;

import org.example.models.PublisherEntity;
import org.example.services.basics.PublisherService;

import java.util.List;

// Lớp điều khiển để xử lý các yêu cầu từ người dùng liên quan đến Publisher
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    // Gọi dịch vụ để thêm nhà xuất bản
    public boolean addPublisher(PublisherEntity publisher) {
        return publisherService.addPublisher(publisher);
    }

    // Gọi dịch vụ để xóa nhà xuất bản theo ID
    public boolean deletePublisherById(int publisherId) {
        return publisherService.deletePublisherById(publisherId);
    }

    // Gọi dịch vụ để tìm nhà xuất bản dựa trên ID
    public PublisherEntity findPublisherById(int publisherId) {
        return publisherService.findPublisherById(publisherId);
    }

    // Gọi dịch vụ để tìm nhà xuất bản dựa trên tên
    public PublisherEntity findPublisherByName(String publisherName) {
        return publisherService.findPublisherByName(publisherName);
    }

    // Gọi dịch vụ để lấy danh sách tất cả các nhà xuất bản
    public List<PublisherEntity> getAllPublishers() {
        return publisherService.getAllPublishers();
    }
}
