package org.lmdlspfinal.bookstore_frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookstoreApplication extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setTitle("Javatheneum");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showRegister() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void showHome() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        primaryStage.setScene(scene);
    }

    public static void showWishlist() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("wishlist.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        primaryStage.setScene(scene);
    }

    public static Long loggedInUserId;
    public static Long currentWishlistId;

    public static void main(String[] args) {
        launch();
    }
}