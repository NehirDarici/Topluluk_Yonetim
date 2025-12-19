package com.proje.controller;

import com.proje.dao.UserDAO;
import com.proje.interfaces.IAramaKriteri; // Interface import edildi
import com.proje.model.BirimUyesi;
import com.proje.utility.AramaMotoru; // Generic sınıf import edildi
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent; // Klavye olaylarını yakalamak için

import java.util.List;
import java.util.Optional;

public class UyelerController {

    @FXML private TableView<BirimUyesi> tabloUyeler;
    @FXML private TableColumn<BirimUyesi, String> colNo;
    @FXML private TableColumn<BirimUyesi, String> colAd;
    @FXML private TableColumn<BirimUyesi, String> colRol;
    @FXML private TableColumn<BirimUyesi, Integer> colBirim;
    @FXML private TextField txtArama;

    private UserDAO userDAO = new UserDAO();

    // Ekranda Görünen Liste
    private ObservableList<BirimUyesi> gosterilenListe = FXCollections.observableArrayList();

    // Ana Veri Listesi - arama yapar.
    private List<BirimUyesi> anaVeriListesi;

    private AramaMotoru<BirimUyesi> aramaMotoru = new AramaMotoru<>();

    @FXML
    public void initialize() {
        // Tablo Sütun Ayarları
        colNo.setCellValueFactory(new PropertyValueFactory<>("ogrenciNo"));
        colAd.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colBirim.setCellValueFactory(new PropertyValueFactory<>("birimId"));

        // Tabloyu observable listeye bağlar.
        tabloUyeler.setItems(gosterilenListe);

        // Verileri ilk kez çeker.
        verileriGetir();
    }

    private void verileriGetir() {
        // Veritabanından her şeyi çekip "anaVeriListesi"ne atar.
        anaVeriListesi = userDAO.getTumKullanicilar();
        gosterilenListe.clear();
        gosterilenListe.addAll(anaVeriListesi);
    }

    // Arama yapmayı sağlayan metod
    @FXML
    void aramaYap(KeyEvent event) {
        String arananMetin = txtArama.getText();

        // Arama kuralını burada belirtilir.
        IAramaKriteri<BirimUyesi> kriter = (uye, kelime) -> {
            String adSoyadKucuk = uye.getAdSoyad().toLowerCase();
            String noStr = String.valueOf(uye.getOgrenciNo());

            // İsimde VEYA numarada geçiyorsa bulmasını sağlar.
            return adSoyadKucuk.contains(kelime) || noStr.contains(kelime);
        };

        // Generic Arama Motorunu çalıştırılır.
        List<BirimUyesi> sonuc = aramaMotoru.ara(anaVeriListesi, arananMetin, kriter);

        // Sonuçlar tabloya basılır.
        gosterilenListe.clear();
        gosterilenListe.addAll(sonuc);
    }

    // Arama kutusunun içini temizler.
    @FXML
    void btnYenile(ActionEvent event) {
        txtArama.clear();
        verileriGetir();
    }

    @FXML
    void btnSil(ActionEvent event) {
        BirimUyesi secilen = tabloUyeler.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            Alert uyari = new Alert(Alert.AlertType.WARNING);
            uyari.setTitle("Uyarı");
            uyari.setHeaderText("Seçim Yapılmadı");
            uyari.setContentText("Lütfen silmek istediğiniz üyeyi seçin.");
            uyari.show();
            return;
        }

        Alert onay = new Alert(Alert.AlertType.CONFIRMATION);
        onay.setTitle("Silme Onayı");
        onay.setHeaderText(secilen.getAdSoyad() + " silinecek.");
        onay.setContentText("Bu işlem geri alınamaz. Emin misiniz?");

        Optional<ButtonType> sonuc = onay.showAndWait();
        if (sonuc.isPresent() && sonuc.get() == ButtonType.OK) {
            boolean silindi = userDAO.kullaniciSil(secilen.getId());

            if (silindi) {
                // Hem ana listeden hem de ekrandaki listeden siler.
                anaVeriListesi.remove(secilen);
                gosterilenListe.remove(secilen);
            } else {
                Alert hata = new Alert(Alert.AlertType.ERROR);
                hata.setTitle("Hata");
                hata.setHeaderText("Silinemedi!");
                hata.show();
            }
        }
    }
}