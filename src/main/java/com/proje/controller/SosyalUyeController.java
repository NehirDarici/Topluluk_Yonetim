package com.proje.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SosyalUyeController {

    @FXML
    public void initialize() {
        System.out.println("✅ Sosyal Medya Üye Ekranı Yüklendi!");
    }

    // FXML'deki onAction="#btnDosyalarTiklandi" için:
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("Dosya İşlemleri butonuna tıklandı.");
        // Buraya dosya işlemlerini açacak kodlar gelecek
    }

    // FXML'deki onAction="#btnCikisYap" için:
    @FXML
    void btnCikisYap(ActionEvent event) {
        System.out.println("Çıkış yapılıyor...");
        System.exit(0); // Uygulamayı kapatır
    }

    // Not: "Takvim" ve "To-Do List" butonlarına FXML'de henüz onAction eklememişsin.
    // İleride onları da eklediğinde buraya metotlarını yazman gerekecek.
}