package org.lmdlspfinal.bookstore_frontend.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class LoadingController {

    @FXML private ProgressBar loadingBar;
    @FXML private Text loadingText;
    @FXML private Text runningMan;

    private Runnable onComplete;
    private static LoadingController instance;

    public static LoadingController getInstance() { return instance; }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    @FXML
    public void initialize() {
        instance = this;
        loadingBar.setProgress(0);
        runningMan.setText("☞");
        loadingText.setText("Connecting...");
    }

    public void updateProgress(double progress, String message) {
        Platform.runLater(() -> {
            loadingBar.setProgress(progress);
            loadingText.setText(message);
            runningMan.setText(progress > 0.5 ? "♟" : "☞");
            if (progress >= 1.0) {
                loadingText.setText("Done!");
                if (onComplete != null) onComplete.run();
            }
        });
    }
}