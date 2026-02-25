package com.moodflix.controller;

import com.moodflix.service.PostgreSQLAuthService;
import com.moodflix.util.SessionManager;
import com.moodflix.view.FeedbackPage;
import com.moodflix.view.AdminDashboard;
import com.moodflix.view.UserDashboard;
import com.moodflix.controller.AdminDashboardController;
import com.moodflix.controller.UserDashboardController;
import com.moodflix.Main;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class FeedbackPageController {
    private final FeedbackPage view;

    public FeedbackPageController(FeedbackPage view) {
        this.view = view;
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        TextArea feedbackArea = view.getFeedbackArea();
        Button submitBtn = view.getSubmitBtn();
        Button backBtn = view.getBackBtn();
        Label statusLabel = view.getStatusLabel();

        submitBtn.setOnAction(e -> {
            System.out.println("💾 Submit feedback button clicked");
            
            String feedback = feedbackArea.getText().trim();
            int rating = getCurrentRating();
            
            // Validation
            if (rating == 0) {
                statusLabel.setText("Please select a rating (1-5 stars)");
                statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Rating Required");
                alert.setHeaderText("Please Rate Your Experience");
                alert.setContentText("Please select a rating from 1 to 5 stars before submitting your feedback.");
                alert.showAndWait();
                return;
            }
            
            if (feedback.isEmpty()) {
                statusLabel.setText(" Please enter your feedback");
                statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Feedback Required");
                alert.setHeaderText("Please Share Your Thoughts");
                alert.setContentText("Please enter your feedback in the text area before submitting.");
                alert.showAndWait();
                return;
            }
            
            if (feedback.length() < 10) {
                statusLabel.setText(" Please provide more detailed feedback (at least 10 characters)");
                statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("More Details Needed");
                alert.setHeaderText("Please Provide More Details");
                alert.setContentText("Please provide more detailed feedback (at least 10 characters) so we can better understand your experience.");
                alert.showAndWait();
                return;
            }
            
            // Disable button during submission
            submitBtn.setDisable(true);
            statusLabel.setText("🔄 Submitting feedback...");
            
            // Perform submission in background thread
            new Thread(() -> {
                try {
                    System.out.println("🚀 Submitting feedback to database...");
                    System.out.println("📊 Rating: " + rating + " stars");
                    System.out.println("📝 Feedback length: " + feedback.length() + " characters");
                    
                    PostgreSQLAuthService service = new PostgreSQLAuthService();
                    service.saveFeedback(SessionManager.getEmail(), feedback, rating);
                    
                    javafx.application.Platform.runLater(() -> {
                        submitBtn.setDisable(false);
                        statusLabel.setText("Feedback submitted successfully!");
                        statusLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                        
                        // Show success dialog
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thank You!");
                        alert.setHeaderText("Feedback Submitted Successfully");
                        alert.setContentText("Thank you for taking the time to share your feedback!\n\n" +
                            "Your input helps us improve Moodflix for everyone.\n\n" +
                            "Rating: " + rating + " stars\n" +
                            "Feedback: " + feedback.length() + " characters\n\n" +
                            "We appreciate your contribution to making Moodflix better!");
                        alert.showAndWait();
                        
                        // Clear form
                        clearForm();
                    });
                } catch (Exception ex) {
                    System.err.println("Error submitting feedback: " + ex.getMessage());
                    ex.printStackTrace();
                    
                    javafx.application.Platform.runLater(() -> {
                        submitBtn.setDisable(false);
                        statusLabel.setText("Error submitting feedback. Please try again.");
                        statusLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                        
                        // Show error dialog
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Submission Failed");
                        alert.setHeaderText("Error Submitting Feedback");
                        alert.setContentText("Unable to submit your feedback at this time.\n\n" +
                            "Error: " + ex.getMessage() + "\n\n" +
                            "Please check your connection and try again.\n" +
                            "Your feedback is important to us!");
                        alert.showAndWait();
                    });
                }
            }).start();
        });
        
        backBtn.setOnAction(e -> {
            System.out.println(" Back button clicked");
            String operationId = com.moodflix.util.PerformanceMonitor.startOperation("feedback_back_navigation");
            
            // Use optimized back navigation
            String userEmail = com.moodflix.util.SessionManager.getEmail();
            com.moodflix.util.BackNavigationOptimizer.smartBackNavigation(userEmail);
            
            com.moodflix.util.PerformanceMonitor.endOperation(operationId, true);
        });
    }
    
    private int getCurrentRating() {
        Button[] starButtons = view.getStarButtons();
        for (int i = 0; i < 5; i++) {
            if (starButtons[i].getStyle().contains("#ffd700")) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private void clearForm() {
        // Clear feedback text
        view.getFeedbackArea().clear();
        
        // Reset rating
        Button[] starButtons = view.getStarButtons();
        for (Button star : starButtons) {
            star.setStyle("-fx-background-color: transparent; -fx-text-fill: #e0e0e0; -fx-border-color: transparent;");
        }
        
        // Reset rating label
        view.getRatingLabel().setText("Rate your experience (1-5 stars)");
        view.getRatingLabel().setStyle("-fx-text-fill: #666666;");
        
        System.out.println("Form cleared for next feedback");
    }
} 