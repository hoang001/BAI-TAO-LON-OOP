package org.example.daos.implementations;

import org.example.daos.interfaces.PublisherDAO;
import org.example.models.PublisherEntity;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAOImpl implements PublisherDAO {
    private final Connection connection;

    // Constructor để khởi tạo kết nối cơ sở dữ liệu
    public PublisherDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean addPublisher(PublisherEntity publisher) throws SQLException {
        String sql = "INSERT INTO Publishers (publisherName, address, contactEmail, contactPhone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, publisher.getPublisherName());
            preparedStatement.setString(2, publisher.getAddress());
            preparedStatement.setString(3, publisher.getContactEmail());
            preparedStatement.setString(4, publisher.getContactPhone());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public boolean deletePublisherById(int publisherId) throws SQLException {
        String sql = "DELETE FROM Publishers WHERE publisherId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, publisherId);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public PublisherEntity findPublisherById(int publisherId) throws SQLException {
        String sql = "SELECT * FROM Publishers WHERE publisherId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, publisherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String publisherName = resultSet.getString("publisherName");
                String address = resultSet.getString("address");
                String contactEmail = resultSet.getString("contactEmail");
                String contactPhone = resultSet.getString("contactPhone");
                return new PublisherEntity(publisherId, publisherName, address, contactEmail, contactPhone);
            }
        }
        return null;
    }

    @Override
    public PublisherEntity findPublisherByName(String publisherName) throws SQLException {
        String sql = "SELECT * FROM Publishers WHERE publisherName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, publisherName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int publisherId = resultSet.getInt("publisherId");
                String address = resultSet.getString("address");
                String contactEmail = resultSet.getString("contactEmail");
                String contactPhone = resultSet.getString("contactPhone");
                return new PublisherEntity(publisherId, publisherName, address, contactEmail, contactPhone);
            }
        }
        return null;
    }

    @Override
    public List<PublisherEntity> getAllPublishers() throws SQLException {
        List<PublisherEntity> publishers = new ArrayList<>();
        String sql = "SELECT * FROM Publishers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int publisherId = resultSet.getInt("publisherId");
                String publisherName = resultSet.getString("publisherName");
                String address = resultSet.getString("address");
                String contactEmail = resultSet.getString("contactEmail");
                String contactPhone = resultSet.getString("contactPhone");
                publishers.add(new PublisherEntity(publisherId, publisherName, address, contactEmail, contactPhone));
            }
        }
        return publishers;
    }
}
