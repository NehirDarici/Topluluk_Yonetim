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
        System.out.println("Takvim açılıyor...");
        sayfaGetir("sayfa_takvim.fxml", true);
    }

    // --- ÜYE YÖNETİMİ ---
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üye yönetimi tıklandı.");
    }

    // --- GÖREV ATAMA ---
    @FXML
    void btnGorevlerTiklandi(ActionEvent event) {
        System.out.println("Görev atama tıklandı.");
    }

    // --- DOSYA İŞLEMLERİ (FXML'de iki butona bağlanmış, sorun değil) ---
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Dosya işlemleri tıklandı.");
    }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loginPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- SAYFA DEĞİŞTİRME MOTORU ---
    private void sayfaGetir(String dosyaAdi, boolean yetkiVarMi) {
        try {
            // Dosya yolunu garantiye alıyoruz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (dosyaAdi.equals("sayfa_takvim.fxml")) {
                TakvimController ctrl = loader.getController();
                if (ctrl != null) {
                    ctrl.yetkiAyarla(yetkiVarMi);
                }
            }
            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }
}