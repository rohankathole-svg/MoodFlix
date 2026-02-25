package com.moodflix.util;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Centralized theme and animation utilities for MoodFlix.
 * Ensures consistent styling and smooth transitions across all views.
 */
public class ThemeManager {
    
    private static final String THEME_CSS = "/moodflix-theme.css";
    
    /**
     * Apply the MoodFlix theme to a scene.
     */
    public static void applyTheme(Scene scene) {
        if (scene != null) {
            String css = ThemeManager.class.getResource(THEME_CSS).toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
        }
    }
    
    /**
     * Create a themed scene from a root node.
     */
    public static Scene createThemedScene(Parent root) {
        Scene scene = new Scene(root);
        applyTheme(scene);
        return scene;
    }
    
    // ---- ANIMATION HELPERS ----
    
    /**
     * Fade-in animation for a node.
     */
    public static void fadeIn(Node node, double durationMs) {
        fadeIn(node, durationMs, 0);
    }
    
    public static void fadeIn(Node node, double durationMs, double delayMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.setInterpolator(Interpolator.EASE_OUT);
        ft.play();
    }
    
    /**
     * Slide-up + fade-in animation.
     */
    public static void slideUp(Node node, double durationMs, double delayMs) {
        node.setOpacity(0);
        node.setTranslateY(30);
        
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.setInterpolator(Interpolator.EASE_OUT);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), node);
        tt.setFromY(30);
        tt.setToY(0);
        tt.setDelay(Duration.millis(delayMs));
        tt.setInterpolator(Interpolator.EASE_OUT);
        
        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.play();
    }
    
    /**
     * Scale bounce animation (for buttons, cards on click).
     */
    public static void scalePop(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(0.95);
        st.setToY(0.95);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }
    
    /**
     * Subtle pulse glow animation (for hero elements).
     */
    public static void pulseGlow(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.03);
        st.setToY(1.03);
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }
    
    /**
     * Stagger animate a list of children with slide-up effect.
     */
    public static void staggerChildren(javafx.scene.layout.Pane parent, double staggerMs) {
        for (int i = 0; i < parent.getChildren().size(); i++) {
            slideUp(parent.getChildren().get(i), 400, i * staggerMs);
        }
    }
}
