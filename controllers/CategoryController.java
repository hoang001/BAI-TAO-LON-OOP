package org.example.controllers;

import org.example.models.CategoryEntity;
import org.example.services.basics.CategoryService;

import java.util.List;

// Lớp điều khiển để xử lý các yêu cầu từ người dùng liên quan đến Category
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Gọi dịch vụ để thêm thể loại
    public boolean addCategory(CategoryEntity category) {
        return categoryService.addCategory(category);
    }

    // Gọi dịch vụ để xóa thể loại theo ID
    public boolean deleteCategoryById(int categoryId) {
        return categoryService.deleteCategoryById(categoryId);
    }

    // Gọi dịch vụ để lấy thông tin thể loại dựa trên ID
    public CategoryEntity getCategoryById(int categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    // Gọi dịch vụ để lấy thông tin thể loại dựa trên name
    public CategoryEntity getCategoryByName(String categoryName) {
        return categoryService.findCategoryByName(categoryName);
    }

    // Gọi dịch vụ để lấy tất cả các thể loại
    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
