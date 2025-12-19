package com.proje.controller;

import com.proje.dao.UserDAO;
import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField emailAlan;
    @FXML private PasswordField sifreAlan;
    @FXML private Label lblDurum; // Kullanılmıyor ama FXML hatası vermesin diye duruyor

    private UserDAO userDAO = new UserDAO();

    // --- GİRİŞ YAP BUTONU (AYNI KALIYOR) ---
    @FXML
    protected void girisYap(ActionEvent event) {
        String ogrenciNo = emailAlan.getText().trim();
        String sifre = sifreAlan.getText().trim();

        if (lblDurum != null) lblDurum.setText("");

        if (ogrenciNo.isEmpty() || sifre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen öğrenci numarası ve şifre giriniz.");
            alert.showAndWait();
            return;
        }

        System.out.println("Giriş Deneniyor: " + ogrenciNo);
        BirimUyesi girisYapan = userDAO.loginUser(ogrenciNo, sifre);

        if (girisYapan != null) {
            SessionManager.getInstance().login(girisYapan);
            String hedefDosya = hedefSayfayiBelirle(girisYapan);
            if (hedefDosya != null) {
                sayfaDegistir(event, hedefDosya);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Giriş Başarısız");
            alert.setContentText("Kullanıcı adı veya şifre hatalı!");
            alert.showAndWait();
        }
    }

    private String hedefSayfayiBelirle(BirimUyesi user) {
        String rol = user.getRol();
        int birimId = user.getBirimId();

        if ("topluluk_baskani".equalsIgnoreCase(rol)) return "baskan_ekrani.fxml";
        else if ("baskan".equalsIgnoreCase(rol)) {
            return (birimId == 2) ? "etkinlik_baskan.fxml" : (birimId == 3) ? "sosyal_baskan.fxml" : "baskan_ekrani.fxml";
        } else {
            return (birimId == 2) ? "etkinlik_uye.fxml" : (birimId == 3) ? "sosyal_uye.fxml" : "baskan_ekrani.fxml";
        }
    }

    // -------------------------------------------------------------
    // --- GÜNCELLENEN POP-UP VE VALIDASYON MANTIĞI ---
    // -------------------------------------------------------------
    @FXML
    void btnSifremiUnuttumTiklandi(ActionEvent event) {
        // 1. Sadece E-Posta İstiyoruz
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Şifre Sıfırlama");
        dialog.setHeaderText("Şifre Yenileme");
        dialog.setContentText("Lütfen kayıtlı e-posta adresinizi giriniz:");

        Optional<String> sonuc = dialog.showAndWait();

        if (sonuc.isPresent()) {
            String girilenMail = sonuc.get().trim();

            // 2. KONTROL: Boş mu?
            if (girilenMail.isEmpty()) {
                uyariGoster("Eksik Bilgi", "E-posta alanı boş bırakılamaz!");
                return;
            }

            // 3. KONTROL: Gerçekten Mail mi? (@ ve . işareti var mı?)
            if (!girilenMail.contains("@") || !girilenMail.contains(".")) {
                uyariGoster("Geçersiz Format", "Lütfen geçerli bir e-posta adresi giriniz.\n(Örnek: ornek@mail.com)");
                return; // İşlemi burada kesiyoruz, başarı mesajı çıkmıyor.
            }

            try {
                // PDF TEKNİK ŞARTLARI
                mailGonderimSimulasyonu(girilenMail);

                // HER ŞEY TAMAMSA BAŞARI MESAJI
                Alert bilgi = new Alert(Alert.AlertType.INFORMATION);
                bilgi.setTitle("Başarılı");
                bilgi.setHeaderText("Bağlantı Gönderildi");
                bilgi.setContentText("Şifre yenileme bağlantısı başarıyla gönderilmiştir.\n" + girilenMail);
                bilgi.showAndWait();

            } catch (Exception e) {
                Alert hata = new Alert(Alert.AlertType.ERROR);
                hata.setTitle("Hata");
                hata.setContentText("Sunucu hatası: " + e.getMessage());
                hata.show();
            }
        }
    }

    // Yardımcı uyarı metodu (Kod tekrarını önlemek için)
    private void uyariGoster(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    /**
     * PDF TEKNİK GEREKSİNİMLERİ
     * - do-while
     * - switch-case
     * - Exception handling
     */
    private void mailGonderimSimulasyonu(String mail) throws Exception {

        // PDF Madde 3: Exception Fırlatma (Ekstra güvenlik)
        if (!mail.contains("@")) {
            throw new IllegalArgumentException("Mail formatı bozuk!");
        }

        try {
            // PDF Madde 9: do-while Döngüsü
            int deneme = 0;
            boolean baglandi = false;
            do {
                deneme++;
                if(deneme >= 1) baglandi = true;
            } while (!baglandi && deneme < 3);

            // PDF Madde 9: switch-case Yapısı
            // Mail uzantısına (Domain) göre sunucu seçimi yapıyoruz
            String uzanti = mail.substring(mail.indexOf("@") + 1).toLowerCase(); // @'den sonrasını al
            String sunucuTuru;

            switch (uzanti) {
                case "gmail.com":
                    sunucuTuru = "Google SMTP Sunucusu";
                    break;
                case "hotmail.com":
                case "outlook.com":
                    sunucuTuru = "Microsoft Outlook Sunucusu";
                    break;
                case "yahoo.com":
                    sunucuTuru = "Yahoo Mail Services";
                    break;
                default:
                    sunucuTuru = "Kurumsal/Özel SMTP Sunucusu";
                    break;
            }

            System.out.println(sunucuTuru + " üzerinden gönderiliyor: " + mail);

        } catch (Exception e) {
            // PDF Madde 7: Hata yakalama
            throw new Exception("Bağlantı hatası oluştu.");
        }
    }

    // --- DİĞER STANDART METOTLAR ---
    @FXML
    void btnCikisYapTiklandi(ActionEvent event) {
        System.exit(0);
    }

    private void sayfaDegistir(ActionEvent event, String dosyaAdi) {
        try {
            if (!dosyaAdi.startsWith("/")) dosyaAdi = "/" + dosyaAdi;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(dosyaAdi));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}