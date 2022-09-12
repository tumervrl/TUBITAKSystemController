module com.example.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;
    requires java.desktop;


    opens com.example.login to javafx.fxml;
    exports com.example.login;
}