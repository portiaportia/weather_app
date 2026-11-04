module com.weather {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;


    opens com.weather to javafx.fxml;
    exports com.weather;
}
