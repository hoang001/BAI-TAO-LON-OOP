package org.example.daos.implementations;

import org.example.daos.interfaces.LogDao;
import org.example.models.LogEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai LogDAO để thực hiện các thao tác CRUD đối với nhật ký (logs).
 */
public class LogDaoImpl implements LogDao {
    private Connection connection;

    /**
     * Constructor để nhận đối tượng Connection từ lớp khác hoặc tạo mới nếu cần.
     *
     * @param connection đối tượng Connection để kết nối cơ sở dữ liệu.
     */
    public LogDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Thêm một log mới.
     *
     * @param logEntity Đối tượng LogEntity chứa thông tin nhật ký.
     * @return true nếu thêm log thành công, ngược lại false.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public boolean addLog(LogEntity logEntity) throws SQLException {
        String query = "INSERT INTO logs (time_stamp, user_name, action_details) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(logEntity.getTimeStamp()));
            statement.setString(2, logEntity.getUserName());
            statement.setString(3, logEntity.getActionDetails());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Nếu có bản ghi được thêm, trả về true
        }
    }

    /**
     * Lấy tất cả các log.
     *
     * @return Danh sách các đối tượng LogEntity.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<LogEntity> getAllLogs() throws SQLException {
        List<LogEntity> logs = new ArrayList<>();
        String query = "SELECT * FROM logs";
        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                LogEntity logEntity = new LogEntity(
                        rs.getTimestamp("time_stamp").toLocalDateTime(),
                        rs.getString("user_name"),
                        rs.getString("action_details")
                );
                logs.add(logEntity);
            }
        }
        return logs;
    }

    /**
     * Lấy log theo ID.
     *
     * @param logId ID của nhật ký.
     * @return Đối tượng LogEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public LogEntity getLogById(int logId) throws SQLException {
        String query = "SELECT * FROM logs WHERE log_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, logId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new LogEntity(
                            rs.getTimestamp("time_stamp").toLocalDateTime(),
                            rs.getString("user_name"),
                            rs.getString("action_details")
                    );
                }
                return null; // Nếu không tìm thấy log
            }
        }
    }

    /**
     * Lấy log theo userName.
     *
     * @param userName Tên người dùng.
     * @return Danh sách các đối tượng LogEntity.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<LogEntity> getLogsByUserName(String userName) throws SQLException {
        List<LogEntity> logs = new ArrayList<>();
        String query = "SELECT * FROM logs WHERE user_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    LogEntity logEntity = new LogEntity(
                            rs.getTimestamp("time_stamp").toLocalDateTime(),
                            rs.getString("user_name"),
                            rs.getString("action_details")
                    );
                    logs.add(logEntity);
                }
            }
        }
        return logs;
    }

    /**
     * Lấy log theo thời gian (ví dụ, lấy log trong khoảng thời gian từ startDate đến endDate).
     *
     * @param startDate Ngày bắt đầu.
     * @param endDate Ngày kết thúc.
     * @return Danh sách các đối tượng LogEntity.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<LogEntity> logs = new ArrayList<>();
        String query = "SELECT * FROM logs WHERE time_stamp BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    LogEntity logEntity = new LogEntity(
                            rs.getTimestamp("time_stamp").toLocalDateTime(),
                            rs.getString("user_name"),
                            rs.getString("action_details")
                    );
                    logs.add(logEntity);
                }
            }
        }
        return logs;
    }
}
