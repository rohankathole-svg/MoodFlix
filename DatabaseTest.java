import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            String url = "jdbc:postgresql://localhost:5432/moodflix";
            Connection conn = DriverManager.getConnection(url, "postgres", "Pass@1234");
            System.out.println("✅ Database connection successful!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }
}