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

public class BaskanController {

    @FXML
    private BorderPane anaIcerik; // Bu değişkenin null olup olmadığını kontrol edeceğiz

    // --- TAKVİM BUTONU ---
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        System.out.println(">> BUTONA BASILDI. Yükleme deneniyor...");
        sayfaGetir("sayfa_takvim.fxml", true);
    }

    // --- DİĞER BUTONLAR ---
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        // Yetki (true/false) önemli değil, TodoController kendi içinde dosyayı seçecek
        sayfaGetir("sayfa_todo.fxml", false);
    }
    @FXML
    void btnGorevlerTiklandi(ActionEvent event) {
        System.out.println("Görevler tıklandı.");
    }
    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üyeler tıklandı.");
    }
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Dosyalar tıklandı.");
    }

    // --- SAYFA DEĞİŞTİRME METODU (HATA AYIKLAMALI) ---
    private void sayfaGetir(String dosyaAdi, boolean yetkiVarMi) {
        try {
            // 1. ADIM: Dosyayı bulmaya çalış
            System.out.println("1. Adım: " + dosyaAdi + " dosyası aranıyor...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();
            System.out.println("   -> Dosya bulundu ve yüklendi!");

            // 2. ADIM: Controller bağlantısını kontrol et
            if (dosyaAdi.equals("sayfa_takvim.fxml")) {
                TakvimController ctrl = loader.getController();
                if (ctrl != null) {
                    ctrl.yetkiAyarla(yetkiVarMi);
                    System.out.println("   -> Yetki ayarlandı.");
                } else {
                    System.out.println("   -> UYARI: TakvimController null geldi! FXML bağlantısını kontrol et.");
                }
            }

            // 3. ADIM: Ekrana yerleştir (EN KRİTİK KISIM)
            if (anaIcerik != null) {
                anaIcerik.setCenter(view);
                System.out.println("BAŞARILI: Turuncu ekran ortadaki alana yerleştirildi.");
            } else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("KRİTİK HATA: 'anaIcerik' değişkeni BOŞ (NULL)!");
                System.out.println("SceneBuilder'da en dıştaki BorderPane'in fx:id kısmına 'anaIcerik' yazdığına emin misin?");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

        } catch (IOException e) {
            System.out.println("DOSYA OKUMA HATASI:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("BİLİNMEYEN BİR HATA:");
            e.printStackTrace();
        }
    }

    // --- ÇIKIŞ YAP ---
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/com/example/kampustopluluksistemi/login.fxml"));
            Scene scene = new Scene(loginPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}