package com.akarpovich.interior.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DatabaseConnection {

    private static final Object lock = new Object();
    private static DatabaseConnection instance;

    private final Connection connection;

    public DatabaseConnection() {
        try {
            Properties dbProps = new Properties();
            dbProps.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties"));
            Class.forName(dbProps.getProperty("driver"));
            connection = DriverManager.getConnection(
                    dbProps.getProperty("url"),
                    dbProps.getProperty("user"),
                    dbProps.getProperty("password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (lock) {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
        }
        return instance;
    }
}
