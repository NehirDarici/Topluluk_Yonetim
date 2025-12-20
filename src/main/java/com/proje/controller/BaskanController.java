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

/*
BaskanController Sınıfı , baskan rolundeki kişinin panelini yönetir.
Sol tarafta bulunan butonlara tıkladığında, ortadaki kısım güncellenir.
 */

public class BaskanController {

    @FXML
    /* .fxml dosyasındaki bileşenle eşleşir
    BorderPane,  sayfaların yükleneceği kısımdır
     */
    private BorderPane anaIcerik; // FXML'deki fx:id="anaIcerik" ile aynı olmalı!

    // Takvim Butonu || tıklandığında sayfa_takvim.fxml dosyası ana ekrana yüklenir
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println("Takvim açılıyor...");
        sayfaGetir("sayfa_takvim.fxml");
    }

    // TO-Do list butonu || sayfa_todo.fxml dosyasını ana ekrana yükler
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        sayfaGetir("sayfa_todo.fxml");
    }

    // Üye Yönetimi butonu || sayfa_uyeler.fxml dosyasını ana ekrana yükler
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üye Yönetimi Sayfası Yükleniyor...");
        sayfaGetir("sayfa_uyeler.fxml");
    }

    // Bütçe Yönetim Butonu
    // sayfa_butce.fxml dosyasını ana ekrana yükler.
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Bütçe Yönetimi açılıyor...");
        sayfaGetir("sayfa_butce.fxml");
    }

    // Tıklanan duruma göre sayfayı günceller.

    //Responsive= Esnek Aya, ekranla birlikte sayfayı yerleştirme işlemi
    private void sayfaGetir(String dosyaAdi) {
        try {
            //Ana iççerik dediğimiz kutu var mı yok mu ona bakıyoruz.
            //fxmlde fx: id yazılmamışsa anaicerik null olur

            if (anaIcerik == null) {
                System.out.println("KRİTİK HATA: 'anaIcerik' (BorderPane) null! FXML'de fx:id kontrol et.");
                return;
            }

            //hangi dosya yüklenecek, konsola yazıyoruz.
            System.out.println("Yükleniyor: " + dosyaAdi);
            //get resource ile kaynak dizininden dosyayı alıyoruz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            //.load ile fxmi okuyup döndürüyor
            Pane view = loader.load();

            //  TAM EKRAN YAPMA AYARI (Responsive)
            // Yüklenen sayfanın boyutunu, anaIcerik'in boyutuna bağlıyoruz
            // Düzenleyici paneller region türündendir. Eğer öyleyse ona göre ayarlamaları yapıyoruz
            if (view instanceof Region) {

                //genişleyip daralmasını sağlar
                Region regionView = (Region) view;
                //max genişlik sınırını kaldırır
                regionView.setMaxWidth(Double.MAX_VALUE);
                //max yükseklik sınırını kaldırır
                regionView.setMaxHeight(Double.MAX_VALUE);
                //tercih edilen boyutları standart boyutları kullandırır
                regionView.setPrefWidth(Region.USE_COMPUTED_SIZE);
                regionView.setPrefHeight(Region.USE_COMPUTED_SIZE);

                // BorderPane'in boyutuna göre genişlemesini sağla
                regionView.prefWidthProperty().bind(anaIcerik.widthProperty());
                regionView.prefHeightProperty().bind(anaIcerik.heightProperty());
            }

            // Yüklenen görüntüyü BorderPane ortasına yerleştirir
            anaIcerik.setCenter(view);
            System.out.println("BAŞARILI: " + dosyaAdi + " sahneye yerleştirildi.");

        } catch (IOException e) {
            //IOEXCEPTİON = FXML DOSYASI BULUNAMAZSA veya okunamazsa fırlatır
            System.out.println("DOSYA BULUNAMADI: /" + dosyaAdi);
            e.printStackTrace();
        } catch (Exception e) {
            //beklenmeyen hatalar için catch bloğu

            System.out.println("BEKLENMEYEN HATA: Controller veya FXML içinde sorun var.");
            e.printStackTrace();
        }
    }

    // ÇIKIŞ YAP
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            //login.fxml dosyasını yükle
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));

             //event.getsource--> olayı kim başlattı onu alır.
            //node ile tıklanan şeyin ne olduğunu alır
            //butona hangi sahnedesin diye sorar
            //get window sahnenin takılı olduğu pencereyi bulur
            //pencereyi stageye çevirir( sahne değiştirmme yetkisi stagede)
            //stage elimiizde istenen sayfa yüklenir
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //eski görüntüyü dğeiştir, login sayfasını ekrana yerleştir.
            stage.setScene(new Scene(loginPage));
            // güncellenen sayfayı göster
            stage.show();
        } catch (IOException e) {
            //login.fxml yoksa bu kısma gelir
            e.printStackTrace();
        }
    }
}