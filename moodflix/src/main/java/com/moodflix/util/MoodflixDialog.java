package com.moodflix.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MoodflixDialog {
    public static void showInfo(String title, String message) {
        showDialog(title, message, "#764ba2", "🎬");
    }
    public static void showSuccess(String title, String message) {
        showDialog(title, message, "#28a745", "✅");
    }
    public static void showError(String title, String message) {
        showDialog(title, message, "#ff3c3c", "❌");
    }
    private static void showDialog(String title, String message, String accentColor, String icon) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle(title);

        VBox root = new VBox(18);
        root.setPadding(new Insets(28, 32, 28, 32));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #23272f; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #764ba2, 16, 0, 0, 2); -fx-border-radius: 18; -fx-border-width: 0;");
        root.setEffect(new DropShadow(18, Color.web("#764ba2", 0.18)));

        HBox iconBar = new HBox(10);
        iconBar.setAlignment(Pos.CENTER);
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        iconLabel.setTextFill(Color.web(accentColor));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web(accentColor));
        iconBar.getChildren().addAll(iconLabel, titleLabel);

        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("Arial", 15));
        msgLabel.setTextFill(Color.web("#f3f3f3"));
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("OK");
        closeBtn.setStyle("-fx-background-color: linear-gradient(to right, #764ba2, #667eea); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 8 28; -fx-font-size: 15;");
        closeBtn.setOnAction(e -> dialog.close());
        closeBtn.setDefaultButton(true);

        root.getChildren().addAll(iconBar, msgLabel, closeBtn);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.sizeToScene();
        dialog.showAndWait();
    }
} 