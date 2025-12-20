package com.proje.controller;

import com.proje.dao.ButceDAO;
import com.proje.model.ButceKaydi;
import com.proje.model.IslemTuru;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

/**
 * ButceController Sınıfı
 * Görevi: Bütçe sayfasındaki (FXML) görsel öğelerle arka plandaki kodların (Logic) haberleşmesini sağlar.
 * Kullanıcı "Ekle" dediğinde veritabanına gider, tabloyu günceller ve hesaplamaları yapar.
 */
public class ButceController {

    // --- FXML'DEN GELEN ARAYÜZ ELEMANLARI ---

    @FXML private Label lblGelir;       // Toplam geliri gösteren yazı alanı
    @FXML private Label lblGider;       // Toplam gideri gösteren yazı alanı
    @FXML private Label lblKayitSayisi; // Kaç tane işlem olduğunu gösteren sayaç
    @FXML private Label lblBakiye;      // (Gelir - Gider) sonucunu gösteren alan

    // --- TABLO (TableView) AYARLARI ---
    @FXML private TableView<ButceKaydi> tabloButce;          // Listenin kendisi
    @FXML private TableColumn<ButceKaydi, String> colTarih;  // Tarih sütunu
    @FXML private TableColumn<ButceKaydi, String> colTur;    // Tür sütunu (Yemek, Aidat vb.)
    @FXML private TableColumn<ButceKaydi, String> colAciklama; // Açıklama sütunu
    @FXML private TableColumn<ButceKaydi, Double> colMiktar; // Para miktarı sütunu

    // --- KULLANICI GİRİŞ ALANLARI ---
    @FXML private ComboBox<IslemTuru> cmbTur; // Açılır liste (Gelir/Gider türleri)
    @FXML private DatePicker datePicker;      // Tarih seçici takvim
    @FXML private TextField txtAciklama;      // Metin giriş kutusu
    @FXML private TextField txtMiktar;        // Sayı giriş kutusu

    // --- VERİTABANI VE LİSTE ---
    // Veritabanı işlemlerini yapan "İşçi Sınıf" (DAO)
    private ButceDAO dao = new ButceDAO();

    // ObservableList: İçine bir şey ekleyince Tablo otomatik güncellenir.
    // Normal ArrayList yerine bunu kullanmamızın sebebi JavaFX'in anlık tepki verebilmesidir.
    private ObservableList<ButceKaydi> veriListesi = FXCollections.observableArrayList();

    /**
     * initialize() Metodu:
     * Bu sayfa ilk açıldığı saniyede (henüz ekrana gelmeden) OTOMATİK çalışır.
     * Başlangıç ayarlarını (Tablo sütunları, varsayılan tarih vb.) burada yaparız.
     */
    @FXML
    public void initialize() {
        System.out.println("ButceController başlatılıyor...");

        // 1. SÜTUN AYARLARI
        // Tabloya diyoruz ki: "colTarih sütununda, ButceKaydi sınıfındaki 'tarih' değişkenini göster."
        // PropertyValueFactory içindeki isimler, Model sınıfındaki değişken isimleriyle AYNI olmalıdır.
        colTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        colTur.setCellValueFactory(new PropertyValueFactory<>("tur"));
        colAciklama.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
        colMiktar.setCellValueFactory(new PropertyValueFactory<>("miktar"));

        // 2. COMBOBOX DOLDURMA
        // IslemTuru Enum'ındaki (YEMEK, AIDAT, ULASIM vb.) tüm seçenekleri kutuya doldur.
        cmbTur.getItems().setAll(IslemTuru.values());
        // Kutunun boş kalmaması için ilk seçeneği  seçili hale getir.
        cmbTur.getSelectionModel().selectFirst();

        // 3. TARİH AYARI
        // Tarih seçiciyi bugünün tarihine ayarla.
        datePicker.setValue(LocalDate.now());

        // 4. TABLO İLE LİSTEYİ BAĞLAMA
        // veriListesinde ne varsa tabloda onu göster diyoruz.
        tabloButce.setItems(veriListesi);

        // 5. SATIR RENKLENDİRME (GELİŞMİŞ ÖZELLİK)
        // Tablo satırlarını tek tek kontrol et:
        // Eğer işlem GELİR ise satır YEŞİL olsun.
        // Eğer işlem GİDER ise satır KIRMIZI olsun.
        tabloButce.setRowFactory(tv -> new TableRow<ButceKaydi>() {
            @Override
            protected void updateItem(ButceKaydi item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    // Satır boşsa rengi sıfırla
                    setStyle("");
                } else {
                    // Enum içindeki isGelirMi() metodunu kullanarak kontrol ediyoruz
                    if (item.getTur().isGelirMi()) {
                        setStyle("-fx-background-color: #E8F5E9;"); // Açık Yeşil
                    } else {
                        setStyle("-fx-background-color: #FFEBEE;"); // Açık Kırmızı
                    }
                }
            }
        });

        // 6. VERİLERİ ÇEK
        // Veritabanındaki eski kayıtları getirip tabloya koy.
        listeyiYenile();
    }

    /**
     * "EKLE" Butonuna Basılınca Çalışan Metot
     */
    @FXML
    void btnEkle(ActionEvent event) {
        try {
            // 1. VERİLERİ AL
            // Ekranda kullanıcının kutulara yazdığı bilgileri değişkenlere alıyoruz.
            String aciklama = txtAciklama.getText().trim();
            String miktarStr = txtMiktar.getText().trim();
            IslemTuru tur = cmbTur.getValue(); // ComboBox'tan seçilen
            LocalDate secilenTarih = datePicker.getValue();

            // 2. KONTROLLER
            // Kullanıcı boş bırakmış mı diye bakıyoruz.
            if (aciklama.isEmpty()) { uyariVer("Açıklama boş olamaz."); return; }
            if (miktarStr.isEmpty()) { uyariVer("Miktar boş olamaz."); return; }
            // Tarih seçmediyse otomatik bugünü al.
            if (secilenTarih == null) secilenTarih = LocalDate.now();

            // 3. ÇEVİRME
            // String olan "500" yazısını sayısal 500.0 değerine çeviriyoruz.
            // Eğer kullanıcı "abc" yazdıysa burada hata  çıkar.
            double miktar = Double.parseDouble(miktarStr);

            // 4. VERİTABANINA KAYDET
            // DAO sınıfına gönderiyoruz. O da veritabanına INSERT atıyor.
            // secilenTarih.toString() -> "2024-05-20" formatına çevirir.
            boolean sonuc = dao.ekle(aciklama, miktar, secilenTarih.toString(), tur);

            if(sonuc) {
                // Başarılıysa kutuları temizle ki yeni kayıt girilebilsin.
                txtAciklama.clear();
                txtMiktar.clear();

                // Listeyi güncelle (Yeni kayıt tabloda görünsün)
                listeyiYenile();

                // Kullanıcıya "Başarılı" mesajı göster
                Alert bilgi = new Alert(Alert.AlertType.INFORMATION);
                bilgi.setTitle("Başarılı");
                bilgi.setHeaderText(null);
                bilgi.setContentText("Kayıt başarıyla eklendi.");
                bilgi.show();
            }

        } catch (NumberFormatException e) {
            // Eğer kullanıcı "Miktar" kutusuna harf yazarsa program çökmesin, bu uyarı çıksın.
            uyariVer("Lütfen 'Tutar' kısmına sadece sayı giriniz.");
        } catch (Exception e) {
            // Beklenmeyen başka bir hata olursa
            Alert hata = new Alert(Alert.AlertType.ERROR);
            hata.setTitle("Hata Detayı");
            hata.setHeaderText("Kayıt Eklenemedi!");
            hata.setContentText("Hata Mesajı: " + e.getMessage());
            hata.showAndWait();
            e.printStackTrace(); // Hatanın detayını konsola bas (Yazılımcı görsün diye)
        }
    }

    /**
     "SİL" Butonuna Basılınca Çalışan Metot
     */
    @FXML
    void btnSil(ActionEvent event) {
        System.out.println("Sil butonuna basıldı.");

        // 1. SEÇİLİ OLANI BUL
        // Kullanıcı tablodan hangi satıra tıkladı
        ButceKaydi secilen = tabloButce.getSelectionModel().getSelectedItem();

        if(secilen != null) {
            // 2. SİLME İŞLEMİ
            // DAO'ya seçilen satırın ID'sini veriyoruz, o da siliyor
            dao.sil(secilen.getId());

            // 3. GÜNCELLEME
            // Kayıt silindikten sonra listeyi yenilemek gerekir.
            listeyiYenile();
        } else {
            // Hiçbir şeye tıklamadan sil butonuna basarsa uyar.
            uyariVer("Silinecek kaydı seçiniz.");
        }
    }

    /**
     * Listeyi Yenileme ve Hesaplama Metodu
     * Veritabanındaki en güncel verileri çeker ve ekrana basar.
     */
    private void listeyiYenile() {
        // Model sınıfındaki static sayacı sıfırla (Yeniden sayacağız)
        if (lblKayitSayisi != null) ButceKaydi.toplamKayitSayisi = 0;

        // Listenin içini tamamen boşalt (Eskileri sil)
        veriListesi.clear();

        // Veritabanından tüm kayıtları çekip listeye ekle
        veriListesi.addAll(dao.tumKayitlariGetir());

        // Toplam Gelir/Gider hesaplarını yap
        hesapla();

        // Toplam kaç kayıt olduğunu etikete yaz
        if (lblKayitSayisi != null)
            lblKayitSayisi.setText(String.valueOf(ButceKaydi.toplamKayitSayisi));
    }

    /**
     * Muhasebe Hesabı Yapan Metot
     * Listedeki tüm elemanları tek tek gezer, gelirleri ve giderleri toplar.
     */
    private void hesapla() {
        double toplamGelir = 0;
        double toplamGider = 0;

        // for-each döngüsü ile listedeki her kaydı (k) geziyoruz
        for (ButceKaydi k : veriListesi) {
            if (k.getTur().isGelirMi()) {
                // Gelirse gelir kasasına ekle
                toplamGelir += k.getMiktar();
            } else {
                // Giderse gider kasasına ekle
                toplamGider += k.getMiktar();
            }
        }

        // Sonuçları ekrandaki Label'lara yazdır.
        // String.format("%.2f TL") -> Virgülden sonra 2 basamak göster (Örn: 150.50 TL)
        if (lblGelir != null) lblGelir.setText(String.format("%.2f TL", toplamGelir));
        if (lblGider != null) lblGider.setText(String.format("%.2f TL", toplamGider));

        // Bakiye = Gelir - Gider
        if (lblBakiye != null) lblBakiye.setText(String.format("%.2f TL", (toplamGelir - toplamGider)));
    }

    // Ekranı temizlemek için yardımcı metot (Gerekirse kullanılır)
    private void temizle() {
        txtAciklama.clear();
        txtMiktar.clear();
        datePicker.setValue(LocalDate.now());
    }

    // Kod tekrarı yapmamak için, uyarı mesajlarını tek bir yerden veriyoruz.
    private void uyariVer(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Uyarı ikonu çıkar
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.show(); // Pencereyi göster
    }
}