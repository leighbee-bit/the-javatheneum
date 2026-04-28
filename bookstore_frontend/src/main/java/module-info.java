module org.lmdlspfinal.bookstore_frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens org.lmdlspfinal.bookstore_frontend to javafx.fxml;
    opens org.lmdlspfinal.bookstore_frontend.controllers to javafx.fxml;
    opens org.lmdlspfinal.bookstore_frontend.models to com.fasterxml.jackson.databind;

    exports org.lmdlspfinal.bookstore_frontend;
    exports org.lmdlspfinal.bookstore_frontend.controllers;
    exports org.lmdlspfinal.bookstore_frontend.models;
}