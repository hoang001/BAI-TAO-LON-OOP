package org.example.models;

import java.time.LocalDateTime;

/**
 * Lớp này đại diện cho thực thể nhật ký, lưu trữ thông tin về hành động của người dùng.
 * Nó mở rộng từ lớp BaseEntity để kế thừa thuộc tính ID.
 */
public class LogEntity extends BaseEntity {
    
    // Thuộc tính của lớp LogEntity
    private LocalDateTime timeStamp;   // Thời gian thực hiện hành động
    private String userName;           // Tên người dùng thực hiện hành động
    private String actionDetails;      // Chi tiết hành động

    /**
     * Constructor mặc định cho LogEntity.
     * Khởi tạo đối tượng LogEntity mà không cần thông tin đầu vào.
     */
    public LogEntity() {}

    /**
     * Constructor cho LogEntity với các thông tin chi tiết.
     *
     * @param timeStamp     Thời điểm diễn ra hành động.
     * @param userName      Tên người dùng thực hiện hành động.
     * @param actionDetails Chi tiết hành động thực hiện.
     */
    public LogEntity(LocalDateTime timeStamp, String userName, String actionDetails) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.actionDetails = actionDetails;
    }

    /**
     * Lấy thời điểm diễn ra hành động.
     * 
     * @return Thời gian của hành động dưới dạng LocalDateTime.
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * Đặt thời điểm diễn ra hành động.
     * 
     * @param timeStamp Thời gian của hành động dưới dạng LocalDateTime.
     */
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Lấy tên người dùng thực hiện hành động.
     * 
     * @return Tên người dùng thực hiện hành động.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Đặt tên người dùng thực hiện hành động.
     * 
     * @param userName Tên người dùng cần thiết lập.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy chi tiết hành động thực hiện.
     * 
     * @return Chi tiết hành động.
     */
    public String getActionDetails() {
        return actionDetails;
    }

    /**
     * Đặt chi tiết hành động thực hiện.
     * 
     * @param actionDetails Chi tiết hành động cần thiết lập.
     */
    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }
}
