package com.proje.controller;

import com.proje.dao.GorevDAO;
import com.proje.manager.SessionManager;
import com.proje.model.*;
import com.proje.exception.GecersizGorevException;
import com.proje.dosya.DosyaIslemleri;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * PDF Madde 1.2: camelCase standartlarına uygun isimlendirmeler.
 * Hem Başkan hem de Üye bu sayfayı ortak bir havuz olarak kullanır.
 */
public class TodoController implements Initializable {

    @FXML private TextField txtYeniGorev;
    @FXML private DatePicker datePicker;

    // DİKKAT: FXML dosyanızda bu alanın fx:id="txtNotlar" olduğundan emin olun!
    @FXML private TextArea txtNotlar;

    @FXML private ListView<TakvimOlayi> listeGorevler;

    private ObservableList<TakvimOlayi> gorevData = FXCollections.observableArrayList();
    private GorevDAO dao = new GorevDAO();
    private BirimUyesi aktifUye;

    // PDF Madde 8: Dosya İşlemleri için dosya yolu değişkeni
    private String notDosyaYolu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Oturumdaki aktif kullanıcıyı al
        aktifUye = SessionManager.getInstance().getCurrentUser();

        if (listeGorevler != null) {
            listeGorevler.setItems(gorevData);

            // Listenin hücre tasarımı (Tarih | Başlık)
            listeGorevler.setCellFactory(param -> new ListCell<TakvimOlayi>() {
                @Override
                protected void updateItem(TakvimOlayi olay, boolean empty) {
                    super.updateItem(olay, empty);
                    if (empty || olay == null) {
                        setText(null);
                    } else {
                        // PDF Madde 6: Tarih formatı ve kullanımı
                        setText(olay.getTarih() + " | " + olay.getBaslik());
                    }
                }
            });
        }

        // 2. Birime özel görevleri veritabanından yükle
        listeyiYenile();

        // --- YENİ EKLENEN KISIM: NOTLAR (DOSYA I/O) ---
        if (aktifUye != null && txtNotlar != null) {
            // Her birim için farklı dosya oluşturuyoruz. Örn: notlar_birim_2.txt
            notDosyaYolu = "notlar_birim_" + aktifUye.getBirimId() + ".txt";

            // 1. Eski notları dosyadan oku ve ekrana yaz
            notlariDosyadanOku();

            // 2. Yazı değiştikçe otomatik kaydet (Listener)
            txtNotlar.textProperty().addListener((observable, oldValue, newValue) -> {
                notlariDosyayaKaydet(newValue);
            });
        }
    }

    // --- PDF MADDE 8: DOSYA OKUMA İŞLEMİ ---
    private void notlariDosyadanOku() {
        try {
            Path yol = Paths.get(notDosyaYolu);
            // Dosya varsa içeriğini oku
            if (Files.exists(yol)) {
                String icerik = Files.readString(yol);
                txtNotlar.setText(icerik);
            } else {
                // Dosya yoksa boş bir dosya oluştur (Hata almamak için)
                Files.createFile(yol);
            }
        } catch (IOException e) {
            System.err.println("Notlar okunurken hata oluştu: " + e.getMessage());
        }
    }

    // --- PDF MADDE 8: DOSYA YAZMA İŞLEMİ ---
    private void notlariDosyayaKaydet(String icerik) {
        try {
            Path yol = Paths.get(notDosyaYolu);
            Files.writeString(yol, icerik);
        } catch (IOException e) {
            System.err.println("Notlar kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    void btnEkle(ActionEvent event) {
        try {
            String input = txtYeniGorev.getText();

            // --- PDF MADDE 2.2: STRING METOTLARI KULLANIMI ---
            if (input == null || input.trim().isEmpty()) {
                throw new GecersizGorevException("Görev başlığı boş olamaz!");
            }

            if (input.length() < 3) {
                throw new GecersizGorevException("Görev adı çok kısa (En az 3 karakter)!");
            }

            String temizInput = input.replace("gereksiz", "").trim();

            if (temizInput.toLowerCase().contains("acil")) {
                System.out.println("Önemli bir görev algılandı.");
            }

            String formatliBaslik = temizInput.substring(0, 1).toUpperCase() + temizInput.substring(1).toLowerCase();

            // --- TARİH AYARI ---
            String tarihStr;
            if (datePicker != null && datePicker.getValue() != null) {
                tarihStr = datePicker.getValue().toString();
            } else {
                tarihStr = LocalDate.now().toString();
            }

            // --- VERİTABANINA EKLEME ---
            if (aktifUye == null) return;

            boolean sonuc = dao.gorevEkle(formatliBaslik, "Birim To-Do Görevi", tarihStr, "To-Do", aktifUye.getBirimId());

            if (sonuc) {
                // PDF Madde 8: Loglama
                try {
                    DosyaIslemleri.logEkle(aktifUye.getAdSoyad() + " [" + aktifUye.getRol() + "] bir görev ekledi: " + formatliBaslik);
                } catch (IOException e) {
                    System.err.println("Log yazma hatası: " + e.getMessage());
                }

                txtYeniGorev.clear();
                listeyiYenile();
                System.out.println("✅ " + aktifUye.getBirimId() + " nolu birime görev eklendi.");
            }

        } catch (GecersizGorevException e) {
            uyariGoster("Giriş Hatası", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            hataGoster("Sistem Hatası", "Beklenmedik bir sorun: " + e.getMessage());
        }
    }

    @FXML
    void btnSil(ActionEvent event) {
        TakvimOlayi secilen = listeGorevler.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            uyariGoster("Seçim Yapın", "Lütfen listeden silmek/tamamlamak için bir görev seçiniz.");
            return;
        }

        if (dao.gorevSil(secilen.getId())) {
            try {
                DosyaIslemleri.logEkle(aktifUye.getAdSoyad() + " [" + aktifUye.getRol() + "] bir görevi sildi.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            listeyiYenile();
        }
    }

    // --- LİSTEYİ YENİLE ---
    private void listeyiYenile() {
        if (aktifUye == null) return;

        gorevData.clear();
        gorevData.addAll(dao.getTodoGorevleriByBirim(aktifUye.getBirimId()));
    }

    // --- YARDIMCI METOTLAR ---
    private void uyariGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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