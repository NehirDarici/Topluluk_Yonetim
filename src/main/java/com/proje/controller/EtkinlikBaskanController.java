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
 Etkinlik birim başkanı giriş yaptığında çalışan kısımdır
 */
public class EtkinlikBaskanController {

    //ortadaki boş alana, bütün alt sınıflar buraya yüklenir
    @FXML
    private StackPane anaIcerik; // FXML'deki fx:id="anaIcerik"

    // sayı sayma işlemleri için bunu çağırırız

    private BirimServisi birimServisi = new BirimServisi();


    //sayfa ilk açıldığında çalışan metot
    @FXML
    public void initialize() {
        System.out.println("✅ Etkinlik Başkanı Paneli Yüklendi!");
    }
    //MENÜ BUTONLARI
    //ortadaki alana yükleme yaparlar
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


    //SONRADAN EKLENECEK KISIM
    @FXML
    void btnDosyalarTiklandi(ActionEvent event) {
        System.out.println("--- Teknik Gereksinimler Çalıştırılıyor ---");

        try {
            DosyaIslemleri.logEkle("Etkinlik Başkanı dosya işlemlerini başlattı.");

            // Çok boyutlu diziden veri çekme
            int oda = birimServisi.odaBul(2); // 2: Etkinlik Birimi
            System.out.println("Birim Lokasyonu (2D Array): Oda " + oda);

            // Görev listesini generic metoda gönderip eleman sayısını raporluyoruz
            GorevDAO dao = new GorevDAO();
            birimServisi.elemanSayisiniRaporla(dao.getTumGorevler());

            // log okuma testi
            DosyaIslemleri.loglariOku();

        } catch (IOException e) {
            System.err.println("Dosya işlemleri sırasında hata: " + e.getMessage());
        }

        System.out.println("Dosya İşlemleri sayfası açılıyor...");
        sayfaGetir("sayfa_dosyalar.fxml");
    }


    // Her buton için ayrı ayrı "FXMLLoader..." yazmak yerine, dosya adını veriyoruz, o yüklüyor.

    private void sayfaGetir(String dosyaAdi) {
        try {
            //Yyeni sayfayı hafızaya yükler
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + dosyaAdi));
            Pane view = loader.load(); //yüklenen görüntü

            //ortadaki alanı temizle ve yeni görüntü ekle
            if (anaIcerik != null) {
                anaIcerik.getChildren().clear();  //eskiyi sil
                anaIcerik.getChildren().add(view); //yeniyi ekle
            }
        } catch (IOException e) {
            System.err.println("HATA: " + dosyaAdi + " yüklenemedi!");
            e.printStackTrace();
        }
    }


    //ÇIKIŞ YAP
    //neden try-catch--> eğer dosya bulunamazsa çökmesin hatayı ekrana yazsın

    // Daha önce öğrendiğimiz metot. Pencereyi (Stage) bulup sahneyi (Scene) Login ile değiştirir.
    //Mevcut pencereyi kapatmadan, içindeki görüntüyü söküp yerine Giriş Ekranını (Login) takar
    @FXML
    void btnCikisYap(ActionEvent event) {
        try {
            //eventgetSource--> butonu bul
            //.getScene butonun olduğu resme bak
            //getwindow o resmin takılı olduğu çerçeveyi al
            Parent loginPage = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //login sayfasını yerleştir
            stage.setScene(new Scene(loginPage));
            //çerçeveyi tekrardan göster
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}