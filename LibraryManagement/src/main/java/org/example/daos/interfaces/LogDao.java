package org.example.daos.interfaces;

import org.example.models.LogEntity;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Giao diện quản lý các log trong hệ thống.
 */
public interface LogDao {

    /**
     * Thêm một log mới vào cơ sở dữ liệu.
     *
     * @param logEntity đối tượng LogEntity chứa thông tin log cần thêm.
     * @return true nếu thêm log thành công, false nếu có lỗi.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    boolean addLog(LogEntity logEntity) throws SQLException;

    /**
     * Lấy tất cả các log từ hệ thống.
     *
     * @return danh sách các đối tượng LogEntity chứa tất cả các log.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> getAllLogs() throws SQLException;

    /**
     * Lấy một log theo ID.
     *
     * @param logId ID của log cần lấy.
     * @return đối tượng LogEntity chứa thông tin log, hoặc null nếu không tìm thấy.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    LogEntity getLogById(int logId) throws SQLException;

    /**
     * Lấy các log theo tên người dùng (userName).
     *
     * @param userName tên người dùng cần lấy log.
     * @return danh sách các đối tượng LogEntity chứa thông tin các log của người dùng.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> getLogsByUserName(String userName) throws SQLException;

    /**
     * Lấy các log trong một khoảng thời gian từ startDate đến endDate.
     *
     * @param startDate thời gian bắt đầu của khoảng thời gian.
     * @param endDate   thời gian kết thúc của khoảng thời gian.
     * @return danh sách các đối tượng LogEntity chứa thông tin các log trong khoảng thời gian đã chỉ định.
     * @throws SQLException nếu có lỗi xảy ra khi thao tác với cơ sở dữ liệu.
     */
    List<LogEntity> getLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
}
