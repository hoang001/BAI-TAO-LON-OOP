package org.example.daos.implementations;

import org.example.daos.interfaces.CategoryDAO;
import org.example.models.CategoryEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public CategoryDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean addCategory(CategoryEntity category) throws SQLException {
        String sql = "INSERT INTO Categories (categoryName, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(0, category.getDescription());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean deleteCategoryById(int categoryId) throws SQLException {
        String sql = "DELETE FROM Categories WHERE categoryId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public CategoryEntity findCategoryById(int categoryId) throws SQLException {
        String query = "SELECT * FROM Categories WHERE categoryId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new CategoryEntity(
                        resultSet.getInt("categoryId"),
                        resultSet.getString("categoryName"),
                        resultSet.getString("description")
                );
            }
            return null;
        }
    }

    @Override
    public CategoryEntity findCategoryByName(String categoryName) throws SQLException {
        String query = "SELECT * FROM Categories WHERE categoryName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new CategoryEntity(
                        resultSet.getInt("categoryId"),
                        resultSet.getString("categoryName"),
                        resultSet.getString("description")
                );
            }
            return null;
        }
    }

    @Override// có thể đa luồng
    public List<CategoryEntity> getAllCategories() throws SQLException {
        String query = "SELECT * FROM Categories";
        List<CategoryEntity> categories = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(new CategoryEntity(
                        resultSet.getInt("categoryId"),
                        resultSet.getString("categoryName"),
                        resultSet.getString("description")
                ));
            }
        }
        return categories;
    }
}
