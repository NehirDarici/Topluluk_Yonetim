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

// Sosyal medya birimindeki üyelerin giriş yapacağı sayfanın işlemlerini yürütür.
public class SosyalUyeController {

    @FXML
    private BorderPane anaIcerik;

    // Sayfa yüklendiğinde bunu konsola yazdırır.
    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Üye Ekranı Yüklendi!");
    }

    // Takvim sayfasını getiren metod
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    // To-do listi getiren metod
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

   // Dosyaların açılmasını sağlayan metod
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        try {
            // İsteğe bağlı log ekleme
            DosyaIslemleri.logEkle("Sosyal Medya Üyesi dosyalar sayfasını görüntüledi.");

            System.out.println("Dosyalar sayfası açılıyor...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sayfanın yüklenmesini sağlayan metod
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Kaynak dosyanın yolunu belirleme
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

    // Çıkış yapma metodu
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