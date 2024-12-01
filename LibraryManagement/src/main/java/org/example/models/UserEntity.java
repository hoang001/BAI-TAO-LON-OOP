package org.example.models;

/**
 * Lớp đại diện cho người dùng trong hệ thống.
 */
public class UserEntity extends BaseEntity {
    private String userName;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profileImageDirectory = "https://cdn.kona-blue.com/upload/kona-blue_com/post/images/2024/09/18/457/avatar-mac-dinh-12.jpg";
    private Roles role = Roles.USER;

    /**
     * Các vai trò có thể có của người dùng.
     */
    public enum Roles {
        ADMIN,
        LIBRARIAN,
        USER;
    }

    /**
     * Khởi tạo một đối tượng UserEntity mặc định.
     */
    public UserEntity() {}

        /**
     * Khởi tạo một đối tượng UserEntity với các thông tin chi tiết.
     *
     * @param userName               Tên đăng nhập của người dùng
     * @param passwordHash           Mã băm mật khẩu của người dùng
     * @param email                  Email của người dùng
     * @param firstName              Tên của người dùng
     * @param lastName               Họ của người dùng
     * @param phoneNumber            Số điện thoại của người dùng
     * @param profileImageDirectory  URL ảnh đại diện của người dùng
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
    }

    /**
     * Khởi tạo một đối tượng UserEntity với các thông tin chi tiết.
     *
     * @param userName               Tên đăng nhập của người dùng
     * @param passwordHash           Mã băm mật khẩu của người dùng
     * @param email                  Email của người dùng
     * @param firstName              Tên của người dùng
     * @param lastName               Họ của người dùng
     * @param phoneNumber            Số điện thoại của người dùng
     * @param profileImageDirectory  URL ảnh đại diện của người dùng
     * @param role                   Vai trò của người dùng
     */
    public UserEntity(String userName, String passwordHash, String email,
                      String firstName, String lastName, String phoneNumber,
                      String profileImageDirectory, Roles role) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageDirectory = profileImageDirectory;
        this.role = role;
    }

        /**
     * Khởi tạo một đối tượng UserEntity với các thông tin chi tiết.
     *
     * @param userName               Tên đăng nhập của người dùng
     * @param passwordHash           Mã băm mật khẩu của người dùng
     * @param email                  Email của người dùng
     * @param firstName              Tên của người dùng
     * @param lastName               Họ của người dùng
     * @param phoneNumber            Số điện thoại của người dùng
     * @param profileImageDirectory  URL ảnh đại diện của người dùng
     * @param role                   Vai trò của người dùng
     */
    public UserEntity(int UserID, String userName, String passwordHash, String email,
                      String firstName, String lastName, String phoneNumber,
                      String profileImageDirectory, Roles role) {
        super(UserID);
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageDirectory = profileImageDirectory;
        this.role = role;
    }

    /**
     * Lấy tên đăng nhập của người dùng.
     *
     * @return Tên đăng nhập của người dùng
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Thiết lập tên đăng nhập của người dùng.
     *
     * @param userName Tên đăng nhập của người dùng
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Lấy mã băm mật khẩu của người dùng.
     *
     * @return Mã băm mật khẩu của người dùng
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Thiết lập mã băm mật khẩu của người dùng.
     *
     * @param passwordHash Mã băm mật khẩu của người dùng
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Lấy email của người dùng.
     *
     * @return Email của người dùng
     */
    public String getEmail() {
        return email;
    }

    /**
     * Thiết lập email của người dùng.
     *
     * @param email Email của người dùng
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Lấy tên của người dùng.
     *
     * @return Tên của người dùng
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Thiết lập tên của người dùng.
     *
     * @param firstName Tên của người dùng
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Lấy họ của người dùng.
     *
     * @return Họ của người dùng
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Thiết lập họ của người dùng.
     *
     * @param lastName Họ của người dùng
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Lấy số điện thoại của người dùng.
     *
     * @return Số điện thoại của người dùng
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Thiết lập số điện thoại của người dùng.
     *
     * @param phoneNumber Số điện thoại của người dùng
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Lấy URL ảnh đại diện của người dùng.
     *
     * @return URL ảnh đại diện của người dùng
     */
    public String getProfileImageDirectory() {
        return profileImageDirectory;
    }

    /**
     * Thiết lập URL ảnh đại diện của người dùng.
     *
     * @param profileImageDirectory URL ảnh đại diện của người dùng
     */
    public void setProfileImageDirectory(String profileImageDirectory) {
        this.profileImageDirectory = profileImageDirectory;
    }

    /**
     * Lấy vai trò của người dùng.
     *
     * @return Vai trò của người dùng
     */
    public Roles getRole() {
        return role;
    }

    /**
     * Thiết lập vai trò của người dùng.
     *
     * @param role Vai trò của người dùng
     */
    public void setRole(Roles role) {
        this.role = role;
    }
}
