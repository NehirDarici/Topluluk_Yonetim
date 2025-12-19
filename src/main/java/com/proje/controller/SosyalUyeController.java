package com.proje.controller;

import com.proje.dosya.DosyaIslemleri;
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

public class SosyalUyeController {

    // FXML'deki merkez alanının tipi BorderPane olduğu için tipi BorderPane yaptık.
    @FXML
    private BorderPane anaIcerik;

    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Üye Ekranı Yüklendi!");
    }

    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_todo.fxml");
    }

    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosya adının başında '/' olduğundan emin oluyoruz
            String path = dosyaAdi.startsWith("/") ? dosyaAdi : "/" + dosyaAdi;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            // Pane yerine Parent olarak yüklemek daha güvenlidir
            Parent view = loader.load();

            if (anaIcerik != null) {
                // BorderPane merkezine yerleştir
                anaIcerik.setCenter(view);
            } else {
                System.out.println("HATA: 'anaIcerik' null! FXML'de fx:id=\"anaIcerik\" kontrol et.");
            }
        } catch (IOException e) {
            System.err.println("HATA: " + dosyaAdi + " yüklenemedi! Hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Sosyal Medya Üye: Dosya işlemleri butonuna basıldı.");
    }

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