package com.proje.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    // Veritabanı dosyası projenin ana klasöründe 'topluluk_yonetim.db' adıyla oluşacak
    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";
    private static Connection connection;

    // Bağlantı Metodu (Singleton Mantığı)
    public static Connection baglan() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("SQLite bağlantısı başarılı.");
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
                System.out.println("Bağlantı kapatıldı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tabloları Otomatik Oluşturan Metot
    // Bunu Main sınıfında en başta bir kez çağıracağız.
    public static void tablolariOlustur() {
        String birimlerTablosu = "CREATE TABLE IF NOT EXISTS Birimler ("
                + "birim_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "birim_adi TEXT NOT NULL"
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

            stmt.execute(birimlerTablosu);
            stmt.execute(kullanicilarTablosu);


            System.out.println("Tablolar kontrol edildi ve hazır.");

        } catch (SQLException e) {
            System.out.println("Tablo oluşturma hatası: " + e.getMessage());
        }
    }
}