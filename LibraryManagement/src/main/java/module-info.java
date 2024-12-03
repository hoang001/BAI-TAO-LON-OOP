module org.example {

    /**
     * tác dụng: Khai báo các module mở và xuất khẩu cho FXML và các package khác sử dụng được các thư viện fxml, controls, và sql.
    */     
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.view;
    opens org.example.view to javafx.fxml;
    opens org.example.models;
    exports org.example.controllers;
    opens org.example.controllers to javafx.fxml;
}
