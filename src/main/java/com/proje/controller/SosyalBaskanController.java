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

//Bu sınıf, sosyal medya başkanının sayfasındaki menü geçişlerini, takvim ve to-do list araçlara erişimini ve log işlemlerini yapar*/

public class SosyalBaskanController {

    // FXML dosyasındaki orta panel
    @FXML
    private StackPane anaIcerik;

    // Controller çalıştığında bu mesajı verecek metod
    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Birim Başkanı Paneli Yüklendi!");
    }

    // Takvim butonuna tıklandığında takvim sayfasını aynı ekrana getirir.
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    // To-do list butonuna tıklandığında çalışır. Aynı zamanda sisteme log olarak kaydedilir.
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        try {
            DosyaIslemleri.logEkle("Sosyal Medya Başkanı birim To-Do listesini görüntüledi.");

            System.out.println("To-Do Listesi açılıyor...");
            sayfaGetir("sayfa_todo.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Birime bağlı üyelerin olduğu sayfayı açar.
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_uyeler.fxml");
    }


    // Verilen FXML dosyasını yükler.
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Kaynak dosyanın yolunu belirleme
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            if (anaIcerik != null) {
                // StackPane'de setCenter yoktur, bu yüzden temizleyip ekliyoruz.
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

    // Çıkış butonuna basıldığında aktifleşir.
    @FXML
    void btnCikisYap(ActionEvent event) {
        cikisIslemi(event);
    }

    //Çıkış yapar ve login ekranına döndürür.
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