package com.proje.controller;

import com.proje.dao.GorevDAO;
import com.proje.manager.SessionManager;
import com.proje.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class TakvimController {

    @FXML private Label lblAyYil;
    @FXML private GridPane takvimGrid;

    // FXML dosyasındaki butonun ID'si btnEkle olmalı
    @FXML private Button btnEkle;

    private YearMonth suankiAy;
    private GorevDAO gorevDAO = new GorevDAO();

    @FXML
    public void initialize() {
        suankiAy = YearMonth.now();

        // 1. Yetki kontrolü (Buton görünürlüğünü ayarlar)
        yetkiKontroluYap();

        // 2. Takvimi çiz
        takvimiCiz();
    }

    // --- BU KISIM GÜNCELLENDİ ---
    private void yetkiKontroluYap() {
        BirimUyesi aktifKullanici = SessionManager.getInstance().getCurrentUser();

        // Hata önleme: Kullanıcı düşmemişse veya buton FXML'de yoksa işlem yapma
        if (aktifKullanici == null || btnEkle == null) return;

        String rol = aktifKullanici.getRol(); // "uye", "baskan" veya "topluluk_baskani"

        // Eğer kullanıcı 'uye' ise Ekleme Butonunu gizle
        if (rol.equals("uye")) {
            btnEkle.setVisible(false);
            btnEkle.setManaged(false); // Ekranda boşluk bırakmasın, tamamen yok olsun
        } else {
            // Başkan ise butonu göster
            btnEkle.setVisible(true);
            btnEkle.setManaged(true);
        }
    }

    private void takvimiCiz() {
        if (takvimGrid == null) return;

        takvimGrid.getChildren().clear();
        lblAyYil.setText(suankiAy.getMonth().name() + " " + suankiAy.getYear());

        LocalDate ayinIlkGunu = suankiAy.atDay(1);
        int gununSirasi = ayinIlkGunu.getDayOfWeek().getValue(); // 1=Pzt ... 7=Pazar
        int toplamGun = suankiAy.lengthOfMonth();
        int gunSayaci = 1;

        // --- VERİTABANINDAN GÜNCEL VERİLERİ ÇEK ---
        List<TakvimOlayi> olayListesi = gorevDAO.getTumGorevler();

        // 6 Satır x 7 Sütun Döngüsü
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                // Boş gün kontrolü (Ay başı ve sonu)
                if ((i == 0 && j < gununSirasi - 1) || gunSayaci > toplamGun) {
                    VBox bosKutu = new VBox();
                    bosKutu.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-width: 0.5;");
                    takvimGrid.add(bosKutu, j, i);
                    continue;
                }

                // --- GÜN KUTUSU TASARIMI ---
                VBox kutu = new VBox();
                kutu.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 0.5; -fx-cursor: hand;");
                kutu.setSpacing(2);

                kutu.setMinHeight(80);
                kutu.setMaxHeight(100);

                // Gün Numarası
                Label lblGun = new Label("  " + gunSayaci);
                lblGun.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-text-fill: #555;");
                kutu.getChildren().add(lblGun);

                // --- GÖREVLERİ EKLEME ---
                LocalDate buGun = suankiAy.atDay(gunSayaci);
                int gosterilenAdet = 0;

                for (TakvimOlayi olay : olayListesi) {
                    if (olay.getTarih().isEqual(buGun)) {
                        if (gosterilenAdet < 2) {
                            Label lblGorev = new Label(olay.getBaslik());
                            lblGorev.setStyle(
                                    "-fx-background-color: " + olay.getRenkKodu() + ";" +
                                            "-fx-text-fill: white;" +
                                            "-fx-background-radius: 10;" +
                                            "-fx-padding: 2 6 2 6;" +
                                            "-fx-font-size: 10px;" +
                                            "-fx-font-weight: bold;"
                            );
                            lblGorev.setMaxWidth(Double.MAX_VALUE);
                            VBox.setMargin(lblGorev, new javafx.geometry.Insets(0, 3, 0, 3));
                            kutu.getChildren().add(lblGorev);
                        }
                        gosterilenAdet++;
                    }
                }

                if (gosterilenAdet > 2) {
                    Label lblDahaFazla = new Label("+ " + (gosterilenAdet - 2) + " tane daha...");
                    lblDahaFazla.setStyle("-fx-font-size: 9px; -fx-text-fill: #888; -fx-padding: 0 0 0 5;");
                    kutu.getChildren().add(lblDahaFazla);
                }

                // --- TIKLAMA OLAYI ---
                final LocalDate tiklananTarih = buGun;
                kutu.setOnMouseClicked(event -> {
                    detayPenceresiniAc(tiklananTarih);
                });

                takvimGrid.add(kutu, j, i);
                gunSayaci++;
            }
        }
    }

    private void detayPenceresiniAc(LocalDate tarih) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_gun_detay.fxml"));
            Parent root = loader.load();

            GunDetayController controller = loader.getController();
            if (controller != null) {
                controller.veriYukle(tarih);
            }

            Stage stage = new Stage();
            stage.setTitle("Gün Detayları");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Pencere kapanınca takvimi yenile (Eklenen görevler görünsün)
            takvimiCiz();

        } catch (IOException e) {
            System.out.println("HATA: Detay penceresi açılamadı.");
            e.printStackTrace();
        }
    }

    @FXML
    void btnOncekiAy(ActionEvent event) {
        suankiAy = suankiAy.minusMonths(1);
        takvimiCiz();
    }

    @FXML
    void btnSonrakiAy(ActionEvent event) {
        suankiAy = suankiAy.plusMonths(1);
        takvimiCiz();
    }

    // --- BAŞKANLAR İÇİN HIZLI EKLEME BUTONU ---
    @FXML
    void btnEkleTiklandi(ActionEvent event) {
        // "+ Yeni Görev" butonuna basınca bugünün tarihini açar
        detayPenceresiniAc(LocalDate.now());
    }
}