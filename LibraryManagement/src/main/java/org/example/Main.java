// package org.example;

// import org.example.controllers.ReadBookController;
// import org.example.controllers.UserController;
// import org.example.models.UserEntity;
// import org.example.services.basics.ReadBookService;
// import org.example.services.basics.UserService;
// import org.example.daos.implementations.ReadBookDAOImpl;
// import org.example.daos.implementations.UserDAOImpl;

// import java.util.Scanner;

// public class Main {

//     public static void main(String[] args) {
//         // Khởi tạo các đối tượng DAO, Service, và Controller
//         UserDAOImpl userDAO = new UserDAOImpl();
//         UserService userService = new UserService(userDAO);
//         UserController userController = new UserController(userService);

//         // Tạo đối tượng ReadBookDAO và ReadBookService
//         ReadBookDAOImpl readBookDAO = new ReadBookDAOImpl();
//         ReadBookService readBookService = new ReadBookService(readBookDAO, userService);
//         ReadBookController readBookController = new ReadBookController(readBookService);

//         // Tạo Scanner để đọc dữ liệu từ người dùng
//         Scanner scanner = new Scanner(System.in);
//         int option = -1;
//         String userName = null; // Ban đầu chưa có người dùng đăng nhập

//         // Menu đơn giản cho người dùng
//         while (option != 0) {
//             // Hiển thị menu
//             System.out.println("\n--- Menu ---");
//             System.out.println("1. Đăng ký người dùng");
//             System.out.println("2. Đăng nhập người dùng");
//             System.out.println("3. Đăng xuất người dùng");
//             System.out.println("4. Thay đổi mật khẩu người dùng");
//             System.out.println("5. Cập nhật email người dùng");
//             System.out.println("6. Cập nhật số điện thoại người dùng");
//             System.out.println("7. Cập nhật ảnh hồ sơ người dùng");
//             System.out.println("8. Lấy thông tin người dùng");
//             System.out.println("9. Quản lý sách đã đọc"); // Thêm menu quản lý sách đã đọc
//             System.out.println("0. Thoát");
//             System.out.print("Lựa chọn của bạn: ");
//             option = scanner.nextInt();
//             scanner.nextLine();  // Đọc bỏ dòng mới

//             switch (option) {
//                 case 1:
//                     // Đăng ký người dùng mới
//                     System.out.print("Nhập tên người dùng: ");
//                     String userNameReg = scanner.nextLine();
//                     System.out.print("Nhập mật khẩu (hash): ");
//                     String passwordHashReg = scanner.nextLine();
//                     System.out.print("Nhập email: ");
//                     String emailReg = scanner.nextLine();
//                     System.out.print("Nhập tên: ");
//                     String firstNameReg = scanner.nextLine();
//                     System.out.print("Nhập họ: ");
//                     String lastNameReg = scanner.nextLine();
//                     System.out.print("Nhập số điện thoại: ");
//                     String phoneNumberReg = scanner.nextLine();
//                     System.out.print("Nhập đường dẫn ảnh hồ sơ: ");
//                     String profileImageDirectoryReg = scanner.nextLine();
//                     UserEntity newUser = new UserEntity(0, userNameReg, passwordHashReg, emailReg, firstNameReg, lastNameReg, phoneNumberReg, profileImageDirectoryReg);
//                     userController.registerUser(newUser);
//                     break;

//                 case 2:
//                     // Đăng nhập người dùng
//                     System.out.print("Nhập tên người dùng: ");
//                     userName = scanner.nextLine();
//                     System.out.print("Nhập mật khẩu: ");
//                     String passwordHash = scanner.nextLine();
//                     userController.loginUser(userName, passwordHash);
//                     break;

//                 case 3:
//                     // Đăng xuất người dùng
//                     System.out.print("Nhập UserName người dùng: ");
//                     scanner.nextLine();  // Đọc bỏ dòng mới
//                     userController.logoutUser();
//                     userName = null; // Xóa thông tin người dùng đã đăng xuất
//                     break;

//                 case 4:
//                     // Thay đổi mật khẩu
//                     System.out.print("Nhập mật khẩu mới: ");
//                     String newPassword = scanner.nextLine();
//                     userController.changePassword(newPassword);
//                     break;

//                 case 5:
//                     // Cập nhật email
//                     System.out.print("Nhập email mới: ");
//                     String email = scanner.nextLine();
//                     userController.updateEmail(email);
//                     break;

//                 case 6:
//                     // Cập nhật số điện thoại
//                     System.out.print("Nhập số điện thoại mới: ");
//                     String phoneNumber = scanner.nextLine();
//                     userController.updatePhoneNumber(phoneNumber);
//                     break;

//                 case 7:
//                     // Cập nhật ảnh hồ sơ
//                     System.out.print("Nhập đường dẫn ảnh hồ sơ mới: ");
//                     String profileImageDirectory = scanner.nextLine();
//                     userController.updateProfileImage(profileImageDirectory);
//                     break;

//                 case 8:
//                     // Lấy thông tin người dùng
//                     userController.getUserInfo();
//                     break;

//                 case 9:
//                     // Quản lý sách đã đọc
//                     if (userName == null) {
//                         System.out.println("Bạn chưa đăng nhập. Vui lòng đăng nhập trước.");
//                         break;
//                     }

//                     int readBookChoice = -1;
//                     while (readBookChoice != 0) {
//                         System.out.println("\n--- Menu Quản Lý Sách Đã Đọc ---");
//                         System.out.println("1. Đánh dấu sách là đã đọc");
//                         System.out.println("2. Lấy danh sách sách đã đọc");
//                         System.out.println("3. Kiểm tra xem sách đã được đánh dấu là đã đọc");
//                         System.out.println("4. Xóa đánh dấu sách đã đọc");
//                         System.out.println("5. Lấy số lượng sách đã đọc");
//                         System.out.println("0. Quay lại menu chính");
//                         System.out.print("Chọn hành động: ");
//                         readBookChoice = scanner.nextInt();
//                         scanner.nextLine();  // Đọc bỏ dòng mới

//                         switch (readBookChoice) {
//                             case 1:
//                                 // Đánh dấu sách đã đọc
//                                 System.out.print("Nhập ID sách để đánh dấu là đã đọc: ");
//                                 int bookIdToMark = scanner.nextInt();
//                                 readBookController.markBookAsRead(bookIdToMark);
//                                 break;

//                             case 2:
//                                 // Lấy danh sách sách đã đọc
//                                 readBookController.getAllReadBooks();
//                                 break;

//                             case 3:
//                                 // Kiểm tra xem sách đã được đánh dấu là đã đọc hay chưa
//                                 System.out.print("Nhập ID sách để kiểm tra: ");
//                                 int bookIdToCheck = scanner.nextInt();
//                                 readBookController.checkIfBookIsRead(bookIdToCheck);
//                                 break;

//                             case 4:
//                                 // Xóa đánh dấu sách đã đọc
//                                 System.out.print("Nhập ID sách để xóa đánh dấu đã đọc: ");
//                                 int bookIdToUnmark = scanner.nextInt();
//                                 readBookController.unmarkBookAsRead(bookIdToUnmark);
//                                 break;

//                             case 5:
//                                 // Lấy số lượng sách đã đọc
//                                 readBookController.getReadBooksCount();
//                                 break;

//                             case 0:
//                                 // Quay lại menu chính
//                                 System.out.println("Quay lại menu chính...");
//                                 break;

//                             default:
//                                 System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
//                         }
//                     }
//                     break;

//                 case 0:
//                     System.out.println("Đang thoát...");
//                     break;

//                 default:
//                     System.out.println("Tùy chọn không hợp lệ. Vui lòng thử lại.");
//             }
//         }

//         scanner.close();
//     }
// }