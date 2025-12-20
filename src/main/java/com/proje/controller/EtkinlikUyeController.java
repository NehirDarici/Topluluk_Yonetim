package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class EtkinlikUyeController {

    // FXML'deki merkeze yerleştirdiğiniz StackPane'in fx:id'si ile aynı olmalı
    @FXML
    private StackPane anaIcerik;

    @FXML
    public void initialize() {
        System.out.println("✅ Etkinlik Üye Ekranı Başarıyla Yüklendi!");
    }

    // TAKVİM BUTONU
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println("Etkinlik Üye: Takvim sadece görüntüleme modunda açılıyor...");
        sayfaGetir("sayfa_takvim.fxml");
    }
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("Birim paylaşımlı To-Do listesi açılıyor...");
        sayfaGetir("sayfa_todo.fxml");
    }

    // SAYFA GETİRME MOTORU
    private void sayfaGetir(String dosyaAdi) {
        try {
            // fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (anaIcerik != null) {
                // Mevcut içeriği temizle
                anaIcerik.getChildren().clear();
                //ve yenisini ekle
                anaIcerik.getChildren().add(view);
            } else {
                // Eğer FXML dosyasında fx:id vermeyi unuttuysan burası seni uyarır.
                System.out.println("HATA: 'anaIcerik' StackPane bulunamadı. FXML'de fx:id kontrol et!");
            }
        } catch (IOException e) {
            //dosya adı yanlışsa çalışır
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }

    // Çıkış yap metodu (Diğer sayfalarla uyumlu olması için düzeltildi)
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            //login sayfasını hazırla
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            //şu anki pencereyi yakala
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //görüntüyü değiştir
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}