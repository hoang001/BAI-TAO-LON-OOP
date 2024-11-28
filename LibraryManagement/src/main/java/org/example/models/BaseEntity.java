package org.example.models;

/** Lớp cơ sở đại diện cho thực thể cơ bản trong hệ thống. */
public abstract class BaseEntity {
  private int id;

  /** Khởi tạo một đối tượng BaseEntity mặc định. */
  public BaseEntity() {}

  /**
   * Khởi tạo một đối tượng BaseEntity với ID cụ thể.
   *
   * @param id ID của thực thể
   */
  public BaseEntity(int id) {
    this.id = id;
  }

  /**
   * Lấy ID của thực thể.
   *
   * @return ID của thực thể
   */
  public int getId() {
    return id;
  }

  /**
   * Thiết lập ID của thực thể.
   *
   * @param id ID của thực thể
   */
  public void setId(int id) {
    this.id = id;
  }
}
