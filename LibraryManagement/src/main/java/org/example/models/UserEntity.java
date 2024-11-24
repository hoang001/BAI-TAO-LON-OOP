package org.example.models;

/**
 * Class đại diện cho thực thể người dùng, mở rộng từ BaseEntity.
 * Class này lưu trữ các thông tin cá nhân của người dùng, bao gồm tên đăng nhập, mật khẩu, email, và các thông tin khác.
 */
public class UserEntity extends BaseEntity {
    
    // Các thuộc tính của người dùng
    private String userName;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profileImageDirectory;
    private Roles role = Roles.USER;

    /**
     * Enum đại diện cho các vai trò của người dùng.
     * Enum này được sử dụng để phân loại các vai trò của người dùng, bao gồm ADMIN, LIBRARIAN, và USER.
     */
    public enum Roles {
        ADMIN,        // Quản trị viên hệ thống
        LIBRARIAN,    // Người quản lý thư viện
        USER;         // Người dùng bình thường
    }

    /**
     * Constructor mặc định cho UserEntity.
     * Khởi tạo một đối tượng UserEntity mà không cần tham số.
     */
    public UserEntity() {}

    /**
     * Constructor cho UserEntity với các thông tin chi tiết.
     * Sử dụng constructor này để khởi tạo một đối tượng UserEntity với đầy đủ thông tin người dùng.
     *
     * @param userId              ID người dùng.
     * @param userName            Tên đăng nhập của người dùng.
     * @param passwordHash        Mật khẩu của người dùng.
     * @param email               Email của người dùng.
     * @param firstName           Tên của người dùng.
     * @param lastName            Họ của người dùng.
     * @param phoneNumber         Số điện thoại của người dùng.
     * @param profileImageDirectory Đường dẫn ảnh đại diện của người dùng.
     */
    public UserEntity(String userName, String passwordHash, String email, 
                      String firstName, String lastName, String phoneNumber, 
                      String profileImageDirectory) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageDirectory = profileImageDirectory;
        // this.role = role;
    }

    // Các phương thức getter và setter cho các thuộc tính của người dùng

    /**
     * Lấy tên đăng nhập của người dùng.
     * @return Tên đăng nhập người dùng.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Đặt tên đăng nhập cho người dùng.
     * @param userName Tên đăng nhập mới.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy mật khẩu của người dùng.
     * @return Mật khẩu người dùng.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Đặt mật khẩu cho người dùng (sau khi mã hóa).
     * @param passwordHash Mật khẩu mới.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Lấy địa chỉ email của người dùng.
     * @return Email của người dùng.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Đặt địa chỉ email cho người dùng.
     * @param email Địa chỉ email mới.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Lấy tên của người dùng.
     * @return Tên người dùng.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Đặt tên cho người dùng.
     * @param firstName Tên mới cho người dùng.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Lấy họ của người dùng.
     * @return Họ của người dùng.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Đặt họ cho người dùng.
     * @param lastName Họ mới cho người dùng.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Lấy số điện thoại của người dùng.
     * @return Số điện thoại của người dùng.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Đặt số điện thoại cho người dùng.
     * @param phoneNumber Số điện thoại mới.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Lấy đường dẫn đến ảnh đại diện của người dùng.
     * @return Đường dẫn đến ảnh đại diện của người dùng.
     */
    public String getProfileImageDirectory() {
        return profileImageDirectory;
    }

    /**
     * Đặt đường dẫn đến ảnh đại diện của người dùng.
     * @param profileImageDirectory Đường dẫn mới cho ảnh đại diện.
     */
    public void setProfileImageDirectory(String profileImageDirectory) {
        this.profileImageDirectory = profileImageDirectory;
    }

    /**
     * Lấy vai trò của người dùng.
     * @return Vai trò của người dùng (ADMIN, LIBRARIAN, USER).
     */
    public Roles getRole() {
        return role;
    }

    /**
     * Đặt vai trò cho người dùng.
     * @param role Vai trò mới cho người dùng.
     */
    public void setRole(Roles role) {
        this.role = role;
    }
}
