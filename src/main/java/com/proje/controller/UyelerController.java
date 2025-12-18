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

    // --- YENİ: Arama Kutusu (FXML ile bağladık) ---
    @FXML private TextField txtArama;

    private UserDAO userDAO = new UserDAO();

    // 1. Ekranda Görünen Liste (Filtrelenmiş halini tutar)
    private ObservableList<BirimUyesi> gosterilenListe = FXCollections.observableArrayList();

    // 2. Ana Veri Listesi (Veritabanındaki her şeyi tutar, arama yaparken buradan bakarız)
    private List<BirimUyesi> anaVeriListesi;

    // --- PDF Madde 5.1: Generic Sınıf Kullanımı ---
    private AramaMotoru<BirimUyesi> aramaMotoru = new AramaMotoru<>();

    @FXML
    public void initialize() {
        // Tablo Sütun Ayarları
        colNo.setCellValueFactory(new PropertyValueFactory<>("ogrenciNo"));
        colAd.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colBirim.setCellValueFactory(new PropertyValueFactory<>("birimId"));

        // Tabloyu observable listeye bağla
        tabloUyeler.setItems(gosterilenListe);

        // Verileri ilk kez çek
        verileriGetir();
    }

    private void verileriGetir() {
        // Veritabanından her şeyi çekip "anaVeriListesi"ne atıyoruz (Yedek gibi düşün)
        anaVeriListesi = userDAO.getTumKullanicilar();

        // Ekrana da aynısını yansıtıyoruz (Başlangıçta hepsi görünür)
        gosterilenListe.clear();
        gosterilenListe.addAll(anaVeriListesi);
    }

    // --- YENİ: ARAMA METODU (FXML'de onKeyReleased="#aramaYap") ---
    @FXML
    void aramaYap(KeyEvent event) {
        String arananMetin = txtArama.getText();

        // PDF Madde 4.3 (Interface) ve Lambda Kullanımı:
        // Arama kuralını burada belirliyoruz.
        IAramaKriteri<BirimUyesi> kriter = (uye, kelime) -> {

            // PDF Madde 2.2: String Metotları (toLowerCase, contains, valueOf)
            String adSoyadKucuk = uye.getAdSoyad().toLowerCase();
            String noStr = String.valueOf(uye.getOgrenciNo());

            // İsimde VEYA numarada geçiyorsa bulsun
            return adSoyadKucuk.contains(kelime) || noStr.contains(kelime);
        };

        // Generic Arama Motorunu çalıştır
        List<BirimUyesi> sonuc = aramaMotoru.ara(anaVeriListesi, arananMetin, kriter);

        // Sonuçları tabloya bas
        gosterilenListe.clear();
        gosterilenListe.addAll(sonuc);
    }

    @FXML
    void btnYenile(ActionEvent event) {
        txtArama.clear(); // Arama kutusunu temizle
        verileriGetir();  // Verileri tekrar çek
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
                // Hem ana listeden hem de ekrandaki listeden silmemiz lazım
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