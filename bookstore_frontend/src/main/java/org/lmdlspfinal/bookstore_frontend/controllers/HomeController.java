package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.Book;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private FlowPane bookGrid;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private final ObjectMapper mapper = new ObjectMapper();
    private Long currentWishlistId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCurrentWishlist();
        loadBooks();
    }

    private void loadCurrentWishlist() {
        try {
            String wishlistResponse = ApiService.get("/api/wishlists/user/" + BookstoreApplication.loggedInUserId);
            System.out.println("Wishlist response on login: " + wishlistResponse);
            JsonNode wishlistNode = mapper.readTree(wishlistResponse);
            if (wishlistNode.isArray() && wishlistNode.size() > 0) {
                currentWishlistId = wishlistNode.get(0).get("wishlistid").asLong();
                System.out.println("Found wishlist: " + currentWishlistId);
                BookstoreApplication.currentWishlistId = currentWishlistId;
            } else {
                System.out.println("No wishlist found for user: " + BookstoreApplication.loggedInUserId);
            }
        } catch (Exception e) {
            System.out.println("Error loading wishlist: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            String response = ApiService.get("/api/books");
            Book[] books = mapper.readValue(response, Book[].class);
            displayBooks(books);
            statusLabel.setText("Showing " + books.length + " books");
        } catch (Exception e) {
            statusLabel.setText("Failed to load books: " + e.getMessage());
        }
    }

    private void displayBooks(Book[] books) {
        bookGrid.getChildren().clear();
        for (Book book : books) {
            bookGrid.getChildren().add(createBookCard(book));
        }
    }

    private boolean isInWishlist(Long bookId) {
        try {
            if (currentWishlistId == null) return false;
            String itemsResponse = ApiService.get("/api/wishlist-items/wishlist/" + currentWishlistId);
            JsonNode items = mapper.readTree(itemsResponse);
            for (JsonNode item : items) {
                if (item.get("book").get("bookid").asLong() == bookId) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Text title = new Text(book.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        title.setWrappingWidth(170);

        Text author = new Text("by " + book.getAuthor());
        author.setStyle("-fx-font-size: 12; -fx-fill: #666;");

        Text price = new Text("$" + book.getPrice());
        price.setStyle("-fx-font-size: 13; -fx-fill: #6a0dad; -fx-font-weight: bold;");

        boolean alreadyInWishlist = isInWishlist(book.getBookid());
        Button wishlistBtn = new Button(alreadyInWishlist ? "In Wishlist ✓" : "+ Wishlist");
        wishlistBtn.setStyle(alreadyInWishlist ?
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11;" :
                "-fx-background-color: #6a0dad; -fx-text-fill: white; -fx-font-size: 11;");

        if (!alreadyInWishlist) {
            wishlistBtn.setOnAction(e -> {
                addToWishlist(book);
                wishlistBtn.setText("In Wishlist ✓");
                wishlistBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11;");
                wishlistBtn.setDisable(true);
            });
        } else {
            wishlistBtn.setDisable(true);
        }

        card.getChildren().addAll(title, author, price, wishlistBtn);
        return card;
    }

    private void addToWishlist(Book book) {
        try {
            String wishlistResponse = ApiService.get("/api/wishlists/user/" + BookstoreApplication.loggedInUserId);
            JsonNode wishlistNode = mapper.readTree(wishlistResponse);

            System.out.println("Wishlist response: " + wishlistResponse);

            Long wishlistId;
            if (wishlistNode.isArray() && wishlistNode.size() > 0) {
                currentWishlistId = wishlistNode.get(0).get("wishlistid").asLong();
            } else {
                String newWishlist = ApiService.post("/api/wishlists",
                        "{\"user\":{\"userid\":" + BookstoreApplication.loggedInUserId + "}}");
                JsonNode newNode = mapper.readTree(newWishlist);
                currentWishlistId = newNode.get("wishlistid").asLong();
            }
            wishlistId = currentWishlistId;

            String json = String.format(
                    "{\"wishlist\":{\"wishlistid\":%d},\"book\":{\"bookid\":%d}}",
                    wishlistId, book.getBookid()
            );

            String response = ApiService.post("/api/wishlist-items", json);
            System.out.println("Add item response: " + response);
            statusLabel.setText("✓ Added \"" + book.getTitle() + "\" to wishlist!");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to add to wishlist: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadBooks();
            return;
        }
        try {
            String response = ApiService.get("/api/books/search/title?title=" + query);
            Book[] books = mapper.readValue(response, Book[].class);
            displayBooks(books);
            statusLabel.setText("Found " + books.length + " results for \"" + query + "\"");
        } catch (Exception e) {
            statusLabel.setText("Search failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleWishlist() {
        try {
            // Make sure we have the wishlist ID
            if (currentWishlistId == null) {
                String wishlistResponse = ApiService.get("/api/wishlists/user/" + BookstoreApplication.loggedInUserId);
                JsonNode wishlistNode = mapper.readTree(wishlistResponse);
                if (wishlistNode.isArray() && wishlistNode.size() > 0) {
                    currentWishlistId = wishlistNode.get(0).get("wishlistid").asLong();
                }
            }
            BookstoreApplication.currentWishlistId = currentWishlistId;
            BookstoreApplication.showWishlist();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            BookstoreApplication.loggedInUserId = null;
            BookstoreApplication.showLogin();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}