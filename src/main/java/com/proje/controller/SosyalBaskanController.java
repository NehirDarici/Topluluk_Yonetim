package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane; // StackPane import edildi
import javafx.stage.Stage;
import java.io.IOException;

public class SosyalBaskanController {

    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Birim Başkanı Paneli Yüklendi!");
    }

    // DÜZELTME 1: FXML'deki yapı StackPane olduğu için türü düzelttik
    @FXML
    private StackPane anaIcerik;

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
    @FXML void btnUyelerTiklandi(ActionEvent event) { System.out.println("Üyeler tıklandı (Boş)"); }
    @FXML void btnGorevlerTiklandi(ActionEvent event) { System.out.println("Görevler tıklandı (Boş)"); }
    @FXML void btnDosyalarTiklandi(ActionEvent event) { System.out.println("Dosyalar tıklandı (Boş)"); }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        cikisIslemi(event);
    }

    // --- SAYFA GETİRME MOTORU ---
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosya yoluna '/' ekledik
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (anaIcerik != null) {
                // DÜZELTME 2: StackPane 'setCenter' desteklemez.
                // İçindekileri temizleyip (clear) yenisini ekliyoruz (addAll)
                anaIcerik.getChildren().clear();
                anaIcerik.getChildren().add(view);
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi! Dosya ismini kontrol et.");
            e.printStackTrace();
        }
    }

    private void cikisIslemi(ActionEvent event) {
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