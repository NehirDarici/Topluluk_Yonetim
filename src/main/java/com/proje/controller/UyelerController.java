package com.proje.controller;

import com.proje.dao.UserDAO;
import com.proje.model.BirimUyesi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class UyelerController {

    // FXML dosyasındaki fx:id'lerle birebir aynı olmalı
    @FXML private TableView<BirimUyesi> tabloUyeler;
    @FXML private TableColumn<BirimUyesi, String> colNo;
    @FXML private TableColumn<BirimUyesi, String> colAd;
    @FXML private TableColumn<BirimUyesi, String> colRol;
    @FXML private TableColumn<BirimUyesi, Integer> colBirim;

    private UserDAO userDAO = new UserDAO();
    private ObservableList<BirimUyesi> uyeListesi = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Tablo Sütunlarını Model Verileriyle Eşleştir
        // Parantez içindekiler BirimUyesi sınıfındaki değişken isimleri değil, getter metodunun sonudur.
        // getOgrenciNo() -> "ogrenciNo"
        colNo.setCellValueFactory(new PropertyValueFactory<>("ogrenciNo"));
        colAd.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colBirim.setCellValueFactory(new PropertyValueFactory<>("birimId"));

        // 2. Listeyi Tabloya Bağla
        tabloUyeler.setItems(uyeListesi);

        // 3. Verileri Çek ve Göster
        verileriGetir();
    }

    private void verileriGetir() {
        uyeListesi.clear();
        uyeListesi.addAll(userDAO.getTumKullanicilar());
    }

    // "Listeyi Yenile" butonu için
    @FXML
    void btnYenile(ActionEvent event) {
        verileriGetir();
    }

    // "Seçili Üyeyi Sil" butonu için
    @FXML
    void btnSil(ActionEvent event) {
        // Tablodan seçilen satırı al
        BirimUyesi secilen = tabloUyeler.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            Alert uyari = new Alert(Alert.AlertType.WARNING);
            uyari.setTitle("Uyarı");
            uyari.setHeaderText("Seçim Yapılmadı");
            uyari.setContentText("Lütfen silmek istediğiniz üyeyi seçin.");
            uyari.show();
            return;
        }

        // Onay iste
        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setTitle("Silme Onayı");
        onay.setHeaderText(secilen.getAdSoyad() + " silinecek.");
        onay.setContentText("Bu işlem geri alınamaz. Emin misiniz?");

        Optional<ButtonType> sonuc = onay.showAndWait();
        if (sonuc.isPresent() && sonuc.get() == ButtonType.OK) {
            // Veritabanından sil
            boolean silindi = userDAO.kullaniciSil(secilen.getId());

            if (silindi) {
                // Başarılıysa ekrandaki listeden de sil
                uyeListesi.remove(secilen);
            } else {
                Alert hata = new Alert(Alert.AlertType.ERROR);
                hata.setTitle("Hata");
                hata.setHeaderText("Silinemedi!");
                hata.setContentText("Veritabanı hatası oluştu.");
                hata.show();
            }
        }
    }
}