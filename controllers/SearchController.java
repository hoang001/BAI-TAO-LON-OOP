package org.example.controllers;

import org.example.services.advanced.SearchService;
import java.util.List;

// Lớp điều khiển để xử lý các yêu cầu tìm kiếm liên quan đến sách, tác giả, thể loại, nhà xuất bản
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // Tìm kiếm tác giả theo từ khóa và trả về danh sách tác giả
    public List<String> searchAuthorsByKeyword(String keyword) {
        try {
            return searchService.searchAuthorsByKeyword(keyword); // Trả về danh sách tác giả tìm được
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu có lỗi, trả về null
        }
    }

    // Tìm kiếm thể loại sách theo từ khóa và trả về danh sách thể loại
    public List<String> searchCategoriesByKeyword(String keyword) {
        try {
            return searchService.searchCategoriesByKeyword(keyword); // Trả về danh sách thể loại tìm được
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu có lỗi, trả về null
        }
    }

    // Tìm kiếm nhà xuất bản theo từ khóa và trả về danh sách nhà xuất bản
    public List<String> searchPublishersByKeyword(String keyword) {
        try {
            return searchService.searchPublishersByKeyword(keyword); // Trả về danh sách nhà xuất bản tìm được
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu có lỗi, trả về null
        }
    }
}
