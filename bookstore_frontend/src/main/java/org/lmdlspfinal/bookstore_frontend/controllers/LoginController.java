package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.User;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML private Label clockLabel;

    @FXML
    public void initialize() {
        startClock();
    }

    private void startClock() {
        javafx.animation.Timeline clock = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.ZERO, e -> updateClock()),
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1))
        );
        clock.setCycleCount(javafx.animation.Animation.INDEFINITE);
        clock.play();
    }

    private void updateClock() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        clockLabel.setText(now.format(formatter));
    }

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

            System.out.println("Sending login request: " + json);
            String response = ApiService.post("/api/users/login", json);
            System.out.println("Login response: " + response);

            if (response.contains("userid")) {
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(response, User.class);
                BookstoreApplication.loggedInUserId = user.getUserid();
                BookstoreApplication.showHome();
            } else {
                errorLabel.setText("Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
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