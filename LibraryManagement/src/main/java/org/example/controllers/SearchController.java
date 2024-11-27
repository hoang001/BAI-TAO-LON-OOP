package org.example.controllers;

import org.example.services.advanced.SearchService;
import java.util.List;

/**
 * Lớp điều khiển (Controller) chịu trách nhiệm xử lý các yêu cầu tìm kiếm liên quan đến sách,
 * tác giả, thể loại, nhà xuất bản. Lớp này tương tác với SearchService để thực hiện các tìm kiếm
 * và trả về kết quả dưới dạng danh sách.
 */
public class SearchController {
    
    // Đối tượng SearchService được sử dụng để thực hiện các chức năng tìm kiếm.
    private final SearchService searchService;

    /**
     * Constructor khởi tạo đối tượng SearchService.
     * Phương thức này được gọi khi tạo đối tượng SearchController để quản lý các thao tác tìm kiếm.
     */
    public SearchController() {
        this.searchService = new SearchService();  // Khởi tạo SearchService để xử lý các yêu cầu tìm kiếm.
    }

    /**
     * Tìm kiếm tác giả theo từ khóa và trả về danh sách các tác giả phù hợp.
     * 
     * @param keyword Từ khóa tìm kiếm mà người dùng nhập vào.
     * @return List<String> Danh sách các tác giả tìm thấy, hoặc danh sách trống nếu không tìm thấy tác giả nào.
     */
    public List<String> searchAuthorsByKeyword(String keyword) {
        return searchService.searchAuthorsByKeyword(keyword);  // Gọi phương thức của SearchService để tìm kiếm tác giả.
    }

    /**
     * Tìm kiếm thể loại sách theo từ khóa và trả về danh sách các thể loại sách phù hợp.
     * 
     * @param keyword Từ khóa tìm kiếm mà người dùng nhập vào.
     * @return List<String> Danh sách các thể loại sách tìm thấy, hoặc danh sách trống nếu không tìm thấy thể loại nào.
     */
    public List<String> searchCategoriesByKeyword(String keyword) {
        return searchService.searchCategoriesByKeyword(keyword);  // Gọi phương thức của SearchService để tìm kiếm thể loại sách.
    }

    /**
     * Tìm kiếm nhà xuất bản theo từ khóa và trả về danh sách các nhà xuất bản phù hợp.
     * 
     * @param keyword Từ khóa tìm kiếm mà người dùng nhập vào.
     * @return List<String> Danh sách các nhà xuất bản tìm thấy, hoặc danh sách trống nếu không tìm thấy nhà xuất bản nào.
     */
    public List<String> searchPublishersByKeyword(String keyword) {
        return searchService.searchPublishersByKeyword(keyword);  // Gọi phương thức của SearchService để tìm kiếm nhà xuất bản.
    }

    public List<String> searchTitleByKeyword(String keyword) {
        return searchService.searchTitlesByKeyword(keyword);
    }
}
