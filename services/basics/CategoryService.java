package org.example.services.basics;

import org.example.daos.interfaces.CategoryDAO;
import org.example.models.CategoryEntity;
import org.example.models.UserEntity.Roles;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class CategoryService {
    private final CategoryDAO categoryDAO;
    private final UserService userService;
    private final LogService logService;

    public CategoryService(CategoryDAO categoryDAO, UserService userService, LogService logService) {
        this.categoryDAO = categoryDAO;
        this.userService = userService;
        this.logService = logService;
    }

    // Thêm thể loại mới
    public boolean addCategory(CategoryEntity category) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để thêm thể loại");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền thêm thể loại");
            }
            if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
                throw new IllegalArgumentException("Tên thể loại không được để trống");
            }
            if (category.getCategoryName().length() > 50) {
                throw new IllegalArgumentException("Tên thể loại vượt quá giới hạn 50 ký tự");
            }
            if (category.getDescription() == null || category.getDescription().isEmpty()) {
                throw new IllegalArgumentException("Mô tả không được để trống");
            }
            boolean result = categoryDAO.addCategory(category);
            if (result) {
                logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Thêm thể loại: " + category.getCategoryName() + " thành công"));
            }
            return result;
        } catch (SQLException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi khi thêm thể loại: " + e.getMessage()));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình thêm thể loại: " + e.getMessage());
            return false;
        } catch (IllegalStateException | SecurityException | IllegalArgumentException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi thêm thể loại: " + e.getMessage()));
            System.out.println("Lỗi thêm thể loại: " + e.getMessage());
            return false;
        }
    }

    // Xóa thể loại theo ID
    public boolean deleteCategoryById(int categoryId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập để xóa thể loại");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền xóa thể loại");
            }
            boolean result = categoryDAO.deleteCategoryById(categoryId);
            if (result) {
                logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Xóa thể loại ID: " + categoryId + " thành công"));
            }
            return result;
        } catch (SQLException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi khi xóa thể loại ID " + categoryId + ": " + e.getMessage()));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình xóa thể loại: " + e.getMessage());
            return false;
        } catch (IllegalStateException | SecurityException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi xóa thể loại: " + e.getMessage()));
            System.out.println("Lỗi xóa thể loại: " + e.getMessage());
            return false;
        }
    }

    // Lấy thông tin thể loại dựa trên ID
    public CategoryEntity findCategoryById(int categoryId) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy thông tin thể loại theo ID");
            }
            CategoryEntity category = categoryDAO.findCategoryById(categoryId);
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Lấy thông tin thể loại ID: " + categoryId));
            return category;
        } catch (SQLException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi khi lấy thông tin thể loại ID " + categoryId + ": " + e.getMessage()));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy thông tin thể loại: " + e.getMessage());
            return null;
        } catch (IllegalStateException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi lấy thông tin thể loại: " + e.getMessage()));
            System.out.println("Lỗi lấy thông tin thể loại: " + e.getMessage());
            return null;
        }
    }

    // Tìm thể loại dựa trên tên
    public CategoryEntity findCategoryByName(String categoryName) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy thông tin thể loại theo tên");
            }
            CategoryEntity category = categoryDAO.findCategoryByName(categoryName);
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Tìm thể loại tên: " + categoryName));
            return category;
        } catch (SQLException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi khi tìm thể loại tên: " + categoryName + ": " + e.getMessage()));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình tìm thể loại: " + e.getMessage());
            return null;
        } catch (IllegalStateException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi tìm thể loại: " + e.getMessage()));
            System.out.println("Lỗi tìm thể loại: " + e.getMessage());
            return null;
        }
    }

    // Lấy tất cả các thể loại
    public List<CategoryEntity> getAllCategories() {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalStateException("Bạn cần đăng nhập trước khi lấy danh sách các thể loại");
            }
            List<CategoryEntity> categories = categoryDAO.getAllCategories();
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Lấy tất cả thể loại"));
            return categories;
        } catch (SQLException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi khi lấy tất cả thể loại: " + e.getMessage()));
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy tất cả thể loại: " + e.getMessage());
            return null;
        } catch (IllegalStateException e) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser() != null ? userService.getLoginUser().getName() : "unknown", "Lỗi lấy tất cả thể loại: " + e.getMessage()));
            System.out.println("Lỗi lấy tất cả thể loại: " + e.getMessage());
            return null;
        }
    }
}
