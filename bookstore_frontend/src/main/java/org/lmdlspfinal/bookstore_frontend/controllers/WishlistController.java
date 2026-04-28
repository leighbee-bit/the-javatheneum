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

public class WishlistController implements Initializable {

    @FXML private VBox wishlistContainer;
    @FXML private Label statusLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadWishlist();
    }

    private void loadWishlist() {
        try {
            Long wishlistId = BookstoreApplication.currentWishlistId;

            if (wishlistId == null) {
                statusLabel.setText("Your wishlist is empty! Add books from the home screen.");
                return;
            }

            String itemsResponse = ApiService.get("/api/wishlist-items/wishlist/" + wishlistId);
            System.out.println("Items response: " + itemsResponse);
            JsonNode items = mapper.readTree(itemsResponse);

            if (items.size() == 0) {
                statusLabel.setText("Your wishlist is empty! Add books from the home screen.");
                return;
            }

            statusLabel.setText(items.size() + " books in your wishlist");
            wishlistContainer.getChildren().clear();

            for (JsonNode item : items) {
                System.out.println("Item: " + item.toString());
                JsonNode book = item.get("book");
                if (book != null) {
                    Long wishlistItemId = item.get("wishlistitemid").asLong();
                    wishlistContainer.getChildren().add(createWishlistCard(book, wishlistItemId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load wishlist: " + e.getMessage());
        }
    }

    private HBox createWishlistCard(JsonNode book, Long wishlistItemId) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Text title = new Text(book.has("title") ? book.get("title").asText() : "Unknown");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Text author = new Text(book.has("author") ? "by " + book.get("author").asText() : "");
        author.setStyle("-fx-font-size: 12; -fx-fill: #666;");

        Text price = new Text(book.has("price") ? "$" + book.get("price").asText() : "");
        price.setStyle("-fx-font-size: 13; -fx-fill: #6a0dad; -fx-font-weight: bold;");

        info.getChildren().addAll(title, author, price);

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #c92c2c; -fx-text-fill: white;");
        removeBtn.setOnAction(e -> removeFromWishlist(wishlistItemId));

        card.getChildren().addAll(info, removeBtn);
        return card;
    }

    private void removeFromWishlist(Long wishlistItemId) {
        try {
            ApiService.delete("/api/wishlist-items/" + wishlistItemId);
            javafx.application.Platform.runLater(() -> {
                wishlistContainer.getChildren().clear();
                statusLabel.setText("✓ Removed from wishlist!");
                loadWishlist();
            });
        } catch (Exception e) {
            statusLabel.setText("Failed to remove: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            BookstoreApplication.showHome();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}