module com.kopniaev {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires com.fasterxml.jackson.annotation;
    requires java.rmi;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;

    opens com.kopniaev to javafx.fxml;
    exports com.kopniaev;
    exports com.kopniaev.util;
    exports com.kopniaev.repository;
    exports com.kopniaev.model;
}
