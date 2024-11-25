package org.example.controllers;

import org.example.models.LogEntity;
import org.example.services.basics.LogService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến việc lấy thông tin các log.
 * Các chức năng bao gồm lấy tất cả log, lấy log theo ID, lấy log theo tên người dùng và lấy log theo khoảng thời gian.
 * Lớp này tương tác với LogService để thực hiện các thao tác trên log.
 */
public class LogController {

    // Đối tượng LogService được sử dụng để thực hiện các chức năng liên quan đến log.
    private final LogService logService;

    /**
     * Constructor khởi tạo đối tượng LogService.
     * Phương thức này được gọi khi tạo đối tượng LogController để quản lý các thao tác liên quan đến log.
     */
    public LogController() {
        this.logService = new LogService();  // Khởi tạo LogService để xử lý các yêu cầu liên quan đến log.
    }

    /**
     * Lấy tất cả các log.
     * 
     * @return List<LogEntity> Danh sách tất cả các log.
     */
    public List<LogEntity> getAllLogs() {
        return logService.getAllLogs();  // Gọi phương thức của LogService để lấy tất cả các log.
    }

    /**
     * Lấy log theo ID.
     * 
     * @param logId ID của log cần lấy.
     * @return LogEntity Đối tượng log tương ứng với logId, hoặc null nếu không tìm thấy log.
     */
    public LogEntity getLogById(int logId) {
        return logService.getLogById(logId);  // Gọi phương thức của LogService để lấy log theo ID.
    }

    /**
     * Lấy log theo tên người dùng.
     * 
     * @param userName Tên người dùng mà bạn muốn tìm log.
     * @return List<LogEntity> Danh sách log của người dùng với tên userName.
     */
    public List<LogEntity> getLogsByUserName(String userName) {
        return logService.getLogsByUserName(userName);  // Gọi phương thức của LogService để lấy log theo tên người dùng.
    }

    /**
     * Lấy log trong một khoảng thời gian nhất định.
     * 
     * @param startDate Thời gian bắt đầu của khoảng thời gian cần tìm log.
     * @param endDate Thời gian kết thúc của khoảng thời gian cần tìm log.
     * @return List<LogEntity> Danh sách log trong khoảng thời gian từ startDate đến endDate.
     */
    public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        return logService.getLogsByTimeRange(startDate, endDate);  // Gọi phương thức của LogService để lấy log theo khoảng thời gian.
    }
}
