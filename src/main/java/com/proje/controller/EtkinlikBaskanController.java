package com.proje.controller;

import com.proje.dosya.DosyaIslemleri;
import com.proje.service.BirimServisi;
import com.proje.dao.GorevDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * PDF Madde 1.1: Alt Sınıf
 */
public class EtkinlikBaskanController {

    @FXML
    private StackPane anaIcerik; // FXML'deki fx:id="anaIcerik"

    // PDF Madde 10: Yardımcı yapıların kullanımı
    private BirimServisi birimServisi = new BirimServisi();

    @FXML
    public void initialize() {
        System.out.println("✅ Etkinlik Başkanı Paneli Yüklendi!");
    }

    @FXML
    void btnTakvimTiklandi(ActionEvent event) {
        sayfaGetir("sayfa_takvim.fxml");
    }

    @FXML
    void btnToDoTiklandi(ActionEvent event) {
        // PDF Madde 15.3: Paylaşımlı To-Do havuzunu açar
        System.out.println("Birim paylaşımlı To-Do listesi açılıyor...");
        sayfaGetir("sayfa_todo.fxml");
    }

    @FXML
    void btnUyelerTiklandi(ActionEvent event) {
        System.out.println("Üye yönetimi tıklandı.");
        sayfaGetir("sayfa_uyeler.fxml");
    }

    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        // PDF Madde 5, 8 ve 9 gereksinimlerini burada tetikliyoruz
        System.out.println("--- Teknik Gereksinimler Çalıştırılıyor ---");

        try {
            // 1. PDF Madde 8: Dosyaya yazma (I/O)
            DosyaIslemleri.logEkle("Etkinlik Başkanı dosya işlemlerini başlattı.");

            // 2. PDF Madde 9: Çok boyutlu diziden veri çekme
            int oda = birimServisi.odaBul(2); // 2: Etkinlik Birimi
            System.out.println("Birim Lokasyonu (2D Array): Oda " + oda);

            // 3. PDF Madde 5.1: Wildcard (List<?>) kullanımı
            // Görev listesini generic metoda gönderip eleman sayısını raporluyoruz
            GorevDAO dao = new GorevDAO();
            birimServisi.elemanSayisiniRaporla(dao.getTumGorevler());

            // 4. PDF Madde 8: Scanner ile log okuma testi
            DosyaIslemleri.loglariOku();

        } catch (IOException e) {
            // PDF Madde 7: Hata yönetimi
            System.err.println("Dosya işlemleri sırasında hata: " + e.getMessage());
        }

        System.out.println("Dosya İşlemleri sayfası açılıyor...");
        sayfaGetir("sayfa_dosyalar.fxml");
    }

    private void sayfaGetir(String dosyaAdi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load();
            if (anaIcerik != null) {
                anaIcerik.getChildren().clear();
                anaIcerik.getChildren().add(view);
            }
        } catch (IOException e) {
            System.err.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}