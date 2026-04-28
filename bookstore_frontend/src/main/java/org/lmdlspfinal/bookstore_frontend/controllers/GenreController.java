package org.lmdlspfinal.bookstore_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.lmdlspfinal.bookstore_frontend.BookstoreApplication;
import org.lmdlspfinal.bookstore_frontend.models.Book;
import org.lmdlspfinal.bookstore_frontend.services.ApiService;

public class GenreController {

    @FXML private FlowPane bookGrid;
    @FXML private Label statusLabel;
    @FXML private Text titleText;

    private final ObjectMapper mapper = new ObjectMapper();
    private String currentGenre;

    @FXML private Label clockLabel;

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

    public void setGenre(String genre) {
        this.currentGenre = genre;
        titleText.setText("📚 " + genre);
        loadBooks();
    }

    private void loadBooks() {
        try {
            String response = ApiService.get("/api/books/genre/" + currentGenre.replace(" ", "%20"));
            Book[] books = mapper.readValue(response, Book[].class);
            statusLabel.setText(books.length + " books in " + currentGenre);
            bookGrid.getChildren().clear();
            for (Book book : books) {
                bookGrid.getChildren().add(createBookCard(book));
            }
        } catch (Exception e) {
            statusLabel.setText("Failed to load: " + e.getMessage());
        }
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(8));
        card.setPrefWidth(140);
        card.setPrefHeight(240);
        card.setStyle("-fx-background-color: #d4d0c8; " +
                "-fx-border-color: #ffffff #808080 #808080 #ffffff; " +
                "-fx-border-width: 2; -fx-cursor: hand;");

        ImageView thumbnail = new ImageView();
        thumbnail.setFitWidth(120);
        thumbnail.setFitHeight(140);
        thumbnail.setPreserveRatio(true);

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(book.getImageUrl(), 120, 140, true, true, true);
                thumbnail.setImage(image);
            } catch (Exception e) {
                System.out.println("Image load failed: " + e.getMessage());
            }
        }

        Text title = new Text(book.getTitle());
        title.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-fill: #000080;");
        title.setWrappingWidth(120);

        Text author = new Text(book.getAuthor());
        author.setStyle("-fx-font-size: 10; -fx-fill: #444;");
        author.setWrappingWidth(120);

        Text price = new Text("$" + book.getPrice());
        price.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-fill: #800000;");

        card.getChildren().addAll(thumbnail, title, author, price);

        card.setOnMouseClicked(e -> {
            try {
                if (currentGenre != null) {
                    BookstoreApplication.navigationHistory.push("genre:" + currentGenre);
                }
                BookstoreApplication.showBookDetail(book);
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        return card;
    }

    public void setSearchQuery(String query) {
        this.currentGenre = null;
        titleText.setText("🔍 Search: " + query);
        try {
            String response = ApiService.get("/api/books/search/title?title=" + query);
            Book[] books = mapper.readValue(response, Book[].class);

            if (books.length == 0) {
                response = ApiService.get("/api/books/search/author?author=" + query);
                books = mapper.readValue(response, Book[].class);
            }

            statusLabel.setText(books.length + " results for \"" + query + "\"");
            bookGrid.getChildren().clear();
            for (Book book : books) {
                bookGrid.getChildren().add(createBookCard(book));
            }
        } catch (Exception e) {
            statusLabel.setText("Search failed: " + e.getMessage());
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