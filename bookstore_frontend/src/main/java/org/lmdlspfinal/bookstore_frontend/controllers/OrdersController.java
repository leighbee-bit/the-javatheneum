package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    @FXML private VBox ordersContainer;
    @FXML private Label statusLabel;
    @FXML private Label clockLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startClock();
        loadOrders();
    }

    private void startClock() {
        javafx.animation.Timeline clock = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, e -> updateClock()),
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

    private void loadOrders() {
        try {
            String response = ApiService.get("/api/orders/user/" + BookstoreApplication.loggedInUserId);
            System.out.println("Orders response: " + response);

            if (response == null || response.trim().equals("[]") || response.trim().isEmpty()) {
                statusLabel.setText("No orders yet!");
                Label empty = new Label("You haven't placed any orders yet!");
                empty.setStyle("-fx-font-size: 13; -fx-text-fill: #000080;");
                ordersContainer.getChildren().add(empty);
                return;
            }

            JsonNode orders = mapper.readTree(response);
            System.out.println("Orders size: " + orders.size());

            if (!orders.isArray() || orders.size() == 0) {
                statusLabel.setText("No orders yet!");
                return;
            }

            statusLabel.setText(orders.size() + " order(s) found");

            for (JsonNode order : orders) {
                System.out.println("Order: " + order.toString());
                ordersContainer.getChildren().add(createOrderCard(order));
            }

        } catch (Exception e) {
            System.out.println("Full error: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Failed to load orders: " + e.getMessage());
        }
    }

    private VBox createOrderCard(JsonNode order) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #000080; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 12;");

        Long orderId = order.get("orderid").asLong();
        String status = order.has("status") ? order.get("status").asText() : "completed";
        String total = order.has("totalPrice") ? "$" + order.get("totalPrice").asText() : "N/A";
        String date = order.has("createdAt") ? order.get("createdAt").asText().substring(0, 10) : "N/A";

        HBox header = new HBox(10);
        Label orderLabel = new Label("Order #" + orderId);
        orderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #000080;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label statusLabel = new Label(status.toUpperCase());
        statusLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #006600; -fx-font-weight: bold;");
        header.getChildren().addAll(orderLabel, spacer, statusLabel);

        Label dateLabel = new Label("Date: " + date);
        dateLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #444;");

        Label totalLabel = new Label("Total: " + total);
        totalLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #800000;");

        // Load order items
        try {
            String itemsResponse = ApiService.get("/api/order-items/order/" + orderId);
            System.out.println("Items response for order " + orderId + ": " + itemsResponse);
            JsonNode items = mapper.readTree(itemsResponse);
            System.out.println("Items size: " + items.size());

            VBox itemsBox = new VBox(4);
            itemsBox.setStyle("-fx-padding: 8; -fx-background-color: white; " +
                    "-fx-border-color: #808080 #ffffff #ffffff #808080; -fx-border-width: 1;");

            Label itemsHeader = new Label("Items:");
            itemsHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");
            itemsBox.getChildren().add(itemsHeader);

            for (JsonNode item : items) {
                JsonNode book = item.get("book");
                if (book != null && !book.isNull()) {
                    String bookTitle = "Unknown Book";
                    if (book.has("title") && !book.get("title").isNull()) {
                        bookTitle = book.get("title").asText();
                    } else if (book.has("bookid")) {
                        // Fetch full book details if title is missing
                        try {
                            String bookResponse = ApiService.get("/api/books/" + book.get("bookid").asText());
                            com.fasterxml.jackson.databind.JsonNode fullBook = mapper.readTree(bookResponse);
                            bookTitle = fullBook.has("title") ? fullBook.get("title").asText() : "Unknown Book";
                        } catch (Exception ex) {
                            System.out.println("Could not fetch book: " + ex.getMessage());
                        }
                    }
                    String bookPrice = item.has("price") ? "$" + item.get("price").asText() : "";
                    Label itemLabel = new Label("• " + bookTitle + "  " + bookPrice);
                    itemLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #333;");
                    itemsBox.getChildren().add(itemLabel);
                }
            }

            card.getChildren().addAll(header, dateLabel, totalLabel, itemsBox);
        } catch (Exception e) {
            card.getChildren().addAll(header, dateLabel, totalLabel);
        }

        return card;
    }

    @FXML
    private void handleHome() {
        try {
            BookstoreApplication.showHome();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            BookstoreApplication.goBack();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}