package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;

public class BaskanController {

    @FXML
    private BorderPane anaIcerik; // FXML'deki fx:id="anaIcerik" ile aynı olmalı!

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println("Takvim açılıyor...");
        sayfaGetir("sayfa_takvim.fxml");
    }

    // --- TO-DO LIST BUTONU ---
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        sayfaGetir("sayfa_todo.fxml");
    }

    // --- DİĞER BUTONLAR ---
    @FXML void btnGorevlerTiklandi(ActionEvent event) { System.out.println("Görevler (Boş)"); }
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üye Yönetimi Sayfası Yükleniyor...");
        sayfaGetir("sayfa_uyeler.fxml");
    }    @FXML void btnDosyalarTiklandi(ActionEvent event) { System.out.println("Dosyalar (Boş)"); }

    // --- SAYFA DEĞİŞTİRME MOTORU ---
    private void sayfaGetir(String dosyaAdi) {
        try {
            // 1. Ana İçerik Alanı Hazır mı?
            if (anaIcerik == null) {
                System.out.println("KRİTİK HATA: 'anaIcerik' (BorderPane) null! FXML'de fx:id kontrol et.");
                return;
            }

            // 2. Dosyayı Yükle
            System.out.println("Yükleniyor: " + dosyaAdi);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load(); // Pane olarak alıyoruz ki boyut ayarı yapabilelim

            // 3. TAM EKRAN YAPMA AYARI (Responsive)
            // Yüklenen sayfanın boyutunu, anaIcerik'in boyutuna bağlıyoruz (Bind)
            if (view instanceof Region) {
                Region regionView = (Region) view;
                // Genişlik ve Yükseklik sınırlarını kaldır
                regionView.setMaxWidth(Double.MAX_VALUE);
                regionView.setMaxHeight(Double.MAX_VALUE);
                regionView.setPrefWidth(Region.USE_COMPUTED_SIZE);
                regionView.setPrefHeight(Region.USE_COMPUTED_SIZE);

                // BorderPane'in boyutuna göre genişlemesini sağla
                regionView.prefWidthProperty().bind(anaIcerik.widthProperty());
                regionView.prefHeightProperty().bind(anaIcerik.heightProperty());
            }

            // 4. Ekrana Yerleştir
            anaIcerik.setCenter(view);
            System.out.println("BAŞARILI: " + dosyaAdi + " sahneye yerleştirildi.");

        } catch (IOException e) {
            System.out.println("DOSYA BULUNAMADI: /" + dosyaAdi);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("BEKLENMEYEN HATA: Controller veya FXML içinde sorun var.");
            e.printStackTrace();
        }
    }

    // --- ÇIKIŞ YAP ---
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