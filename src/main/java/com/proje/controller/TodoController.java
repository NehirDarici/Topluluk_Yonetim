package com.proje.controller;

import com.proje.dao.GorevDAO;
import com.proje.model.TakvimOlayi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TodoController implements Initializable {

    // --- FXML BİLEŞENLERİ ---
    @FXML private TextField txtYeniGorev;
    @FXML private DatePicker datePicker;
    @FXML private TextArea txtNot;

    // DEĞİŞİKLİK 1: Listemiz artık String değil, Nesne tutacak (ID'yi bilmek için)
    @FXML private ListView<TakvimOlayi> listeGorevler;

    // Veri Modeli
    private ObservableList<TakvimOlayi> gorevData = FXCollections.observableArrayList();
    private GorevDAO dao = new GorevDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (txtYeniGorev == null || listeGorevler == null) {
            System.err.println("❌ HATA: FXML bağlantıları eksik! SceneBuilder fx:id kontrol et.");
            return;
        }

        // Listeyi bağla
        listeGorevler.setItems(gorevData);

        // DEĞİŞİKLİK 2: Nesnelerin ekranda nasıl görüneceğini ayarla
        // Listede "TakvimOlayi" objesi var ama biz ekranda "Tarih | Başlık" görmek istiyoruz.
        listeGorevler.setCellFactory(new Callback<ListView<TakvimOlayi>, ListCell<TakvimOlayi>>() {
            @Override
            public ListCell<TakvimOlayi> call(ListView<TakvimOlayi> param) {
                return new ListCell<TakvimOlayi>() {
                    @Override
                    protected void updateItem(TakvimOlayi olay, boolean empty) {
                        super.updateItem(olay, empty);
                        if (empty || olay == null) {
                            setText(null);
                        } else {
                            // Ekranda görünecek yazı formatı:
                            setText(olay.getTarih() + " | " + olay.getBaslik());
                        }
                    }
                };
            }
        });

        // Verileri çek
        listeyiYenile();
    }

    // --- EKLEME ---
    @FXML
    void btnEkle(ActionEvent event) {
        if (txtYeniGorev.getText().isEmpty()) {
            uyariGoster("Uyarı", "Lütfen bir görev yazınız.");
            return;
        }

        String baslik = txtYeniGorev.getText();
        String tarih;
        if (datePicker != null && datePicker.getValue() != null) {
            tarih = datePicker.getValue().toString();
        } else {
            tarih = LocalDate.now().toString();
        }

        // Veritabanına Ekle
        boolean sonuc = dao.gorevEkle(baslik, "To-Do", tarih, "Gorev", 2);

        if (sonuc) {
            bilgiGoster("Başarılı", "Görev eklendi.");
            txtYeniGorev.clear();
            listeyiYenile(); // Listeyi güncelle
        } else {
            hataGoster("Hata", "Kayıt başarısız.");
        }
    }

    // --- SİLME (DÜZELTİLDİ) ---
    @FXML
    void btnSil(ActionEvent event) {
        // Seçilen nesneyi al (String değil, TakvimOlayi geliyor artık)
        TakvimOlayi secilen = listeGorevler.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            uyariGoster("Seçim Yapın", "Silmek için listeden bir görev seçiniz.");
            return;
        }

        // DEĞİŞİKLİK 3: Veritabanından ID ile silme işlemi
        boolean silindi = dao.gorevSil(secilen.getId());

        if (silindi) {
            // Başarılıysa ekrandaki listeden de kaldır
            gorevData.remove(secilen);
            System.out.println("✅ Görev kalıcı olarak silindi: " + secilen.getBaslik());
        } else {
            hataGoster("Hata", "Veritabanından silinemedi!");
        }
    }

    private void listeyiYenile() {
        gorevData.clear();
        // Tüm görevleri çekip listeye NESNE olarak ekliyoruz
        gorevData.addAll(dao.getTumGorevler());
    }

    // --- YARDIMCI METOTLAR ---
    private void uyariGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
    private void bilgiGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
    private void hataGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}