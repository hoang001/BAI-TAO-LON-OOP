package org.example.models;

import java.time.LocalDateTime;

/**
 * Class đại diện cho thực thể nhật ký, mở rộng từ BaseEntity.
 */
public class LogEntity extends BaseEntity {
    private LocalDateTime timeStamp;
    private String userName;
    private String actionDetails;

    /**
     * Constructor không tham số cho LogEntity.
     */
    public LogEntity() {}

    /**
     * Constructor có tham số cho LogEntity.
     *
     * @param timeStamp Thời điểm diễn ra hành động.
     * @param userName Tên người dùng thực hiện hành động.
     * @param actionDetails Chi tiết hành động.
     */
    public LogEntity(LocalDateTime timeStamp, String userName, String actionDetails) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.actionDetails = actionDetails;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }
}
