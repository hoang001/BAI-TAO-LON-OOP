package org.example.models;

import java.time.LocalDateTime;

/** Lớp đại diện cho log hành động của người dùng. */
public class LogEntity extends BaseEntity {
  private LocalDateTime timeStamp;
  private String userName;
  private String actionDetails;

  /** Khởi tạo một đối tượng LogEntity mặc định. */
  public LogEntity() {}

  /**
   * Khởi tạo một đối tượng LogEntity với các thông tin chi tiết.
   *
   * @param timeStamp Thời gian xảy ra hành động
   * @param userName Tên người dùng thực hiện hành động
   * @param actionDetails Chi tiết hành động
   */
  public LogEntity(LocalDateTime timeStamp, String userName, String actionDetails) {
    this.timeStamp = timeStamp;
    this.userName = userName;
    this.actionDetails = actionDetails;
  }

  /**
   * Lấy thời gian xảy ra hành động.
   *
   * @return Thời gian xảy ra hành động
   */
  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  /**
   * Thiết lập thời gian xảy ra hành động.
   *
   * @param timeStamp Thời gian xảy ra hành động
   */
  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }

  /**
   * Lấy tên người dùng thực hiện hành động.
   *
   * @return Tên người dùng thực hiện hành động
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Thiết lập tên người dùng thực hiện hành động.
   *
   * @param userName Tên người dùng thực hiện hành động
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Lấy chi tiết hành động.
   *
   * @return Chi tiết hành động
   */
  public String getActionDetails() {
    return actionDetails;
  }

  /**
   * Thiết lập chi tiết hành động.
   *
   * @param actionDetails Chi tiết hành động
   */
  public void setActionDetails(String actionDetails) {
    this.actionDetails = actionDetails;
  }
}
