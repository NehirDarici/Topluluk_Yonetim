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

    // Takvim Butonu
    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        // Sosyal Başkanı da yetkili olsun (TRUE)
        sayfaGetir("sayfa_takvim.fxml", true);
    }
    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        System.out.println("To-Do Listesi açılıyor...");
        // Yetki önemli değil, TodoController içeride dosyayı kendi seçecek
        sayfaGetir("sayfa_todo.fxml", false);
    }



    // Diğer Butonlar (Boş - Hata vermesin diye)
    @FXML void btnUyelerTiklandi(ActionEvent event) { System.out.println("Boş"); }
    @FXML void btnGorevlerTiklandi(ActionEvent event) { System.out.println("Boş"); }
    @FXML void btnDosyalarTiklandi(ActionEvent event) { System.out.println("Boş"); }

    @FXML
    void btnCikisYap(ActionEvent event) {
        cikisIslemi(event);
    }

    // Ortak Metotlar
    private void sayfaGetir(String dosyaAdi, boolean yetkiVarMi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kampustopluluksistemi/" + dosyaAdi));
            Pane view = loader.load();
            if (dosyaAdi.equals("sayfa_takvim.fxml")) {
                TakvimController ctrl = loader.getController();
                if (ctrl != null) ctrl.yetkiAyarla(yetkiVarMi);
            }
            if (anaIcerik != null) anaIcerik.setCenter(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void cikisIslemi(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}