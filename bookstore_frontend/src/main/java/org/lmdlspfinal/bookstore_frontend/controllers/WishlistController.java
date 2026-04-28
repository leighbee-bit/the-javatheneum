package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

import java.net.URL;
import java.util.ResourceBundle;

public class WishlistController implements Initializable {

    @FXML private VBox wishlistContainer;
    @FXML private Label statusLabel;
    @FXML private Label clockLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startClock();
        loadWishlist();
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

    private void loadWishlist() {
        try {
            Long wishlistId = BookstoreApplication.currentWishlistId;

            if (wishlistId == null) {
                statusLabel.setText("Your wishlist is empty! Add books from the home screen.");
                return;
            }

            String itemsResponse = ApiService.get("/api/wishlist-items/wishlist/" + wishlistId);
            JsonNode items = mapper.readTree(itemsResponse);

            if (items.size() == 0) {
                statusLabel.setText("Your wishlist is empty! Add books from the home screen.");
                return;
            }

            statusLabel.setText(items.size() + " books in your wishlist");
            wishlistContainer.getChildren().clear();

            for (JsonNode item : items) {
                JsonNode book = item.get("book");
                if (book != null) {
                    Long wishlistItemId = item.get("wishlistitemid").asLong();
                    wishlistContainer.getChildren().add(createWishlistCard(book, wishlistItemId));
                }
            }

        } catch (Exception e) {
            statusLabel.setText("Failed to load wishlist: " + e.getMessage());
        }
    }

    private HBox createWishlistCard(JsonNode book, Long wishlistItemId) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #d4d0c8; " +
                "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                "-fx-border-width: 2;");

        // Cover image
        ImageView cover = new ImageView();
        cover.setFitWidth(60);
        cover.setFitHeight(80);
        cover.setPreserveRatio(true);
        if (book.has("imageUrl") && !book.get("imageUrl").asText().isEmpty()) {
            try {
                Image image = new Image(book.get("imageUrl").asText(), 60, 80, true, true, true);
                cover.setImage(image);
            } catch (Exception e) {
                System.out.println("Image load failed: " + e.getMessage());
            }
        }

        // Book info
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label title = new Label(book.has("title") ? book.get("title").asText() : "Unknown");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #000080;");

        Label author = new Label(book.has("author") ? "by " + book.get("author").asText() : "");
        author.setStyle("-fx-font-size: 11; -fx-text-fill: #444;");

        Label price = new Label(book.has("price") ? "$" + book.get("price").asText() : "");
        price.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #800000;");

        info.getChildren().addAll(title, author, price);

        org.lmdlspfinal.bookstore_frontend.models.Book bookModel = jsonToBook(book);

        card.setStyle("-fx-background-color: #d4d0c8; " +
                "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                "-fx-border-width: 2; -fx-cursor: hand;");

        card.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                try {
                    BookstoreApplication.navigationHistory.push("wishlist");
                    BookstoreApplication.showBookDetail(bookModel);
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        });

        // Remove button
        Button removeBtn = new Button("✕ Remove");
        removeBtn.setStyle("-fx-background-color: #d4d0c8; " +
                "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                "-fx-border-width: 2; -fx-text-fill: #cc0000; -fx-font-weight: bold;");
        removeBtn.setOnAction(e -> removeFromWishlist(wishlistItemId));

        card.getChildren().addAll(cover, info, removeBtn);
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
    private void handleHome() {
        try {
            BookstoreApplication.showHome();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private org.lmdlspfinal.bookstore_frontend.models.Book jsonToBook(JsonNode book) {
        org.lmdlspfinal.bookstore_frontend.models.Book b = new org.lmdlspfinal.bookstore_frontend.models.Book();
        b.setBookid(book.has("bookid") ? book.get("bookid").asLong() : null);
        b.setTitle(book.has("title") ? book.get("title").asText() : "Unknown");
        b.setAuthor(book.has("author") ? book.get("author").asText() : "Unknown");
        b.setPrice(book.has("price") ? new java.math.BigDecimal(book.get("price").asText()) : null);
        b.setImageUrl(book.has("imageUrl") ? book.get("imageUrl").asText() : null);
        b.setDescription(book.has("description") ? book.get("description").asText() : null);
        b.setPageCount(book.has("pageCount") ? book.get("pageCount").asInt() : null);
        b.setPreviewLink(book.has("previewLink") ? book.get("previewLink").asText() : null);
        return b;
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