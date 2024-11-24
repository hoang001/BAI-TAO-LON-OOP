package org.example.daos.interfaces;

import org.example.models.LogEntity;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface quản lý các log trong hệ thống.
 * Cung cấp các phương thức để thêm log mới, lấy tất cả log, tìm log theo ID,
 * lấy log theo tên người dùng và lấy log trong khoảng thời gian cụ thể.
 */
public interface LogDao {

    /**
     * Thêm một log mới vào cơ sở dữ liệu.
     *
     * @param logEntity Đối tượng LogEntity chứa thông tin log cần thêm.
     * @return true nếu thêm log thành công, false nếu có lỗi.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean addLog(LogEntity logEntity) throws SQLException;

    /**
     * Tìm tất cả các log từ hệ thống.
     *
     * @return Danh sách các đối tượng LogEntity chứa tất cả các log.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> findAllLogs() throws SQLException;

    /**
     * Lấy một log theo ID.
     *
     * @param logId ID của log cần lấy.
     * @return Đối tượng LogEntity chứa thông tin log, hoặc null nếu không tìm thấy.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    LogEntity findLogById(int logId) throws SQLException;

    /**
     * Lấy các log theo tên người dùng (userName).
     *
     * @param userName Tên người dùng cần lấy log.
     * @return Danh sách các đối tượng LogEntity chứa thông tin các log của người dùng.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> findLogsByUserName(String userName) throws SQLException;

    /**
     * Lấy các log trong một khoảng thời gian từ startDate đến endDate.
     *
     * @param startDate Thời gian bắt đầu của khoảng thời gian.
     * @param endDate   Thời gian kết thúc của khoảng thời gian.
     * @return Danh sách các đối tượng LogEntity chứa thông tin các log trong khoảng thời gian đã chỉ định.
     * @throws SQLException Nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> findLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
}
