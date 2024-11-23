package org.example.models;

/**
 * Class đại diện cho thực thể người dùng, mở rộng từ PersonEntity.
 */
public class UserEntity extends BaseEntity {
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
     */
    public enum Roles {
        ADMIN,
        LIBRARYAN,
        USER;
    }

    /**
     * Constructor mặc định cho UserEntity.
     */
    public UserEntity() {}

    /**
     * Constructor cho UserEntity với các thông tin chi tiết.
     *
     * @param userId              ID người dùng.
     * @param userName            Tên đăng nhập người dùng.
     * @param passwordHash        Mã hóa mật khẩu.
     * @param email               Email của người dùng.
     * @param firstName           Tên của người dùng.
     * @param lastName            Họ của người dùng.
     * @param phoneNumber         Số điện thoại của người dùng.
     * @param profileImageDirectory Đường dẫn ảnh đại diện của người dùng.
     */
    public UserEntity(int userId, String userName, String passwordHash, String email, String firstName, String lastName, String phoneNumber, String profileImageDirectory) {
        super(userId);
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageDirectory = profileImageDirectory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageDirectory() {
        return profileImageDirectory;
    }

    public void setProfileImageDirectory(String profileImageDirectory) {
        this.profileImageDirectory = profileImageDirectory;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
