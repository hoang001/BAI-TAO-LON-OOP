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

/**
 * Lớp SearchService.
 * Được sử dụng để quản lý và thực hiện các thao tác tìm kiếm liên quan đến sách và người dùng.
 */
public class SearchService {
  private final BorrowedBookService borrowedBookService;
  private final ReadBookService readBookService;
  private final BookService bookService;
  private final UserService userService;
  private final LogDao logDao;

  /**
   * Constructor của lớp SearchService.
   * Khởi tạo các đối tượng dịch vụ và DAO cần thiết.
   */
  public SearchService() {
    this.borrowedBookService = new BorrowedBookService();
    this.readBookService = new ReadBookService();
    this.bookService = new BookService();
    this.userService = UserService.getInstance();
    this.logDao = new LogDaoImpl();
  }

  /**
   * Tìm kiếm tên các tác giả dựa trên từ khóa.
   *
   * @param keyword Từ khóa tìm kiếm.
   * @return Danh sách tên tác giả phù hợp với từ khóa, tối đa 5 tên, danh sách rỗng nếu lỗi.
   */
  public List<String> searchAuthorsByKeyword(String keyword) {
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
          allBooks.add(book);
        }
      }
      for (BorrowedBookEntity borrowedBook : borrowedBooks) {
        BookEntity book = bookService.getBookById(borrowedBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }

      //Chuyển từ khóa tìm kiếm thành chữ thường.
      String normalizedKeyword = keyword.toLowerCase();

      // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
      ExecutorService executor = Executors.newFixedThreadPool(4);
      List<Callable<List<String>>> tasks = new ArrayList<>();

      // Chia các cuốn sách thành các phần nhỏ để xử lý song song
      int partitionSize = allBooks.size() / 4 + 1;

      // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
      for (int i = 0; i < allBooks.size(); i += partitionSize) {
        final List<BookEntity> sublist =
            allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
        tasks.add(
            () ->
                sublist.stream()
                    .map(
                        BookEntity::getAuthorName)
                    .filter(Objects::nonNull)
                    .filter(
                        author ->
                            author
                                .toLowerCase()
                                .contains(normalizedKeyword))
                    .distinct()
                    .collect(Collectors.toList()));
      }

      // Thực thi các tác vụ song song
      List<Future<List<String>>> futures = executor.invokeAll(tasks);
      Set<String> resultSet = new TreeSet<>();
      for (Future<List<String>> future : futures) {
        resultSet.addAll(future.get());
      }

      // Đóng ExecutorService
      executor.shutdown();

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
      prioritizedAuthors.addAll(result.stream().sorted().toList());

      // Trả về danh sách 5 tác giả đầu tiên sau khi sắp xếp
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Tìm kiếm tác giả thành công với từ khóa: " + keyword));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }
      return prioritizedAuthors.stream().limit(5).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi tìm kiếm tác giả: " + e.getMessage()));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Lỗi: " + e.getMessage());
    }
    return Collections.emptyList();
  }

  /**
   * Tìm kiếm thể loại dựa trên từ khóa.
   *
   * @param keyword Từ khóa tìm kiếm.
   * @return Danh sách thể loại phù hợp với từ khóa, tối đa 5 tên, danh sách rỗng nếu lỗi.
   */
  public List<String> searchCategoriesByKeyword(String keyword) {
    try {
      // Lấy danh sách sách đã đọc và đã mượn
      List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
      List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();

      // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
      List<BookEntity> allBooks = new ArrayList<>();
      for (ReadBookEntity readBook : readBooks) {
        BookEntity book = bookService.getBookById(readBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }
      for (BorrowedBookEntity borrowedBook : borrowedBooks) {
        BookEntity book = bookService.getBookById(borrowedBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }

      // Chuyển từ khóa tìm kiếm thành chữ thường
      String normalizedKeyword = keyword.toLowerCase();

      // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
      ExecutorService executor = Executors.newFixedThreadPool(4);
      List<Callable<List<String>>> tasks = new ArrayList<>();

      // Chia các cuốn sách thành các phần nhỏ để xử lý song song
      int partitionSize = allBooks.size() / 4 + 1;

      // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
      for (int i = 0; i < allBooks.size(); i += partitionSize) {
        final List<BookEntity> sublist =
            allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
        tasks.add(
            () ->
                sublist.stream()
                    .map(
                        BookEntity::getCategoryName)
                    .filter(Objects::nonNull)
                    .filter(
                        category ->
                            category
                                .toLowerCase()
                                .contains(normalizedKeyword))
                    .distinct()
                    .collect(Collectors.toList()));
      }

      // Thực thi các tác vụ song song
      List<Future<List<String>>> futures = executor.invokeAll(tasks);
      Set<String> resultSet = new TreeSet<>();
      for (Future<List<String>> future : futures) {
        resultSet.addAll(future.get());
      }

      // Đóng ExecutorService
      executor.shutdown();

      // Tạo danh sách kết quả cuối cùng
      List<String> result = new ArrayList<>(resultSet);

      // Đưa các thể loại có trong sách đã đọc và sách đã mượn lên đầu
      List<String> prioritizedCategories = new ArrayList<>();
      for (BookEntity book : allBooks) {
        if (book != null && result.contains(book.getCategoryName())) {
          prioritizedCategories.add(book.getCategoryName());
          result.remove(book.getCategoryName());
        }
      }

      // Kết hợp danh sách ưu tiên và danh sách còn lại
      prioritizedCategories.addAll(result.stream().sorted().toList());

      // Ghi log tìm kiếm thành công
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Tìm kiếm thể loại thành công với từ khóa: " + keyword));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }

      // Trả về danh sách 5 thể loại đầu tiên sau khi sắp xếp
      return prioritizedCategories.stream().limit(5).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      // Xử lý ngoại lệ khi thực thi tác vụ song song
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi tìm kiếm thể loại: " + e.getMessage()));
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
   * Tìm kiếm tên các nhà xuất bản dựa trên từ khóa.
   *
   * @param keyword Từ khóa tìm kiếm.
   * @return Danh sách tên nhà xuất bản phù hợp với từ khóa, tối đa 5 tên, danh sách rỗng nếu lỗi.
   */
  public List<String> searchPublishersByKeyword(String keyword) {
    try {
      List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
      List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();

      // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
      List<BookEntity> allBooks = new ArrayList<>();
      for (ReadBookEntity readBook : readBooks) {
        BookEntity book = bookService.getBookById(readBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }
      for (BorrowedBookEntity borrowedBook : borrowedBooks) {
        BookEntity book = bookService.getBookById(borrowedBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }

      // Chuyển từ khóa tìm kiếm thành chữ thường
      String normalizedKeyword = keyword.toLowerCase();

      // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
      ExecutorService executor = Executors.newFixedThreadPool(4);
      List<Callable<List<String>>> tasks = new ArrayList<>();

      // Chia các cuốn sách thành các phần nhỏ để xử lý song song
      int partitionSize = allBooks.size() / 4 + 1;

      // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
      for (int i = 0; i < allBooks.size(); i += partitionSize) {
        final List<BookEntity> sublist =
            allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
        tasks.add(
            () ->
                sublist.stream()
                    .map(
                        BookEntity::getPublisherName)
                    .filter(Objects::nonNull)
                    .filter(
                        publisher ->
                            publisher
                                .toLowerCase()
                                .contains(
                                    normalizedKeyword))
                    .distinct()
                    .collect(Collectors.toList()));
      }

      // Thực thi các tác vụ song song
      List<Future<List<String>>> futures = executor.invokeAll(tasks);
      Set<String> resultSet = new TreeSet<>();
      for (Future<List<String>> future : futures) {
        resultSet.addAll(future.get());
      }

      // Đóng ExecutorService
      executor.shutdown();

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
      prioritizedPublishers.addAll(result.stream().sorted().toList());

      // Ghi log tìm kiếm thành công
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Tìm kiếm nhà xuất bản thành công với từ khóa: " + keyword));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }

      // Trả về danh sách 5 nhà xuất bản đầu tiên sau khi sắp xếp
      return prioritizedPublishers.stream().limit(5).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      // Xử lý ngoại lệ khi thực thi tác vụ song song
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi tìm kiếm nhà xuất bản: " + e.getMessage()));
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
   * Tìm kiếm tên các tiêu đề dựa trên từ khóa.
   *
   * @param keyword Từ khóa tìm kiếm.
   * @return Danh sách tên tiêu đề phù hợp với từ khóa, tối đa 5 tên, danh sách rỗng nếu có lỗi.
   */
  public List<String> searchTitlesByKeyword(String keyword) {
    try {
      List<ReadBookEntity> readBooks = readBookService.getReadBooksByUser();
      List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooksByUser();

      // Kết hợp tất cả các sách đã đọc và đã mượn vào một danh sách chung
      List<BookEntity> allBooks = new ArrayList<>();
      for (ReadBookEntity readBook : readBooks) {
        BookEntity book = bookService.getBookById(readBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }
      for (BorrowedBookEntity borrowedBook : borrowedBooks) {
        BookEntity book = bookService.getBookById(borrowedBook.getBookId());
        if (book != null) {
          allBooks.add(book);
        }
      }

      // Chuyển từ khóa tìm kiếm thành chữ thường
      String normalizedKeyword = keyword.toLowerCase();

      // Khởi tạo một ExecutorService để xử lý song song các tác vụ tìm kiếm
      ExecutorService executor = Executors.newFixedThreadPool(4);
      List<Callable<List<String>>> tasks = new ArrayList<>();

      // Chia các cuốn sách thành các phần nhỏ để xử lý song song
      int partitionSize = allBooks.size() / 4 + 1;

      // Phân chia các cuốn sách thành các phần nhỏ và thêm vào danh sách tác vụ
      for (int i = 0; i < allBooks.size(); i += partitionSize) {
        final List<BookEntity> sublist =
            allBooks.subList(i, Math.min(i + partitionSize, allBooks.size()));
        tasks.add(
            () ->
                sublist.stream()
                    .map(
                        BookEntity::getTitle)
                    .filter(Objects::nonNull)
                    .filter(
                        publisher ->
                            publisher
                                .toLowerCase()
                                .contains(
                                    normalizedKeyword))
                    .distinct()
                    .collect(Collectors.toList()));
      }

      // Thực thi các tác vụ song song
      List<Future<List<String>>> futures = executor.invokeAll(tasks);
      Set<String> resultSet = new TreeSet<>();
      for (Future<List<String>> future : futures) {
        resultSet.addAll(future.get());
      }

      // Đóng ExecutorService
      executor.shutdown();

      // Tạo danh sách kết quả cuối cùng
      List<String> result = new ArrayList<>(resultSet);

      // Đưa các nhà xuất bản có trong sách đã đọc và sách đã mượn lên đầu
      List<String> prioritizedCategorys = new ArrayList<>();
      for (BookEntity book : allBooks) {
        if (book != null && result.contains(book.getCategoryName())) {
          prioritizedCategorys.add(book.getCategoryName());
          result.remove(book.getCategoryName());
        }
      }

      // Kết hợp danh sách ưu tiên và danh sách còn lại
      prioritizedCategorys.addAll(result.stream().sorted().toList());

      // Ghi log tìm kiếm thành công
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Tìm kiếm nhà xuất bản thành công với từ khóa: " + keyword));
      } catch (SQLException logException) {
        System.out.println("Lỗi khi ghi log: " + logException.getMessage());
      }

      // Trả về danh sách 5 nhà xuất bản đầu tiên sau khi sắp xếp
      return prioritizedCategorys.stream().limit(5).collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      // Xử lý ngoại lệ khi thực thi tác vụ song song
      System.out.println("Lỗi: " + e.getMessage());
      try {
        logDao.addLog(
            new LogEntity(
                LocalDateTime.now(),
                userService.getLoginUser().getUserName(),
                "Lỗi khi tìm kiếm nhà xuất bản: " + e.getMessage()));
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
