package org.example.services.basics;

import org.example.models.UserEntity.Roles;
import org.example.daos.interfaces.PublisherDAO;
import org.example.models.PublisherEntity;
import org.example.models.LogEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PublisherService {
    private final PublisherDAO publisherDAO;
    private final UserService userService;
    private final LogService logService;

    public PublisherService(PublisherDAO publisherDAO, UserService userService, LogService logService) {
        this.publisherDAO = publisherDAO;
        this.userService = userService;
        this.logService = logService;
    }

    // Thêm nhà xuất bản
    public boolean addPublisher(PublisherEntity publisher) {
        try {
            if (userService.getLoginUser() == null) {
                throw new IllegalArgumentException("Bạn cần đăng nhập để thêm nhà xuất bản");
            }
            if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
                throw new SecurityException("Bạn không có quyền");
            }
            if (publisher.getPublisherName() == null || publisher.getPublisherName().isEmpty()) {
                throw new IllegalArgumentException("Tên nhà xuất bản không được để trống");
            }
            if (publisher.getPublisherName().length() > 100) {
                throw new IllegalArgumentException("Tên nhà xuất bản vượt quá giới hạn 100 ký tự");
            }
            if (publisher.getAddress() != null && publisher.getAddress().length() > 200) {
                throw new IllegalArgumentException("Địa chỉ vượt quá giới hạn 200 ký tự");
            }
            if (publisher.getContactEmail() != null && publisher.getContactEmail().length() > 50) {
                throw new IllegalArgumentException("Email liên hệ vượt quá giới hạn 50 ký tự");
            }
            if (publisher.getContactPhone() != null && publisher.getContactPhone().length() > 20) {
                throw new IllegalArgumentException("Số điện thoại liên hệ vượt quá giới hạn 20 ký tự");
            }
            boolean result = publisherDAO.addPublisher(publisher);
            if (result) {
                logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Thêm nhà xuất bản: " + publisher.getPublisherName()));
            }
            return result;
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu trong quá trình thêm nhà xuất bản: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Lỗi thêm nhà xuất bản: " + e.getMessage());
            return false;
        }
    }    

public boolean deletePublisherById(int publisherId) {
    try {
        if (userService.getLoginUser() == null) {
            throw new IllegalArgumentException("Bạn cần đăng nhập để xóa nhà xuất bản");
        }
        if (!userService.getLoginUser().getRole().equals(Roles.admin)) {
            throw new SecurityException("Bạn không có quyền");
        }
        boolean result = publisherDAO.deletePublisherById(publisherId);
        if (result) {
            logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Xóa nhà xuất bản ID: " + publisherId));
        }
        return result;
    } catch (SQLException e) {
        System.out.println("Lỗi cơ sở dữ liệu trong quá trình xóa nhà xuất bản: " + e.getMessage());
        return false;
    } catch (IllegalArgumentException | SecurityException e) {
        System.out.println("Lỗi xóa nhà xuất bản: " + e.getMessage());
        return false;
    }
}

// Tìm nhà xuất bản dựa trên ID
public PublisherEntity findPublisherById(int publisherId) {
    try {
        if (userService.getLoginUser() == null) {
            throw new IllegalArgumentException("Bạn cần đăng nhập để xem thông tin nhà xuất bản");
        }
        PublisherEntity publisher = publisherDAO.findPublisherById(publisherId);
        logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Xem thông tin nhà xuất bản ID: " + publisherId));
        return publisher;
    } catch (SQLException e) {
        System.out.println("Lỗi cơ sở dữ liệu trong quá trình tìm nhà xuất bản: " + e.getMessage());
        return null;
    } catch (IllegalArgumentException e) {
        System.out.println("Lỗi tìm nhà xuất bản: " + e.getMessage());
        return null;
    }
}

public PublisherEntity findPublisherByName(String publisherName) {
    try {
        if (userService.getLoginUser() == null) {
            throw new IllegalArgumentException("Bạn cần đăng nhập để xem thông tin nhà xuất bản");
        }
        PublisherEntity publisher = publisherDAO.findPublisherByName(publisherName);
        logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Xem thông tin nhà xuất bản tên: " + publisherName));
        return publisher;
    } catch (SQLException e) {
        System.out.println("Lỗi cơ sở dữ liệu trong quá trình tìm nhà xuất bản: " + e.getMessage());
        return null;
    } catch (IllegalArgumentException e) {
        System.out.println("Lỗi tìm nhà xuất bản: " + e.getMessage());
        return null;
    }
}

// Lấy tất cả các nhà xuất bản
public List<PublisherEntity> getAllPublishers() {
    try {
        if (userService.getLoginUser() == null) {
            throw new IllegalArgumentException("Bạn cần đăng nhập để lấy danh sách nhà xuất bản");
        }
        List<PublisherEntity> publishers = publisherDAO.getAllPublishers();
        logService.addLog(new LogEntity(LocalDateTime.now(), userService.getLoginUser().getName(), "Lấy danh sách tất cả các nhà xuất bản"));
        return publishers;
    } catch (SQLException e) {
        System.out.println("Lỗi cơ sở dữ liệu trong quá trình lấy danh sách nhà xuất bản: " + e.getMessage());
        return null;
    } catch (IllegalArgumentException e) {
        System.out.println("Lỗi lấy danh sách nhà xuất bản: " + e.getMessage());
        return null;
    }
}
}
