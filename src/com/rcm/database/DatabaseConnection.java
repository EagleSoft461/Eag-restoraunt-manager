package com.rcm.database;

import java.sql.*;

public class DatabaseConnection {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "eag_restaurant_db";
    private static final String URL = BASE_URL + DATABASE_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "47749099";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Önce veritabanı yoksa oluştur
                createDatabaseIfNotExists();

                // Sonra bağlan
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("MySQL veritabanına bağlandı!");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    private static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Veritabanını oluştur
            stmt.execute("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            System.out.println("Veritabanı oluşturuldu: " + DATABASE_NAME);

        } catch (SQLException e) {
            System.err.println("Veritabanı oluşturma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        Connection conn = getConnection();
        if (conn == null) {
            System.err.println("Veritabanı bağlantısı kurulamadı!");
            return;
        }

        try (Statement stmt = conn.createStatement()) {

            // Tabloları oluştur
            String createMenuTable = "CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price DECIMAL(10,2) NOT NULL," +
                    "category VARCHAR(50) NOT NULL," +
                    "description TEXT," +
                    "image_url VARCHAR(255)," +
                    "is_available BOOLEAN DEFAULT TRUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "table_number INT NOT NULL," +
                    "total_amount DECIMAL(10,2) NOT NULL," +
                    "status ENUM('PENDING', 'PREPARING', 'READY', 'COMPLETED') DEFAULT 'PENDING'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "completed_at TIMESTAMP NULL" +
                    ")";

            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "order_id INT," +
                    "menu_item_id INT," +
                    "quantity INT NOT NULL," +
                    "price DECIMAL(10,2) NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)" +
                    ")";

            stmt.execute(createMenuTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);

            // Örnek veriler
            String checkData = "SELECT COUNT(*) FROM menu_items";
            ResultSet rs = stmt.executeQuery(checkData);
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSampleData = "INSERT INTO menu_items (name, price, category, description) VALUES " +
                        "('Lahmacun', 25.00, 'Ana Yemek', 'İnce hamur üzerine kıymalı harç'), " +
                        "('Adana Kebap', 85.00, 'Ana Yemek', 'Özel baharatlı kıyma'), " +
                        "('Ayran', 8.00, 'İçecek', 'Doğal yoğurt'), " +
                        "('Künefe', 35.00, 'Tatlı', 'Tel kadayıf ve peynir'), " +
                        "('Çiğ Köfte', 30.00, 'Başlangıç', 'Etli çiğ köfte'), " +
                        "('Pide', 40.00, 'Ana Yemek', 'Çeşitli malzemelerle')";

                stmt.execute(insertSampleData);
                System.out.println("Örnek veriler eklendi!");
            }

            System.out.println("MySQL veritabanı hazır!");

        } catch (SQLException e) {
            System.err.println("Veritabanı başlatma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı!");
            }
        } catch (SQLException e) {
            System.err.println("Bağlantı kapatma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}