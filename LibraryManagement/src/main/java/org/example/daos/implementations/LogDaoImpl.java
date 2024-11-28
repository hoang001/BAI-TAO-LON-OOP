package org.example.daos.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.daos.interfaces.LogDao;
import org.example.models.LogEntity;
import org.example.utils.DatabaseConnection;

/** Lớp triển khai LogDAO để thực hiện các thao tác CRUD đối với nhật ký (logs). */
public class LogDaoImpl implements LogDao {
  private final Connection connection;

  /** Hàm khởi tạo để thiết lập kết nối cơ sở dữ liệu. */
  public LogDaoImpl() {
    this.connection = DatabaseConnection.getConnection();
  }

  /**
   * Thêm một log mới.
   *
   * @param log Đối tượng LogEntity chứa thông tin log cần thêm.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public void addLog(LogEntity log) throws SQLException {
    String query = "INSERT INTO logs (timeStamp, userName, actionDetails) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setTimestamp(1, Timestamp.valueOf(log.getTimeStamp()));
      statement.setString(2, log.getUserName());
      statement.setString(3, log.getActionDetails());
      // Thực thi câu lệnh chèn
      statement.executeUpdate();
    }
  }

  /**
   * Lấy tất cả các log.
   *
   * @return Danh sách các đối tượng LogEntity.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public List<LogEntity> findAllLogs() throws SQLException {
    List<LogEntity> logs = new ArrayList<>();
    String query = "SELECT * FROM logs";
    try (Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query)) {
      while (rs.next()) {
        LogEntity logEntity =
            new LogEntity(
                rs.getTimestamp("timeStamp").toLocalDateTime(),
                rs.getString("userName"),
                rs.getString("actionDetails"));
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
  public LogEntity findLogById(int logId) throws SQLException {
    String query = "SELECT * FROM logs WHERE logId = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, logId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new LogEntity(
              rs.getTimestamp("timeStamp").toLocalDateTime(),
              rs.getString("userName"),
              rs.getString("actionDetails"));
        }
        return null;
      }
    }
  }

  /**
   * Lấy log theo tên người dùng.
   *
   * @param userName Tên người dùng.
   * @return Danh sách các đối tượng LogEntity.
   * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
   */
  @Override
  public List<LogEntity> findLogsByUserName(String userName) throws SQLException {
    List<LogEntity> logs = new ArrayList<>();
    String query = "SELECT * FROM logs WHERE userName = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, userName);
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          LogEntity logEntity =
              new LogEntity(
                  rs.getTimestamp("timeStamp").toLocalDateTime(),
                  rs.getString("userName"),
                  rs.getString("actionDetails"));
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
  public List<LogEntity> findLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate)
      throws SQLException {
    List<LogEntity> logs = new ArrayList<>();
    String query = "SELECT * FROM logs WHERE timeStamp BETWEEN ? AND ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setTimestamp(1, Timestamp.valueOf(startDate));
      statement.setTimestamp(2, Timestamp.valueOf(endDate));
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          LogEntity logEntity =
              new LogEntity(
                  rs.getTimestamp("timeStamp").toLocalDateTime(),
                  rs.getString("userName"),
                  rs.getString("actionDetails"));
          logs.add(logEntity);
        }
      }
    }
    return logs;
  }
}
