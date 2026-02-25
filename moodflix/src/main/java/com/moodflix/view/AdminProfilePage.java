package com.moodflix.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import com.moodflix.util.ThemeManager;

public class AdminProfilePage {
    private VBox view;
    private Label emailLabel;
    private Label displayNameLabel;
    private TextField displayNameField;
    private ImageView profileImageView;
    private Button uploadPicBtn;
    private Button saveBtn;
    private Label statusLabel;
    private Button backBtn;
    private Label userNameLabel;
    private Label userRoleLabel;
    private Label joinDateLabel;
    private Label lastLoginLabel;
    private VBox userDetailsBox;
    private VBox profileSection;
    private VBox detailsSection;
    private ScrollPane scrollPane;
    private TextField ageField;
    private ComboBox<String> genderField;

    public AdminProfilePage(String adminEmail) {
        createView(adminEmail);
    }

    private void createView(String adminEmail) {
        view = new VBox(28);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(40));
        view.getStyleClass().add("auth-container");

        Label title = new Label("👑 Admin Profile");
        title.getStyleClass().add("hero-title");
        title.setStyle("-fx-font-size: 30px;");

        createProfileSection();
        createUserDetailsSection(adminEmail);
        createActionButtons();

        view.getChildren().addAll(title, profileSection, detailsSection);
        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        ThemeManager.fadeIn(view, 400);
        ThemeManager.slideUp(profileSection, 500, 100);
        ThemeManager.slideUp(detailsSection, 500, 250);
    }

    private void createProfileSection() {
        profileSection = new VBox(18);
        profileSection.setAlignment(Pos.CENTER);
        profileSection.setPadding(new Insets(24));
        profileSection.getStyleClass().add("glass-card");

        profileImageView = new ImageView();
        profileImageView.setFitWidth(150);
        profileImageView.setFitHeight(150);
        profileImageView.setPreserveRatio(true);
        Circle clip = new Circle(75, 75, 75);
        profileImageView.setClip(clip);
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-profile.png"));
            if (!defaultImage.isError()) {
                profileImageView.setImage(defaultImage);
            }
        } catch (Exception e) {
            // No default image available
        }

        uploadPicBtn = new Button("📷 Upload Photo");
        uploadPicBtn.getStyleClass().addAll("btn", "btn-outline");

        profileSection.getChildren().addAll(profileImageView, uploadPicBtn);
    }

    private void createUserDetailsSection(String adminEmail) {
        detailsSection = new VBox(22);
        detailsSection.setAlignment(Pos.CENTER);
        detailsSection.setPadding(new Insets(28));
        detailsSection.getStyleClass().add("glass-card");

        userDetailsBox = new VBox(14);
        userDetailsBox.setAlignment(Pos.CENTER);

        userNameLabel = new Label("👋 Welcome, Admin!");
        userNameLabel.getStyleClass().add("section-title");

        emailLabel = new Label("📧 " + adminEmail);
        emailLabel.getStyleClass().add("label-secondary");

        VBox personalInfoSection = createPersonalInfoSection();
        VBox accountInfoSection = createAccountInfoSection();

        userDetailsBox.getChildren().addAll(
            userNameLabel,
            emailLabel,
            new Separator(),
            personalInfoSection,
            new Separator(),
            accountInfoSection
        );
        detailsSection.getChildren().add(userDetailsBox);
    }

    private VBox createPersonalInfoSection() {
        VBox personalSection = new VBox(14);
        personalSection.setAlignment(Pos.CENTER);
        personalSection.setPadding(new Insets(20));
        personalSection.getStyleClass().add("card");

        Label sectionTitle = new Label("👑 Admin Information");
        sectionTitle.getStyleClass().add("section-title");

        Label fullNameLabel = new Label("👤 Full Name:");
        fullNameLabel.getStyleClass().add("label-secondary");

        displayNameField = new TextField();
        displayNameField.setPromptText("Enter your full name");
        displayNameField.setPrefWidth(300);

        Label ageLabel = new Label("🎂 Age:");
        ageLabel.getStyleClass().add("label-secondary");

        ageField = new TextField();
        ageField.setPromptText("Enter your age");
        ageField.setPrefWidth(120);

        Label genderLabel = new Label("⚧ Gender:");
        genderLabel.getStyleClass().add("label-secondary");

        genderField = new ComboBox<>();
        genderField.getItems().addAll("Male", "Female", "Other");
        genderField.setPromptText("Select gender");
        genderField.setPrefWidth(140);

        userRoleLabel = new Label("👑 Role: Admin");
        userRoleLabel.getStyleClass().add("badge-danger");

        personalSection.getChildren().addAll(
            sectionTitle,
            fullNameLabel,
            displayNameField,
            ageLabel,
            ageField,
            genderLabel,
            genderField,
            userRoleLabel
        );
        return personalSection;
    }

    private VBox createAccountInfoSection() {
        VBox accountSection = new VBox(14);
        accountSection.setAlignment(Pos.CENTER);
        accountSection.setPadding(new Insets(20));
        accountSection.getStyleClass().add("card");

        Label sectionTitle = new Label("📊 Account Information");
        sectionTitle.getStyleClass().add("section-title");

        joinDateLabel = new Label("📅 Joined: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        joinDateLabel.getStyleClass().add("label-muted");

        lastLoginLabel = new Label("🕒 Last Login: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")));
        lastLoginLabel.getStyleClass().add("label-muted");

        Label accountStatusLabel = new Label("Account Status: Active");
        accountStatusLabel.getStyleClass().add("badge-success");

        Label memberSinceLabel = new Label("👑 Admin Since: December 2024");
        memberSinceLabel.getStyleClass().add("label-muted");

        accountSection.getChildren().addAll(
            sectionTitle,
            joinDateLabel,
            lastLoginLabel,
            accountStatusLabel,
            memberSinceLabel
        );
        return accountSection;
    }

    private void createActionButtons() {
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);

        saveBtn = new Button("💾 Save Changes");
        saveBtn.getStyleClass().addAll("btn", "btn-success");

        backBtn = new Button("← Back to Admin Dashboard");
        backBtn.getStyleClass().addAll("btn", "btn-outline");

        buttonBox.getChildren().addAll(saveBtn, backBtn);
        detailsSection.getChildren().add(buttonBox);

        statusLabel = new Label();
        statusLabel.getStyleClass().add("label-accent");
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        detailsSection.getChildren().add(statusLabel);
    }

    // Getters
    public ScrollPane getView() { return scrollPane; }
    public Label getEmailLabel() { return emailLabel; }
    public TextField getDisplayNameField() { return displayNameField; }
    public ImageView getProfileImageView() { return profileImageView; }
    public Button getUploadPicBtn() { return uploadPicBtn; }
    public Button getSaveBtn() { return saveBtn; }
    public Label getStatusLabel() { return statusLabel; }
    public Button getBackBtn() { return backBtn; }
    public Label getUserNameLabel() { return userNameLabel; }
    public Label getUserRoleLabel() { return userRoleLabel; }
    public Label getJoinDateLabel() { return joinDateLabel; }
    public Label getLastLoginLabel() { return lastLoginLabel; }
    public TextField getAgeField() { return ageField; }
    public ComboBox<String> getGenderField() { return genderField; }
} 