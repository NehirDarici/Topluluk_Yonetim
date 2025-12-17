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
        String rol = user.getRol(); // Veritabanından gelen: "topluluk_baskani" veya "birim_baskani"

        System.out.println("Yönlendirilecek ROL: " + rol); // Kontrol için yazdıralım

        // --- SENARYO 1: GENEL BAŞKAN ---
        // Veritabanında "topluluk_baskani" yazıyor, buna dikkat!
        if ("topluluk_baskani".equalsIgnoreCase(rol) || "baskan".equalsIgnoreCase(rol)) {
            return "baskan_ekrani.fxml"; // BU DOSYANIN İSMİNİN DOĞRU OLDUĞUNA EMİN MİSİN?
        }

        // --- SENARYO 2: BİRİM BAŞKANI ---
        else if ("birim_baskani".equalsIgnoreCase(rol)) {
            // Şimdilik onları da başkan ekranına atalım veya kendi dosyaları varsa onu yaz
            // return "etkinlik_baskan.fxml";
            return "baskan_ekrani.fxml";
        }

        // --- SENARYO 3: ÜYELER ---
        else {
            // Eğer "uye_ekrani.fxml" diye bir dosyan YOKSA, burası hata verir.
            // O dosya yapılana kadar geçici olarak baskan ekranına yönlendir:
            return "baskan_ekrani.fxml";
        }
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