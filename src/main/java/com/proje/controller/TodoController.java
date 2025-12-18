package com.proje.controller;

import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Scanner;

public class TodoController {

    @FXML private Label lblBaslik;
    @FXML private ListView<String> listeGorevler;
    @FXML private TextField txtYeniGorev;
    @FXML private TextArea txtNotlar; // Yeni eklediğimiz not alanı

    private ObservableList<String> gorevListesi = FXCollections.observableArrayList();
    private String dosyaAdi = "genel_todo.txt";
    private String notDosyaAdi = "genel_notlar.txt"; // Notlar için dosya adı

    @FXML
    public void initialize() {
        BirimUyesi aktifUye = SessionManager.getInstance().getCurrentUser();

        if (aktifUye != null) {
            dosyaBelirle(aktifUye);
        }

        listeGorevler.setItems(gorevListesi);

        // Verileri yükle
        dosyadanOku();
        notlariDosyadanOku();

        // Notlar değiştikçe otomatik kaydetme (Listener)
        txtNotlar.textProperty().addListener((observable, oldValue, newValue) -> {
            notlariDosyayaYaz(newValue);
        });
    }

    private void dosyaBelirle(BirimUyesi user) {
        int birimId = user.getBirimId();

        // Birim ID'ye göre hem To-Do hem Not dosyasını belirliyoruz
        if (birimId == 1) {
            dosyaAdi = "todo_yonetim.txt";
            notDosyaAdi = "notlar_yonetim.txt";
        } else if (birimId == 2) {
            dosyaAdi = "todo_etkinlik.txt";
            notDosyaAdi = "notlar_etkinlik.txt";
        } else {
            dosyaAdi = "todo_genel.txt";
            notDosyaAdi = "notlar_genel.txt";
        }
    }

    // --- TO-DO İŞLEMLERİ ---
    @FXML
    void btnEkle(ActionEvent event) {
        String yeniGorev = txtYeniGorev.getText().trim();
        if (!yeniGorev.isEmpty()) {
            gorevListesi.add("• " + yeniGorev); // Maddeli görünüm için simge ekledik
            txtYeniGorev.clear();
            dosyayaYaz();
        }
    }

    @FXML
    void btnSil(ActionEvent event) {
        String secilen = listeGorevler.getSelectionModel().getSelectedItem();
        if (secilen != null) {
            gorevListesi.remove(secilen);
            dosyayaYaz();
        }
    }

    // --- NOTLAR İÇİN I/O İŞLEMLERİ ---
    private void notlariDosyadanOku() {
        File file = new File(notDosyaAdi);
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
            txtNotlar.setText(sb.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void notlariDosyayaYaz(String icerik) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(notDosyaAdi))) {
            writer.write(icerik);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Mevcut dosyadanOku ve dosyayaYaz metodların aynı kalabilir...
    private void dosyadanOku() {
        File dosya = new File(dosyaAdi);
        if (!dosya.exists()) return;
        gorevListesi.clear();
        try (Scanner scanner = new Scanner(dosya)) {
            while (scanner.hasNextLine()) {
                gorevListesi.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    private void dosyayaYaz() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (String gorev : gorevListesi) {
                writer.write(gorev);
                writer.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}