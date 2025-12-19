package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EtkinlikUyeController {

    @FXML
    public void initialize() {
        System.out.println("✅ Etkinlik Üye Ekranı Başarıyla Yüklendi!");
    }

    // FXML'de "Dosya İşlemleri" ve "Etkinlik Üye" butonlarına atanmış metot
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Etkinlik Üye: Dosya işlemleri butonuna basıldı.");
        // Buraya dosya yükleme ekranını açan kodlar gelecek
    }

    // FXML'de "Çıkış Yap" butonuna atanmış metot
    @FXML
    void btnCikisYap(ActionEvent event) {
        System.out.println("Uygulamadan çıkılıyor...");
        System.exit(0);
    }
}