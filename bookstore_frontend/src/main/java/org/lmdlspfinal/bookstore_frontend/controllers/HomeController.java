package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.Book;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {

    @FXML private VBox mainContainer;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private final ObjectMapper mapper = new ObjectMapper();
    private Long currentWishlistId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCurrentWishlist();
        loadCarousels();
    }

    private void loadCurrentWishlist() {
        try {
            String wishlistResponse = ApiService.get("/api/wishlists/user/" + BookstoreApplication.loggedInUserId);
            JsonNode wishlistNode = mapper.readTree(wishlistResponse);
            if (wishlistNode.isArray() && wishlistNode.size() > 0) {
                currentWishlistId = wishlistNode.get(0).get("wishlistid").asLong();
                BookstoreApplication.currentWishlistId = currentWishlistId;
            }
        } catch (Exception e) {
            System.out.println("No wishlist found: " + e.getMessage());
        }
    }

    private void loadCarousels() {
        mainContainer.getChildren().clear();

        try {
            // Popular row first
            String popularResponse = ApiService.get("/api/books/popular");
            Book[] popularBooks = mapper.readValue(popularResponse, Book[].class);
            if (popularBooks.length > 0) {
                mainContainer.getChildren().add(createCarouselSection("⭐ Most Popular", popularBooks));
            }

            // Genre carousels
            String[] genres = {"Fiction", "Science Fiction", "Mystery", "Romance", "Biography", "Fantasy", "History"};
            for (String genre : genres) {
                String response = ApiService.get("/api/books/genre/" + genre.replace(" ", "%20"));
                Book[] books = mapper.readValue(response, Book[].class);
                if (books.length > 0) {
                    mainContainer.getChildren().add(createCarouselSection("📖 " + genre, books));
                }
            }

        } catch (Exception e) {
            statusLabel.setText("Error loading books: " + e.getMessage());
        }
    }

    private VBox createCarouselSection(String title, Book[] books) {
        VBox section = new VBox(8);
        section.setStyle("-fx-background-color: transparent;");

        // Section title bar with More button
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setStyle("-fx-background-color: linear-gradient(to right, #000080, #b0c4e8); " +
                "-fx-padding: 4 10;");
        Text sectionTitle = new Text(title);
        sectionTitle.setStyle("-fx-fill: white; -fx-font-size: 13; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleBar.getChildren().addAll(sectionTitle, spacer);

        // Add "More" button if more than 10 books and not a search/popular row
        if (books.length > 10 && !title.contains("🔍") && !title.contains("⭐")) {
            String genre = title.replace("📖 ", "");
            Button moreBtn = new Button("More ▶");
            moreBtn.setStyle("-fx-background-color: #f4a0c0; " +
                    "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                    "-fx-border-width: 1; -fx-font-size: 10; -fx-padding: 2 8;");
            moreBtn.setOnAction(e -> {
                try {
                    BookstoreApplication.showGenre(genre);
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            });
            titleBar.getChildren().add(moreBtn);
        }

        // Only show first 10 books in carousel
        Book[] displayBooks = books.length > 10 ? Arrays.copyOfRange(books, 0, 10) : books;

        HBox carousel = new HBox(10);
        carousel.setPadding(new Insets(10));
        carousel.setMinHeight(250);
        carousel.setMaxHeight(250);
        carousel.setStyle("-fx-background-color: rgba(255,255,255,0.3);");

        for (Book book : displayBooks) {
            carousel.getChildren().add(createBookCard(book));
        }

        ScrollPane scrollPane = new ScrollPane(carousel);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(270); // card height (250) + padding (10 top + 10 bottom)
        scrollPane.setMinHeight(270);
        scrollPane.setMaxHeight(270);
        scrollPane.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: #808080 #ffffff #ffffff #808080; -fx-border-width: 1;");

        section.getChildren().addAll(titleBar, scrollPane);
        return section;
    }

    @FXML
    private void handleHome() {
        try {
            BookstoreApplication.showHome();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private VBox createBookCard(Book book) {
        VBox card = new VBox(6);
        card.setPrefWidth(140);
        card.setPrefHeight(250);
        card.setMinHeight(250);
        card.setMaxHeight(250);
        card.setStyle("-fx-background-color: #d4d0c8; " +
                "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                "-fx-border-width: 2; -fx-cursor: hand;");

        // Thumbnail
        ImageView thumbnail = new ImageView();
        thumbnail.setFitWidth(120);
        thumbnail.setFitHeight(140);
        thumbnail.setPreserveRatio(true);

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(book.getImageUrl(), 120, 140, true, true, true);
                thumbnail.setImage(image);
            } catch (Exception e) {
                setPlaceholderImage(thumbnail);
            }
        } else {
            setPlaceholderImage(thumbnail);
        }

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #000080;");
        title.setPrefWidth(120);
        title.setPrefHeight(30);
        title.setMaxHeight(30);
        title.setWrapText(true);

        Label author = new Label(book.getAuthor());
        author.setStyle("-fx-font-size: 10; -fx-text-fill: #444;");
        author.setPrefWidth(120);
        author.setPrefHeight(20);
        author.setMaxHeight(20);
        author.setWrapText(false);

        Label price = new Label("$" + book.getPrice());
        price.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #800000;");
        price.setPrefHeight(20);
        price.setMaxHeight(20);

        boolean inWishlist = isInWishlist(book.getBookid());
        Button wishlistBtn = new Button(inWishlist ? "✓ Saved" : "+ Wishlist");
        wishlistBtn.setPrefWidth(120);
        wishlistBtn.setStyle(inWishlist ?
                "-fx-background-color: #f4a0c0; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 1; -fx-font-size: 10;" :
                "-fx-background-color: #d4d0c8; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 1; -fx-font-size: 10;");
        wishlistBtn.setDisable(inWishlist);

        if (!inWishlist) {
            wishlistBtn.setOnAction(e -> {
                addToWishlist(book);
                wishlistBtn.setText("✓ Saved");
                wishlistBtn.setStyle("-fx-background-color: #f4a0c0; -fx-border-color: #ffffff #808080 #808080 #ffffff; -fx-border-width: 1; -fx-font-size: 10;");
                wishlistBtn.setDisable(true);
                e.consume();
            });
        }

        card.getChildren().addAll(thumbnail, title, author, price, wishlistBtn);

        card.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                try {
                    BookstoreApplication.navigationHistory.push("home");
                    BookstoreApplication.showBookDetail(book);
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        });

        return card;
    }

    private void setPlaceholderImage(ImageView imageView) {
        imageView.setStyle("-fx-background-color: #d4d0c8;");
    }

    @FXML
    private void handleOrders() {
        try {
            BookstoreApplication.showOrders();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private boolean isInWishlist(Long bookId) {
        try {
            if (currentWishlistId == null) return false;
            String itemsResponse = ApiService.get("/api/wishlist-items/wishlist/" + currentWishlistId);
            JsonNode items = mapper.readTree(itemsResponse);
            for (JsonNode item : items) {
                if (item.get("book").get("bookid").asLong() == bookId) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void addToWishlist(Book book) {
        try {
            if (currentWishlistId == null) {
                String newWishlist = ApiService.post("/api/wishlists",
                        "{\"user\":{\"userid\":" + BookstoreApplication.loggedInUserId + "}}");
                JsonNode newNode = mapper.readTree(newWishlist);
                currentWishlistId = newNode.get("wishlistid").asLong();
                BookstoreApplication.currentWishlistId = currentWishlistId;
            }

            String json = String.format(
                    "{\"wishlist\":{\"wishlistid\":%d},\"book\":{\"bookid\":%d}}",
                    currentWishlistId, book.getBookid()
            );
            ApiService.post("/api/wishlist-items", json);
            statusLabel.setText("✓ Added \"" + book.getTitle() + "\" to wishlist!");
        } catch (Exception e) {
            statusLabel.setText("Failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;
        try {
            BookstoreApplication.showSearchResults(query);
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
    @FXML
    private void handleWishlist() {
        try {
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
            BookstoreApplication.currentWishlistId = null;
            BookstoreApplication.showLogin();
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}