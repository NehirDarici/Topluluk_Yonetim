package com.proje.dao;

import com.proje.model.BirimUyesi; // Artık User değil BirimUyesi kullanıyoruz
import java.sql.*;

public class UserDAO {

    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Ekleme Metodu
    public boolean addUser(BirimUyesi uye) {
        String sql = "INSERT INTO Kullanicilar(ogrenci_no, ad_soyad, sifre, rol, birim_id) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uye.getOgrenciNo());
            pstmt.setString(2, uye.getAdSoyad()); // Miras alınan metot
            pstmt.setString(3, uye.getSifre());   // Miras alınan metot
            pstmt.setString(4, uye.getRol());
            pstmt.setInt(5, uye.getBirimId());

            pstmt.executeUpdate();
            System.out.println("Kullanıcı eklendi: " + uye.getAdSoyad());
            return true;
        } catch (SQLException e) {
            System.out.println("Kullanıcı ekleme hatası: " + e.getMessage());
            return false;
        }
    }

    // Giriş (Login) Metodu
    public BirimUyesi loginUser(String studentNo, String password) {
        String sql = "SELECT * FROM Kullanicilar WHERE ogrenci_no = ? AND sifre = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentNo);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                BirimUyesi uye = new BirimUyesi();
                uye.setId(rs.getInt("kullanici_id")); // Miras
                uye.setOgrenciNo(rs.getString("ogrenci_no"));
                uye.setAdSoyad(rs.getString("ad_soyad")); // Miras
                uye.setSifre(rs.getString("sifre"));      // Miras
                uye.setRol(rs.getString("rol"));
                uye.setBirimId(rs.getInt("birim_id"));
                return uye;
            }
        } catch (SQLException e) {
            System.out.println("Giriş hatası: " + e.getMessage());
        }
        return null;
    }

    // Birim Ekleme (Aynı kalıyor)
    public void addUnit(String unitName) {
        String sql = "INSERT INTO Birimler(birim_adi) VALUES(?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, unitName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Birim zaten var veya hata: " + e.getMessage());
        }
    }
}
