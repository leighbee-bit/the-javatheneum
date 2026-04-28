package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.User;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields!");
            return;
        }

        try {
            String json = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\"}",
                    username, password
            );

            String response = ApiService.post("/api/users/login", json);

            if (response.contains("userid")) {
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(response, User.class);
                BookstoreApplication.loggedInUserId = user.getUserid();
                BookstoreApplication.showHome();
            } else {
                errorLabel.setText("Invalid username or password!");
            }
        } catch (Exception e) {
            errorLabel.setText("Invalid username or password!");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            BookstoreApplication.showRegister();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage() + " - " + e.getClass().getSimpleName());
        }
    }
}