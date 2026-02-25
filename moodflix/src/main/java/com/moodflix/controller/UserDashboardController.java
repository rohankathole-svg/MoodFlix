package com.moodflix.controller;

import com.moodflix.model.Content;
import com.moodflix.service.PostgreSQLContentService;
import com.moodflix.service.PostgreSQLWatchlistService;
import com.moodflix.service.PostgreSQLAuthService;
import com.moodflix.util.SessionManager;
import com.moodflix.view.UserDashboard;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import java.util.List;
import java.util.Iterator;
import com.moodflix.Main;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;

public class UserDashboardController {
    private final UserDashboard view;
    private List<Content> lastFetchedContentList;

    public UserDashboardController(UserDashboard view) {
        this.view = view;
        loadUserProfilePhoto();
        setupEventHandlers();
        updateTrendingCarousel();
        updateAchievements();
        setupSocialFeatures();
    }

    private void loadUserProfilePhoto() {
        String userEmail = com.moodflix.util.SessionManager.getEmail();
        if (userEmail == null) return;
        javafx.scene.image.ImageView profilePhotoView = view.getProfilePhotoView();
        
        // Use async operation with caching
        String operationId = com.moodflix.util.PerformanceMonitor.startOperation("profile_photo_load");
        
        com.moodflix.service.PostgreSQLAuthService service = new com.moodflix.service.PostgreSQLAuthService();
        service.getUserDetailsAsync(userEmail)
            .thenAcceptAsync(userObj -> {
                com.moodflix.util.PerformanceMonitor.endOperation(operationId, userObj != null);
                
                if (userObj != null && userObj.has("profilePicUrl") && !userObj.getString("profilePicUrl").isEmpty()) {
                    String photoUrl = userObj.getString("profilePicUrl");
                    javafx.scene.image.Image profileImage = new javafx.scene.image.Image(photoUrl);
                    javafx.application.Platform.runLater(() -> {
                        if (profilePhotoView != null && !profileImage.isError()) {
                            profilePhotoView.setImage(profileImage);
                        }
                    });
                }
            }).exceptionally(throwable -> {
                System.err.println("Error loading profile photo: " + throwable.getMessage());
                return null;
            });
    }

    private void setupEventHandlers() {
        // Modern recommendation UI event handlers
        Button moodRecBtn = view.getMoodRecBtn();
        Button generalRecBtn = view.getGeneralRecBtn();
        // smartRecBtn has been removed, do not reference it
        ComboBox<String> moodCombo = view.getMoodCombo();
        ComboBox<String> typeCombo = view.getTypeCombo();
        Button profileBtn = view.getProfileBtn();
        Button watchlistBtn = view.getWatchlistBtn();
        Button feedbackBtn = view.getFeedbackBtn();
        Button logoutBtn = view.getLogoutBtn();
        Button activityBtn = view.getActivityBtn();

        // Only set event handlers for moodRecBtn and generalRecBtn
        if (moodRecBtn != null) {
            System.out.println("[DEBUG] Setting up moodRecBtn event handler");
            moodRecBtn.setOnAction(e -> {
                System.out.println("[DEBUG] Mood recommendation button clicked!");
                String mood = moodCombo.getValue();
                if (mood == null || mood.isEmpty()) {
                    view.showRecommendationsError("Please select a mood.");
                    return;
                }
                view.showRecommendationsLoading();
                
                // Use performance monitoring
                String operationId = com.moodflix.util.PerformanceMonitor.startOperation("mood_recommendations");
                
                com.moodflix.util.PerformanceOptimizer.runAsync(() -> {
                    try {
                        PostgreSQLContentService service = new PostgreSQLContentService();
                        return service.getFilteredContentList(mood, null);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }).thenAcceptAsync(results -> {
                    com.moodflix.util.PerformanceMonitor.endOperation(operationId, true);
                    javafx.application.Platform.runLater(() -> view.showRecommendationsGrid(results));
                }).exceptionally(throwable -> {
                    com.moodflix.util.PerformanceMonitor.endOperation(operationId, false);
                    javafx.application.Platform.runLater(() -> view.showRecommendationsError(throwable.getMessage()));
                    return null;
                });
            });
        }
        if (generalRecBtn != null) {
            System.out.println("[DEBUG] Setting up generalRecBtn event handler");
            generalRecBtn.setOnAction(e -> {
                System.out.println("[DEBUG] General recommendation button clicked!");
                String type = typeCombo.getValue();
                if (type == null || type.isEmpty()) {
                    view.showRecommendationsError("Please select a type.");
                    return;
                }
                view.showRecommendationsLoading();
                
                // Use performance monitoring
                String operationId = com.moodflix.util.PerformanceMonitor.startOperation("type_recommendations");
                
                com.moodflix.util.PerformanceOptimizer.runAsync(() -> {
                    try {
                        PostgreSQLContentService service = new PostgreSQLContentService();
                        return service.getFilteredContentList(null, type);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }).thenAcceptAsync(results -> {
                    com.moodflix.util.PerformanceMonitor.endOperation(operationId, true);
                    javafx.application.Platform.runLater(() -> view.showRecommendationsGrid(results));
                }).exceptionally(throwable -> {
                    com.moodflix.util.PerformanceMonitor.endOperation(operationId, false);
                    javafx.application.Platform.runLater(() -> view.showRecommendationsError(throwable.getMessage()));
                    return null;
                });
            });
        }

        // Navigation and other button handlers with optimized click handling
        if (profileBtn != null) {
            com.moodflix.util.ClickOptimizer.setNavigationClickHandler(profileBtn, "ProfilePage", () -> {
                String userEmail = com.moodflix.util.SessionManager.getEmail();
                com.moodflix.view.ProfilePage profileView = new com.moodflix.view.ProfilePage(userEmail);
                com.moodflix.controller.ProfilePageController profileController = new com.moodflix.controller.ProfilePageController(profileView, userEmail);
                return new javafx.scene.Scene(profileView.getView());
            });
        }
        if (watchlistBtn != null) {
            com.moodflix.util.ClickOptimizer.setNavigationClickHandler(watchlistBtn, "WatchlistPage", () -> {
                try {
                    com.moodflix.view.WatchlistPage watchlistView = new com.moodflix.view.WatchlistPage();
                    com.moodflix.controller.WatchlistPageController watchlistController = new com.moodflix.controller.WatchlistPageController(watchlistView);
                    return new javafx.scene.Scene(watchlistView.getView());
                } catch (Exception ex) {
                    System.err.println("[ERROR] Exception in watchlist navigation: " + ex.getMessage());
                    ex.printStackTrace();
                    throw ex;
                }
            });
        }
        if (feedbackBtn != null) {
            com.moodflix.util.ClickOptimizer.setNavigationClickHandler(feedbackBtn, "FeedbackPage", () -> {
                com.moodflix.view.FeedbackPage feedbackView = new com.moodflix.view.FeedbackPage();
                com.moodflix.controller.FeedbackPageController feedbackController = new com.moodflix.controller.FeedbackPageController(feedbackView);
                return new javafx.scene.Scene(feedbackView.getView());
            });
        }
        if (logoutBtn != null) {
            logoutBtn.setOnAction(e -> {
                System.out.println("🚪 User logout button clicked");
                // Use centralized logout manager
                com.moodflix.util.LogoutManager.performLogoutWithStage(logoutBtn);
            });
        }
        if (activityBtn != null) {
            com.moodflix.util.ClickOptimizer.setNavigationClickHandler(activityBtn, "ActivityHistoryPage", () -> {
                try {
                    com.moodflix.view.ActivityHistoryPage activityView = new com.moodflix.view.ActivityHistoryPage();
                    com.moodflix.controller.ActivityHistoryController activityController = new com.moodflix.controller.ActivityHistoryController(activityView);
                    return new javafx.scene.Scene(activityView.getView());
                } catch (Exception ex) {
                    System.err.println("Error opening Activity History: " + ex.getMessage());
                    ex.printStackTrace();
                    throw ex;
                }
            });
        }

    }

    private void updateTrendingCarousel() {
        // Find the trending carousel HBox
        javafx.scene.layout.HBox trendingCarousel = (javafx.scene.layout.HBox) view.getView().lookup(".hbox");
        if (trendingCarousel == null) return;
        // Remove all except the title
        while (trendingCarousel.getChildren().size() > 1) trendingCarousel.getChildren().remove(1);
        // Fetch trending content (top 3 by title for now) in background
        new Thread(() -> {
            try {
                com.moodflix.service.PostgreSQLContentService service = new com.moodflix.service.PostgreSQLContentService();
                java.util.List<com.moodflix.model.Content> trending = service.getAllContent();
                // Sort alphabetically for now (simulate trending)
                trending.sort(java.util.Comparator.comparing(com.moodflix.model.Content::getTitle));
                java.util.List<com.moodflix.model.Content> top3 = trending.size() > 3 ? trending.subList(0, 3) : trending;
                javafx.application.Platform.runLater(() -> {
                    for (com.moodflix.model.Content c : top3) {
                        javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(5);
                        card.setAlignment(javafx.geometry.Pos.CENTER);
                        card.setPadding(new javafx.geometry.Insets(10));
                        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 4, 0, 0, 1);");
                        javafx.scene.image.ImageView img = new javafx.scene.image.ImageView();
                        if (c.getImageUrl() != null && !c.getImageUrl().isEmpty()) {
                            new Thread(() -> {
                                javafx.scene.image.Image image = new javafx.scene.image.Image(c.getImageUrl(), 80, 120, true, true);
                                javafx.application.Platform.runLater(() -> img.setImage(image));
                            }).start();
                        } else {
                            img.setImage(new javafx.scene.image.Image("https://images.unsplash.com/photo-1517602302552-471fe67acf66?auto=format&fit=crop&w=200&q=80", 80, 120, true, true));
                        }
                        img.setPreserveRatio(true);
                        Label title = new Label(c.getTitle());
                        title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
                        title.setTextFill(Color.web("#333333"));
                        card.getChildren().addAll(img, title);
                        trendingCarousel.getChildren().add(card);
                    }
                });
            } catch (Exception ex) {
                // Ignore, keep placeholders
            }
        }).start();
    }

    private void updateAchievements() {
        // Find badgesBox in achievementsSection
        VBox achievementsSection = (VBox) view.getView().lookup(".vbox");
        if (achievementsSection == null) return;
        // Find badgesBox (should be HBox)
        HBox badgesBox = null;
        for (Node node : achievementsSection.getChildren()) {
            if (node instanceof HBox) { badgesBox = (HBox) node; break; }
        }
        if (badgesBox == null) return;
        badgesBox.getChildren().clear();
        // Example: award badges based on stats
        int watched = getWatchlistCount();
        int feedbacks = getFeedbackCount();
        // Always show Newbie
        Label badge1 = new Label("🏅 Newbie");
        badge1.setStyle("-fx-background-color: #e1bee7; -fx-background-radius: 10; -fx-padding: 8 18; -fx-font-size: 14;");
        badgesBox.getChildren().add(badge1);
        if (watched >= 5) {
            Label badge2 = new Label("🎬 Movie Buff");
            badge2.setStyle("-fx-background-color: #ffe082; -fx-background-radius: 10; -fx-padding: 8 18; -fx-font-size: 14;");
            badgesBox.getChildren().add(badge2);
        }
        if (feedbacks >= 1) {
            Label badge3 = new Label("💬 Feedback Star");
            badge3.setStyle("-fx-background-color: #b2dfdb; -fx-background-radius: 10; -fx-padding: 8 18; -fx-font-size: 14;");
            badgesBox.getChildren().add(badge3);
        }
    }

    private int getWatchlistCount() {
        try {
            com.moodflix.service.PostgreSQLWatchlistService service = new com.moodflix.service.PostgreSQLWatchlistService();
            return service.getWatchlistCount(com.moodflix.util.SessionManager.getEmail());
        } catch (Exception ex) {}
        return 0;
    }
    private int getFeedbackCount() {
        // Placeholder: return 1 for demo; implement real feedback count if available
        return 1;
    }

    private void setupSocialFeatures() {
        // Remove all logic for social features
    }

    // Helper methods to show loading, grid, and error
    private void showRecommendationsLoading() {
        view.showRecommendationsLoading();
    }
    private void showRecommendationsGrid(List<Content> results) {
        view.showRecommendationsGrid(results);
    }
    private void showRecommendationsError(String message) {
        view.showRecommendationsError(message);
    }
} 