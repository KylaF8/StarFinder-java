module com.example.ca1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ca1 to javafx.fxml;
    exports com.example.ca1;
}