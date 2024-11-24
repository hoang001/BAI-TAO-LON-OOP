package org.example.models;

/**
 * Lớp cơ sở đại diện cho các thực thể có định danh duy nhất.
 * Lớp này được kế thừa bởi các lớp khác để định nghĩa các thuộc tính chung của các thực thể.
 */
public abstract class BaseEntity {
    
    // ID duy nhất cho thực thể
    private int id;

    /**
     * Constructor mặc định cho BaseEntity.
     * Dùng để khởi tạo đối tượng mà không cần cung cấp ID.
     */
    public BaseEntity() {}

    /**
     * Constructor có tham số cho BaseEntity.
     * Dùng để khởi tạo đối tượng với một ID xác định.
     *
     * @param id Giá trị định danh duy nhất của thực thể.
     */
    public BaseEntity(int id) {
        this.id = id;
    }

    /**
     * Lấy giá trị của ID.
     * Phương thức này trả về giá trị của trường id.
     *
     * @return Giá trị ID của thực thể.
     */
    public int getId() {
        return id;
    }

    /**
     * Đặt giá trị cho ID.
     * Phương thức này cho phép thay đổi giá trị của trường id.
     *
     * @param id Giá trị định danh mới cần đặt cho thực thể.
     */
    public void setId(int id) {
        this.id = id;
    }
}
