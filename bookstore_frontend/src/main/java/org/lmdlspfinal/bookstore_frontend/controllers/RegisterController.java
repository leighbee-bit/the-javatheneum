package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields!");
            return;
        }

        try {
            String json = String.format(
                    "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                    username, email, password
            );
            String response = ApiService.post("/api/users/register", json);
            successLabel.setText("Account created! Please login.");
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        try {
            BookstoreApplication.showLogin();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}