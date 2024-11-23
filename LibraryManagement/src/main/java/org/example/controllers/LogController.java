package org.example.controllers;

import org.example.models.LogEntity;
import org.example.services.basics.LogService;
import java.time.LocalDateTime;
import java.util.List;

public class LogController {
    private final LogService logService;

    // Constructor để khởi tạo LogService
    public LogController(LogService logService) {
        this.logService = logService;
    }

    // Thêm một log mới
    public boolean addLog(LogEntity logEntity) {
        return logService.addLog(logEntity);
    }

    // Lấy tất cả log
    public List<LogEntity> getAllLogs() {
        return logService.getAllLogs();
    }

    // Lấy log theo ID
    public LogEntity getLogById(int logId) {
        return logService.getLogById(logId);
    }

    // Lấy log theo userName
    public List<LogEntity> getLogsByUserName(String userName) {
        return logService.getLogsByUserName(userName);
    }

    // Lấy log theo thời gian
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        return logService.getLogsByTimeRange(startDate, endDate);
    }
}
