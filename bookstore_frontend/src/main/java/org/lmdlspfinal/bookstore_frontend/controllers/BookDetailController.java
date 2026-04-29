package org.lmdlspfinal.bookstore_frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.Book;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BookDetailController {

    @FXML private ImageView bookCover;
    @FXML private Text titleText;
    @FXML private Text authorText;
    @FXML private Text priceText;
    @FXML private Text pageCountText;
    @FXML private TextArea descriptionText;
    @FXML private Button wishlistBtn;
    @FXML private Button previewBtn;
    @FXML private Label statusLabel;
    @FXML private Label clockLabel;
    @FXML private Button buyBtn;
    @FXML private Text windowTitle;

    private Book currentBook;

    @FXML
    public void initialize() {
        startClock();
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

    public void setBook(Book book) {
        this.currentBook = book;

        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        priceText.setText("$" + book.getPrice());
        pageCountText.setText(book.getPageCount() != null ? book.getPageCount() + " pages" : "N/A");
        descriptionText.setText(book.getDescription() != null ? book.getDescription() : "No description available.");

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(book.getImageUrl(), 180, 250, true, true, true);
                bookCover.setImage(image);
            } catch (Exception e) {
                System.out.println("Could not load image: " + e.getMessage());
            }
        }

        if (book.getPreviewLink() == null || book.getPreviewLink().isEmpty()) {
            previewBtn.setDisable(true);
            previewBtn.setText("No Preview Available");
        }

        updateWishlistButton();
    }

    @FXML
    private void handleOrders() {
        try {
            BookstoreApplication.navigationHistory.push("bookdetail");
            BookstoreApplication.showOrders();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void updateWishlistButton() {
        try {
            if (BookstoreApplication.currentWishlistId != null) {
                String itemsResponse = ApiService.get("/api/wishlist-items/wishlist/" + BookstoreApplication.currentWishlistId);
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode items = mapper.readTree(itemsResponse);
                for (com.fasterxml.jackson.databind.JsonNode item : items) {
                    if (item.get("book").get("bookid").asLong() == currentBook.getBookid()) {
                        wishlistBtn.setText("✓ In Wishlist");
                        wishlistBtn.setStyle("-fx-background-color: #f4a0c0; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 2; -fx-padding: 5 10;");
                        wishlistBtn.setDisable(true);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking wishlist: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddToWishlist() {
        try {
            if (BookstoreApplication.currentWishlistId == null) {
                String newWishlist = ApiService.post("/api/wishlists",
                        "{\"user\":{\"userid\":" + BookstoreApplication.loggedInUserId + "}}");
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(newWishlist);
                BookstoreApplication.currentWishlistId = node.get("wishlistid").asLong();
            }

            String json = String.format(
                    "{\"wishlist\":{\"wishlistid\":%d},\"book\":{\"bookid\":%d}}",
                    BookstoreApplication.currentWishlistId, currentBook.getBookid()
            );
            ApiService.post("/api/wishlist-items", json);
            wishlistBtn.setText("✓ In Wishlist");
            wishlistBtn.setStyle("-fx-background-color: #f4a0c0; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 2; -fx-padding: 5 10;");
            wishlistBtn.setDisable(true);
            statusLabel.setText("✓ Added to wishlist!");
        } catch (Exception e) {
            statusLabel.setText("Failed: " + e.getMessage());
        }
    }

    @FXML
    private void handlePreview() {
        try {
            javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
            webView.getEngine().load(currentBook.getPreviewLink());

            VBox container = new VBox();
            container.setStyle("-fx-background-color: #d4d0c8;");

            HBox titleBar = new HBox();
            titleBar.setStyle("-fx-background-color: linear-gradient(to right, #000080, #1084d0); -fx-padding: 4 6;");
            Text title = new Text("📖 Book Preview - " + currentBook.getTitle());
            title.setStyle("-fx-fill: white; -fx-font-size: 11; -fx-font-weight: bold;");
            Button closeBtn = new Button("✕ Close");
            closeBtn.setStyle("-fx-background-color: #d4d0c8; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 1;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            titleBar.getChildren().addAll(title, spacer, closeBtn);

            VBox.setVgrow(webView, Priority.ALWAYS);
            container.getChildren().addAll(titleBar, webView);

            javafx.scene.Scene previewScene = new javafx.scene.Scene(container, 1000, 700);

            javafx.stage.Stage previewStage = new javafx.stage.Stage();
            previewStage.setTitle("Book Preview");
            previewStage.setScene(previewScene);
            previewStage.show();

            closeBtn.setOnAction(e -> previewStage.close());

        } catch (Exception e) {
            statusLabel.setText("Could not open preview: " + e.getMessage());
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

    @FXML
    private void handleBuyNow() {
        try {
            // Create order
            String orderJson = String.format(
                    "{\"user\":{\"userid\":%d},\"totalPrice\":%s,\"status\":\"completed\"}",
                    BookstoreApplication.loggedInUserId,
                    currentBook.getPrice().toString()
            );
            String orderResponse = ApiService.post("/api/orders", orderJson);

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode orderNode = mapper.readTree(orderResponse);
            Long orderId = orderNode.get("orderid").asLong();

            // Add order item
            String itemJson = String.format(
                    "{\"order\":{\"orderid\":%d},\"book\":{\"bookid\":%d},\"quantity\":1,\"price\":%s}",
                    orderId, currentBook.getBookid(), currentBook.getPrice().toString()
            );
            ApiService.post("/api/order-items", itemJson);

            statusLabel.setText("✓ Order placed successfully!");
            buyBtn.setText("✓ Purchased");
            buyBtn.setStyle("-fx-background-color: #006600; -fx-text-fill: white; " +
                    "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                    "-fx-border-width: 2; -fx-padding: 5 10;");
            buyBtn.setDisable(true);
        } catch (Exception e) {
            statusLabel.setText("Order failed: " + e.getMessage());
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

    @FXML
    private void handleWishlist() {
        try {
            BookstoreApplication.showWishlist();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}