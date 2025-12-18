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

public class SosyalBaskanController {

    @FXML
    private BorderPane anaIcerik;

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println("Takvim açılıyor...");
        // ARTIK 'true' veya 'false' GÖNDERMİYORUZ.
        // TakvimController, kimin giriş yaptığını SessionManager'dan kendisi öğreniyor.
        sayfaGetir("sayfa_takvim.fxml");
    }

    // --- TO-DO LIST BUTONU ---
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        // Burada da parametreye gerek yok
        sayfaGetir("sayfa_todo.fxml");
    }

    // --- DİĞER BUTONLAR (Placeholder) ---
    @FXML void btnUyelerTiklandi(ActionEvent event) { System.out.println("Üyeler tıklandı (Boş)"); }
    @FXML void btnGorevlerTiklandi(ActionEvent event) { System.out.println("Görevler tıklandı (Boş)"); }
    @FXML void btnDosyalarTiklandi(ActionEvent event) { System.out.println("Dosyalar tıklandı (Boş)"); }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        cikisIslemi(event);
    }

    // --- SAYFA GETİRME MOTORU (GÜNCELLENDİ) ---
    // 'boolean yetkiVarMi' parametresini sildik!
    private void sayfaGetir(String dosyaAdi) {
        try {
            // Dosya yolunu düzelttik: "/com/example/..." yerine direkt resource köküne bakıyoruz "/"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();

            // --- ESKİ HATALI KODLAR SİLİNDİ ---
            // if (dosyaAdi.equals("sayfa_takvim.fxml")) { ... ctrl.yetkiAyarla(...) }
            // Bu kısım silindi çünkü artık TakvimController kendi işini kendi yapıyor.

            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
            }
        } catch (IOException e) {
            System.out.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }

    private void cikisIslemi(ActionEvent event) {
        try {
            // Login fxml yolunu düzelttik
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}