package com.proje.controller;

import javafx.scene.layout.VBox;
import com.proje.dao.GorevDAO;
import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;
import com.proje.model.TakvimOlayi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

//GİREN KİŞİ KİM ONA GÖRE ekle sil butonlarını görür

public class GunDetayController {

    //ekrandaki nesneler
//pencerenin tepesindeki başlık
    @FXML private Label lblTarihBaslik;
    //görev ekleme kutusu
    @FXML private VBox boxEklemeFormu;
    //o günün görevlerinin listelendiği alan
    @FXML private ListView<TakvimOlayi> listeEtkinlikler;

    // --- GÖREV EKLEME ALANLARI ---
    @FXML private TextField txtBaslik;
    @FXML private TextArea txtAciklama;
    @FXML private Button btnKaydet;

    // --- BAŞKAN SEÇİMİ KUTUSU --- radyo butonları
    //ıd 1 ise görünecek ekran
    @FXML private HBox boxBirimSecimi;
    @FXML private RadioButton rbSosyal;
    @FXML private RadioButton rbEtkinlik;
    @FXML private ToggleGroup grupBirim;

    //Değişkenler

    private LocalDate secilenTarih;
    private GorevDAO gorevDAO = new GorevDAO();
    //bu kişi ekleme silme yapabilir mi
    private boolean yetkiVarMi = false;

    //pencere açılırken çalışan ayarlar
    @FXML
    public void initialize() {
        //  KULLANICI YETKİ KONTROLÜ --- usersession ile bulunur
        BirimUyesi aktifUye = SessionManager.getInstance().getCurrentUser();

        // Rol "uye" değilse (baskan veya topluluk_baskani ise) yetki ver
        if (aktifUye != null && !aktifUye.getRol().equals("uye")) {
            yetkiVarMi = true;
        }

        // -Yetkiye göre gizle-göster
        if (boxEklemeFormu != null) {
            // Yetki yoksa kutu komple gizlenir ve yer kaplamaz (Managed: false)
            boxEklemeFormu.setVisible(yetkiVarMi);
            boxEklemeFormu.setManaged(yetkiVarMi);
        }

        // Başkanlar için birim seçimi detayı
        if (yetkiVarMi) {
            // Sadece Genel Başkan (Birim ID: 1) seçim kutusunu görsün
            if (aktifUye != null && aktifUye.getBirimId() == 1) {
                if (boxBirimSecimi != null) {
                    boxBirimSecimi.setVisible(true);
                    boxBirimSecimi.setManaged(true);
                    rbEtkinlik.setSelected(true);
                }
            } else {
                // Diğer birim başkanları seçim yapamaz
                if (boxBirimSecimi != null) {
                    boxBirimSecimi.setVisible(false);
                    boxBirimSecimi.setManaged(false);
                }
            }
        }

        // LİSTE TASARIMI (Sil Butonu Mantığı)
        //listview normalde sadece yazı gösterir.biz ona her satırı özel tasarla diyoruz
        listeEtkinlikler.setCellFactory(new Callback<ListView<TakvimOlayi>, ListCell<TakvimOlayi>>() {
            @Override
            public ListCell<TakvimOlayi> call(ListView<TakvimOlayi> param) {
                return new ListCell<TakvimOlayi>() {
                    @Override
                    protected void updateItem(TakvimOlayi olay, boolean empty) {
                        super.updateItem(olay, empty);

                        //eğer satır boşsa yani veri yoksa temizle
                        if (empty || olay == null) {
                            setText(null);
                            setGraphic(null);
                        } else {

                            //satır tasarımı-> birim rengine göre
                            Region renkKutu = new Region();
                            renkKutu.setPrefSize(10, 10);
                            renkKutu.setStyle("-fx-background-color: " + olay.getRenkKodu() + "; -fx-background-radius: 5;");

                            Label lblBaslik = new Label(olay.getBaslik());
                            lblBaslik.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

                            Region bosluk = new Region();
                            HBox.setHgrow(bosluk, Priority.ALWAYS);

                            HBox satir = new HBox(10, renkKutu, lblBaslik, bosluk);

                            // Sadece yetki varsa sil butonu ekle
                            if (yetkiVarMi) {
                                Button btnSil = new Button("Sil");
                                btnSil.setStyle("-fx-background-color: #ffe6e6; -fx-text-fill: red; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 11px;");
                                btnSil.setOnAction(event -> silmeIslemiYap(olay));
                                satir.getChildren().add(btnSil);
                            }
                            //hazırladığımız tasarımı satıra yerleştir
                            satir.setAlignment(Pos.CENTER_LEFT);
                            setGraphic(satir);
                            setText(null);
                        }
                    }
                };
            }
        });
    }
    //SİLME İŞLEMİ

    private void silmeIslemiYap(TakvimOlayi olay) {
        //emin misin diye soran kutu
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sil");
        alert.setHeaderText("Siliniyor: " + olay.getBaslik());
        alert.setContentText("Bu görevi silmek istediğinize emin misiniz?");

        Optional<ButtonType> sonuc = alert.showAndWait();
        if (sonuc.isPresent() && sonuc.get() == ButtonType.OK) {
            //veritabanından sil
            if (gorevDAO.gorevSil(olay.getId())) {
                listeyiGuncelle(); //listeyi yenile
            } else {
                Alert hata = new Alert(Alert.AlertType.ERROR);
                hata.setTitle("Hata");
                hata.setHeaderText("Silinemedi!");
                hata.show();
            }
        }
    }
    //DIŞARIDAN VERİ ALMA
    //ana takvim sayfası tıklanılan tarihi buraya gönderir
    public void veriYukle(LocalDate tarih) {
        this.secilenTarih = tarih;
        //tarihi istenilen formatta yaz
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        //o tarihteki verileri çek
        lblTarihBaslik.setText(tarih.format(format));
        listeyiGuncelle();
    }

    //Listeyi Yenileme
    private void listeyiGuncelle() {
        listeEtkinlikler.getItems().clear();
        //tüm görevleri getir
        List<TakvimOlayi> tumOlaylar = gorevDAO.getTumGorevler();

        //sadece seçili tarihe ait olanları filtrele
        for (TakvimOlayi olay : tumOlaylar) {
            if (olay.getTarih().isEqual(secilenTarih)) {
                listeEtkinlikler.getItems().add(olay);
            }
        }
    }
  //YENİ GÖREV KAYDETME
    @FXML
    void btnKaydetTiklandi(ActionEvent event) {
        if (!yetkiVarMi) return; //güvenlik kontrolü

        String baslik = txtBaslik.getText();
        String aciklama = txtAciklama.getText();

        if (baslik.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hata");
            alert.setHeaderText("Başlık Boş Olamaz");
            alert.showAndWait();
            return;
        }
        // HANGİ BİRİME kaydedeceğiz
        BirimUyesi aktifUye = SessionManager.getInstance().getCurrentUser();
        int hedefBirimId = 0;

        if (aktifUye != null) {
            if (aktifUye.getBirimId() == 1) {
                //topluluk başkanı ise
                hedefBirimId = rbSosyal.isSelected() ? 3 : 2;
            } else {
                //birim başkanı ise kendi idsi
                hedefBirimId = aktifUye.getBirimId();
            }
        }
       //veri tabanına kaydet
        gorevDAO.gorevEkle(baslik, aciklama, secilenTarih.toString(), "Gorev", hedefBirimId);
        //pencereyi kapat
        Stage stage = (Stage) txtBaslik.getScene().getWindow();
        stage.close();
    }
}