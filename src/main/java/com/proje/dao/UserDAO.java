package com.proje.dao;

import com.proje.database.DBHelper;
import com.proje.model.BirimUyesi;

import java.sql.*;
import java.util.ArrayList; // Liste yapısı için gerekli
import java.util.List;

// Bu sınıfta kullanıcı işlemleri yapılır.
public class UserDAO {

    // Kullanıcıların öğrenci no ve şifresinin eşleştiğinden emin olan metod
    public BirimUyesi loginUser(String ogrenciNo, String sifre) {
        BirimUyesi uye = null;
        String sql = "SELECT * FROM Kullanicilar WHERE ogrenci_no = ? AND sifre = ?";

        try (Connection conn = DBHelper.baglan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ogrenciNo);
            pstmt.setString(2, sifre);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    uye = mapResultSetToUye(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return uye;
    }

    // Tüm kullanıcılar listelenir.
    public List<BirimUyesi> getTumKullanicilar() {
        List<BirimUyesi> uyeListesi = new ArrayList<>();
        String sql = "SELECT * FROM Kullanicilar";

        try (Connection conn = DBHelper.baglan();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Her satırı bir BirimUyesi nesnesine çevirip listeye atar.
                uyeListesi.add(mapResultSetToUye(rs));
            }
        } catch (SQLException e) {
            System.out.println("Listeleme Hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return uyeListesi;
    }

    // Kullanıcı silme işlemini yapan metod
    public boolean kullaniciSil(int id) {
        String sql = "DELETE FROM Kullanicilar WHERE kullanici_id = ?";

        try (Connection conn = DBHelper.baglan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            // executeUpdate etkilenen satır sayısını döner. 0'dan büyükse silinmiştir.
            int etkilenenSatir = pstmt.executeUpdate();
            return etkilenenSatir > 0;

        } catch (SQLException e) {
            System.out.println("Silme Hatası: " + e.getMessage());
            return false;
        }
    }

    // Yardımcı metod
    private BirimUyesi mapResultSetToUye(ResultSet rs) throws SQLException {
        return new BirimUyesi(
                rs.getInt("kullanici_id"),
                rs.getString("ogrenci_no"),
                rs.getString("ad_soyad"),
                rs.getString("sifre"),
                rs.getString("rol"),
                rs.getInt("birim_id") // Senin 6. parametren
        );
    }

    // Şifre güncellemeyi sağlayan metod
    public boolean sifreGuncelle(String ogrenciNo, String yeniSifre) throws java.sql.SQLException {
        String sql = "UPDATE Kullanicilar SET sifre = ? WHERE ogrenci_no = ?";
        try (java.sql.Connection conn = com.proje.database.DBHelper.baglan();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, yeniSifre);
            pstmt.setString(2, ogrenciNo);
            return pstmt.executeUpdate() > 0;
        }
    }
}