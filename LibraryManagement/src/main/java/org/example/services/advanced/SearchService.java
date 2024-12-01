package org.example.services.advanced;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.example.daos.implementations.LogDaoImpl;
import org.example.daos.interfaces.LogDao;
import org.example.models.BookEntity;
import org.example.models.BorrowedBookEntity;
import org.example.models.LogEntity;
import org.example.models.ReadBookEntity;
import org.example.services.basics.BookService;
import org.example.services.basics.BorrowedBookService;
import org.example.services.basics.ReadBookService;
import org.example.services.basics.UserService;

public class SearchService {
    /**
     * Các service được inject vào để phục vụ cho việc tìm kiếm
     */
    private final BorrowedBookService borrowedBookService;
    private final ReadBookService readBookService;
    private final BookService bookService;
    private final UserService userService;
    private final LogDao logDao;

    /**
     * Constructor.
     */
    public SearchService() {
        this.borrowedBookService = new BorrowedBookService();
        this.readBookService = new ReadBookService();
        this.bookService = new BookService();
        this.userService = UserService.getInstance();
        this.logDao = new LogDaoImpl();
    }

    /**
     * Tìm kiếm các tác giả theo từ khóa
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các tác giả phù hợp với từ khóa
     * @throws InterruptedException Nếu luồng bị gián đoạn
     * @throws ExecutionException Nếu có lỗi khi thực thi các tác vụ song song
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<String> searchAuthorsByKeyword(String keyword) {
        // Kiểm tra xem người dùng đã đăng nhập chưa, nếu chưa thì yêu cầu đăng nhập
        try {
            // Lấy danh sách các cuốn sách người dùng đã đọc và đã mượn
            List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
            List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();
    
            // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
            List<BookEntity> allBooks = new ArrayList<>();
    
            // Thêm các cuốn sách đã đọc vào danh sách
            for (ReadBookEntity readBook : readBooks) {
                BookEntity book = bookService.getBookById(readBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Nếu tìm thấy sách, thêm vào danh sách
                }
            }
    
            // Thêm các cuốn sách đã mượn vào danh sách
            for (BorrowedBookEntity borrowedBook : borrowedBooks) {
                BookEntity book = bookService.getBookById(borrowedBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Nếu tìm thấy sách, thêm vào danh sách
                }
            }
    
            String normalizedKeyword = keyword.toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường để so sánh
    
            // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
            ExecutorService executor = Executors.newFixedThreadPool(4); // Sử dụng thread pool với 4 luồng
            List<Callable<List<String>>> tasks = new ArrayList<>(); // Danh sách các tác vụ tìm kiếm
    
            // Chia các cuốn sách thành các phần nhỏ để xử lý song song
            int partitionSize = allBooks.size() / 4 + 1; // Chia danh sách thành 4 phần
    
            // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
            for (int i = 0; i < allBooks.size(); i += partitionSize) {
                final List<BookEntity> sublist = allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
                tasks.add(() -> sublist.stream()
                        .map(book -> {
                            // Lấy tên tác giả của cuốn sách
                            return book.getAuthorName(); // Nếu tìm thấy tác giả, trả về tên
                        })
                        .filter(Objects::nonNull) // Lọc các kết quả không có tên tác giả
                        .filter(author -> author.toLowerCase().contains(normalizedKeyword)) // So sánh từ khóa với tên tác giả
                        .distinct() // Lọc các tác giả trùng lặp
                        .collect(Collectors.toList())); // Thu thập kết quả vào danh sách
            }
    
            // Thực thi các tác vụ song song
            List<Future<List<String>>> futures = executor.invokeAll(tasks); // Thực thi tất cả các tác vụ
            Set<String> resultSet = new TreeSet<>(); // Sử dụng TreeSet để lưu trữ các tác giả mà không có bản sao
            for (Future<List<String>> future : futures) {
                resultSet.addAll(future.get()); // Thêm kết quả từ các tác vụ vào resultSet
            }
    
            executor.shutdown(); // Đóng ExecutorService
    
            // Tạo danh sách kết quả cuối cùng
            List<String> result = new ArrayList<>(resultSet);
    
            // Đưa các tác giả có trong sách đã đọc và sách đã mượn lên đầu
            List<String> prioritizedAuthors = new ArrayList<>();
            for (BookEntity book : allBooks) {
                if (book != null && result.contains(book.getAuthorName())) {
                    prioritizedAuthors.add(book.getAuthorName());
                    result.remove(book.getAuthorName());
                }
            }
    
            // Kết hợp danh sách ưu tiên và danh sách còn lại
            prioritizedAuthors.addAll(result.stream().sorted().collect(Collectors.toList()));
    
            // Trả về danh sách 5 tác giả đầu tiên sau khi sắp xếp
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm kiếm tác giả thành công với từ khóa: " + keyword));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return prioritizedAuthors.stream().limit(5).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm kiếm tác giả: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Tìm kiếm thể loại sách theo từ khóa
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các thể loại sách phù hợp với từ khóa
     * @throws InterruptedException Nếu luồng bị gián đoạn
     * @throws ExecutionException Nếu có lỗi khi thực thi các tác vụ song song
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<String> searchCategoriesByKeyword(String keyword) {
        // Kiểm tra nếu người dùng chưa đăng nhập
        try {
            // Lấy danh sách sách đã đọc và đã mượn
            List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
            List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();
    
            // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
            List<BookEntity> allBooks = new ArrayList<>();
            for (ReadBookEntity readBook : readBooks) {
                BookEntity book = bookService.getBookById(readBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã đọc vào danh sách
                }
            }
    
            for (BorrowedBookEntity borrowedBook : borrowedBooks) {
                BookEntity book = bookService.getBookById(borrowedBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã mượn vào danh sách
                }
            }
    
            String normalizedKeyword = keyword.toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường
    
            // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
            ExecutorService executor = Executors.newFixedThreadPool(4); // Sử dụng thread pool với 4 luồng
            List<Callable<List<String>>> tasks = new ArrayList<>(); // Danh sách các tác vụ tìm kiếm
    
            // Chia các cuốn sách thành các phần nhỏ để xử lý song song
            int partitionSize = allBooks.size() / 4 + 1;
    
            // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
            for (int i = 0; i < allBooks.size(); i += partitionSize) {
                final List<BookEntity> sublist = allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
                tasks.add(() -> sublist.stream()
                        .map(book -> {
                            return book.getCategory(); // Nếu tìm thấy thể loại, trả về tên
                        })
                        .filter(Objects::nonNull) // Lọc các kết quả không có thể loại
                        .filter(category -> category.toLowerCase().contains(normalizedKeyword)) // So sánh từ khóa với tên thể loại
                        .distinct() // Lọc các thể loại trùng lặp
                        .collect(Collectors.toList())); // Thu thập kết quả vào danh sách
            }
    
            // Thực thi các tác vụ song song
            List<Future<List<String>>> futures = executor.invokeAll(tasks); // Thực thi tất cả các tác vụ
            Set<String> resultSet = new TreeSet<>(); // Sử dụng TreeSet để lưu trữ các thể loại mà không có bản sao
            for (Future<List<String>> future : futures) {
                resultSet.addAll(future.get()); // Thêm kết quả từ các tác vụ vào resultSet
            }
    
            executor.shutdown(); // Đóng ExecutorService
    
            // Tạo danh sách kết quả cuối cùng
            List<String> result = new ArrayList<>(resultSet);
    
            // Đưa các thể loại có trong sách đã đọc và sách đã mượn lên đầu
            List<String> prioritizedCategories = new ArrayList<>();
            for (BookEntity book : allBooks) {
                if (book != null && result.contains(book.getCategory())) {
                    prioritizedCategories.add(book.getCategory());
                    result.remove(book.getCategory());
                }
            }
    
            // Kết hợp danh sách ưu tiên và danh sách còn lại
            prioritizedCategories.addAll(result.stream().sorted().collect(Collectors.toList()));
    
            // Ghi log tìm kiếm thành công
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm kiếm thể loại thành công với từ khóa: " + keyword));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
    
            // Trả về danh sách 5 thể loại đầu tiên sau khi sắp xếp
            return prioritizedCategories.stream().limit(5).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            // Xử lý ngoại lệ khi thực thi tác vụ song song
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm kiếm thể loại: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return Collections.emptyList();
        } catch (IllegalArgumentException e) {
            // Xử lý ngoại lệ liên quan đến đầu vào không hợp lệ
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        }
    }    


    /**
     * Tìm kiếm nhà xuất bản theo từ khóa
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các nhà xuất bản phù hợp với từ khóa
     * @throws InterruptedException Nếu luồng bị gián đoạn
     * @throws ExecutionException Nếu có lỗi khi thực thi các tác vụ song song
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<String> searchPublishersByKeyword(String keyword) {
        // Kiểm tra nếu người dùng chưa đăng nhập
        try {
            List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
            List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();
    
            // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
            List<BookEntity> allBooks = new ArrayList<>();
            for (ReadBookEntity readBook : readBooks) {
                BookEntity book = bookService.getBookById(readBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã đọc vào danh sách
                }
            }
    
            for (BorrowedBookEntity borrowedBook : borrowedBooks) {
                BookEntity book = bookService.getBookById(borrowedBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã mượn vào danh sách
                }
            }
    
            String normalizedKeyword = keyword.toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường
    
            // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
            ExecutorService executor = Executors.newFixedThreadPool(4); // Sử dụng thread pool với 4 luồng
            List<Callable<List<String>>> tasks = new ArrayList<>(); // Danh sách các tác vụ tìm kiếm
    
            // Chia các cuốn sách thành các phần nhỏ để xử lý song song
            int partitionSize = allBooks.size() / 4 + 1;
    
            // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
            for (int i = 0; i < allBooks.size(); i += partitionSize) {
                final List<BookEntity> sublist = allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
                tasks.add(() -> sublist.stream()
                        .map(book -> {
                            return book.getPublisherName(); // Nếu tìm thấy nhà xuất bản, trả về tên
                        })
                        .filter(Objects::nonNull) // Lọc các kết quả không có nhà xuất bản
                        .filter(publisher -> publisher.toLowerCase().contains(normalizedKeyword)) // So sánh từ khóa với tên nhà xuất bản
                        .distinct() // Lọc các nhà xuất bản trùng lặp
                        .collect(Collectors.toList())); // Thu thập kết quả vào danh sách
            }
    
            // Thực thi các tác vụ song song
            List<Future<List<String>>> futures = executor.invokeAll(tasks); // Thực thi tất cả các tác vụ
            Set<String> resultSet = new TreeSet<>(); // Sử dụng TreeSet để lưu trữ các nhà xuất bản mà không có bản sao
            for (Future<List<String>> future : futures) {
                resultSet.addAll(future.get()); // Thêm kết quả từ các tác vụ vào resultSet
            }
    
            executor.shutdown(); // Đóng ExecutorService
    
            // Tạo danh sách kết quả cuối cùng
            List<String> result = new ArrayList<>(resultSet);
    
            // Đưa các nhà xuất bản có trong sách đã đọc và sách đã mượn lên đầu
            List<String> prioritizedPublishers = new ArrayList<>();
            for (BookEntity book : allBooks) {
                if (book != null && result.contains(book.getPublisherName())) {
                    prioritizedPublishers.add(book.getPublisherName());
                    result.remove(book.getPublisherName());
                }
            }
    
            // Kết hợp danh sách ưu tiên và danh sách còn lại
            prioritizedPublishers.addAll(result.stream().sorted().collect(Collectors.toList()));
    
            // Ghi log tìm kiếm thành công
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm kiếm nhà xuất bản thành công với từ khóa: " + keyword));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
    
            // Trả về danh sách 5 nhà xuất bản đầu tiên sau khi sắp xếp
            return prioritizedPublishers.stream().limit(5).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            // Xử lý ngoại lệ khi thực thi tác vụ song song
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm kiếm nhà xuất bản: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return Collections.emptyList();
        } catch (IllegalArgumentException e) {
            // Xử lý ngoại lệ liên quan đến đầu vào không hợp lệ
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Tìm sách theo tiêu đề.
     * @param keyword tiêu đề cần tìm.
     * @return trả về gợi ý tìm kiếm.
     */
    public List<String> searchTitlesByKeyword(String keyword) {
        // Kiểm tra nếu người dùng chưa đăng nhập
        try {
            List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
            List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();
    
            // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
            List<BookEntity> allBooks = new ArrayList<>();
            for (ReadBookEntity readBook : readBooks) {
                BookEntity book = bookService.getBookById(readBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã đọc vào danh sách
                }
            }
    
            for (BorrowedBookEntity borrowedBook : borrowedBooks) {
                BookEntity book = bookService.getBookById(borrowedBook.getBookId());
                if (book != null) {
                    allBooks.add(book); // Thêm sách đã mượn vào danh sách
                }
            }
    
            String normalizedKeyword = keyword.toLowerCase(); // Chuyển từ khóa tìm kiếm thành chữ thường
    
            // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
            ExecutorService executor = Executors.newFixedThreadPool(4); // Sử dụng thread pool với 4 luồng
            List<Callable<List<String>>> tasks = new ArrayList<>(); // Danh sách các tác vụ tìm kiếm
    
            // Chia các cuốn sách thành các phần nhỏ để xử lý song song
            int partitionSize = allBooks.size() / 4 + 1;
    
            // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
            for (int i = 0; i < allBooks.size(); i += partitionSize) {
                final List<BookEntity> sublist = allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
                tasks.add(() -> sublist.stream()
                        .map(book -> {
                            return book.getCategory(); // Nếu tìm thấy nhà xuất bản, trả về tên
                        })
                        .filter(Objects::nonNull) // Lọc các kết quả không có nhà xuất bản
                        .filter(publisher -> publisher.toLowerCase().contains(normalizedKeyword)) // So sánh từ khóa với tên nhà xuất bản
                        .distinct() // Lọc các nhà xuất bản trùng lặp
                        .collect(Collectors.toList())); // Thu thập kết quả vào danh sách
            }
    
            // Thực thi các tác vụ song song
            List<Future<List<String>>> futures = executor.invokeAll(tasks); // Thực thi tất cả các tác vụ
            Set<String> resultSet = new TreeSet<>(); // Sử dụng TreeSet để lưu trữ các nhà xuất bản mà không có bản sao
            for (Future<List<String>> future : futures) {
                resultSet.addAll(future.get()); // Thêm kết quả từ các tác vụ vào resultSet
            }
    
            executor.shutdown(); // Đóng ExecutorService
    
            // Tạo danh sách kết quả cuối cùng
            List<String> result = new ArrayList<>(resultSet);
    
            // Đưa các nhà xuất bản có trong sách đã đọc và sách đã mượn lên đầu
            List<String> prioritizedCategorys = new ArrayList<>();
            for (BookEntity book : allBooks) {
                if (book != null && result.contains(book.getCategory())) {
                    prioritizedCategorys.add(book.getCategory());
                    result.remove(book.getCategory());
                }
            }
    
            // Kết hợp danh sách ưu tiên và danh sách còn lại
            prioritizedCategorys.addAll(result.stream().sorted().collect(Collectors.toList()));
    
            // Ghi log tìm kiếm thành công
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Tìm kiếm nhà xuất bản thành công với từ khóa: " + keyword));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
    
            // Trả về danh sách 5 nhà xuất bản đầu tiên sau khi sắp xếp
            return prioritizedCategorys.stream().limit(5).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            // Xử lý ngoại lệ khi thực thi tác vụ song song
            System.out.println("Lỗi: " + e.getMessage());
            try {
                logDao.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getUserName(), "Lỗi khi tìm kiếm nhà xuất bản: " + e.getMessage()));
            } catch (SQLException logException) {
                System.out.println("Lỗi khi ghi log: " + logException.getMessage());
            }
            return Collections.emptyList();
        } catch (IllegalArgumentException e) {
            // Xử lý ngoại lệ liên quan đến đầu vào không hợp lệ
            System.out.println("Lỗi: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
