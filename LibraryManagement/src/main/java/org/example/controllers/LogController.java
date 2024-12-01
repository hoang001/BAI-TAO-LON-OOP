package org.example.controllers;

import java.time.LocalDateTime;
import java.util.List;
import org.example.models.LogEntity;
import org.example.services.basics.LogService;

/**
 * Lớp điều khiển (Controller) quản lý các chức năng liên quan đến việc lấy thông tin các log. Các
 * chức năng bao gồm lấy tất cả log, lấy log theo ID, lấy log theo tên người dùng và lấy log theo
 * khoảng thời gian. Lớp này tương tác với LogService để thực hiện các thao tác trên log.
 */
public class LogController {

  // Đối tượng LogService được sử dụng để thực hiện các chức năng liên quan đến log.
  private final LogService logService;

  /**
   * Constructor khởi tạo đối tượng LogService. Phương thức này được gọi khi tạo đối tượng
   * LogController để quản lý các thao tác liên quan đến log.
   */
  public LogController() {
    this.logService = new LogService();
  }

  /**
   * Lấy tất cả các log.
   *
   * @return Danh sách tất cả các log.
   */
  public List<LogEntity> getAllLogs() {
    return logService.getAllLogs();
  }

  /**
   * Lấy log theo ID.
   *
   * @param logId ID của log cần lấy.
   * @return LogEntity Đối tượng log tương ứng với logId, hoặc null nếu không tìm thấy log.
   */
  public LogEntity getLogById(int logId) {
    return logService.getLogById(logId);
  }

  /**
   * Lấy log theo tên người dùng.
   *
   * @param userName Tên người dùng mà bạn muốn tìm log.
   * @return Danh sách log của người dùng với tên userName.
   */
  public List<LogEntity> getLogsByUserName(String userName) {
    return logService.getLogsByUserName(userName);
  }

  /**
   * Lấy log trong một khoảng thời gian nhất định.
   *
   * @param startDate Thời gian bắt đầu của khoảng thời gian cần tìm log.
   * @param endDate Thời gian kết thúc của khoảng thời gian cần tìm log.
   * @return Danh sách log trong khoảng thời gian từ startDate đến endDate.
   */
  public List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
    return logService.getLogsByTimeRange(startDate, endDate);
  }
}
