package com.proje.dao;

import com.proje.database.DBHelper;
import com.proje.model.BirimUyesi;

import java.sql.*;
import java.util.ArrayList; // Liste yapısı için gerekli
import java.util.List;

public class UserDAO {

    // 1. GİRİŞ YAPMA (LoginController kullanıyor)
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

    // 2. TÜM KULLANICILARI LİSTELEME (UyelerController kullanıyor - HATA BURADA ÇÖZÜLÜYOR)
    public List<BirimUyesi> getTumKullanicilar() {
        List<BirimUyesi> uyeListesi = new ArrayList<>();
        String sql = "SELECT * FROM Kullanicilar";

        try (Connection conn = DBHelper.baglan();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Her satırı bir BirimUyesi nesnesine çevirip listeye atıyoruz
                uyeListesi.add(mapResultSetToUye(rs));
            }
        } catch (SQLException e) {
            System.out.println("Listeleme Hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return uyeListesi;
    }

    // 3. KULLANICI SİLME (UyelerController kullanıyor - DELETE işlemi)
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

    // --- YARDIMCI METOT (Kod tekrarını önlemek için) ---
    // Veritabanından gelen satırı Java Nesnesine çevirir.
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
}