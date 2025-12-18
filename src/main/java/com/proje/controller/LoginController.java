package com.proje.controller;

import com.proje.dao.UserDAO;
import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi; // Senin gerçek modelin
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailAlan; // FXML'de fx:id="emailAlan" olduğundan emin ol (OgrenciNo girilecek)

    @FXML
    private PasswordField sifreAlan;

    // Veritabanı bağlantısı için DAO
    private UserDAO userDAO = new UserDAO();

    @FXML
    protected void girisYap(ActionEvent event) {
        String ogrenciNo = emailAlan.getText().trim();
        String sifre = sifreAlan.getText().trim();

        System.out.println("Giriş Deneniyor: " + ogrenciNo);

        // 1. ADIM: Senin yazdığın GERÇEK UserDAO'yu kullanıyoruz
        BirimUyesi girisYapan = userDAO.loginUser(ogrenciNo, sifre);

        if (girisYapan != null) {
            System.out.println("✅ Giriş Başarılı! Rol: " + girisYapan.getRol());

            // 2. ADIM: Oturumu SessionManager'a kaydet
            SessionManager.getInstance().login(girisYapan);

            // 3. ADIM: Yönlendirme
            String hedefDosya = hedefSayfayiBelirle(girisYapan);
            sayfaDegistir(event, hedefDosya);

        } else {
            System.out.println("❌ Hatalı kullanıcı adı veya şifre!");
            // Buraya ekrana uyarı çıkaran kod eklenebilir
        }
    }

    private String hedefSayfayiBelirle(BirimUyesi user) {
        String rol = user.getRol();
        int birimId = user.getBirimId(); // 2: Etkinlik, 3: Sosyal Medya

        // --- SENARYO 1: BAŞKANLAR (Hepsini başkan ekranına alalım) ---
        if ("topluluk_baskani".equalsIgnoreCase(rol) || "baskan".equalsIgnoreCase(rol)) {
            return "baskan_ekrani.fxml";
        }

        // --- SENARYO 2: ÜYELER (Birime Göre Ayrışacak) ---
        else {
            if (birimId == 2) {
                // Etkinlik Üyesi
                return "etkinlik_uye.fxml";
            }
            else if (birimId == 3) {
                // Sosyal Medya Üyesi
                return "sosyal_uye.fxml";
            }
            else {
                // Tanımsız birimse veya Yönetim üyesiyse (ID=1)
                return "baskan_ekrani.fxml";
            }
        }
    }
    // LoginController.java içine ekle:

    @FXML
    void btnSifremiUnuttumTiklandi(ActionEvent event) {
        // Basit bir dialog penceresi (TextInputDialog kullanılabilir veya yeni sahne)
        // Şimdilik konsol simülasyonu:
        System.out.println("Şifre sıfırlama ekranı açılıyor...");

        // PDF Gereksinimi: String işlemleri (contains, equals vb.) [cite: 31]
        String email = "ornek@ogrenci.edu.tr"; // Normalde kullanıcıdan alınır
        if(email.contains("@") && email.endsWith(".edu.tr")) {
            System.out.println("Mail gönderildi: " + email);
            // Alert (Uyarı) mesajı gösterilebilir
        } else {
            System.out.println("Geçersiz format!");
        }
    }

    @FXML
    void btnCikisYapTiklandi(ActionEvent event) {
        System.out.println("Uygulama kapatılıyor...");
        System.exit(0); // Uygulamayı tamamen kapatır
    }

    private void sayfaDegistir(ActionEvent event, String dosyaAdi) {
        try {
            // Dosya yoluna dikkat! FXML dosyaların resources/ altında ise başına / koyuyoruz.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " dosyası açılamadı! Dosya ismini ve yolunu kontrol et.");
            e.printStackTrace();
        }
    }
}