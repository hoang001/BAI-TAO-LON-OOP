package org.example;

import java.util.List;
import java.util.Scanner;

import org.example.controllers.SearchController;
import org.example.controllers.UserController;
import org.example.models.UserEntity;

public class Main {

    public static void main(String[] args) {
        // Khởi tạo các đối tượng DAO, Service, và Controller
        UserController userController = new UserController();
        SearchController searchController = new SearchController();

        // Tạo Scanner để đọc dữ liệu từ người dùng
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        String userName = null; // Ban đầu chưa có người dùng đăng nhập

        // Menu đơn giản cho người dùng
        while (option != 0) {
            // Hiển thị menu
            System.out.println("\n--- Menu ---");
            System.out.println("1. Đăng ký người dùng");
            System.out.println("2. Đăng nhập người dùng");
            System.out.println("3. Đăng xuất người dùng");
            System.out.println("4. Thay đổi mật khẩu người dùng");
            System.out.println("5. Cập nhật email người dùng");
            System.out.println("6. Cập nhật số điện thoại người dùng");
            System.out.println("7. Cập nhật ảnh hồ sơ người dùng");
            System.out.println("8. Lấy thông tin người dùng");
            System.out.println("9. Quản lý sách đã đọc"); // Thêm menu quản lý sách đã đọc
            System.out.println("10. Tìm kiếm tác giả theo từ khóa");
            System.out.println("11. Tìm kiếm thể loại sách theo từ khóa");
            System.out.println("12. Tìm kiếm nhà xuất bản theo từ khóa");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            option = scanner.nextInt();
            scanner.nextLine();  // Đọc bỏ dòng mới

            switch (option) {
                case 1:
                    // Đăng ký người dùng mới
                    System.out.print("Nhập tên người dùng: ");
                    String userNameReg = scanner.nextLine();
                    System.out.print("Nhập mật khẩu (hash): ");
                    String passwordHashReg = scanner.nextLine();
                    System.out.print("Nhập email: ");
                    String emailReg = scanner.nextLine();
                    System.out.print("Nhập tên: ");
                    String firstNameReg = scanner.nextLine();
                    System.out.print("Nhập họ: ");
                    String lastNameReg = scanner.nextLine();
                    System.out.print("Nhập số điện thoại: ");
                    String phoneNumberReg = scanner.nextLine();
                    UserEntity newUser = new UserEntity(userNameReg, passwordHashReg, emailReg, firstNameReg, lastNameReg, phoneNumberReg);
                    userController.registerUser(newUser);
                    break;

                case 2:
                    // Đăng nhập người dùng
                    System.out.print("Nhập tên người dùng: ");
                    userName = scanner.nextLine();
                    System.out.print("Nhập mật khẩu: ");
                    String passwordHash = scanner.nextLine();
                    userController.loginUser(userName, passwordHash);
                    break;

                case 3:
                    // Đăng xuất người dùng
                    System.out.print("Nhập UserName người dùng: ");
                    scanner.nextLine();  // Đọc bỏ dòng mới
                    userController.logoutUser();
                    userName = null; // Xóa thông tin người dùng đã đăng xuất
                    break;

                case 4:
                    // Thay đổi mật khẩu
                    System.out.print("Nhập mật khẩu mới: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(newPassword);
                    break;

                case 5:
                    // Cập nhật email
                    System.out.print("Nhập email mới: ");
                    String email = scanner.nextLine();
                    userController.updateEmail(email);
                    break;

                case 6:
                    // Cập nhật số điện thoại
                    System.out.print("Nhập số điện thoại mới: ");
                    String phoneNumber = scanner.nextLine();
                    userController.updatePhoneNumber(phoneNumber);
                    break;

                case 7:
                    // Cập nhật ảnh hồ sơ
                    System.out.print("Nhập đường dẫn ảnh hồ sơ mới: ");
                    String profileImageDirectory = scanner.nextLine();
                    userController.updateProfileImage(profileImageDirectory);
                    break;

                case 8:
                    // Lấy thông tin người dùng
                    userController.getUserInfo();
                    break;

                case 9:
                    // Quản lý sách đã đọc
                    if (userName == null) {
                        System.out.println("Bạn chưa đăng nhập. Vui lòng đăng nhập trước.");
                        break;
                    }

                    int readBookChoice = -1;
                    while (readBookChoice != 0) {
                        System.out.println("\n--- Menu Quản Lý Sách Đã Đọc ---");
                        System.out.println("1. Đánh dấu sách là đã đọc");
                        System.out.println("2. Lấy danh sách sách đã đọc");
                        System.out.println("3. Kiểm tra xem sách đã được đánh dấu là đã đọc");
                        System.out.println("4. Xóa đánh dấu sách đã đọc");
                        System.out.println("5. Lấy số lượng sách đã đọc");
                        System.out.println("0. Quay lại menu chính");
                        System.out.print("Chọn hành động: ");
                        readBookChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (readBookChoice) {
                            case 1:
                                // Đánh dấu sách là đã đọc
                                System.out.print("Nhập ID sách: ");
                                int bookId = scanner.nextInt();
                                scanner.nextLine();
                                // Gọi phương thức đánh dấu sách là đã đọc
                                break;

                            case 2:
                                // Lấy danh sách sách đã đọc
                                // Gọi phương thức lấy danh sách sách đã đọc
                                break;

                            case 3:
                                // Kiểm tra xem sách đã được đánh dấu là đã đọc
                                System.out.print("Nhập ID sách: ");
                                bookId = scanner.nextInt();
                                scanner.nextLine();
                                // Gọi phương thức kiểm tra sách đã đọc
                                break;

                            case 4:
                                // Xóa đánh dấu sách đã đọc
                                System.out.print("Nhập ID sách: ");
                                bookId = scanner.nextInt();
                                scanner.nextLine();
                                // Gọi phương thức xóa đánh dấu sách đã đọc
                                break;

                            case 5:
                                // Lấy số lượng sách đã đọc
                                // Gọi phương thức lấy số lượng sách đã đọc
                                break;

                            case 0:
                                // Quay lại menu chính
                                break;

                            default:
                                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                                break;
                        }
                    }
                    break;

                case 10:
                    // Tìm kiếm tác giả theo từ khóa
                    System.out.print("Nhập từ khóa tìm kiếm tác giả: ");
                    String authorKeyword = scanner.nextLine();
                    List<String> authors = searchController.searchAuthorsByKeyword(authorKeyword);
                    System.out.println("Kết quả tìm kiếm tác giả: " + authors);
                    break;

                case 11:
                    // Tìm kiếm thể loại sách theo từ khóa
                    System.out.print("Nhập từ khóa tìm kiếm thể loại sách: ");
                    String categoryKeyword = scanner.nextLine();
                    List<String> categories = searchController.searchCategoriesByKeyword(categoryKeyword);
                    System.out.println("Kết quả tìm kiếm thể loại sách: " + categories);
                    break;

                case 12:
                    // Tìm kiếm nhà xuất bản theo từ khóa
                    System.out.print("Nhập từ khóa tìm kiếm nhà xuất bản: ");
                    String publisherKeyword = scanner.nextLine();
                    List<String> publishers = searchController.searchPublishersByKeyword(publisherKeyword);
                    System.out.println("Kết quả tìm kiếm nhà xuất bản: " + publishers);
                    break;

                case 0:
                    // Thoát
                    System.out.println("Thoát chương trình.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        }

        scanner.close();
    }
}
