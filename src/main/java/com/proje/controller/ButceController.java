package com.proje.controller;

import com.proje.dao.ButceDAO;
import com.proje.model.ButceKaydi;
import com.proje.model.IslemTuru;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; // DİKKAT: Burası javafx olmalı!
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class ButceController {

    @FXML private Label lblGelir, lblGider, lblKayitSayisi, lblBakiye;
    @FXML private TableView<ButceKaydi> tabloButce;
    @FXML private TableColumn<ButceKaydi, String> colTarih;
    @FXML private TableColumn<ButceKaydi, String> colTur;
    @FXML private TableColumn<ButceKaydi, String> colAciklama;
    @FXML private TableColumn<ButceKaydi, Double> colMiktar;

    @FXML private ComboBox<IslemTuru> cmbTur;
    @FXML private DatePicker datePicker;
    @FXML private TextField txtAciklama;
    @FXML private TextField txtMiktar;

    private ButceDAO dao = new ButceDAO();
    private ObservableList<ButceKaydi> veriListesi = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("ButceController başlatılıyor...");

        colTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        colTur.setCellValueFactory(new PropertyValueFactory<>("tur"));
        colAciklama.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
        colMiktar.setCellValueFactory(new PropertyValueFactory<>("miktar"));

        cmbTur.getItems().setAll(IslemTuru.values());
        cmbTur.getSelectionModel().selectFirst();
        datePicker.setValue(LocalDate.now());

        tabloButce.setItems(veriListesi);

        // Renklendirme
        tabloButce.setRowFactory(tv -> new TableRow<ButceKaydi>() {
            @Override
            protected void updateItem(ButceKaydi item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getTur().isGelirMi()) {
                        setStyle("-fx-background-color: #E8F5E9;");
                    } else {
                        setStyle("-fx-background-color: #FFEBEE;");
                    }
                }
            }
        });

        listeyiYenile();
    }

    @FXML
    void btnEkle(ActionEvent event) {
        try {
            // Verileri Al
            String aciklama = txtAciklama.getText().trim();
            String miktarStr = txtMiktar.getText().trim();
            IslemTuru tur = cmbTur.getValue();
            LocalDate secilenTarih = datePicker.getValue();

            // Kontroller
            if (aciklama.isEmpty()) { uyariVer("Açıklama boş olamaz."); return; }
            if (miktarStr.isEmpty()) { uyariVer("Miktar boş olamaz."); return; }
            if (secilenTarih == null) secilenTarih = LocalDate.now();

            double miktar = Double.parseDouble(miktarStr);

            // Veritabanına Ekle (Artık hata fırlatabiliyor)
            boolean sonuc = dao.ekle(aciklama, miktar, secilenTarih.toString(), tur);

            if(sonuc) {
                txtAciklama.clear();
                txtMiktar.clear();
                listeyiYenile(); // Listeyi güncelle

                // Başarılı Mesajı (İsteğe bağlı)
                Alert bilgi = new Alert(Alert.AlertType.INFORMATION);
                bilgi.setTitle("Başarılı");
                bilgi.setHeaderText(null);
                bilgi.setContentText("Kayıt başarıyla eklendi.");
                bilgi.show();
            }

        } catch (NumberFormatException e) {
            uyariVer("Lütfen 'Tutar' kısmına sadece sayı giriniz.");
        } catch (Exception e) {
            // İŞTE BURASI! Hatayı ekrana basıyoruz.
            Alert hata = new Alert(Alert.AlertType.ERROR);
            hata.setTitle("Hata Detayı");
            hata.setHeaderText("Kayıt Eklenemedi!");
            // Hatanın teknik sebebini (İngilizce) ekrana yazdırır:
            hata.setContentText("Hata Mesajı: " + e.getMessage());
            hata.showAndWait();
            e.printStackTrace(); // Konsola da bassın
        }
    }

    @FXML
    void btnSil(ActionEvent event) {
        System.out.println("Sil butonuna basıldı.");
        ButceKaydi secilen = tabloButce.getSelectionModel().getSelectedItem();
        if(secilen != null) {
            dao.sil(secilen.getId());
            listeyiYenile();
        } else {
            uyariVer("Silinecek kaydı seçiniz.");
        }
    }

    private void listeyiYenile() {
        if (lblKayitSayisi != null) ButceKaydi.toplamKayitSayisi = 0;
        veriListesi.clear();
        veriListesi.addAll(dao.tumKayitlariGetir());
        hesapla();

        if (lblKayitSayisi != null)
            lblKayitSayisi.setText(String.valueOf(ButceKaydi.toplamKayitSayisi));
    }

    private void hesapla() {
        double toplamGelir = 0;
        double toplamGider = 0;

        for (ButceKaydi k : veriListesi) {
            if (k.getTur().isGelirMi()) toplamGelir += k.getMiktar();
            else toplamGider += k.getMiktar();
        }

        if (lblGelir != null) lblGelir.setText(String.format("%.2f TL", toplamGelir));
        if (lblGider != null) lblGider.setText(String.format("%.2f TL", toplamGider));
        if (lblBakiye != null) lblBakiye.setText(String.format("%.2f TL", (toplamGelir - toplamGider)));
    }

    private void temizle() {
        txtAciklama.clear();
        txtMiktar.clear();
        datePicker.setValue(LocalDate.now());
    }

    private void uyariVer(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.show();
    }
}