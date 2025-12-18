package com.proje.dao;

import com.proje.model.*;
import com.proje.utility.DatabaseUtility;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GorevDAO {

    // --- YENİ EKLENEN METOT: GÖREV SİLME ---
    public boolean gorevSil(int gorevId) {
        String sql = "DELETE FROM Gorevler WHERE gorev_id = ?";

        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gorevId);
            int etkilenenSatir = pstmt.executeUpdate();
            return etkilenenSatir > 0; // Silindiyse true döner

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- GÖREV EKLEME ---
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

    // --- TÜM GÖREVLERİ GETİRME ---
    public List<TakvimOlayi> getTumGorevler() {
        List<TakvimOlayi> liste = new ArrayList<>();
        String sql = "SELECT * FROM Gorevler";

        try (Connection conn = DatabaseUtility.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tur = rs.getString("tur");
                int id = rs.getInt("gorev_id"); // ID'yi aldık

                // Tarih Dönüşümü (String -> LocalDate)
                String tarihStr = rs.getString("tarih");
                LocalDate tarihNesnesi;
                try {
                    tarihNesnesi = LocalDate.parse(tarihStr);
                } catch (DateTimeParseException | NullPointerException e) {
                    tarihNesnesi = LocalDate.now();
                }

                TakvimOlayi olay; // Genel değişken

                if (tur.equals("Sinav") || tur.equals("Tatil")) {
                    // OzelGun constructor'ı ID alıyorsa burası kalabilir.
                    // Eğer almıyorsa alt taraftaki setId yöntemini kullanırız.
                    olay = new OzelGun(id, rs.getString("baslik"), tarihNesnesi, tur);
                } else {
                    // BirimGorevi constructor'ı ID almıyordu (3 parametreliydi).
                    // Bu yüzden önce nesneyi oluşturup sonra ID'sini set ediyoruz.
                    BirimGorevi bg = new BirimGorevi(
                            rs.getString("baslik"),
                            tarihNesnesi,
                            rs.getInt("birim_id")
                    );
                    bg.setId(id); // DİKKAT: TakvimOlayi sınıfında setId() olmalı!
                    olay = bg;
                }

                // Eğer OzelGun içinde setId yapmadıysak, garanti olsun diye burada da yapabiliriz:
                // olay.setId(id);

                liste.add(olay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}