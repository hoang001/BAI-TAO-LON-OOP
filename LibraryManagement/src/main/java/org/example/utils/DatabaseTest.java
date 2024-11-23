package org.example.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        // Gọi phương thức getConnection từ lớp DatabaseConnection
        Connection connection = DatabaseConnection.getConnection();

        // Kiểm tra xem kết nối có khác null không
        if (connection != null) {
            System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
            try {
                connection.close(); // Đóng kết nối nếu nó thành công
            } catch (SQLException e) {
                System.out.println("Không thể đóng kết nối!");
                e.printStackTrace();
            }
        } else {
            System.out.println("Kết nối đến cơ sở dữ liệu thất bại!");
        }
    }
}
