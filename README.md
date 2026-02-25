# 🎬 MoodFlix - Mood-Based Content Recommendation System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-blue.svg)](https://openjfx.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-316192.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36.svg)](https://maven.apache.org/)

**MoodFlix** is an intelligent content recommendation platform that suggests movies, series, and songs based on your current mood. Built with JavaFX and PostgreSQL, it provides a seamless user experience with modern UI/UX design and robust mood-based filtering.

---

## 🌟 Key Features

### For Users
- **🎭 Mood-Based Recommendations** - Get personalized content suggestions based on 7+ moods:
  - Happy, Romantic, Sad, Thriller, Comedy, Calm, Energetic
- **📊 User Dashboard** - Personalized dashboard with mood tracking and activity history
- **📝 Watchlist Management** - Save and organize your favorite content
- **💬 Feedback System** - Share your thoughts and rate recommended content
- **📈 Activity Tracking** - Monitor your viewing patterns and mood trends
- **👤 Profile Management** - Update personal information and preferences
- **🎵 Multi-Format Support** - Movies, TV Series, and Music all in one place

### For Administrators
- **🔧 Admin Dashboard** - Comprehensive content management system
- **➕ Content Upload** - Add new movies, series, and songs with metadata
- **✏️ Content Editing** - Update existing content details and categorization
- **🗑️ Content Deletion** - Remove outdated or inappropriate content
- **👥 User Management** - View and manage user accounts
- **📊 Analytics** - Track platform usage and user engagement statistics
- **💬 Feedback Monitoring** - Review and respond to user feedback

---

## 🛠️ Technology Stack

### Frontend
- **JavaFX 17.0.2** - Modern desktop application framework
- **CSS3** - Custom theming and responsive UI design
- **Scene Builder** - UI layout design

### Backend
- **Java 17** - Core programming language
- **PostgreSQL** - Relational database for data persistence
- **JDBC** - Database connectivity
- **JSON** - Data interchange format

### Tools & Build
- **Maven** - Dependency management and build automation
- **Git** - Version control
- **IntelliJ IDEA** - Primary IDE

---

## 📋 Prerequisites

Before running MoodFlix, ensure you have the following installed:

1. **Java Development Kit (JDK) 17+**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version`

2. **Apache Maven 3.6+**
   - Download from [Maven Official Site](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **PostgreSQL 14+**
   - Download from [PostgreSQL Official Site](https://www.postgresql.org/download/)
   - Create a database named `moodflix`

4. **Git** (for cloning the repository)
   - Download from [Git Official Site](https://git-scm.com/)

---

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/rohankathole-svg/MoodFlix.git
cd MoodFlix
```

### 2. Configure Database
Update the database configuration in:
```
moodflix/src/main/java/com/moodflix/database/DatabaseConfig.java
```

Default settings:
```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/moodflix";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

### 3. Build the Project
```bash
cd moodflix
mvn clean install
```

### 4. Run the Application

**Normal User Mode:**
```bash
mvn javafx:run
```

**Admin Mode:**
```bash
mvn javafx:run -Dargs="admin"
```

---

## 🔐 Demo Accounts

Use these pre-configured accounts for testing:

| Role  | Email                 | Password  | Purpose                |
|-------|-----------------------|-----------|------------------------|
| Admin | admin@moodflix.com    | admin123  | Full admin access      |
| User  | demo@moodflix.com     | demo123   | General demonstration  |
| User  | test@moodflix.com     | test123   | Testing features       |
| User  | guest@moodflix.com    | guest123  | Guest access           |

---

## 📂 Project Structure

```
MoodFlix/
├── moodflix/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/moodflix/
│   │   │   │   ├── admin/          # Admin-specific components
│   │   │   │   ├── api/            # API services
│   │   │   │   ├── controller/     # MVC controllers
│   │   │   │   ├── database/       # Database configuration
│   │   │   │   ├── model/          # Data models (User, Content, etc.)
│   │   │   │   ├── service/        # Business logic services
│   │   │   │   ├── util/           # Utility classes
│   │   │   │   ├── view/           # JavaFX views/UI
│   │   │   │   └── Main.java       # Application entry point
│   │   │   └── resources/
│   │   │       ├── images/         # Content thumbnails
│   │   │       └── *.css           # Stylesheets
│   ├── pom.xml                     # Maven configuration
│   └── README.md                   # Detailed documentation
└── README.md                       # This file
```

---

## 💡 How It Works

### User Flow
1. **Login/Signup** - Users create an account or log in
2. **Select Mood** - Choose from 7+ mood categories
3. **Browse Content** - View personalized recommendations
4. **Interact** - Add to watchlist, provide feedback, watch content
5. **Track Activity** - View history and mood patterns

### Admin Flow
1. **Admin Login** - Access with admin credentials
2. **Manage Content** - Upload, edit, or delete content
3. **Monitor Users** - View user statistics and activity
4. **Review Feedback** - Analyze user feedback and ratings

### Architecture
```
┌─────────────┐
│   JavaFX    │  ← Presentation Layer
│     UI      │
└──────┬──────┘
       │
┌──────▼──────┐
│ Controllers │  ← Business Logic Layer
│  & Services │
└──────┬──────┘
       │
┌──────▼──────┐
│ PostgreSQL  │  ← Data Layer
│  Database   │
└─────────────┘
```

---

## 🎨 UI Highlights

- **Modern Dark Theme** - Eye-friendly interface with custom color palette
- **Responsive Design** - Adapts to different window sizes
- **Smooth Animations** - Polished transitions and hover effects
- **Intuitive Navigation** - Easy-to-use sidebar and breadcrumb navigation
- **Rich Content Cards** - Visual content representation with images and details

---

## 📊 Database Schema

### Main Tables
- **users** - User account information and authentication
- **content** - Movies, series, and songs catalog
- **mood_entries** - User mood tracking history
- **watchlist** - User's saved content
- **feedback** - User reviews and ratings
- **activity** - User activity logs

---

## 🔮 Future Enhancements

- [ ] AI-powered mood detection using facial recognition
- [ ] Social features (friends, sharing recommendations)
- [ ] Advanced filtering (genre, year, rating, language)
- [ ] Mobile app version (Android/iOS)
- [ ] Integration with streaming platforms (Netflix, Prime Video)
- [ ] Multi-language support
- [ ] Offline mode with local caching
- [ ] Recommendation algorithm improvements using ML
- [ ] User reviews and community ratings

---

## 🤝 Contributing

Contributions are welcome! If you'd like to improve MoodFlix:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is developed as part of an academic project. Feel free to use it for educational purposes.

---

## 👨‍💻 Author

**Roshan Kathole**
- GitHub: [@rohankathole-svg](https://github.com/rohankathole-svg)
- Email: Available on request

---

## 🙏 Acknowledgments

- JavaFX Documentation and Community
- PostgreSQL Team
- Maven Central Repository
- All the amazing content creators whose work inspired this platform

---

## 📞 Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/rohankathole-svg/MoodFlix/issues) page
2. Create a new issue with detailed description
3. Contact the development team

---

## ⭐ Star This Repository

If you find MoodFlix useful or interesting, please consider giving it a star! It helps increase visibility and motivates further development.

---

**Made with ❤️ by the MoodFlix Team**
