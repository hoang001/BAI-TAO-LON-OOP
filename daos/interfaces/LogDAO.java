package org.example.daos.interfaces;

import org.example.models.LogEntity;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface LogDAO {
    // Thêm một log mới
    boolean addLog(LogEntity logEntity) throws SQLException;

    // Lấy tất cả các log
    List<LogEntity> getAllLogs() throws SQLException;

    // Lấy log theo ID
    LogEntity getLogById(int logId) throws SQLException;

    // Lấy log theo userName
    List<LogEntity> getLogsByUserName(String userName) throws SQLException;

    // Lấy log theo thời gian (ví dụ, lấy log trong khoảng thời gian từ startDate đến endDate)
    List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
}
