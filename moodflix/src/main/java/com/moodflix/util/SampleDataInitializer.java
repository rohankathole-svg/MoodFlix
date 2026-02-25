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
        createUserIfMissing(authService, "demo@moodflix.com", "demo123", "user");
        createUserIfMissing(authService, "test@moodflix.com", "test123", "user");
        createUserIfMissing(authService, "guest@moodflix.com", "guest123", "user");

        Content[] demoCatalog = new Content[] {
            // ========== HAPPY MOOD ==========
            new Content(
                "3 Idiots", "Happy", "Movie",
                "https://www.youtube.com/watch?v=K0eDlFX9GMc",
                "A hilarious friendship tale about following your dreams. Three engineering students challenge the educational system with humor and heart.",
                "3 idiots.jpeg"
            ),
            new Content(
                "The Pursuit of Happyness", "Happy", "Movie",
                "https://www.youtube.com/watch?v=dmOORuiIQCg",
                "An inspiring true story of struggle, determination, and triumph. A heartwarming journey of a father's love.",
                "pursuit-of-happyness.jpeg"
            ),
            new Content(
                "Happy Song Mix", "Happy", "Song",
                "https://www.youtube.com/results?search_query=happy+playlist",
                "Upbeat music compilation to boost your mood and energy. Perfect for celebrations and good vibes!",
                "Happy song.jpeg"
            ),
            new Content(
                "Pharrell Williams - Happy", "Happy", "Song",
                "https://www.youtube.com/watch?v=ZbZSe6N_BXs",
                "The ultimate feel-good anthem that will make you clap along and smile instantly.",
                "happy-pharrell.jpeg"
            ),
            
            // ========== ROMANTIC MOOD ==========
            new Content(
                "Sita Ramam", "Romantic", "Movie",
                "https://www.youtube.com/results?search_query=sita+ramam+trailer",
                "A beautiful period romance with stunning visuals. An orphan soldier's life changes when he receives letters from Sita.",
                "sitaRamam.jpeg"
            ),
            new Content(
                "DDLJ - Dilwale Dulhania Le Jayenge", "Romantic", "Movie",
                "https://www.youtube.com/watch?v=lfISFrf6VQk",
                "The iconic Bollywood romance that defined an era. A timeless love story that continues to win hearts.",
                "ddlj.jpeg"
            ),
            new Content(
                "The Notebook", "Romantic", "Movie",
                "https://www.youtube.com/watch?v=4M7LOFKAmLc",
                "A passionate love story that spans decades. The ultimate romantic movie that proves true love never dies.",
                "the-notebook.jpeg"
            ),
            new Content(
                "Romantic Melodies", "Romantic", "Song",
                "https://www.youtube.com/results?search_query=romantic+songs+playlist",
                "Beautiful love songs perfect for romantic moments. Express your feelings with timeless melodies.",
                "romantic-songs.jpeg"
            ),
            
            // ========== SAD MOOD ==========
            new Content(
                "Dil Bechara", "Sad", "Movie",
                "https://www.hotstar.com",
                "An emotional journey of love and loss. Sushant Singh Rajput's final film that touches hearts deeply.",
                "dil bechara.jpeg"
            ),
            new Content(
                "Taare Zameen Par", "Sad", "Movie",
                "https://www.youtube.com/watch?v=TrsV_iVfZJo",
                "A moving story about a dyslexic child. Aamir Khan's masterpiece that will bring tears to your eyes.",
                "taare-zameen-par.jpeg"
            ),
            new Content(
                "Marley & Me", "Sad", "Movie",
                "https://www.youtube.com/watch?v=WZUVsW19HTg",
                "A heartwarming yet tearful story of a family and their lovable dog. Be ready with tissues!",
                "marley-and-me.jpeg"
            ),
            new Content(
                "Sad Songs Collection", "Sad", "Song",
                "https://www.youtube.com/results?search_query=sad+songs+playlist",
                "Emotional tracks for when you need to let it all out. Sometimes we need music that understands our pain.",
                "sad-songs.jpeg"
            ),
            
            // ========== THRILLER MOOD ==========
            new Content(
                "Stranger Things", "Thriller", "Series",
                "https://www.netflix.com",
                "Mystery, sci-fi, and supernatural thrills in 1980s Indiana. A group of kids uncover dark government secrets.",
                "strangerthings.jpeg"
            ),
            new Content(
                "Drishyam", "Thriller", "Movie",
                "https://www.youtube.com/watch?v=AuuX2j14HSg",
                "A gripping thriller about a man protecting his family. Mind-bending plot twists that will keep you guessing!",
                "drishyam.jpeg"
            ),
            new Content(
                "Money Heist", "Thriller", "Series",
                "https://www.netflix.com",
                "Eight thieves take hostages in Spain's Royal Mint. Edge-of-your-seat heist drama with brilliant strategy.",
                "money-heist.jpeg"
            ),
            new Content(
                "Thriller Songs Mix", "Thriller", "Song",
                "https://www.youtube.com/results?search_query=thriller+songs",
                "Dark, intense tracks for high-energy moments. Perfect background for suspenseful activities.",
                "Thriller Songs.jpeg"
            ),
            
            // ========== COMEDY MOOD ==========
            new Content(
                "Gullak", "Comedy", "Series",
                "https://www.sonyliv.com",
                "A heartwarming family comedy about middle-class life. Simple, relatable, and genuinely funny moments.",
                "Gullak.jpeg"
            ),
            new Content(
                "Hostel Daze", "Comedy", "Series",
                "https://www.primevideo.com",
                "College hostel life portrayed with perfect humor. Nostalgia and laughter guaranteed!",
                "Hostel daze.jpeg"
            ),
            new Content(
                "PK", "Comedy", "Movie",
                "https://www.youtube.com/watch?v=80D0gRMi1RY",
                "An alien's hilarious take on human behavior and religion. Comedy with a meaningful message.",
                "pk.jpeg"
            ),
            new Content(
                "The Office", "Comedy", "Series",
                "https://www.netflix.com",
                "Mockumentary-style sitcom about office life. Michael Scott's antics will have you laughing non-stop!",
                "the-office.jpeg"
            ),
            new Content(
                "Comedy Songs Mix", "Comedy", "Song",
                "https://www.youtube.com/results?search_query=funny+songs",
                "Funny and quirky songs to make you laugh. Light-hearted tunes for a good time!",
                "comedy-songs.jpeg"
            ),
            
            // ========== CALM/RELAXED MOOD ==========
            new Content(
                "Little Things", "Calm", "Series",
                "https://www.netflix.com",
                "A gentle series about everyday relationship moments. Simple, sweet, and incredibly relatable.",
                "little things.jpeg"
            ),
            new Content(
                "Panchayat", "Calm", "Series",
                "https://www.primevideo.com",
                "Small-town life portrayed beautifully. Slow-paced, charming, and full of heart.",
                "panchayat.jpeg"
            ),
            new Content(
                "Zindagi Na Milegi Dobara", "Calm", "Movie",
                "https://www.youtube.com/watch?v=ItmN6HCqPrM",
                "A road trip through Spain that changes three friends forever. About living life to the fullest.",
                "znmd.jpeg"
            ),
            new Content(
                "Calm Instrumentals", "Calm", "Song",
                "https://www.youtube.com/results?search_query=relaxing+music",
                "Soothing instrumental music for meditation and relaxation. Perfect for unwinding after a long day.",
                "calm-music.jpeg"
            ),
            new Content(
                "Lofi Hip Hop Radio", "Calm", "Song",
                "https://www.youtube.com/results?search_query=lofi+hip+hop",
                "Chill beats to study, relax, or work to. The perfect background for focused productivity.",
                "lofi.jpeg"
            ),
            
            // ========== ENERGETIC/MOTIVATIONAL ==========
            new Content(
                "Rocky", "Energetic", "Movie",
                "https://www.youtube.com/watch?v=3VdqpzO01ds",
                "The ultimate underdog story. A small-time boxer gets a shot at the heavyweight championship.",
                "rocky.jpeg"
            ),
            new Content(
                "Chak De India", "Energetic", "Movie",
                "https://www.youtube.com/watch?v=68ApmxBx5OI",
                "A disgraced hockey player coaches the Indian women's team to glory. Pure motivation and pride!",
                "chak-de.jpeg"
            ),
            new Content(
                "Workout Motivation Mix", "Energetic", "Song",
                "https://www.youtube.com/results?search_query=workout+motivation+songs",
                "High-energy tracks to power through your workout. Get pumped and push your limits!",
                "workout-songs.jpeg"
            )
        };

        int inserted = 0;
        for (Content item : demoCatalog) {
            if (createContentIfMissing(contentService, item)) {
                inserted++;
            }
        }

        System.out.println("[seed] Completed. Added " + inserted + " new content records.");
        System.out.println("=".repeat(60));
        System.out.println("🎬 MOODFLIX - DEMO ACCOUNTS");
        System.out.println("=".repeat(60));
        System.out.println("Admin Account  : admin@moodflix.com / admin123");
        System.out.println("Demo Account   : demo@moodflix.com  / demo123");
        System.out.println("Test Account   : test@moodflix.com  / test123");
        System.out.println("Guest Account  : guest@moodflix.com / guest123");
        System.out.println("=".repeat(60));
        System.out.println("✅ Total Content Available: " + (demoCatalog.length) + " items across all moods");
        System.out.println("=".repeat(60));
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

