package com.proje.dao;

import com.proje.database.DBHelper;
import com.proje.model.ButceKaydi;
import com.proje.model.IslemTuru;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Bu sınıfın amacı, bütçe kısmındaki işlemleri yapmaktır.
public class ButceDAO {

    // Tüm kayıtlar listelenir.
    public List<ButceKaydi> tumKayitlariGetir() {
        List<ButceKaydi> liste = new ArrayList<>();
        String sql = "SELECT * FROM Butce ORDER BY id DESC";

        try (Connection conn = DBHelper.baglan();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String turStr = rs.getString("tur");
                IslemTuru turEnum;

                try {
                    turEnum = IslemTuru.valueOf(turStr);
                } catch (IllegalArgumentException e) {
                    // Eğer veritabanında bozuk veri varsa varsayılan olarak DIGER yapar.
                    turEnum = IslemTuru.DIGER;
                }

                liste.add(new ButceKaydi(
                        rs.getInt("id"),
                        rs.getString("aciklama"),
                        rs.getDouble("miktar"),
                        rs.getString("tarih"),
                        turEnum
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    // Bütçe tablosuna yeni girilen verileri ekler.
    public boolean ekle(String aciklama, double miktar, String tarih, IslemTuru tur) throws SQLException {
        String sql = "INSERT INTO Butce(aciklama, miktar, tarih, tur) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBHelper.baglan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, aciklama);
            pstmt.setDouble(2, miktar);
            pstmt.setString(3, tarih);
            pstmt.setString(4, tur.name());

            return pstmt.executeUpdate() > 0;
        }
    }

    // Silme metoodu
    public boolean sil(int id) {
        String sql = "DELETE FROM Butce WHERE id = ?";
        try (Connection conn = DBHelper.baglan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}