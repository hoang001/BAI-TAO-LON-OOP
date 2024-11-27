package org.example.daos.interfaces;

import org.example.models.LogEntity;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Giao diện cho các phương thức thao tác với dữ liệu log.
 */
public interface LogDao {

    /**
     * Thêm log mới.
     *
     * @param logEntity Đối tượng LogEntity chứa thông tin log cần thêm.
     * @return true nếu thêm log thành công, ngược lại false.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    boolean addLog(LogEntity logEntity) throws SQLException;

    /**
     * Tìm tất cả các log.
     *
     * @return Danh sách tất cả các LogEntity.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<LogEntity> findAllLogs() throws SQLException;

    /**
     * Tìm log theo ID.
     *
     * @param logId ID của log.
     * @return Đối tượng LogEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    LogEntity findLogById(int logId) throws SQLException;

    /**
     * Tìm log theo tên người dùng.
     *
     * @param userName Tên người dùng.
     * @return Danh sách LogEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<LogEntity> findLogsByUserName(String userName) throws SQLException;

    /**
     * Tìm log trong một khoảng thời gian.
     *
     * @param startDate Ngày bắt đầu.
     * @param endDate   Ngày kết thúc.
     * @return Danh sách LogEntity nếu tìm thấy, ngược lại null.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu.
     */
    List<LogEntity> findLogsByTimeRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
}
