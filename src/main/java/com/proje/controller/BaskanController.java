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
        // Dosya adının tam olarak 'sayfa_todo.fxml' olduğundan emin ol (küçük/büyük harf duyarlıdır)
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
            System.out.println("1. Adım: " + dosyaAdi + " dosyası aranıyor...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();
            System.out.println("   -> Dosya bulundu ve yüklendi!");

            if (dosyaAdi.equals("sayfa_takvim.fxml")) {
                TakvimController ctrl = loader.getController();
                if (ctrl != null) {
                    ctrl.yetkiAyarla(yetkiVarMi);
                }
            }

            // --- DÜZELTİLEN KRİTİK KISIM ---
            if (anaIcerik != null) {
                // 1. İçeriğin kendi genişleme sınırlarını kaldır
                view.setMaxWidth(Double.MAX_VALUE);
                view.setMaxHeight(Double.MAX_VALUE);

                // 2. BorderPane'e bu içeriği merkeze koymasını söyle
                anaIcerik.setCenter(view);

                // 3. İçeriğin BorderPane içinde her yöne yayılmasını zorla
                // Bu metot statik genişlik vermekten çok daha sağlıklıdır.
                BorderPane.setAlignment(view, javafx.geometry.Pos.CENTER);

                // Eğer sayfanın en dışı HBox ise yatayda yayılmasını garanti et
                if (view instanceof javafx.scene.layout.HBox) {
                    javafx.scene.layout.HBox.setHgrow(view, javafx.scene.layout.Priority.ALWAYS);
                }

                System.out.println("BAŞARILI: İçerik tam genişliğe zorlandı.");
            } else {
                System.out.println("KRİTİK HATA: 'anaIcerik' (BorderPane) null!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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