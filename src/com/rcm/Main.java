package com.rcm;

import com.rcm.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // MySQL veritabanını başlat
            DatabaseConnection.initializeDatabase();

            // Ana uygulamayı yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rcm/view/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setTitle("Eag Restoran Yönetim Sistemi");
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Uygulama başarıyla başlatıldı!");

        } catch (Exception e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();

            // Hata durumunda kullanıcıya bilgi ver
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Başlatma Hatası");
            alert.setHeaderText("Uygulama başlatılamadı");
            alert.setContentText("MySQL veritabanına bağlanılamıyor. Lütfen MySQL'in çalıştığından emin olun.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        System.out.println("Uygulama başlatılıyor...");
        launch(args);
    }
}