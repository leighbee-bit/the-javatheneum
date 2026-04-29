package org.lmdlspfinal.bookstore_frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lmdlspfinal.bookstore_frontend.controllers.BookDetailController;
import org.lmdlspfinal.bookstore_frontend.controllers.GenreController;
import org.lmdlspfinal.bookstore_frontend.controllers.HomeController;
import org.lmdlspfinal.bookstore_frontend.controllers.LoadingController;
import org.lmdlspfinal.bookstore_frontend.models.Book;
import org.lmdlspfinal.bookstore_frontend.controllers.OrdersController;

public class BookstoreApplication extends Application {

    public static Stage primaryStage;
    public static java.util.Stack<String> navigationHistory = new java.util.Stack<>();
    public static Book lastBook;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void showSearchResults(String query) throws Exception {
        navigationHistory.push("search:" + query);
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        GenreController controller = loader.getController();
        controller.setSearchQuery(query);
        primaryStage.setScene(scene);
    }

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setTitle("Javatheneum");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showLoading(Runnable onComplete) throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("loading.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        LoadingController controller = loader.getController();
        controller.setOnComplete(null);
        primaryStage.setScene(scene);

        // Run loading on background thread
        Thread thread = new Thread(onComplete);
        thread.setDaemon(true);
        thread.start();
    }

    public static void showRegister() throws Exception {
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void showHome() throws Exception {
        navigationHistory.clear(); // home clears history
        showLoading(() -> {
            try {
                LoadingController lc = LoadingController.getInstance();
                if (lc != null) lc.updateProgress(0.2, "Loading interface...");
                FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("home.fxml"));
                Scene scene = new Scene(loader.load(), 1000, 700);
                if (lc != null) lc.updateProgress(0.8, "Almost ready...");
                Thread.sleep(300);
                if (lc != null) lc.updateProgress(1.0, "Done!");
                Platform.runLater(() -> primaryStage.setScene(scene));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void showWishlist() throws Exception {
        navigationHistory.push("wishlist");
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("wishlist.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        primaryStage.setScene(scene);
    }

    public static void showOrders() throws Exception {
        navigationHistory.push("orders");
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("orders.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        primaryStage.setScene(scene);
    }

    public static void goBack() throws Exception {
        System.out.println("Navigation history: " + navigationHistory);
        System.out.println("Stack size: " + navigationHistory.size());

        if (navigationHistory.isEmpty()) {
            showHome();
            return;
        }

        String last = navigationHistory.pop();
        System.out.println("Popped: " + last);
        System.out.println("Remaining: " + navigationHistory);

        if (last.equals("wishlist")) {
            // Don't push to history again when going back
            FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("wishlist.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            primaryStage.setScene(scene);
        } else if (last.equals("bookdetail")) {
            // Check what's underneath bookdetail
            if (!navigationHistory.isEmpty()) {
                String previous = navigationHistory.peek();
                if (previous.equals("wishlist")) {
                    navigationHistory.pop();
                    FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("wishlist.fxml"));
                    Scene scene = new Scene(loader.load(), 1000, 700);
                    primaryStage.setScene(scene);
                } else if (previous.startsWith("genre:")) {
                    String genre = navigationHistory.pop().replace("genre:", "");
                    FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
                    Scene scene = new Scene(loader.load(), 1000, 700);
                    GenreController controller = loader.getController();
                    controller.setGenre(genre);
                    primaryStage.setScene(scene);
                } else if (previous.startsWith("search:")) {
                    String query = navigationHistory.pop().replace("search:", "");
                    FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
                    Scene scene = new Scene(loader.load(), 1000, 700);
                    GenreController controller = loader.getController();
                    controller.setSearchQuery(query);
                    primaryStage.setScene(scene);
                } else {
                    showHome();
                }
            } else {
                showHome();
            }
        } else if (last.startsWith("genre:")) {
            String genre = last.replace("genre:", "");
            FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            GenreController controller = loader.getController();
            controller.setGenre(genre);
            primaryStage.setScene(scene);
        } else if (last.startsWith("search:")) {
            String query = last.replace("search:", "");
            FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            GenreController controller = loader.getController();
            controller.setSearchQuery(query);
            primaryStage.setScene(scene);
        }  else if (last.equals("home")) {
        showHome();
        } else if (last.equals("orders")) {
            if (!navigationHistory.isEmpty()) {
                String previous = navigationHistory.peek();
                if (previous.equals("wishlist")) {
                    navigationHistory.pop();
                    FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("wishlist.fxml"));
                    Scene scene = new Scene(loader.load(), 1000, 700);
                    primaryStage.setScene(scene);
                } else if (previous.equals("bookdetail")) {
                    navigationHistory.pop();
                    if (lastBook != null) {
                        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("bookdetail.fxml"));
                        Scene scene = new Scene(loader.load(), 1000, 700);
                        BookDetailController controller = loader.getController();
                        controller.setBook(lastBook);
                        primaryStage.setScene(scene);
                    } else {
                        showHome();
                    }
                } else if (previous.startsWith("genre:")) {
                    String genre = navigationHistory.pop().replace("genre:", "");
                    FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
                    Scene scene = new Scene(loader.load(), 1000, 700);
                    GenreController controller = loader.getController();
                    controller.setGenre(genre);
                    primaryStage.setScene(scene);
                } else {
                    showHome();
                }
            } else {
                showHome();
            }
        }
    }

    public static void showBookDetail(Book book) throws Exception {
        navigationHistory.push("bookdetail");
        lastBook = book;
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("bookdetail.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        BookDetailController controller = loader.getController();
        controller.setBook(book);
        primaryStage.setScene(scene);
    }

    public static void showGenre(String genre) throws Exception {
        navigationHistory.push("genre:" + genre);
        FXMLLoader loader = new FXMLLoader(BookstoreApplication.class.getResource("genreview.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        GenreController controller = loader.getController();
        controller.setGenre(genre);
        primaryStage.setScene(scene);
    }


    public static Long loggedInUserId;
    public static Long currentWishlistId;

    static void main(String[] args) {
        launch();
    }
}