package com.proje.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    // Veritabanı dosya yolu (Proje kök dizininde)
    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";
    private static Connection connection;

    // Singleton Bağlantı Metodu
    public static Connection baglan() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return connection;
    }

    // Bağlantıyı Kapatma
    public static void kapat() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tablo Kontrolü (Veri Ekleme YOK)
    public static void tablolariKontrolEt() {

        // Tabloların yapısı (Eğer yanlışlıkla silinirse diye güvenlik amaçlı buradalar)
        String birimlerTablosu = "CREATE TABLE IF NOT EXISTS Birimler ("
                + "birim_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "birim_adi TEXT NOT NULL UNIQUE"
                + ");";

        String kullanicilarTablosu = "CREATE TABLE IF NOT EXISTS Kullanicilar ("
                + "kullanici_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ogrenci_no TEXT UNIQUE NOT NULL, "
                + "ad_soyad TEXT NOT NULL, "
                + "sifre TEXT NOT NULL, "
                + "rol TEXT NOT NULL, "
                + "birim_id INTEGER, "
                + "FOREIGN KEY(birim_id) REFERENCES Birimler(birim_id)"
                + ");";

        try (Connection conn = baglan();
             Statement stmt = conn.createStatement()) {

            // 1. Tabloları oluştur (Varsa hiçbir şey yapmaz, yoksa oluşturur)
            stmt.execute(birimlerTablosu);
            stmt.execute(kullanicilarTablosu);

            // 2. Bağlantı Testi: İçerideki mevcut veri sayısını yazdıralım
            System.out.println("Veritabanı bağlantısı başarılı. Tablolar hazır.");

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Kullanicilar")) {
                if (rs.next()) {
                    System.out.println("Mevcut Kullanıcı Sayısı: " + rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Veritabanı başlatma hatası: " + e.getMessage());
        }
    }
}