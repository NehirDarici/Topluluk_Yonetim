package com.proje.ui;

import com.proje.database.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Önce Veritabanını Hazırla (Tablolar yoksa oluşturur)


            // 2. Login FXML Dosyasını Yükle
            // Dosya yolunun başında '/' olduğuna emin ol (resources kök dizini için)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));

            // 3. Sahne Ayarları
            Scene scene = new Scene(root);

            // Eğer CSS dosyan varsa buraya ekleyebilirsin:
            // scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            primaryStage.setTitle("Topluluk Yönetim Sistemi");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Pencere boyutu sabit kalsın istersen
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("UYGULAMA BAŞLATMA HATASI:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}