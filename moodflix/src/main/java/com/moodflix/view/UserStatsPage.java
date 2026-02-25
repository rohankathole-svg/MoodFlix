package com.moodflix.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.moodflix.util.ThemeManager;

public class UserStatsPage {
    private VBox mainLayout;
    private Label totalActivitiesLabel;
    private Label totalWatchTimeLabel;
    private Label favoriteMoodLabel;
    private Label favoriteTypeLabel;
    private Label mostWatchedLabel;
    private Button backBtn;

    public UserStatsPage() {
        initializeUI();
    }

    private void initializeUI() {
        mainLayout = new VBox(28);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getStyleClass().add("auth-container");

        // Header
        VBox headerBox = new VBox(6);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20));
        headerBox.getStyleClass().add("glass-card");

        Label title = new Label("📈 User Stats");
        title.getStyleClass().add("hero-title");
        title.setStyle("-fx-font-size: 28px;");

        Label subtitle = new Label("Your entertainment analytics in real time");
        subtitle.getStyleClass().add("label-secondary");

        headerBox.getChildren().addAll(title, subtitle);

        // Stats Section
        VBox statsBox = new VBox(18);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(28));
        statsBox.getStyleClass().add("glass-card");

        totalActivitiesLabel = new Label("Total Activities: ...");
        totalActivitiesLabel.getStyleClass().add("stat-value");

        totalWatchTimeLabel = new Label("Total Watch Time: ...");
        totalWatchTimeLabel.getStyleClass().add("stat-value");

        favoriteMoodLabel = new Label("Favorite Mood: ...");
        favoriteMoodLabel.getStyleClass().add("stat-value");

        favoriteTypeLabel = new Label("Favorite Type: ...");
        favoriteTypeLabel.getStyleClass().add("stat-value");

        mostWatchedLabel = new Label("Most Watched: ...");
        mostWatchedLabel.getStyleClass().add("stat-value");

        statsBox.getChildren().addAll(totalActivitiesLabel, totalWatchTimeLabel, favoriteMoodLabel, favoriteTypeLabel, mostWatchedLabel);

        // Back Button
        backBtn = new Button("⬅️ Back to Dashboard");
        backBtn.getStyleClass().addAll("btn", "btn-outline");
        backBtn.setStyle("-fx-min-width: 180;");

        mainLayout.getChildren().addAll(headerBox, statsBox, backBtn);

        ThemeManager.fadeIn(mainLayout, 400);
        ThemeManager.slideUp(statsBox, 500, 200);
    }

    public VBox getView() { return mainLayout; }
    public Label getTotalActivitiesLabel() { return totalActivitiesLabel; }
    public Label getTotalWatchTimeLabel() { return totalWatchTimeLabel; }
    public Label getFavoriteMoodLabel() { return favoriteMoodLabel; }
    public Label getFavoriteTypeLabel() { return favoriteTypeLabel; }
    public Label getMostWatchedLabel() { return mostWatchedLabel; }
    public Button getBackBtn() { return backBtn; }
} 