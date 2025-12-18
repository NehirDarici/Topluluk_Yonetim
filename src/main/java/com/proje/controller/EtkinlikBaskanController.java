package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class EtkinlikBaskanController {

    @FXML
    private BorderPane anaIcerik;

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println("Takvim sayfası yükleniyor...");
        // Sadece FXML adını veriyoruz, yetki kontrolünü TakvimController kendisi yapacak.
        sayfaGetir("sayfa_takvim.fxml");
    }

    // sayfaGetir metodun zaten var, onu değiştirmene gerek yok.
    // Ancak anaIcerik (BorderPane) null hatası almamak için FXML kontrolü yapalım (Adım 2).

    // --- ÜYE YÖNETİMİ ---
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üye yönetimi tıklandı.");
        // İleride buraya da sayfaGetir("uye_yonetimi.fxml") gelecek
    }

    // --- GÖREV ATAMA ---
    @FXML
    void btnGorevlerTiklandi(ActionEvent event) {
        System.out.println("Görev atama tıklandı.");
        // sayfaGetir("gorev_atama.fxml");
    }

    // --- DOSYA İŞLEMLERİ ---
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Dosya işlemleri tıklandı.");
    }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            // Login sayfasına geri dön
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml")); // Slash işaretine dikkat
            Scene scene = new Scene(loginPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- SAYFA DEĞİŞTİRME MOTORU (GÜNCELLENDİ) ---
    // 'boolean yetkiVarMi' parametresini sildik, artık gerek yok.
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosya yolunu garantiye alıyoruz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            // --- BURADAKİ ESKİ 'yetkiAyarla' KODUNU SİLDİK ---
            // TakvimController artık kendi yetkisini initialize() metodunda
            // SessionManager üzerinden kontrol ediyor. Dışarıdan müdahaleye gerek kalmadı.

            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }
}