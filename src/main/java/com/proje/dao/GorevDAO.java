package com.proje.dao;

import com.proje.model.*;
import com.proje.utility.DatabaseUtility;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// Bu sınıf birimlerin görev işlemlerini yapar.
public class GorevDAO {

    // Etkinlik Üye/Başkan veya Sosyal Medya Üye/Başkan için ortak havuz sağlar.
    public List<TakvimOlayi> getTodoGorevleriByBirim(int birimId) {
        List<TakvimOlayi> liste = new ArrayList<>();
        // Sadece 'To-Do' türündeki ve belirli birime ait kayıtları seçilir.
        String sql = "SELECT * FROM Gorevler WHERE birim_id = ? AND tur = 'To-Do'";

        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Integer wrappedBirimId = birimId;
            pstmt.setInt(1, wrappedBirimId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
            System.out.println("Bilgi: Birim ID " + birimId + " için To-Do sorgusu tamamlandı.");
        }
        return liste;
    }

    // Görev silme metodu
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

    // Görev ekleme metodu
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

    // Tüm görevleri listeleme
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