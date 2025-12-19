package com.proje.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Veritabanlarının oluşturulduğu kısımdır.
public class DBHelper {
    // Veritabanı dosyasının yolu
    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";

    // static olmasının sebebi her defasında yeni bağlantı olmasını önlemek
    private static Connection connection;

    // Veritabanına bağlanır.
    public static Connection baglan() {
        try {
            // Bağlantı kopmuşsa veya hiç açılmamışsa yeniden aç
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return connection;
    }

    // Açık olan bağlantıyı kapatır.
    public static void kapat() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Uygulama açıldığında tabloları kontrol eder. Eğer tablo boşsa başlangıç değerleri verir.
    public static void tablolariKontrolEt() {
        // Tablo Oluşturma Komutları
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

        String butceTablosu = "CREATE TABLE IF NOT EXISTS Butce ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "aciklama TEXT NOT NULL, "
                + "miktar REAL NOT NULL, "
                + "tarih TEXT, "
                + "tur TEXT NOT NULL"
                + ");";

        String duyuruTablosu = "CREATE TABLE IF NOT EXISTS Duyurular ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "baslik TEXT, metin TEXT, tarih TEXT"
                + ");";

        String gorevlerTablosu = "CREATE TABLE IF NOT EXISTS Gorevler ("
                + "gorev_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "baslik TEXT, aciklama TEXT, tarih TEXT, tur TEXT, birim_id INTEGER"
                + ");";

        try (Connection conn = baglan();
             Statement stmt = conn.createStatement()) {

            // Tabloları oluşturur.
            stmt.execute(birimlerTablosu);
            stmt.execute(kullanicilarTablosu);
            stmt.execute(butceTablosu);
            stmt.execute(duyuruTablosu);
            stmt.execute(gorevlerTablosu);

            // İlk verilerin yüklenmesi
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Kullanicilar");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Veritabanı boş, başlangıç verileri ekleniyor...");

                // Birimleri ekler.
                stmt.execute("INSERT INTO Birimler (birim_id, birim_adi) VALUES (1, 'Yonetim'), (2, 'Etkinlik'), (3, 'Sosyal Medya')");

                // Kullanıcıları ekler.
                stmt.execute("INSERT INTO Kullanicilar (ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES ('2023001', 'Nehir Yılmaz', '12345', 'topluluk_baskani', 1)");
                stmt.execute("INSERT INTO Kullanicilar (ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES ('2023010', 'Zeynep Yılmaz', '123', 'baskan', 2)");
                stmt.execute("INSERT INTO Kullanicilar (ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES ('5643', 'Sude', '456', 'baskan', 3)");
                stmt.execute("INSERT INTO Kullanicilar (ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES ('20658912', 'Mehmet', '36', 'uye', 2)");
                stmt.execute("INSERT INTO Kullanicilar (ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES ('2045', 'Hasan', '63', 'uye', 3)");

                System.out.println("✅ Kullanıcılar ve Birimler başarıyla eklendi!");
            }

        } catch (SQLException e) {
            System.out.println("Veritabanı başlatma hatası: " + e.getMessage());
        }
    }
}