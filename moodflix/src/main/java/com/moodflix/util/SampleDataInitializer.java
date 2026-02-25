package com.moodflix.util;

import com.moodflix.model.Content;
import com.moodflix.service.PostgreSQLAuthService;
import com.moodflix.service.PostgreSQLContentService;

public class SampleDataInitializer {

    public static void initializeSampleData() {
        System.out.println("[seed] Initializing sample users and content...");

        PostgreSQLAuthService authService = new PostgreSQLAuthService();
        PostgreSQLContentService contentService = new PostgreSQLContentService();

        createUserIfMissing(authService, "admin@moodflix.com", "admin123", "admin");
        createUserIfMissing(authService, "user@moodflix.com", "user123", "user");

        Content[] demoCatalog = new Content[] {
            new Content(
                "3 Idiots", "Happy", "Movie",
                "https://www.youtube.com/watch?v=K0eDlFX9GMc",
                "A feel-good friendship drama with humor and heart.",
                "3 idiots.jpeg"
            ),
            new Content(
                "Panchayat", "Feel Good", "Series",
                "https://www.primevideo.com",
                "Small-town comedy drama with relatable storytelling.",
                "panchayat.jpeg"
            ),
            new Content(
                "Stranger Things", "Thriller", "Series",
                "https://www.netflix.com",
                "Mystery and sci-fi adventure with suspense.",
                "strangerthings.jpeg"
            ),
            new Content(
                "Sita Ramam", "Romantic", "Movie",
                "https://www.youtube.com/results?search_query=sita+ramam+trailer",
                "Romantic period drama with emotional depth.",
                "sitaRamam.jpeg"
            ),
            new Content(
                "Happy Song Mix", "Happy", "Song",
                "https://www.youtube.com/results?search_query=happy+playlist",
                "Upbeat music to boost mood and energy.",
                "Happy song.jpeg"
            ),
            new Content(
                "Thriller Songs", "Thriller", "Song",
                "https://www.youtube.com/results?search_query=thriller+songs",
                "Dark, intense tracks for high-energy moments.",
                "Thriller Songs.jpeg"
            ),
            new Content(
                "Little Things", "Calm", "Series",
                "https://www.netflix.com",
                "Light relationship series with warm everyday moments.",
                "little things.jpeg"
            ),
            new Content(
                "Dil Bechara", "Sad", "Movie",
                "https://www.hotstar.com",
                "Emotional story with heartfelt performances.",
                "dil bechara.jpeg"
            ),
            new Content(
                "Gullak", "Comedy", "Series",
                "https://www.sonyliv.com",
                "Family-based slice-of-life comedy drama.",
                "Gullak.jpeg"
            ),
            new Content(
                "Hostel Daze", "Comedy", "Series",
                "https://www.primevideo.com",
                "College-life comedy with high relatability.",
                "Hostel daze.jpeg"
            )
        };

        int inserted = 0;
        for (Content item : demoCatalog) {
            if (createContentIfMissing(contentService, item)) {
                inserted++;
            }
        }

        System.out.println("[seed] Completed. Added " + inserted + " new content records.");
        System.out.println("[seed] Admin login: admin@moodflix.com / admin123");
        System.out.println("[seed] User login : user@moodflix.com / user123");
    }

    private static void createUserIfMissing(PostgreSQLAuthService authService, String email, String password, String role) {
        try {
            authService.signup(email, password, role);
            System.out.println("[seed] User created: " + email + " (" + role + ")");
        } catch (Exception ex) {
            System.out.println("[seed] User already exists: " + email);
        }
    }

    private static boolean createContentIfMissing(PostgreSQLContentService contentService, Content content) {
        try {
            Content existing = contentService.getContentByTitle(content.getTitle());
            if (existing != null) {
                return false;
            }
            contentService.uploadContent(content);
            return true;
        } catch (Exception ex) {
            System.err.println("[seed] Failed to upsert content '" + content.getTitle() + "': " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        initializeSampleData();
    }
}

