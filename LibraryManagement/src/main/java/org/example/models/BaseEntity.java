package org.example.models;

public abstract class BaseEntity {
    private int id;

    /**
     * Constructor mặc định cho BaseEntity.
     */
    public BaseEntity() {}

    /**
     * Constructor có tham số cho BaseEntity.
     *
     * @param id Giá trị định danh duy nhất.
     */
    public BaseEntity(int id) {
        this.id = id;
    }

    /**
     * Lấy giá trị của ID.
     *
     * @return giá trị ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Đặt giá trị cho ID.
     *
     * @param id Giá trị định danh cần đặt.
     */
    public void setId(int id) {
        this.id = id;
    }
}
