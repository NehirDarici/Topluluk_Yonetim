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

//Bu sınıf takvim işlemlerini yapar.

public class TakvimController {

    @FXML private Label lblAyYil;
    @FXML private GridPane takvimGrid;
    @FXML private Button btnEkle;

    private YearMonth suankiAy;
    private GorevDAO gorevDAO = new GorevDAO();

    //Sayfa ilk açıldığında başlatılan metod
    @FXML
    public void initialize() {
        suankiAy = YearMonth.now();

        // Yetki kontrolü (Buton görünürlüğünü ayarlar)
        yetkiKontroluYap();

        // Takvimi oluşturur.
        takvimiCiz();
    }

    // Giriş yapan kullanıcıya göre yetki veren metod
    private void yetkiKontroluYap() {
        BirimUyesi aktifKullanici = SessionManager.getInstance().getCurrentUser();

        if (aktifKullanici == null || btnEkle == null) return;

        // Giriş yapan kullanıcının rolünü öğrenir.
        String rol = aktifKullanici.getRol();

        // Eğer kullanıcı 'uye' ise Ekleme Butonunu gizler.
        if (rol.equals("uye")) {
            btnEkle.setVisible(false);
            btnEkle.setManaged(false);
        } else {
            // Başkan ise butonu göster
            btnEkle.setVisible(true);
            btnEkle.setManaged(true);
        }
    }
    /**
     * Seçili ayın günlerini hesaplar ve GridPane üzerine kutucuklar (VBox) olarak ekler.
     * Ayrıca veritabanından o aya ait görevleri çekip ilgili günün içine yerleştirir.
     */

    private void takvimiCiz() {
        if (takvimGrid == null) return;

        // Önceki çizimden kalma kutuları temizler.
        takvimGrid.getChildren().clear();
        lblAyYil.setText(suankiAy.getMonth().name() + " " + suankiAy.getYear());

        LocalDate ayinIlkGunu = suankiAy.atDay(1);
        int gununSirasi = ayinIlkGunu.getDayOfWeek().getValue(); // 1=Pzt ... 7=Pazar
        int toplamGun = suankiAy.lengthOfMonth();
        int gunSayaci = 1;

        // Veritabanından güncel verileri çeker.
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

                // Gün Kutularının Tasarımı
                VBox kutu = new VBox();
                kutu.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 0.5; -fx-cursor: hand;");
                kutu.setSpacing(2);

                kutu.setMinHeight(80);
                kutu.setMaxHeight(100);

                // Gün Numarası
                Label lblGun = new Label("  " + gunSayaci);
                lblGun.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-text-fill: #555;");
                kutu.getChildren().add(lblGun);

                // Görevleri kutuya yerleştirme
                LocalDate buGun = suankiAy.atDay(gunSayaci);
                int gosterilenAdet = 0;

                for (TakvimOlayi olay : olayListesi) {
                    if (olay.getTarih().isEqual(buGun)) {
                        // Kutu taşmasın diye aynı kutuda sadece 2 görev
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

                // 2den fazla görev olan günler için
                if (gosterilenAdet > 2) {
                    Label lblDahaFazla = new Label("+ " + (gosterilenAdet - 2) + " tane daha...");
                    lblDahaFazla.setStyle("-fx-font-size: 9px; -fx-text-fill: #888; -fx-padding: 0 0 0 5;");
                    kutu.getChildren().add(lblDahaFazla);
                }

                // Tıklama
                final LocalDate tiklananTarih = buGun;
                kutu.setOnMouseClicked(event -> {
                    detayPenceresiniAc(tiklananTarih);
                });

                takvimGrid.add(kutu, j, i);
                gunSayaci++;
            }
        }
    }

    // Seçilen tarihe dair detay penceresini açar. Pencere kapandığında takvim otomatik güncellenir.
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

            // Pencere kapanınca takvimi yeniler.
            takvimiCiz();

        } catch (IOException e) {
            System.out.println("HATA: Detay penceresi açılamadı.");
            e.printStackTrace();
        }
    }

    // Tarihler arasında gezinmeyi sağlayan metodlar
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

    // Başkanların hızlı ekleme yapmasını sağlar.
    @FXML
    void btnEkleTiklandi(ActionEvent event) {
        // "+ Yeni Görev" butonuna basınca bugünün tarihini açar.
        detayPenceresiniAc(LocalDate.now());
    }
}