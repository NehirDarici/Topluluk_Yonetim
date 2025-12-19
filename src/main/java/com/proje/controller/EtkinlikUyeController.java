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

    // --- TAKVİM BUTONU ---
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

    // --- SAYFA GETİRME MOTORU ---
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosyayı yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (anaIcerik != null) {
                // Mevcut içeriği temizle ve yenisini ekle
                anaIcerik.getChildren().clear();
                anaIcerik.getChildren().add(view);
            } else {
                System.out.println("HATA: 'anaIcerik' StackPane bulunamadı. FXML'de fx:id kontrol et!");
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Etkinlik Üye: Dosya işlemleri butonuna basıldı.");
    }

    // Çıkış yap metodu (Diğer sayfalarla uyumlu olması için düzeltildi)
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}