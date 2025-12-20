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

//VERİTABANI KONTROLÜ YAPAR
//SAYFA YÖNLENDİRMESİ YAPAR
public class LoginController {
    // FXML dosyasındaki kutucukları Java'ya tanıtıyoruz

    @FXML private TextField emailAlan; //ogrenci no girilen kısım
    @FXML private PasswordField sifreAlan;  //sifre girilen kutu
    @FXML private Label lblDurum; // Kullanılmıyor ama FXML hatası vermesin diye duruyor


    //veritabanı işlemleri için yardımcımızı çağırıyoruz

    private UserDAO userDAO = new UserDAO();

    // GİRİŞ YAP BUTONU
    //kullanıcı butona bastığında burası çalışır
    @FXML
    protected void girisYap(ActionEvent event) {

        //kutulardaki yazıları al ve boşluklarını temizle
        String ogrenciNo = emailAlan.getText().trim();
        String sifre = sifreAlan.getText().trim();

        //eski hata mesajı varsa temizle
        if (lblDurum != null) lblDurum.setText("");
        //kutular boş mu?
        if (ogrenciNo.isEmpty() || sifre.isEmpty()) {
            //boşsa uyarı penceresi açılır
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen öğrenci numarası ve şifre giriniz.");
            alert.showAndWait();
            return; // kodun devamını çalıştırma burada dur
        }

        System.out.println("Giriş Deneniyor: " + ogrenciNo);

        //VERİTABANI KONTROLÜ
        //Userdaoya soruyoruz : Bu numaralı ve şifreli biri var mı?
        BirimUyesi girisYapan = userDAO.loginUser(ogrenciNo, sifre);

        //Böyle birisi var mı
        if (girisYapan != null) {
            // SessionManager: Kullanıcıyı uygulamanın hafızasına kaydet
            SessionManager.getInstance().login(girisYapan);

            // Kullanıcının rolüne göre gideceği sayfayı bul
            String hedefDosya = hedefSayfayiBelirle(girisYapan);
            //o sayfaya git
            if (hedefDosya != null) {
                sayfaDegistir(event, hedefDosya);
            }
        } else {
            // giriş başarısız
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Giriş Başarısız");
            alert.setContentText("Kullanıcı adı veya şifre hatalı!");
            alert.showAndWait();
        }
    }

    //kullanıcının kim olduğuna bakıp hangi sayfaya gideceğine karar verir

    private String hedefSayfayiBelirle(BirimUyesi user) {
        String rol = user.getRol(); //or baskan
        int birimId = user.getBirimId();

        //topluluk başkanı
        if ("topluluk_baskani".equalsIgnoreCase(rol)) return "baskan_ekrani.fxml";

        //birim başkanları
        else if ("baskan".equalsIgnoreCase(rol)) {
            return (birimId == 2) ? "etkinlik_baskan.fxml" : (birimId == 3) ? "sosyal_baskan.fxml" : "baskan_ekrani.fxml";

            //normal üyeler
        } else {
            return (birimId == 2) ? "etkinlik_uye.fxml" : (birimId == 3) ? "sosyal_uye.fxml" : "baskan_ekrani.fxml";
        }
    }


    // şifremi unuttum alanı
    @FXML
    void btnSifremiUnuttumTiklandi(ActionEvent event) {
        // Sadece E-Posta İstenen küçük pencere aç
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Şifre Sıfırlama");
        dialog.setHeaderText("Şifre Yenileme");
        dialog.setContentText("Lütfen kayıtlı e-posta adresinizi giriniz:");

        //kullanıcının cevabını vermesini bekle
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

    //bu metot gerçekte mail atmaz ama ileride eklenecek kısım

    private void mailGonderimSimulasyonu(String mail) throws Exception {

        //eğer mailde @yoksa hata verir
        if (!mail.contains("@")) {
            throw new IllegalArgumentException("Mail formatı bozuk!");
        }

        try {
           // bu kısımda sunucuya bağlanıyormuşuz gibi yapıyoruz
            int deneme = 0;
            boolean baglandi = false;
            do {
                deneme++;
                if(deneme >= 1) baglandi = true; //ilk denemede bağlanmış gibi yap
            } while (!baglandi && deneme < 3);


            // Mail uzantısına  göre sunucu seçimi yapıyoruz
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
            // olası bağlanma hatasında mesaj atıyoruz
            throw new Exception("Bağlantı hatası oluştu.");
        }
    }

    // UYGULAMAYI  KAPAT
    @FXML
    void btnCikisYapTiklandi(ActionEvent event) {
        System.exit(0);  //programı durdurur
    }

    //kod tekrarı olmasın diye sayfalar arası geçişi buraya yazdık

    private void sayfaDegistir(ActionEvent event, String dosyaAdi) {
        try {
            //dosya yolu düzeltmesi
            if (!dosyaAdi.startsWith("/")) dosyaAdi = "/" + dosyaAdi;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(dosyaAdi));
            Parent root = fxmlLoader.load();
            //mevcut pencereyi bul ve sahnesini değiştir

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}