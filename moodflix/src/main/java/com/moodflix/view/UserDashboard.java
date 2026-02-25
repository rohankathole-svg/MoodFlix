package com.moodflix.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Hyperlink;
import java.util.List;
import com.moodflix.model.Content;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.input.KeyCode;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.scene.text.TextAlignment;
import javafx.scene.Cursor;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.application.HostServices;
import com.moodflix.util.ThemeManager;

public class UserDashboard {
    private final HostServices hostServices;
    private BorderPane view;
    private ComboBox<String> moodCombo;
    private ComboBox<String> typeCombo;
    private Button moodRecBtn;
    private Button generalRecBtn;
    private Button smartRecBtn;
    private VBox recommendationsContainer;

    // Restore these fields as private class fields:
    private VBox sidebar;
    private ImageView profilePhotoView;
    private Button profileBtn;
    private Button watchlistBtn;
    private Button feedbackBtn;
    private Button logoutBtn;
    private Button activityBtn;
    private VBox mainContent;
    private ScrollPane scrollPane;
    private VBox headerSection;
    private Label welcomeLabel;
    private Label subtitleLabel;
    private VBox illustrationSection;

    public UserDashboard() {
        this(null);
    }
    public UserDashboard(HostServices hostServices) {
        this.hostServices = hostServices;
        createModernDashboard();
    }
  
    private void createModernDashboard() {
        BorderPane mainView = new BorderPane();
        mainView.setStyle("-fx-background-color: #181c24;");

        // Ensure sidebar is created and added first
        createSidebar();
        mainView.setLeft(sidebar); // Sidebar is only set here, not added to any StackPane or VBox

        VBox mainContainer = new VBox(32);
        mainContainer.setPadding(new Insets(0, 0, 0, 0));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // HERO SECTION
        StackPane heroSection = new StackPane();
        heroSection.setPrefHeight(260);
        Image heroBackground;
        try {
            heroBackground = new Image(getClass().getResourceAsStream("/backgroundimage.jpeg"));
        } catch (Exception ex) {
            heroBackground = new Image("https://images.unsplash.com/photo-1464983953574-0892a716854b?auto=format&fit=crop&w=900&q=80", 900, 260, false, true);
        }
        ImageView heroBg = new ImageView(heroBackground);
        heroBg.setPreserveRatio(false);
        heroBg.setOpacity(0.7);
        // Bind heroBg width to heroSection width for full screen effect
        heroBg.fitWidthProperty().bind(heroSection.widthProperty());
        heroBg.setFitHeight(260);
        VBox heroTextBox = new VBox(10);
        heroTextBox.setAlignment(Pos.CENTER_LEFT);
        heroTextBox.setPadding(new Insets(30, 0, 0, 40));
        // Animated MoodFlix Title
        Text moodflixTitle = new Text(" 🎬 MoodFlix");
        moodflixTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 48));
        moodflixTitle.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#667eea")),
            new Stop(1, Color.web("#764ba2"))
        ));
        moodflixTitle.setStyle("-fx-effect: dropshadow(gaussian, #764ba2, 8, 0, 0, 2);");
        // Animation: scale and color pulse
        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.2), moodflixTitle);
        scale.setFromX(1.0); scale.setFromY(1.0);
        scale.setToX(1.12); scale.setToY(1.12);
        scale.setAutoReverse(true);
        scale.setCycleCount(Animation.INDEFINITE);
        scale.play();
        FillTransition fill = new FillTransition(Duration.seconds(2.2), moodflixTitle,
            Color.web("#667eea"), Color.web("#764ba2"));
        fill.setAutoReverse(true);
        fill.setCycleCount(Animation.INDEFINITE);
        fill.play();
        // Add MoodFlix title above heroTitle
        Label heroTitle = new Label("Discover\nEntertainment\nThat Matches Your Mood");
        heroTitle.getStyleClass().add("hero-title");
        Label heroDesc = new Label("Let your emotions guide your entertainment journey. From thrilling adventures to heartwarming stories.");
        heroDesc.getStyleClass().add("hero-subtitle");
        heroTextBox.getChildren().addAll(moodflixTitle, heroTitle, heroDesc);
        heroSection.getChildren().addAll(heroBg, heroTextBox);
        mainContainer.getChildren().add(heroSection);

        // MOOD SELECTION SECTION
        VBox moodSection = new VBox(10);
        moodSection.setAlignment(Pos.CENTER);
        Label moodTitle = new Label("How Are You Feeling Today?");
        moodTitle.getStyleClass().add("section-title");
        HBox moodCards = new HBox(18);
        moodCards.setAlignment(Pos.CENTER);
        moodCards.setPadding(new Insets(10, 0, 10, 0));
        String[][] moods = {
            {"😊", "Happy", "Feel good vibes"},
            {"😢", "Sad", "Emotional stories"},
            {"😱", "Thriller", "Heart-pounding"},
            {"😂", "Comedy", "Comedies galore"},
            {"😍", "Romantic", "Love stories"}
        };
        moodCombo = new ComboBox<>(); // assign to class field
        for (String[] mood : moods) {
            moodCombo.getItems().add(mood[0] + " " + mood[1]);
        }
        moodCombo.setPromptText("Select your mood...");
        moodCombo.setPrefWidth(210);
        moodCombo.setStyle("-fx-background-radius: 10; -fx-padding: 8 12; -fx-font-size: 15; -fx-font-family: 'Segoe UI Emoji', 'Apple Color Emoji', 'Noto Color Emoji', 'Arial Unicode MS', Arial, sans-serif; -fx-background-color: #23272f; -fx-border-color: #a78bfa; -fx-border-radius: 10; -fx-text-fill: white;");
        // Custom cell factory for emoji + mood name, with emoji font
        moodCombo.setCellFactory(list -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 15; -fx-padding: 6 10; -fx-background-color: transparent; -fx-text-fill: #fff; -fx-font-family: 'Segoe UI Emoji', 'Apple Color Emoji', 'Noto Color Emoji', 'Arial Unicode MS', Arial, sans-serif;");
                }
            }
        });
        moodCombo.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 15; -fx-padding: 6 10; -fx-background-color: transparent; -fx-text-fill: #fff; -fx-font-family: 'Segoe UI Emoji', 'Apple Color Emoji', 'Noto Color Emoji', 'Arial Unicode MS', Arial, sans-serif;");
                }
            }
        });
        for (int i = 0; i < moods.length; i++) {
            String[] mood = moods[i];
            VBox card = new VBox(6);
            card.setAlignment(Pos.CENTER);
            card.setPrefWidth(110);
            card.setPrefHeight(90);
            card.getStyleClass().add("mood-card");
            Label emoji = new Label(mood[0]);
            emoji.setFont(Font.font("Arial", FontWeight.BOLD, 32));
            Label label = new Label(mood[1]);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            label.setTextFill(Color.WHITE);
            Label desc = new Label(mood[2]);
            desc.setFont(Font.font("Arial", 12));
            desc.setTextFill(Color.LIGHTGRAY);
            card.getChildren().addAll(emoji, label, desc);
            final int moodIndex = i;
            card.setOnMouseClicked(e -> {
                for (Node n : moodCards.getChildren()) n.setStyle("-fx-background-color: #23272f; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #00000044, 4, 0, 0, 2);");
                card.setStyle("-fx-background-color: #a78bfa; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #a78bfa88, 8, 0, 0, 2);");
                moodCombo.setValue(mood[1]);
            });
            moodCards.getChildren().add(card);
        }
        moodSection.getChildren().addAll(moodTitle, moodCards);
        mainContainer.getChildren().add(moodSection);

        // RECOMMENDATIONS SECTION
        VBox recSection = new VBox(18);
        recSection.setAlignment(Pos.CENTER);
        recSection.setPadding(new Insets(10, 0, 10, 0));
        recSection.setStyle("-fx-background-color: #23272f; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #00000033, 4, 0, 0, 2);");
        Label recTitle = new Label("Get Personalized Recommendations");
        recTitle.getStyleClass().add("section-title");
        HBox recBtnBox = new HBox(12);
        recBtnBox.setAlignment(Pos.CENTER);
        moodRecBtn = new Button("Mood Based"); // assign to class field
        moodRecBtn.getStyleClass().addAll("btn", "btn-success");
        generalRecBtn = new Button("General"); // assign to class field
        generalRecBtn.getStyleClass().addAll("btn", "btn-success");
        // Remove smartRecBtn from the UI
        // recBtnBox.getChildren().addAll(moodRecBtn, generalRecBtn, smartRecBtn);
        recBtnBox.getChildren().clear();
        recBtnBox.getChildren().addAll(moodRecBtn, generalRecBtn);

        // Add moodCombo and typeCombo dropdowns
        HBox recDropdownBox = new HBox(10);
        recDropdownBox.setAlignment(Pos.CENTER);
        moodCombo = new ComboBox<>(); // assign to class field
        moodCombo.getItems().addAll("Happy", "Sad", "Thriller", "Feel Good", "Comedy", "Romantic");
        moodCombo.setPromptText("Mood...");
        moodCombo.setStyle("-fx-background-radius: 10; -fx-padding: 8 12; -fx-font-size: 13; -fx-background-color:rgb(233, 236, 241); -fx-border-color: #a78bfa; -fx-border-radius: 10; -fx-text-fill: white;");
        typeCombo = new ComboBox<>(); // assign to class field
        typeCombo.getItems().addAll("Movie", "Series", "Song", "Trailer", "Shorts");
        typeCombo.setPromptText("Type...");
        typeCombo.setStyle("-fx-background-radius: 10; -fx-padding: 8 12; -fx-font-size: 13; -fx-background-color:rgb(214, 217, 224); -fx-border-color: #a78bfa; -fx-border-radius: 10; -fx-text-fill: white;");
        recDropdownBox.getChildren().addAll(moodCombo, typeCombo);

        // Add Recommended button
        Button recommendedBtn = new Button("Recommend");
        recommendedBtn.getStyleClass().addAll("btn", "btn-warning");
        recommendedBtn.setDisable(true);
        // Enable only when both mood and type are selected
        moodCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            recommendedBtn.setDisable(moodCombo.getValue() == null || moodCombo.getValue().isEmpty() || typeCombo.getValue() == null || typeCombo.getValue().isEmpty());
        });
        typeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            recommendedBtn.setDisable(moodCombo.getValue() == null || moodCombo.getValue().isEmpty() || typeCombo.getValue() == null || typeCombo.getValue().isEmpty());
        });
        // Update Recommended button action to navigate to a new RecommendationPage
        recommendedBtn.setOnAction(e -> {
            if (moodCombo.getValue() != null && !moodCombo.getValue().isEmpty() && typeCombo.getValue() != null && !typeCombo.getValue().isEmpty()) {
                // Show loading if desired
                showRecommendationsLoading();
                new Thread(() -> {
                    try {
                        com.moodflix.service.PostgreSQLContentService service = new com.moodflix.service.PostgreSQLContentService();
                        java.util.List<com.moodflix.model.Content> results = service.getFilteredContentList(moodCombo.getValue(), typeCombo.getValue());
                        javafx.application.Platform.runLater(() -> {
                            // Navigate to RecommendationPage (new scene)
                            com.moodflix.view.RecommendationPage recPage = new com.moodflix.view.RecommendationPage(results);
                            javafx.stage.Stage stage = (javafx.stage.Stage) recommendedBtn.getScene().getWindow();
                            stage.setScene(new javafx.scene.Scene(recPage.getView(), 1200, 800));
                        });
                    } catch (Exception ex) {
                        javafx.application.Platform.runLater(() -> showRecommendationsError(ex.getMessage()));
                    }
                }).start();
            }
        });

        // Add the Recommended button below the dropdowns
        VBox recDropdownAndBtn = new VBox(8, recDropdownBox, recommendedBtn);
        recDropdownAndBtn.setAlignment(Pos.CENTER);

        recommendationsContainer = new VBox(10); // assign to class field
        recommendationsContainer.setAlignment(Pos.CENTER);
        recommendationsContainer.setPadding(new Insets(10, 0, 10, 0));
        recSection.getChildren().addAll(recTitle, recBtnBox, recDropdownAndBtn, recommendationsContainer);
        mainContainer.getChildren().add(recSection);

        // TRENDING NOW SECTION
        VBox trendingSection = new VBox(10);
        trendingSection.setAlignment(Pos.CENTER_LEFT);
        trendingSection.setPadding(new Insets(10, 0, 10, 40));
        Label trendingTitle = new Label("Trending Now");
        trendingTitle.getStyleClass().add("section-title");
        HBox trendingRow = new HBox(18);
        trendingRow.setAlignment(Pos.CENTER_LEFT);
        trendingRow.setPadding(new Insets(10, 0, 10, 0));
        String[][] trending = {
            {"https://m.media-amazon.com/images/I/71niXI3lxlL._AC_SY679_.jpg", "Action Thriller"},
            {"https://m.media-amazon.com/images/I/81p+xe8cbnL._AC_SY679_.jpg", "Love Story"},
            {"https://m.media-amazon.com/images/I/91G8kOe7tLL._AC_SY679_.jpg", "Space Series"},
            {"https://m.media-amazon.com/images/I/81Q1bJz4GLL._AC_SY679_.jpg", "Dark Secrets"},
            {"https://m.media-amazon.com/images/I/81Zt42ioCgL._AC_SY679_.jpg", "Laugh Out Loud"},
            {"https://m.media-amazon.com/images/I/81VwqH9hQbL._AC_SY679_.jpg", "Wild Nature"}
        };
        for (String[] t : trending) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.TOP_CENTER);
            card.setPrefWidth(120);
            card.getStyleClass().add("poster-card");
            ImageView poster = new ImageView(new Image(t[0], 120, 160, true, true));
            poster.setFitWidth(120);
            poster.setFitHeight(160);
            poster.setPreserveRatio(true);
            poster.setSmooth(true);
            Label label = new Label(t[1]);
            card.getChildren().addAll(poster, label);
            card.setOnMouseClicked(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trending");
                alert.setHeaderText(null);
                alert.setContentText("You clicked on: " + t[1]);
                alert.showAndWait();
            });
            trendingRow.getChildren().add(card);
        }
        ScrollPane trendingScroll = new ScrollPane(trendingRow);
        trendingScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        trendingScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        trendingScroll.setFitToHeight(true);
        trendingScroll.setFitToWidth(false);
        trendingScroll.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        trendingSection.getChildren().addAll(trendingTitle, trendingScroll);
        mainContainer.getChildren().add(trendingSection);

        // STATS BAR
        HBox statsBar = new HBox(40);
        statsBar.setAlignment(Pos.CENTER);
        statsBar.setPadding(new Insets(30, 0, 30, 0));
        statsBar.setStyle("-fx-background-color: #23272f; -fx-background-radius: 0 0 18 18;");
        String[][] stats = {
            {"10K+", "Movies & Shows"},
            {"50K+", "Happy Users"},
            {"95%", "Match Accuracy"},
            {"24/7", "Support"}
        };
        for (String[] stat : stats) {
            VBox statBox = new VBox(4);
            statBox.setAlignment(Pos.CENTER);
            statBox.getStyleClass().add("stat-card");
            Label statNum = new Label(stat[0]);
            statNum.getStyleClass().add("stat-value");
            Label statLabel = new Label(stat[1]);
            statLabel.getStyleClass().add("stat-label");
            statBox.getChildren().addAll(statNum, statLabel);
            statsBar.getChildren().add(statBox);
        }
        mainContainer.getChildren().add(statsBar);

        recommendationsContainer = new VBox();
        recommendationsContainer.setAlignment(Pos.CENTER);
        recommendationsContainer.setPadding(new Insets(10, 0, 10, 0));
        mainContainer.getChildren().add(recommendationsContainer);

        // After mainContainer is fully built:
        ScrollPane dashboardScrollPane = new ScrollPane(mainContainer);
        dashboardScrollPane.setFitToWidth(true);
        dashboardScrollPane.setFitToHeight(false);
        dashboardScrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        // --- Chatbot Floating Button and Window ---
        Button chatBtn = new Button("🎬"); // Film clapperboard emoji for movie/entertainment theme
        chatBtn.getStyleClass().addAll("btn", "btn-success");
        chatBtn.setPrefSize(80, 60);
        chatBtn.setLayoutX(1000); // Will be positioned in StackPane
        chatBtn.setLayoutY(600);

        StackPane chatWindow = new StackPane();
        chatWindow.setMaxWidth(320);
        chatWindow.setPrefWidth(320);
        chatWindow.setStyle("-fx-border-color: #764ba2; -fx-border-radius: 18; -fx-effect: dropshadow(gaussian, #764ba2, 12, 0, 0, 2);");
        chatWindow.setVisible(false);
        // Background image
        ImageView chatBgImg = new ImageView(new Image("https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80", 320, 400, false, true));
        chatBgImg.setFitWidth(320);
        chatBgImg.setPreserveRatio(false);
        chatBgImg.setSmooth(true);
        // Overlay for chat content
        VBox chatOverlay = new VBox(16);
        chatOverlay.setPadding(new Insets(18));
        chatOverlay.setStyle("-fx-background-color: rgba(35,39,47,0.82); -fx-background-radius: 18;");
        chatOverlay.setMaxWidth(320);
        chatOverlay.setPrefWidth(320);

        Label chatTitle = new Label("MoodFlix ChatBot 🤖");
        chatTitle.getStyleClass().add("section-title");
        VBox chatHistory = new VBox(10);
        chatHistory.setPadding(new Insets(8));
        chatHistory.setStyle("-fx-background-color: #181c24; -fx-background-radius: 12;");
        chatHistory.setPrefHeight(300);
        chatHistory.setPrefWidth(284);
        chatHistory.setFillWidth(true);
        ScrollPane chatScroll = new ScrollPane(chatHistory);
        chatScroll.setFitToWidth(true);
        chatScroll.setPrefHeight(300);
        chatScroll.setPrefWidth(284);
        chatScroll.setStyle("-fx-background: #181c24; -fx-background-radius: 12; -fx-border-color: #764ba2; -fx-border-radius: 12;");
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        TextField chatInput = new TextField();
        chatInput.setPromptText("Ask me about MoodFlix, movies, or get help...");
        Button sendBtn = new Button("Send");
        sendBtn.getStyleClass().addAll("btn", "btn-success");
        HBox chatInputBox = new HBox(8, chatInput, sendBtn);
        chatOverlay.getChildren().setAll(chatTitle, chatScroll, chatInputBox);
        chatWindow.getChildren().setAll(chatBgImg, chatOverlay);

        // Show/hide chat window
        chatBtn.setOnAction(e -> chatWindow.setVisible(!chatWindow.isVisible()));

        // Chatbot logic (rich chat)
        sendBtn.setOnAction(e -> handleRichChat(chatInput, chatHistory, chatScroll));
        chatInput.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleRichChat(chatInput, chatHistory, chatScroll); });

        // Add to main StackPane overlay
        StackPane overlay = new StackPane();
        overlay.setPickOnBounds(false);
        overlay.getChildren().addAll(dashboardScrollPane, chatBtn, chatWindow);
        StackPane.setAlignment(chatBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(chatBtn, new Insets(0, 40, 40, 0));
        StackPane.setAlignment(chatWindow, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(chatWindow, new Insets(0, 40, 110, 0));
        mainView.setCenter(overlay);
        this.view = mainView;
    }

    private void createSidebar() {
        System.out.println("[DEBUG] Creating sidebar...");
        sidebar = new VBox(30);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(40, 10, 40, 10));
        sidebar.setPrefWidth(160);
        sidebar.getStyleClass().add("sidebar");

        // User avatar section
        VBox avatarSection = new VBox(8);
        avatarSection.setAlignment(Pos.CENTER);
        // Profile photo ImageView (circular)
        profilePhotoView = new ImageView();
        profilePhotoView.setFitWidth(64);
        profilePhotoView.setFitHeight(64);
        profilePhotoView.setPreserveRatio(true);
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(32, 32, 32);
        profilePhotoView.setClip(clip);
        // Set default profile image
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-profile.png"));
            if (defaultImage.isError()) {
                profilePhotoView.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 32;");
            } else {
                profilePhotoView.setImage(defaultImage);
            }
        } catch (Exception e) {
            profilePhotoView.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 32;");
        }
        Label userLabel = new Label("🎬 Moodflix");
        userLabel.getStyleClass().add("hero-title");
        userLabel.setAlignment(Pos.CENTER);
        avatarSection.getChildren().addAll(profilePhotoView, userLabel);
        sidebar.getChildren().add(avatarSection);

        // Navigation buttons
        profileBtn = createNavButton("Profile");
        System.out.println("[DEBUG] Profile button created: " + profileBtn);
        watchlistBtn = createNavButton("Watchlist");
        feedbackBtn = createNavButton("Feedback");
        logoutBtn = createNavButton("Logout");
        sidebar.getChildren().addAll(profileBtn, watchlistBtn, feedbackBtn, logoutBtn);

        // Activity button at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        activityBtn = createNavButton("Activity");
        sidebar.getChildren().addAll(spacer, activityBtn);
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-btn");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void createMainContent() {
        mainContent = new VBox(32);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(48, 0, 48, 0));

        // Add randomly placed circular mood images in a Pane
        Pane floatingImagesPane = new Pane();
        floatingImagesPane.setPrefSize(700, 400); // Adjust as needed
        String[] moodImageUrls = {
            "https://cdn-icons-png.flaticon.com/512/742/742751.png", // Happy
            "https://cdn-icons-png.flaticon.com/512/742/742752.png", // Sad
            "https://cdn-icons-png.flaticon.com/512/742/742753.png", // Thriller
            "https://cdn-icons-png.flaticon.com/512/742/742754.png", // Feel Good
            "https://cdn-icons-png.flaticon.com/512/742/742755.png", // Comedy
            "https://cdn-icons-png.flaticon.com/512/742/742756.png"  // Romantic
        };
        String[] moodNames = {"Happy", "Sad", "Thriller", "Feel Good", "Comedy", "Romantic"};
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < moodImageUrls.length; i++) {
            ImageView moodImage = new ImageView(moodImageUrls[i]);
            moodImage.setFitWidth(64);
            moodImage.setFitHeight(64);
            moodImage.setPreserveRatio(true);
            moodImage.setSmooth(true);
            moodImage.setStyle("-fx-background-color: white; -fx-background-radius: 50%; -fx-border-radius: 50%; -fx-border-color: #764ba2; -fx-padding: 4; -fx-effect: dropshadow(gaussian, #764ba2, 2, 0, 0, 1);");
            Tooltip.install(moodImage, new Tooltip(moodNames[i]));
            // Random position within the pane
            double x = 40 + rand.nextDouble() * 600;
            double y = 20 + rand.nextDouble() * 300;
            moodImage.setLayoutX(x);
            moodImage.setLayoutY(y);
            floatingImagesPane.getChildren().add(moodImage);
        }
        // Add the floating images pane to the mainContent (behind other content)
        StackPane stack = new StackPane();
        stack.getChildren().addAll(floatingImagesPane, mainContent);

        // Header Section
        createHeaderSection();
        // Illustration/Banner Section
        createIllustrationSection();

        mainContent.getChildren().addAll(headerSection, illustrationSection);
        // Replace mainContent in the scrollPane with the stack
        scrollPane = new ScrollPane(stack);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        view.setCenter(scrollPane);
    }

    private void createHeaderSection() {
        headerSection = new VBox(8);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setPadding(new Insets(18));
        headerSection.setStyle("-fx-background-color: rgba(131, 107, 107, 0.97); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, #764ba2, 4, 0, 0, 1);");

        // Welcome message with entertainment theme
        welcomeLabel = new Label("Welcome to 🎬 MoodFlix!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        welcomeLabel.setTextFill(Color.web("#000000"));
        welcomeLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 8, 0, 0, 2);");

        subtitleLabel = new Label("Discover entertainment that matches your mood");
        subtitleLabel.setFont(Font.font("Arial", 15));
        subtitleLabel.setTextFill(Color.web("#000000"));

        headerSection.getChildren().addAll(welcomeLabel, subtitleLabel);
    }

    private void createIllustrationSection() {
        illustrationSection = new VBox(15);
        illustrationSection.setAlignment(Pos.CENTER);
        illustrationSection.setPadding(new Insets(10, 0, 30, 0));
        illustrationSection.setStyle("-fx-background-color: transparent;");

        // Modern illustration (web image)
        ImageView illustration = new ImageView();
        illustration.setFitWidth(320);
        illustration.setFitHeight(180);
        illustration.setPreserveRatio(true);
        illustration.setSmooth(true);
        illustrationSection.getChildren().add(illustration);
        new Thread(() -> {
            Image img = new Image("https://undraw.co/api/illustrations/undraw_movie_night_re_9umk.svg", 320, 180, true, true);
            javafx.application.Platform.runLater(() -> illustration.setImage(img));
        }).start();

        // Motivational quote
        Label quote = new Label("\"Movies, music, and shows for every mood. Let your feelings guide your entertainment!\"");
        quote.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        quote.setTextFill(Color.web("#764ba2"));
        quote.setStyle("-fx-background-color: rgba(137, 25, 25, 0.85); -fx-background-radius: 12; -fx-padding: 10 24; -fx-effect: dropshadow(gaussian, #764ba2, 6, 0, 0, 2);");
        quote.setWrapText(true);
        quote.setMaxWidth(400);

        illustrationSection.getChildren().add(quote);
    }

    public static Scene createRecommendationGridScene(List<Content> contentList) {
        System.out.println("[DEBUG] createRecommendationGridScene called");
        System.out.println("[DEBUG] Number of content items: " + (contentList == null ? "null" : contentList.size()));
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        // Back button
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 20; -fx-font-size: 14;");
        // The actual action will be set by the controller
        // Grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);
        int cols = 3;
        int row = 0, col = 0;
        for (Content c : contentList) {
            System.out.println("[DEBUG] Content: " + c.getTitle() + ", imageUrl: '" + c.getImageUrl() + "'");
            VBox card = new VBox(10);
            card.setPadding(new Insets(20));
            card.setAlignment(Pos.TOP_CENTER);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
            card.setPrefWidth(220);
            card.setMaxWidth(220);
            card.setPrefHeight(400);
            card.setMaxHeight(400);
            // Icon or Image
            final String icon;
            if (c.getType().equalsIgnoreCase("series")) icon = "\uD83D\uDCFA";
            else if (c.getType().equalsIgnoreCase("song")) icon = "\uD83C\uDFB5";
            else if (c.getType().equalsIgnoreCase("documentary")) icon = "\uD83D\uDCC4";
            else if (c.getType().equalsIgnoreCase("short")) icon = "\uD83C\uDFAC";
            else icon = "\uD83C\uDFAC";
            Node topNode;
            if (c.getImageUrl() != null && !c.getImageUrl().isEmpty()) {
                try {
                    System.out.println("[DEBUG] Loading image for card: " + c.getTitle() + " | URL: " + c.getImageUrl());
                    Image img = new Image(c.getImageUrl(), 160, 220, true, true);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(160);
                    imgView.setFitHeight(220);
                    imgView.setSmooth(true);
                    imgView.setPreserveRatio(true);
                    imgView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 8, 0, 0, 2); -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-radius: 8;");
                    Tooltip.install(imgView, new Tooltip(c.getTitle()));
                    img.errorProperty().addListener((obs, wasError, isError) -> {
                        if (isError) {
                            System.out.println("[WARNING] Failed to load image for card: " + c.getTitle() + " | URL: " + c.getImageUrl());
                            Image placeholder = new Image("https://via.placeholder.com/160x220?text=No+Image");
                            imgView.setImage(placeholder);
                        }
                    });
                    topNode = imgView;
                } catch (Exception ex) {
                    System.out.println("[ERROR] Exception loading image for card: " + c.getTitle() + " | URL: " + c.getImageUrl() + " | Exception: " + ex.getMessage());
                    ImageView imgView = new ImageView(new Image("https://via.placeholder.com/160x220?text=No+Image"));
                    imgView.setFitWidth(160);
                    imgView.setFitHeight(220);
                    imgView.setPreserveRatio(true);
                    imgView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 8, 0, 0, 2); -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-radius: 8;");
                    Tooltip.install(imgView, new Tooltip(c.getTitle()));
                    topNode = imgView;
                }
            } else {
                System.out.println("[WARNING] No imageUrl for card: " + c.getTitle());
                ImageView imgView = new ImageView(new Image("https://via.placeholder.com/160x220?text=No+Image"));
                imgView.setFitWidth(160);
                imgView.setFitHeight(220);
                imgView.setPreserveRatio(true);
                imgView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 8, 0, 0, 2); -fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-radius: 8;");
                Tooltip.install(imgView, new Tooltip(c.getTitle()));
                topNode = imgView;
            }
            // Title
            Label titleLabel = new Label(c.getTitle());
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            titleLabel.setTextFill(Color.web("#333333"));
            // Type
            Label typeLabel = new Label(c.getType());
            typeLabel.setFont(Font.font("Arial", 12));
            typeLabel.setTextFill(Color.web("#666666"));
            // Description
            Text descText = new Text(c.getDescription() != null ? c.getDescription() : "No description available.");
            descText.setFont(Font.font("Arial", 13));
            descText.setWrappingWidth(180);
            // Link
            Hyperlink link = new Hyperlink(c.getLink());
            link.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(c.getLink()));
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Could not open link");
                    error.setContentText("Failed to open the link: " + ex.getMessage());
                    error.showAndWait();
                }
            });
            // Add to Watchlist button for this card
            Button addToWatchlistBtn = new Button("Add to Watchlist");
            addToWatchlistBtn.setStyle("-fx-background-color: #764ba2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 16; -fx-font-size: 13;");
            addToWatchlistBtn.setUserData(c); // Store content object for controller
            // Scrollable content area
            VBox cardContent = new VBox(8);
            cardContent.getChildren().addAll(titleLabel, typeLabel, descText, link, addToWatchlistBtn);
            cardContent.setPrefWidth(180);
            ScrollPane cardScroll = new ScrollPane(cardContent);
            cardScroll.setFitToWidth(true);
            cardScroll.setPrefHeight(120);
            cardScroll.setMaxHeight(120);
            cardScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            cardScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            card.getChildren().addAll(topNode, cardScroll);
            // Make card clickable to open the content link directly
            card.setOnMouseClicked(event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(c.getLink()));
                } catch (Exception ex) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Could not open link");
                    error.setContentText("Failed to open the link: " + ex.getMessage());
                    error.showAndWait();
                }
            });
            grid.add(card, col, row);
            col++;
            if (col == cols) { col = 0; row++; }
        }
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Optionally, limit grid's max height for better scrolling experience
        // grid.setMaxHeight(600);
        root.getChildren().addAll(backBtn, scrollPane);
        Scene scene = new Scene(root, 1200, 800);
        // Expose backBtn for controller to set action
        scene.getProperties().put("backBtn", backBtn);
        return scene;
    }

    // Getters
    public Pane getView() { return view; }
    public ComboBox<String> getMoodBox() { return moodCombo; }
    public ComboBox<String> getTypeBox() { return typeCombo; }
    public Button getFetchBtn() { return null; } // Removed
    public ListView<String> getRecommendationsList() { return null; } // Removed
    public Label getStatusLabel() { return null; } // Removed
    public Button getViewHistoryBtn() { return null; } // Removed
    public Button getProfileBtn() { return profileBtn; }
    public Button getWatchlistBtn() { return watchlistBtn; }
    public Button getFeedbackBtn() { return feedbackBtn; }
    public Button getLogoutBtn() { return logoutBtn; }
    public Button getActivityBtn() { return activityBtn; }
    public ImageView getProfilePhotoView() { return profilePhotoView; }
    public Button getMoodRecBtn() { return moodRecBtn; }
    public Button getGeneralRecBtn() { return generalRecBtn; }
    public Button getSmartRecBtn() { return smartRecBtn; }
    public ComboBox<String> getMoodCombo() { return moodCombo; }
    public ComboBox<String> getTypeCombo() { return typeCombo; }

    public void showRecommendationsLoading() {
        recommendationsContainer.getChildren().clear();
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(60, 60);
        Label loadingLabel = new Label("Fetching recommendations...");
        loadingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        loadingLabel.setTextFill(Color.LIGHTGRAY);
        VBox box = new VBox(12, spinner, loadingLabel);
        box.setAlignment(Pos.CENTER);
        recommendationsContainer.getChildren().add(box);
    }
    public void showRecommendationsGrid(java.util.List<com.moodflix.model.Content> results) {
        recommendationsContainer.getChildren().clear();
        if (results == null || results.isEmpty()) {
            showRecommendationsError("No recommendations found for your selection.");
            return;
        }
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(false);
        scroll.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        HBox grid = new HBox(18);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(10, 0, 10, 0));
        for (com.moodflix.model.Content c : results) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.TOP_CENTER);
            card.setPrefWidth(140);
            card.setStyle("-fx-background-color: #23272f; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, #00000033, 4, 0, 0, 2);");
            ImageView poster;
            if (c.getImageUrl() != null && !c.getImageUrl().isEmpty()) {
                poster = new ImageView(new Image(c.getImageUrl(), 120, 160, true, true));
            } else {
                poster = new ImageView(new Image("https://via.placeholder.com/120x160?text=No+Image", 120, 160, true, true));
            }
            poster.setFitWidth(120);
            poster.setFitHeight(160);
            poster.setPreserveRatio(true);
            poster.setSmooth(true);
            Label title = new Label(c.getTitle());
            title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            title.setTextFill(Color.WHITE);
            Button addBtn = new Button("Add to Watchlist");
            addBtn.setStyle("-fx-background-color: #a78bfa; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 6 14; -fx-font-size: 13;");
            card.getChildren().addAll(poster, title, addBtn);
            grid.getChildren().add(card);
        }
        scroll.setContent(grid);
        recommendationsContainer.getChildren().add(scroll);
    }
    public void showRecommendationsError(String message) {
        recommendationsContainer.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        errorLabel.setTextFill(Color.web("#ff3c3c"));
        recommendationsContainer.getChildren().add(errorLabel);
    }

    // Rich chat handler: styled bubbles, images, links
    private void handleRichChat(TextField chatInput, VBox chatHistory, ScrollPane chatScroll) {
        String userMsg = chatInput.getText();
        if (userMsg == null || userMsg.trim().isEmpty()) return;
        addChatBubble(chatHistory, userMsg, true, null, null);
        String lower = userMsg.toLowerCase();
        if (lower.contains("tell me about moodflix") || lower.contains("information about moodflix")) {
            addChatBubble(chatHistory,
                "MoodFlix is a mood-based entertainment application that recommends content based on users' emotions. " +
                "It leverages intuitive UI and smart algorithms to deliver personalized movie and music suggestions.",
                false, null, null);
        } else
        if (lower.contains("hello") || lower.contains("hi")) {
            addChatBubble(chatHistory, "Hello! How can I help you with MoodFlix today?", false, null, null);
        } else if (lower.contains("problem") || lower.contains("help") || lower.contains("issue")) {
            addChatBubble(chatHistory, "I'm here to help! You can ask about login issues, watchlist, or recommendations.", false, null, null);
        } else if (lower.contains("watchlist")) {
            addChatBubble(chatHistory, "You can add movies or series to your watchlist from the recommendations page.", false, null, null);
        } else if (lower.contains("admin")) {
            addChatBubble(chatHistory, "Admins can manage content and users from the Admin Dashboard.", false, null, null);
        } else if (lower.contains("bye")) {
            addChatBubble(chatHistory, "Goodbye! Enjoy MoodFlix!", false, null, null);
        } else {
            addChatBubble(chatHistory, "Searching for info...", false, null, null);
            new Thread(() -> {
                String searchResults = fetchOmdbListRich(userMsg, chatHistory, chatScroll);
                if (searchResults != null) {
                    javafx.application.Platform.runLater(() -> replaceLastBotBubble(chatHistory, searchResults, null, null));
                } else {
                    String title = extractTitle(userMsg);
                    OmdbResult result = (title != null && !title.isEmpty()) ? fetchOmdbInfoWithPosterRich(title) : null;
                    javafx.application.Platform.runLater(() -> {
                        if (result != null && result.text != null) {
                            replaceLastBotBubble(chatHistory, result.text, result.posterUrl, result.imdbUrl);
                        } else {
                            replaceLastBotBubble(chatHistory, "Sorry, I couldn't extract a movie or series title from your message.", null, null);
                        }
                    });
                }
                javafx.application.Platform.runLater(() -> chatScroll.setVvalue(1.0));
            }).start();
        }
        chatInput.clear();
        chatScroll.setVvalue(1.0);
    }

    // Add a chat bubble to the chat history
    private void addChatBubble(VBox chatHistory, String text, boolean isUser, String posterUrl, String imdbUrl) {
        HBox bubbleRow = new HBox();
        bubbleRow.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        bubbleRow.setPadding(new Insets(2, 0, 2, 0));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox bubble = new VBox(2);
        bubble.setPadding(new Insets(8, 12, 8, 12));
        bubble.setMaxWidth(340);
        bubble.getStyleClass().add(isUser ? "chat-bubble-user" : "chat-bubble-bot");
        Text msgText = new Text(text);
        msgText.setFill(isUser ? Color.WHITE : Color.WHITE);
        TextFlow textFlow = new TextFlow(msgText);
        textFlow.setTextAlignment(isUser ? TextAlignment.RIGHT : TextAlignment.LEFT);
        bubble.getChildren().add(textFlow);
        if (posterUrl != null && !posterUrl.equals("N/A")) {
            ImageView poster = new ImageView(new Image(posterUrl, 80, 120, true, true));
            poster.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #764ba2; -fx-border-width: 2;");
            bubble.getChildren().add(poster);
        }
        if (imdbUrl != null && !imdbUrl.isEmpty() && hostServices != null) {
            Hyperlink imdbLink = new Hyperlink("View on IMDb");
            imdbLink.setStyle("-fx-text-fill: #ff9800; -fx-font-weight: bold; -fx-font-size: 13;");
            imdbLink.setOnAction(e -> hostServices.showDocument(imdbUrl));
            bubble.getChildren().add(imdbLink);
        }
        if (isUser) {
            bubbleRow.getChildren().addAll(spacer, bubble);
        } else {
            bubbleRow.getChildren().addAll(bubble, spacer);
        }
        chatHistory.getChildren().add(bubbleRow);
    }

    // Replace the last bot bubble with new content
    private void replaceLastBotBubble(VBox chatHistory, String text, String posterUrl, String imdbUrl) {
        for (int i = chatHistory.getChildren().size() - 1; i >= 0; i--) {
            HBox row = (HBox) chatHistory.getChildren().get(i);
            if (row.getAlignment() == Pos.CENTER_LEFT) {
                chatHistory.getChildren().remove(i);
                addChatBubble(chatHistory, text, false, posterUrl, imdbUrl);
                break;
            }
        }
    }

    // OMDb list search for rich chat
    private String fetchOmdbListRich(String query, VBox chatHistory, ScrollPane chatScroll) {
        try {
            String apiKey = "76dfa4c6";
            String urlStr = "https://www.omdbapi.com/?apikey=" + apiKey + "&s=" + URLEncoder.encode(query, "UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            JSONObject obj = new JSONObject(sb.toString());
            if (obj.has("Response") && obj.getString("Response").equals("True") && obj.has("Search")) {
                org.json.JSONArray arr = obj.getJSONArray("Search");
                if (arr.length() == 1) return null; // Let detailed fetch handle single result
                javafx.application.Platform.runLater(() -> showOmdbListResultsRich(arr, chatHistory, chatScroll));
                return "Here are some results. Click a title for details.";
            }
        } catch (Exception e) { /* ignore */ }
        return null;
    }

    // Show clickable list of OMDb results in the chat (rich)
    private void showOmdbListResultsRich(org.json.JSONArray arr, VBox chatHistory, ScrollPane chatScroll) {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject item = arr.getJSONObject(i);
            String title = item.optString("Title", "N/A");
            String year = item.optString("Year", "N/A");
            String imdbID = item.optString("imdbID", "");
            String poster = item.optString("Poster", "");
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            if (poster != null && !poster.equals("N/A")) {
                ImageView img = new ImageView(new Image(poster, 40, 60, true, true));
                row.getChildren().add(img);
            }
            Hyperlink link = new Hyperlink(title + " (" + year + ")");
            link.setStyle("-fx-text-fill: #a78bfa; -fx-font-size: 14; -fx-font-weight: bold;");
            link.setCursor(Cursor.HAND);
            link.setOnAction(e -> {
                // Fetch and show details for this title
                new Thread(() -> {
                    OmdbResult details = fetchOmdbInfoWithPosterByIdRich(imdbID);
                    javafx.application.Platform.runLater(() -> addChatBubble(chatHistory, details.text, false, details.posterUrl, details.imdbUrl));
                    javafx.application.Platform.runLater(() -> chatScroll.setVvalue(1.0));
                }).start();
            });
            row.getChildren().add(link);
            chatHistory.getChildren().add(row);
        }
        chatScroll.setVvalue(1.0);
    }

    // Fetch detailed info by IMDb ID (rich)
    private OmdbResult fetchOmdbInfoWithPosterByIdRich(String imdbID) {
        try {
            String apiKey = "76dfa4c6";
            String urlStr = "https://www.omdbapi.com/?apikey=" + apiKey + "&i=" + imdbID;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            JSONObject obj = new JSONObject(sb.toString());
            if (obj.has("Response") && obj.getString("Response").equals("True")) {
                return formatOmdbDetailsRich(obj);
            }
        } catch (Exception e) { /* ignore */ }
        return new OmdbResult("Sorry, I couldn't fetch details for that title.", null, null);
    }

    // Fetch detailed info and show poster and IMDb link (rich)
    private OmdbResult fetchOmdbInfoWithPosterRich(String title) {
        try {
            String apiKey = "76dfa4c6";
            String typeParam = "";
            String seasonParam = "";
            String episodeParam = "";
            String lowerTitle = title.toLowerCase();
            int season = extractNumberAfterKeyword(lowerTitle, "season");
            int episode = extractNumberAfterKeyword(lowerTitle, "episode");
            if (lowerTitle.contains("series") || lowerTitle.contains("show")) {
                typeParam = "&type=series";
                title = title.replaceAll("(?i)series|show", "").trim();
            }
            if (season > 0) seasonParam = "&Season=" + season;
            if (episode > 0) episodeParam = "&Episode=" + episode;
            title = title.replaceAll("(?i)season \\d+", "").replaceAll("(?i)episode \\d+", "").trim();
            String urlStr = "https://www.omdbapi.com/?apikey=" + apiKey + "&t=" + URLEncoder.encode(title, "UTF-8") + typeParam + seasonParam + episodeParam;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            JSONObject obj = new JSONObject(sb.toString());
            if (obj.has("Response") && obj.getString("Response").equals("True")) {
                return formatOmdbDetailsRich(obj);
            }
        } catch (Exception e) { /* ignore */ }
        return new OmdbResult("Sorry, I couldn't find info on that title.", null, null);
    }

    // Format OMDb details with poster and IMDb link (rich)
    private OmdbResult formatOmdbDetailsRich(JSONObject obj) {
        StringBuilder info = new StringBuilder();
        info.append("Title: ").append(obj.optString("Title", "N/A")).append("\n");
        info.append("Year: ").append(obj.optString("Year", "N/A")).append("\n");
        info.append("Type: ").append(obj.optString("Type", "N/A")).append("\n");
        info.append("Genre: ").append(obj.optString("Genre", "N/A")).append("\n");
        info.append("IMDb: ").append(obj.optString("imdbRating", "N/A")).append("\n");
        info.append("Plot: ").append(obj.optString("Plot", "N/A")).append("\n");
        String poster = obj.optString("Poster", "");
        String imdbID = obj.optString("imdbID", "");
        String imdbUrl = imdbID.isEmpty() ? null : ("https://www.imdb.com/title/" + imdbID);
        return new OmdbResult(info.toString(), poster, imdbUrl);
    }

    // Helper: extract number after a keyword (e.g., 'season 2')
    private int extractNumberAfterKeyword(String text, String keyword) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(keyword + " (\\d+)").matcher(text);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception e) { return -1; }
        }
        return -1;
    }

    // Improved title extraction: use quoted text, or if not found, use the whole message
    private String extractTitle(String msg) {
        // Try to extract quoted title
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("[\"']([^\"']+)[\"']").matcher(msg);
        if (m.find()) return m.group(1);
        // Remove known keywords
        String cleaned = msg.replaceAll("(?i)movie|series|about|info|recommend|please|show|me|tell|find|watch|of|the|a|an|on|for|give|suggest|\\?", "").trim();
        if (cleaned.length() >= 2) return cleaned;
        // If nothing left, use the original message
        return msg.trim();
    }

    // Helper class for rich OMDb results
    private static class OmdbResult {
        String text;
        String posterUrl;
        String imdbUrl;
        OmdbResult(String text, String posterUrl, String imdbUrl) {
            this.text = text;
            this.posterUrl = posterUrl;
            this.imdbUrl = imdbUrl;
        }
    }
} 
