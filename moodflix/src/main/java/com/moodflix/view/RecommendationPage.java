package com.moodflix.view;

import com.moodflix.model.Content;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;
import com.moodflix.service.PostgreSQLWatchlistService;
import com.moodflix.util.SessionManager;
import com.moodflix.util.ThemeManager;
import javafx.scene.control.Alert;

public class RecommendationPage {
    private VBox view;

    public RecommendationPage(List<Content> recommendations) {
        view = new VBox(28);
        view.setPadding(new Insets(40, 0, 40, 0));
        view.setAlignment(Pos.TOP_CENTER);
        view.getStyleClass().add("auth-container");

        Label title = new Label("Recommended for You");
        title.getStyleClass().add("hero-title");

        view.getChildren().add(title);

        HBox recList = new HBox(24);
        recList.setAlignment(Pos.CENTER_LEFT);
        recList.setPadding(new Insets(0, 40, 0, 40));

        for (Content c : recommendations) {
            VBox card = new VBox(0);
            card.setMinWidth(260);
            card.setMaxWidth(260);
            card.setPrefWidth(260);
            card.setMinHeight(340);
            card.setMaxHeight(340);
            card.setPrefHeight(340);
            card.setAlignment(Pos.BOTTOM_LEFT);
            card.getStyleClass().add("poster-card");
            card.setPadding(new Insets(0));
            card.setSpacing(0);

            // Poster image
            ImageView imgView = new ImageView();
            imgView.setFitWidth(260);
            imgView.setFitHeight(180);
            imgView.setPreserveRatio(false);
            imgView.setSmooth(true);
            if (c.getImageUrl() != null && !c.getImageUrl().isEmpty()) {
                try {
                    Image img = new Image(c.getImageUrl(), 260, 180, false, true);
                    imgView.setImage(img);
                } catch (Exception ex) {
                    // fallback: no image
                }
            } else {
                try {
                    imgView.setImage(new Image("https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=260&q=80", 260, 180, false, true));
                } catch (Exception ex) {
                    // fallback
                }
            }

            VBox overlay = new VBox(6);
            overlay.setAlignment(Pos.BOTTOM_LEFT);
            overlay.setPadding(new Insets(12, 16, 12, 16));

            Label contentTitle = new Label(c.getTitle());
            contentTitle.getStyleClass().add("section-title");
            contentTitle.setStyle("-fx-font-size: 16px;");

            Label contentType = new Label(c.getType() != null ? c.getType() : "");
            contentType.getStyleClass().add("label-accent");

            String desc = c.getDescription() != null ? c.getDescription() : "";
            if (desc.length() > 90) desc = desc.substring(0, 90) + "...";
            Label contentDesc = new Label(desc);
            contentDesc.getStyleClass().add("label-muted");
            contentDesc.setWrapText(true);
            contentDesc.setMaxWidth(220);

            overlay.getChildren().addAll(contentTitle, contentType, contentDesc);

            Button addToWatchlistBtn = new Button("★ Watchlist");
            addToWatchlistBtn.getStyleClass().addAll("btn", "btn-outline");
            addToWatchlistBtn.setOnAction(e -> {
                String userEmail = SessionManager.getEmail();
                if (userEmail == null || userEmail.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No user session found. Please login again.");
                    alert.showAndWait();
                    return;
                }
                new Thread(() -> {
                    try {
                        PostgreSQLWatchlistService service = new PostgreSQLWatchlistService();
                        service.addToWatchlist(userEmail, c.getTitle());
                        javafx.application.Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added to watchlist!");
                            alert.showAndWait();
                        });
                    } catch (Exception ex) {
                        javafx.application.Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add to watchlist: " + ex.getMessage());
                            alert.showAndWait();
                        });
                    }
                }).start();
            });
            addToWatchlistBtn.setMaxWidth(140);
            addToWatchlistBtn.setPrefWidth(140);

            // Card click for link
            String link = c.getLink();
            if (link != null && !link.isEmpty()) {
                card.setOnMouseClicked(e -> {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(link));
                        }
                    } catch (Exception ex) {
                        // Optionally show an error dialog
                    }
                });
            }

            VBox cardContent = new VBox(overlay, addToWatchlistBtn);
            cardContent.setAlignment(Pos.BOTTOM_LEFT);
            cardContent.setSpacing(10);
            cardContent.setPadding(new Insets(10, 0, 0, 0));

            card.getChildren().setAll(imgView, cardContent);
            HBox.setMargin(card, new Insets(0, 18, 0, 0));
            recList.getChildren().add(card);
        }

        ScrollPane scroll = new ScrollPane(recList);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefHeight(350);
        view.getChildren().add(scroll);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.getStyleClass().addAll("btn", "btn-outline");
        backBtn.setOnAction(e -> {
            String operationId = com.moodflix.util.PerformanceMonitor.startOperation("recommendation_back_navigation");
            String userEmail = com.moodflix.util.SessionManager.getEmail();
            com.moodflix.util.BackNavigationOptimizer.smartBackNavigation(userEmail);
            com.moodflix.util.PerformanceMonitor.endOperation(operationId, true);
        });
        view.getChildren().add(backBtn);

        ThemeManager.fadeIn(view, 400);
    }

    public VBox getView() {
        return view;
    }
} 