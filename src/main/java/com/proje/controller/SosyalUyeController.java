package com.proje.controller;

import com.proje.dosya.DosyaIslemleri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SosyalUyeController {

    @FXML
    private BorderPane anaIcerik;

    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Üye Ekranı Yüklendi!");
    }

    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        try {
            DosyaIslemleri.logEkle("Sosyal Medya Üyesi birim To-Do listesini görüntüledi.");
            System.out.println("To-Do Listesi açılıyor...");
            sayfaGetir("sayfa_todo.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // EKLENEN KISIM: Hata veren eksik metot buraya eklendi
    // ---------------------------------------------------------
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        try {
            // İsteğe bağlı log ekleme (Diğer butonlardaki yapıya uygun olarak)
            DosyaIslemleri.logEkle("Sosyal Medya Üyesi dosyalar sayfasını görüntüledi.");

            System.out.println("Dosyalar sayfası açılıyor...");

            // BURAYA DİKKAT: Eğer dosyalar için hazırladığın bir .fxml dosyası varsa
            // ismini aşağıya yazmalısın. Şimdilik hata vermemesi için boş bıraktım
            // veya örnek bir isim yazdım. Kendi dosya isminle değiştirebilirsin.

            // sayfaGetir("sayfa_dosyalar.fxml"); // <-- Dosya ismini buraya yazıp baştaki // işaretlerini kaldır.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ---------------------------------------------------------

    private void sayfaGetir(String dosyaAdi) {
        try {
            String path = dosyaAdi.startsWith("/") ? dosyaAdi : "/" + dosyaAdi;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();

            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
            } else {
                System.out.println("HATA: 'anaIcerik' null! FXML'de fx:id=\"anaIcerik\" kontrol et.");
            }
        } catch (IOException e) {
            System.err.println("HATA: " + dosyaAdi + " yüklenemedi! Hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            DosyaIslemleri.logEkle("Sosyal Medya Üyesi oturumu kapattı.");
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}