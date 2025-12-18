package com.proje.dao;

import com.proje.model.*;
import com.proje.utility.DatabaseUtility; // Utility import edildi
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GorevDAO {

    // Buradaki "connect()" metodunu sildik, Utility'den çağıracağız.

    public boolean gorevEkle(String baslik, String aciklama, String tarih, String tur, int birimId) {
        String sql = "INSERT INTO Gorevler(baslik, aciklama, tarih, tur, birim_id) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtility.connect(); // Utility kullanımı
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, baslik);
            pstmt.setString(2, aciklama);
            pstmt.setString(3, tarih);
            pstmt.setString(4, tur);
            pstmt.setInt(5, birimId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public List<TakvimOlayi> getTumGorevler() {
        List<TakvimOlayi> liste = new ArrayList<>();
        String sql = "SELECT * FROM Gorevler";

        try (Connection conn = DatabaseUtility.connect(); // Utility kullanımı
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tur = rs.getString("tur");
                if (tur.equals("Sinav") || tur.equals("Tatil")) {
                    liste.add(new OzelGun(rs.getInt("gorev_id"), rs.getString("baslik"), rs.getString("tarih"), tur));
                } else {
                    liste.add(new BirimGorevi(rs.getInt("gorev_id"), rs.getString("baslik"), rs.getString("tarih"), rs.getInt("birim_id")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
}
