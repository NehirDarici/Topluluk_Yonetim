package com.proje.controller;

import com.proje.utility.AlertUtility;
import com.proje.dao.GorevDAO;
import com.proje.model.TakvimOlayi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TodoController extends BaseController implements Initializable {

    // FXML ile Eşleşen ID'ler
    @FXML
    private TextField txtGorevBaslik;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> listeGorevler;

    // Listeyi tutacak model
    private ObservableList<String> gorevData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeyi ekrana bağla
        listeGorevler.setItems(gorevData);
        // Verileri çek
        listeyiYenile();
    }

// Önce yukarıya import eklemeyi unutma:

// ...

    @FXML
    void btnEkleTiklandi(ActionEvent event) {
        // ESKİSİ: System.out.println("Eksik bilgi...");
        // YENİSİ: Utility ile Uyarı Penceresi
        if (txtGorevBaslik.getText().isEmpty() || datePicker.getValue() == null) {
            AlertUtility.showWarning("Eksik Bilgi", "Lütfen görev başlığı ve tarih alanlarını doldurunuz!");
            return;
        }

        String baslik = txtGorevBaslik.getText();
        String tarih = datePicker.getValue().toString();

        GorevDAO dao = new GorevDAO();
        boolean sonuc = dao.gorevEkle(baslik, "Aciklama Yok", tarih, "Gorev", 2);

        if (sonuc) {
            // ESKİSİ: System.out.println("Görev eklendi.");
            // YENİSİ: Başarılı Mesajı
            AlertUtility.showInfo("Başarılı", "Görev başarıyla listeye eklendi.");

            txtGorevBaslik.clear();
            listeyiYenile();
        } else {
            // Hata Mesajı
            AlertUtility.showError("Hata", "Veritabanına kayıt yapılırken bir sorun oluştu.");
        }
    }

    // Listeyi veritabanından güncelleyen metot
    private void listeyiYenile() {
        gorevData.clear();
        GorevDAO dao = new GorevDAO();

        // Polymorphism: Her tür olayı (Sınav, Görev, Tatil) tek listede çekiyoruz
        for (TakvimOlayi olay : dao.getTumGorevler()) {
            gorevData.add(olay.getTarih() + " | " + olay.getBaslik());
        }
    }
}