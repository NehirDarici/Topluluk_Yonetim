package com.proje.controller;

import com.proje.dosya.DosyaIslemleri;
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

public class SosyalBaskanController {

    @FXML
    private StackPane anaIcerik; // FXML'deki fx:id="anaIcerik" ile birebir aynı olmalı

    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Birim Başkanı Paneli Yüklendi!");
    }

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    // --- TO-DO LIST BUTONU (Birim Paylaşımlı) ---
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        try {
            // PDF Madde 8: Loglama (I/O)
            DosyaIslemleri.logEkle("Sosyal Medya Başkanı birim To-Do listesini görüntüledi.");

            System.out.println("To-Do Listesi açılıyor...");
            sayfaGetir("sayfa_todo.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- DİĞER BUTONLAR ---
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_uyeler.fxml");
    }

    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Dosya işlemleri tıklandı.");
        // İleride buraya sayfa_dosyalar.fxml gelecek
    }

    // --- SAYFA GETİRME MOTORU ---
    private void sayfaGetir(String dosyaAdi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (anaIcerik != null) {
                // StackPane'de setCenter yoktur, bu yüzden temizleyip ekliyoruz
                anaIcerik.getChildren().clear();
                anaIcerik.getChildren().add(view);
            } else {
                System.out.println("HATA: 'anaIcerik' (StackPane) null! FXML dosyasında fx:id kontrol et.");
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnCikisYap(ActionEvent event) {
        cikisIslemi(event);
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