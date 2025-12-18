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

public class UyeController {

    @FXML
    private BorderPane anaIcerik;

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        // 'false' parametresini sildik.
        // TakvimController artık üye olduğunu SessionManager'dan kendisi anlıyor.
        sayfaGetir("sayfa_takvim.fxml");
    }

    // --- TO-DO BUTONU ---
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        // 'false' parametresini sildik.
        sayfaGetir("sayfa_todo.fxml");
    }

    // --- DİĞER BUTONLAR ---
    @FXML void btnGorevlerTiklandi(ActionEvent event) { System.out.println("Yetkiniz yok"); }
    @FXML void btnDosyalarTiklandi(ActionEvent event) { System.out.println("Dosyalar"); }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            // Dosya yolunu "/login.fxml" olarak garantiye aldık
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- SAYFA GETİRME MOTORU ---
    // 'boolean yetkiVarMi' parametresini kaldırdık
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosya yolunu düzelttik (Eski kodda uzun bir package yolu vardı, "/" yeterli)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            // --- ESKİ HATALI KOD SİLİNDİ ---
            // ctrl.yetkiAyarla(yetkiVarMi); SATIRI ARTIK YOK.
            // TakvimController initialize() metodunda kendi yetkisini kontrol ediyor.

            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }
}