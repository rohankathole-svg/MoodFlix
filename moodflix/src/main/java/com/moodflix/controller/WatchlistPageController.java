package com.moodflix.controller;

import com.moodflix.service.PostgreSQLWatchlistService;
import com.moodflix.util.SessionManager;
import com.moodflix.view.WatchlistPage;
import com.moodflix.view.AdminDashboard;
import com.moodflix.view.UserDashboard;
import com.moodflix.controller.AdminDashboardController;
import com.moodflix.controller.UserDashboardController;
import com.moodflix.Main;
import javafx.scene.control.*;
import javafx.scene.Scene;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class WatchlistPageController {
    private final WatchlistPage view;
    private List<JSONObject> watchlistItems;
    private String targetUserEmail; // For admin viewing specific user's watchlist

    public WatchlistPageController(WatchlistPage view) {
        this.view = view;
        this.watchlistItems = new ArrayList<>();
        this.targetUserEmail = null;
        setupEventHandlers();
        loadWatchlist();
    }
    
    // Constructor for admin viewing specific user's watchlist
    public WatchlistPageController(WatchlistPage view, String targetUserEmail) {
        this.view = view;
        this.watchlistItems = new ArrayList<>();
        this.targetUserEmail = targetUserEmail;
        setupEventHandlers();
        
        // Set admin mode header
        view.setAdminMode(targetUserEmail);
        
        loadUserWatchlist(targetUserEmail);
    }

    private void setupEventHandlers() {
        ListView<JSONObject> watchlistView = view.getWatchlistView();
        Button refreshBtn = view.getRefreshBtn();
        Button removeBtn = view.getRemoveBtn();
        Button clearAllBtn = view.getClearAllBtn();
        Button backBtn = view.getBackBtn();
        Label statusLabel = view.getStatusLabel();

        // Initially disable remove and clear buttons
        removeBtn.setDisable(true);
        clearAllBtn.setDisable(true);

        // Selection listener
        watchlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            removeBtn.setDisable(newVal == null);
        });

        // Remove selected item
        removeBtn.setOnAction(e -> {
            JSONObject selected = watchlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("🗑️ Remove selected item clicked");
                String title = selected.optString("title", "Unknown Title");
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove from Watchlist");
                alert.setHeaderText("Remove \"" + title + "\"?");
                alert.setContentText("Are you sure you want to remove this item from your watchlist?");
                
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        removeFromWatchlist(selected);
                    }
                });
            }
        });

        // Clear all items
        clearAllBtn.setOnAction(e -> {
            System.out.println("🗑️ Clear all items clicked");
            
            if (watchlistItems.isEmpty()) {
                statusLabel.setText("ℹ️ Your watchlist is already empty");
                statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
                return;
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Watchlist");
            alert.setHeaderText("Clear All Items?");
            alert.setContentText("Are you sure you want to remove all items from your watchlist?\n\n" +
                               "This action cannot be undone.");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    clearAllWatchlist();
                }
            });
        });

        // Refresh watchlist
        refreshBtn.setOnAction(e -> {
            System.out.println("🔄 Refresh watchlist clicked");
            loadWatchlist();
        });
        
        backBtn.setOnAction(e -> {
            System.out.println("\u2190 Back button clicked");
            String operationId = com.moodflix.util.PerformanceMonitor.startOperation("watchlist_back_navigation");
            
            // Use optimized back navigation
            String userEmail = com.moodflix.util.SessionManager.getEmail();
            com.moodflix.util.BackNavigationOptimizer.smartBackNavigation(userEmail);
            
            com.moodflix.util.PerformanceMonitor.endOperation(operationId, true);
        });
    }

    public void loadUserWatchlist(String targetEmail) {
        System.out.println("🔍 Loading watchlist for user: " + targetEmail);
        
        ListView<JSONObject> watchlistView = view.getWatchlistView();
        Label statusLabel = view.getStatusLabel();
        Label countLabel = view.getCountLabel();
        
        // Disable buttons during loading
        view.getRefreshBtn().setDisable(true);
        view.getRemoveBtn().setDisable(true);
        view.getClearAllBtn().setDisable(true);
        
        statusLabel.setText("🔄 Loading watchlist for " + targetEmail + "...");
        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
        
        // Load in background thread
        new Thread(() -> {
            try {
                watchlistView.getItems().clear();
                watchlistItems.clear();
                
                PostgreSQLWatchlistService service = new PostgreSQLWatchlistService();
                String json = service.getWatchlistJson(targetEmail);
                
                if (json == null || json.equals("null") || json.isEmpty()) {
                    javafx.application.Platform.runLater(() -> {
                        updateWatchlistDisplay(0);
                        statusLabel.setText("ℹ️ " + targetEmail + " has an empty watchlist");
                        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
                        view.getRefreshBtn().setDisable(false);
                        view.getRemoveBtn().setDisable(true); // Disable remove for admin viewing
                        view.getClearAllBtn().setDisable(true); // Disable clear for admin viewing
                    });
                    return;
                }
                
                JSONObject obj = new JSONObject(json);
                Iterator<String> keys = obj.keys();
                
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = obj.getJSONObject(key);
                    watchlistItems.add(item);
                }
                
                final int finalItemCount = watchlistItems.size();
                System.out.println("📋 Found " + finalItemCount + " items in " + targetEmail + "'s watchlist");
                
                javafx.application.Platform.runLater(() -> {
                    watchlistView.getItems().addAll(watchlistItems);
                    updateWatchlistDisplay(finalItemCount);
                    
                    if (finalItemCount > 0) {
                        statusLabel.setText(targetEmail + "'s watchlist loaded successfully!");
                        statusLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                    } else {
                        statusLabel.setText("ℹ️ " + targetEmail + " has an empty watchlist");
                        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
                    }
                    
                    view.getRefreshBtn().setDisable(false);
                    view.getRemoveBtn().setDisable(true); // Disable remove for admin viewing
                    view.getClearAllBtn().setDisable(true); // Disable clear for admin viewing
                });
                
            } catch (Exception ex) {
                System.err.println("Error loading watchlist for " + targetEmail + ": " + ex.getMessage());
                ex.printStackTrace();
                
                javafx.application.Platform.runLater(() -> {
                    watchlistView.getItems().clear();
                    updateWatchlistDisplay(0);
                    statusLabel.setText("Error loading " + targetEmail + "'s watchlist. Please try again.");
                    statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    view.getRefreshBtn().setDisable(false);
                    view.getRemoveBtn().setDisable(true); // Disable remove for admin viewing
                    view.getClearAllBtn().setDisable(true); // Disable clear for admin viewing
                    
                    // Show error dialog
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Load Failed");
                    alert.setHeaderText("Error Loading User Watchlist");
                    alert.setContentText("Unable to load " + targetEmail + "'s watchlist at this time.\n\n" +
                        "Error: " + ex.getMessage() + "\n\n" +
                        "Please check your connection and try again.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void loadWatchlist() {
        // Check if we're in admin mode viewing a specific user's watchlist
        if (targetUserEmail != null) {
            loadUserWatchlist(targetUserEmail);
            return;
        }
        
        System.out.println("🔍 Loading watchlist for: " + SessionManager.getEmail());
        
        ListView<JSONObject> watchlistView = view.getWatchlistView();
        Label statusLabel = view.getStatusLabel();
        Label countLabel = view.getCountLabel();
        
        // Disable buttons during loading
        view.getRefreshBtn().setDisable(true);
        view.getRemoveBtn().setDisable(true);
        view.getClearAllBtn().setDisable(true);
        
        statusLabel.setText("🔄 Loading your watchlist...");
        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
        
        // Load in background thread
        new Thread(() -> {
            try {
                System.out.println("[DEBUG] Starting watchlist load in background thread");
                watchlistView.getItems().clear();
                watchlistItems.clear();
                
                System.out.println("[DEBUG] Creating PostgreSQLWatchlistService...");
                PostgreSQLWatchlistService service = new PostgreSQLWatchlistService();
                System.out.println("[DEBUG] PostgreSQLWatchlistService created successfully");
                
                String userEmail = SessionManager.getEmail();
                System.out.println("[DEBUG] User email: " + userEmail);
                
                System.out.println("[DEBUG] Calling getWatchlist...");
                String json = service.getWatchlistJson(userEmail);
                System.out.println("[DEBUG] Retrieved watchlist JSON: " + (json != null ? json.substring(0, Math.min(100, json.length())) + "..." : "null"));
                
                if (json == null || json.equals("null") || json.isEmpty()) {
                    javafx.application.Platform.runLater(() -> {
                        updateWatchlistDisplay(0);
                        statusLabel.setText("ℹ️ Your watchlist is empty");
                        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
                        view.getRefreshBtn().setDisable(false);
                    });
                    return;
                }
                
                JSONObject obj = new JSONObject(json);
                Iterator<String> keys = obj.keys();
                
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject item = obj.getJSONObject(key);
                    watchlistItems.add(item);
                }
                
                final int finalItemCount = watchlistItems.size();
                System.out.println("📋 Found " + finalItemCount + " items in watchlist");
                
                javafx.application.Platform.runLater(() -> {
                    watchlistView.getItems().addAll(watchlistItems);
                    updateWatchlistDisplay(finalItemCount);
                    
                    if (finalItemCount > 0) {
                        statusLabel.setText("Watchlist loaded successfully!");
                        statusLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                    } else {
                        statusLabel.setText("ℹ️ Your watchlist is empty");
                        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
                    }
                    
                    view.getRefreshBtn().setDisable(false);
                    view.getClearAllBtn().setDisable(finalItemCount == 0);
                });
                
            } catch (Exception ex) {
                System.err.println("Error loading watchlist: " + ex.getMessage());
                ex.printStackTrace();
                
                javafx.application.Platform.runLater(() -> {
                    watchlistView.getItems().clear();
                    updateWatchlistDisplay(0);
                    statusLabel.setText("Error loading watchlist. Please try again.");
                    statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    view.getRefreshBtn().setDisable(false);
                    
                    // Show error dialog
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Load Failed");
                    alert.setHeaderText("Error Loading Watchlist");
                    alert.setContentText("Unable to load your watchlist at this time.\n\n" +
                        "Error: " + ex.getMessage() + "\n\n" +
                        "Please check your connection and try again.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void removeFromWatchlist(JSONObject item) {
        String title = item.optString("title", "Unknown Title");
        System.out.println("🗑️ Removing item: " + title);
        
        // Disable buttons during removal
        view.getRemoveBtn().setDisable(true);
        view.getRefreshBtn().setDisable(true);
        view.getClearAllBtn().setDisable(true);
        
        Label statusLabel = view.getStatusLabel();
        statusLabel.setText("🔄 Removing item...");
        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
        
        new Thread(() -> {
            try {
                PostgreSQLWatchlistService service = new PostgreSQLWatchlistService();
                service.removeFromWatchlist(SessionManager.getEmail(), title);
                
                javafx.application.Platform.runLater(() -> {
                    // Remove from local list and update display
                    watchlistItems.remove(item);
                    view.getWatchlistView().getItems().remove(item);
                    
                    int newCount = watchlistItems.size();
                    updateWatchlistDisplay(newCount);
                    
                    statusLabel.setText("Removed: " + title);
                    statusLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                    
                    view.getRemoveBtn().setDisable(true);
                    view.getRefreshBtn().setDisable(false);
                    view.getClearAllBtn().setDisable(newCount == 0);
                    
                    // Show success dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Item Removed");
                    alert.setHeaderText("Successfully Removed");
                    alert.setContentText("\"" + title + "\" has been removed from your watchlist.");
                    alert.showAndWait();
                });
                
            } catch (Exception ex) {
                System.err.println("Error removing item: " + ex.getMessage());
                ex.printStackTrace();
                
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("Error removing item. Please try again.");
                    statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    
                    view.getRemoveBtn().setDisable(false);
                    view.getRefreshBtn().setDisable(false);
                    view.getClearAllBtn().setDisable(watchlistItems.isEmpty());
                    
                    // Show error dialog
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Remove Failed");
                    alert.setHeaderText("Error Removing Item");
                    alert.setContentText("Unable to remove the item at this time.\n\n" +
                        "Error: " + ex.getMessage() + "\n\n" +
                        "Please try again.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void clearAllWatchlist() {
        System.out.println("🗑️ Clearing all watchlist items");
        
        // Disable all buttons during operation
        view.getRemoveBtn().setDisable(true);
        view.getRefreshBtn().setDisable(true);
        view.getClearAllBtn().setDisable(true);
        
        Label statusLabel = view.getStatusLabel();
        statusLabel.setText("🔄 Clearing watchlist...");
        statusLabel.setStyle("-fx-text-fill: #17a2b8; -fx-font-weight: bold;");
        
        new Thread(() -> {
            try {
                PostgreSQLWatchlistService service = new PostgreSQLWatchlistService();
                
                // Remove each item individually
                for (JSONObject item : new ArrayList<>(watchlistItems)) {
                    String title = item.optString("title", "Unknown Title");
                    service.removeFromWatchlist(SessionManager.getEmail(), title);
                }
                
                javafx.application.Platform.runLater(() -> {
                    // Clear local list and update display
                    watchlistItems.clear();
                    view.getWatchlistView().getItems().clear();
                    updateWatchlistDisplay(0);
                    
                    statusLabel.setText("Watchlist cleared successfully!");
                    statusLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                    
                    view.getRemoveBtn().setDisable(true);
                    view.getRefreshBtn().setDisable(false);
                    view.getClearAllBtn().setDisable(true);
                    
                    // Show success dialog
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Watchlist Cleared");
                    alert.setHeaderText("Successfully Cleared");
                    alert.setContentText("All items have been removed from your watchlist.");
                    alert.showAndWait();
                });
                
            } catch (Exception ex) {
                System.err.println("Error clearing watchlist: " + ex.getMessage());
                ex.printStackTrace();
                
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("Error clearing watchlist. Please try again.");
                    statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    
                    view.getRemoveBtn().setDisable(watchlistItems.isEmpty());
                    view.getRefreshBtn().setDisable(false);
                    view.getClearAllBtn().setDisable(watchlistItems.isEmpty());
                    
                    // Show error dialog
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Clear Failed");
                    alert.setHeaderText("Error Clearing Watchlist");
                    alert.setContentText("Unable to clear your watchlist at this time.\n\n" +
                        "Error: " + ex.getMessage() + "\n\n" +
                        "Please try again.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void updateWatchlistDisplay(int count) {
        Label countLabel = view.getCountLabel();
        
        // Check if we're in admin mode viewing a specific user's watchlist
        if (targetUserEmail != null) {
            if (count == 0) {
                countLabel.setText("0 items in " + targetUserEmail + "'s watchlist");
            } else if (count == 1) {
                countLabel.setText("1 item in " + targetUserEmail + "'s watchlist");
            } else {
                countLabel.setText(count + " items in " + targetUserEmail + "'s watchlist");
            }
        } else {
            if (count == 0) {
                countLabel.setText("0 items in your watchlist");
            } else if (count == 1) {
                countLabel.setText("1 item in your watchlist");
            } else {
                countLabel.setText(count + " items in your watchlist");
            }
        }
        
        System.out.println("📊 Updated watchlist display: " + count + " items");
    }
} 