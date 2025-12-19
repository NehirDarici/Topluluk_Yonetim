package com.proje.dao;

import com.proje.model.*;
import com.proje.utility.DatabaseUtility;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GorevDAO {

    // --- ADIM 2: BİRİME ÖZEL TO-DO LİSTESİNİ GETİRME ---
    // Etkinlik Üye/Başkan veya Sosyal Medya Üye/Başkan için ortak havuz sağlar
    public List<TakvimOlayi> getTodoGorevleriByBirim(int birimId) {
        List<TakvimOlayi> liste = new ArrayList<>();
        // Sadece 'To-Do' türündeki ve belirli birime ait kayıtları seçiyoruz
        String sql = "SELECT * FROM Gorevler WHERE birim_id = ? AND tur = 'To-Do'";

        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // PDF Madde 2.1: Wrapper Sınıf Kullanımı (int -> Integer)
            Integer wrappedBirimId = birimId;
            pstmt.setInt(1, wrappedBirimId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // PDF Madde 4.1: Kalıtım ve Polimorfizm (BirimGorevi nesnesi oluşturuluyor)
                BirimGorevi bg = new BirimGorevi(
                        rs.getString("baslik"),
                        LocalDate.parse(rs.getString("tarih")),
                        rs.getInt("birim_id")
                );
                bg.setId(rs.getInt("gorev_id"));
                liste.add(bg);
            }
        } catch (SQLException e) {
            System.err.println("SQL Hatası (Birim To-Do): " + e.getMessage());
        } finally {
            // PDF Madde 7: finally bloğu kullanımı
            // Kaynaklar try-with-resources ile kapansa da kural gereği log amaçlı kullanıyoruz
            System.out.println("Bilgi: Birim ID " + birimId + " için To-Do sorgusu tamamlandı.");
        }
        return liste;
    }

    // --- MEVCUT METOT: GÖREV SİLME ---
    public boolean gorevSil(int gorevId) {
        String sql = "DELETE FROM Gorevler WHERE gorev_id = ?";

        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gorevId);
            int etkilenenSatir = pstmt.executeUpdate();
            return etkilenenSatir > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- MEVCUT METOT: GÖREV EKLEME ---
    public boolean gorevEkle(String baslik, String aciklama, String tarih, String tur, int birimId) {
        String sql = "INSERT INTO Gorevler(baslik, aciklama, tarih, tur, birim_id) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, baslik);
            pstmt.setString(2, aciklama);
            pstmt.setString(3, tarih);
            pstmt.setString(4, tur);
            pstmt.setInt(5, birimId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- MEVCUT METOT: TÜM GÖREVLERİ GETİRME (Takvim İçin) ---
    public List<TakvimOlayi> getTumGorevler() {
        List<TakvimOlayi> liste = new ArrayList<>();
        String sql = "SELECT * FROM Gorevler";

        try (Connection conn = DatabaseUtility.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tur = rs.getString("tur");
                int id = rs.getInt("gorev_id");

                String tarihStr = rs.getString("tarih");
                LocalDate tarihNesnesi;
                try {
                    tarihNesnesi = LocalDate.parse(tarihStr);
                } catch (DateTimeParseException | NullPointerException e) {
                    tarihNesnesi = LocalDate.now();
                }

                TakvimOlayi olay;

                if (tur.equals("Sinav") || tur.equals("Tatil")) {
                    olay = new OzelGun(id, rs.getString("baslik"), tarihNesnesi, tur);
                } else {
                    BirimGorevi bg = new BirimGorevi(
                            rs.getString("baslik"),
                            tarihNesnesi,
                            rs.getInt("birim_id")
                    );
                    bg.setId(id);
                    olay = bg;
                }
                liste.add(olay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}