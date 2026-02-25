package com.moodflix.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    
    // Database Configuration - Update these with your PostgreSQL setup
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "moodflix";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Pass@1234";
    
    // Connection pool
    private static HikariDataSource dataSource;
    
    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection pool: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        
        // Connection pool settings
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);
        
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    // Validation methods
    public static boolean isConfigured() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static void validateConfiguration() {
        if (!isConfigured()) {
            System.err.println("DATABASE CONFIGURATION ERROR!");
            System.err.println("Please ensure PostgreSQL is running and update DatabaseConfig.java:");
            System.err.println("- Update DB_HOST, DB_PORT, DB_NAME");
            System.err.println("- Update DB_USER and DB_PASSWORD");
            throw new RuntimeException("Database not configured properly. Please check DatabaseConfig.java");
        }
    }
}