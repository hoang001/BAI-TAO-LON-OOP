package org.example.daos.interfaces;

import org.example.models.CategoryEntity;
import java.sql.SQLException;
import java.util.List;

// Giao diện cho các thao tác CRUD đối với Category
public interface CategoryDAO {

    // Thêm thể loại mới
    boolean addCategory(CategoryEntity category) throws SQLException;

    // Xóa thể loại theo ID
    boolean deleteCategoryById(int categoryId) throws SQLException;

    // Lấy thông tin thể loại dựa trên ID
    CategoryEntity findCategoryById(int categoryId) throws SQLException;

    // Lấy thông tin thể loại dựa trên name
    CategoryEntity findCategoryByName(String categoryName) throws SQLException;

    // Lấy tất cả các thể loại
    List<CategoryEntity> getAllCategories() throws SQLException;
}