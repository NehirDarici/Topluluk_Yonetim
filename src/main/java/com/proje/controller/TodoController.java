package com.proje.controller;

import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Scanner;

public class TodoController {

    @FXML private Label lblBaslik;
    @FXML private ListView<String> listeGorevler;
    @FXML private TextField txtYeniGorev;

    // Listeyi ekranda göstermek için özel liste yapısı
    private ObservableList<String> gorevListesi = FXCollections.observableArrayList();

    // Varsayılan dosya adı
    private String dosyaAdi = "genel_todo.txt";

    @FXML
    public void initialize() {
        // 1. Giriş yapan gerçek kullanıcıyı al (SessionManager'dan)
        BirimUyesi aktifUye = SessionManager.getInstance().getCurrentUser();

        if (aktifUye != null) {
            dosyaBelirle(aktifUye);
        }

        // 2. Listeyi ekrana bağla
        listeGorevler.setItems(gorevListesi);

        // 3. Dosyadaki eski görevleri oku ve getir
        dosyadanOku();
    }

    // --- KİMİN HANGİ DOSYAYI GÖRECEĞİNİ BELİRLE ---
    private void dosyaBelirle(BirimUyesi user) {
        // Senin veritabanındaki Birim ID'lerine göre dosya seçiyoruz.
        // NOT: Veritabanındaki ID'lerini kontrol et (1: Yazılım, 2: Etkinlik vb.)

        int birimId = user.getBirimId();
        String rol = user.getRol(); // "baskan" veya "uye"

        System.out.println("Todo Yükleniyor... BirimID: " + birimId + " Rol: " + rol);

        // SENARYO 1: YAZILIM EKİBİ / YÖNETİM (ID = 1 varsayıyoruz)
        if (birimId == 1) {
            dosyaAdi = "todo_yonetim.txt";
            lblBaslik.setText("YÖNETİM KURULU AJANDASI");
        }
        // SENARYO 2: ETKİNLİK EKİBİ (ID = 2 varsayıyoruz)
        else if (birimId == 2) {
            dosyaAdi = "todo_etkinlik.txt";
            lblBaslik.setText("ETKİNLİK EKİBİ GÖREVLERİ");
        }
        // SENARYO 3: DİĞER BİRİMLER
        else {
            dosyaAdi = "todo_genel.txt";
            lblBaslik.setText("GENEL GÖREV LİSTESİ");
        }
    }

    // --- GÖREV EKLEME ---
    @FXML
    void btnEkle(ActionEvent event) {
        String yeniGorev = txtYeniGorev.getText().trim();
        if (!yeniGorev.isEmpty()) {
            gorevListesi.add(yeniGorev); // Listeye ekle
            txtYeniGorev.clear();        // Kutuyu temizle
            dosyayaYaz();                // Dosyayı güncelle
        }
    }

    // --- GÖREV SİLME ---
    @FXML
    void btnSil(ActionEvent event) {
        String secilen = listeGorevler.getSelectionModel().getSelectedItem();
        if (secilen != null) {
            gorevListesi.remove(secilen); // Listeden sil
            dosyayaYaz();                 // Dosyayı güncelle
        }
    }

    // --- DOSYADAN OKUMA (I/O) ---
    private void dosyadanOku() {
        File dosya = new File(dosyaAdi);
        if (!dosya.exists()) return;

        try (Scanner scanner = new Scanner(dosya)) {
            while (scanner.hasNextLine()) {
                String satir = scanner.nextLine();
                gorevListesi.add(satir);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // --- DOSYAYA YAZMA (I/O) ---
    private void dosyayaYaz() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (String gorev : gorevListesi) {
                writer.write(gorev);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}