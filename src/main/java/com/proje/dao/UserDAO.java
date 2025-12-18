package com.proje.dao;

import com.proje.interfaces.IUserService;
import com.proje.model.BirimBaskani;
import com.proje.model.BirimUyesi;
import com.proje.utility.DatabaseUtility; // Utility import edildi

import java.sql.*;

public class UserDAO implements IUserService {

    // ARTIK BURADA "DB_URL" veya "connect()" metoduna gerek yok!
    // DatabaseUtility sınıfını kullanacağız.

    @Override
    public boolean addUser(BirimUyesi uye) {
        String sql = "INSERT INTO Kullanicilar(ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES(?,?,?,?,?)";

        // DİKKAT: 'this.connect()' yerine 'DatabaseUtility.connect()' yazdık
        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uye.getOgrenciNo());
            pstmt.setString(2, uye.getAdSoyad());
            pstmt.setString(3, uye.getSifre());
            pstmt.setString(4, uye.getRol());
            pstmt.setInt(5, uye.getBirimId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BirimUyesi loginUser(String no, String sifre) {
        String sql = "SELECT * FROM Kullanicilar WHERE ogrenci_no = ? AND sifre = ?";

        // DİKKAT: 'DatabaseUtility.connect()' kullanıldı
        try (Connection conn = DatabaseUtility.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, no);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                // Polymorphism
                if (rol.contains("baskan")) {
                    return new BirimBaskani(rs.getInt("kullanici_id"), rs.getString("ogrenci_no"), rs.getString("ad_soyad"), rs.getString("sifre"), rol, rs.getInt("birim_id"));
                } else {
                    return new BirimUyesi(rs.getInt("kullanici_id"), rs.getString("ogrenci_no"), rs.getString("ad_soyad"), rs.getString("sifre"), rol, rs.getInt("birim_id"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}