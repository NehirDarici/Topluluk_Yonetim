package com.proje.controller;

import com.proje.dao.UserDAO;
import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;
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
    private TextField emailAlan; // FXML fx:id="emailAlan"

    @FXML
    private PasswordField sifreAlan; // FXML fx:id="sifreAlan"

    // Veritabanı bağlantısı için DAO
    private UserDAO userDAO = new UserDAO();

    @FXML
    protected void girisYap(ActionEvent event) {
        String ogrenciNo = emailAlan.getText().trim();
        String sifre = sifreAlan.getText().trim();

        if (ogrenciNo.isEmpty() || sifre.isEmpty()) {
            System.out.println("UYARI: Lütfen tüm alanları doldurunuz.");
            return;
        }

        System.out.println("Giriş Deneniyor: " + ogrenciNo);

        // 1. ADIM: UserDAO üzerinden giriş kontrolü
        BirimUyesi girisYapan = userDAO.loginUser(ogrenciNo, sifre);

        if (girisYapan != null) {
            System.out.println("✅ Giriş Başarılı! Rol: " + girisYapan.getRol() + " | BirimID: " + girisYapan.getBirimId());

            // 2. ADIM: Oturumu SessionManager'a kaydet
            SessionManager.getInstance().login(girisYapan);

            // 3. ADIM: Rol ve Birime göre doğru sayfayı bul
            String hedefDosya = hedefSayfayiBelirle(girisYapan);

            if (hedefDosya != null) {
                sayfaDegistir(event, hedefDosya);
            } else {
                System.out.println("HATA: Bu kullanıcı için uygun bir sayfa bulunamadı.");
            }

        } else {
            System.out.println("❌ Hatalı kullanıcı adı veya şifre!");
        }
    }

    // --- YÖNLENDİRME MANTIĞI (Aynı Kalıyor) ---
    private String hedefSayfayiBelirle(BirimUyesi user) {
        String rol = user.getRol();
        int birimId = user.getBirimId(); // 1: Yönetim, 2: Etkinlik, 3: Sosyal Medya

        // SENARYO 1: GENEL BAŞKAN
        if ("topluluk_baskani".equalsIgnoreCase(rol)) {
            return "baskan_ekrani.fxml";
        }

        // SENARYO 2: BİRİM BAŞKANLARI
        else if ("baskan".equalsIgnoreCase(rol)) {
            if (birimId == 2) {
                return "etkinlik_baskan.fxml";
            } else if (birimId == 3) {
                return "sosyal_baskan.fxml";
            } else {
                return "baskan_ekrani.fxml";
            }
        }

        // SENARYO 3: ÜYELER
        else {
            if (birimId == 2) {
                return "etkinlik_uye.fxml";
            } else if (birimId == 3) {
                return "sosyal_uye.fxml";
            } else {
                return "baskan_ekrani.fxml"; // Tanımsız durumlar için güvenli çıkış
            }
        }
    }

    @FXML
    void btnSifremiUnuttumTiklandi(ActionEvent event) {
        String girilenNo = emailAlan.getText();
        if(!girilenNo.isEmpty()) {
            System.out.println("Şifre sıfırlama talebi alındı. Öğrenci No: " + girilenNo);
        } else {
            System.out.println("Önce öğrenci numarasını giriniz.");
        }
    }

    @FXML
    void btnCikisYapTiklandi(ActionEvent event) {
        System.out.println("Uygulama kapatılıyor...");
        System.exit(0);
    }

    private void sayfaDegistir(ActionEvent event, String dosyaAdi) {
        try {
            if (!dosyaAdi.startsWith("/")) {
                dosyaAdi = "/" + dosyaAdi;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(dosyaAdi));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("HATA: '" + dosyaAdi + "' sayfası yüklenemedi!");
            e.printStackTrace();
        }
    }
}