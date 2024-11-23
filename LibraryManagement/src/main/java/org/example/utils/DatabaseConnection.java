package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL kết nối tới cơ sở dữ liệu
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    // Tên người dùng cơ sở dữ liệu
    private static final String USER = "root";
    // Mật khẩu của người dùng cơ sở dữ liệu
    private static final String PASSWORD = "S01T004651";

    /**
     * Lấy kết nối cơ sở dữ liệu.
     *
     * @return Connection đối tượng đại diện cho kết nối cơ sở dữ liệu, hoặc null nếu kết nối thất bại.
     */
    public static Connection getConnection() {
        try {
            // Trả về kết nối đã được thiết lập
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Nếu kết nối thất bại, in ra thông báo lỗi và chi tiết ngoại lệ
            System.err.println("Error: Connection failed");
            e.printStackTrace();
            return null;
        }
    }
}
