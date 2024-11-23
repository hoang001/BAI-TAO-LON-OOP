package org.example.models;

import java.time.LocalDateTime;

public class LogEntity extends BaseEntity{
    private LocalDateTime timeStamp;
    private String userName;
    private String actionDetails;

    // Constructor không tham số
    public LogEntity() {}

    // Constructor có tham số
    public LogEntity(LocalDateTime timeStamp, String userName, String actionDetails) {
        // this.logId = logId;
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.actionDetails = actionDetails;
    }
    
    // Getter và Setter cho timeStamp
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    // Getter và Setter cho userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter và Setter cho actionDetails
    public String getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }
}