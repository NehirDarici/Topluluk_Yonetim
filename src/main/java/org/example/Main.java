package org.example;

import com.proje.dao.UserDAO;
import com.proje.database.DBHelper;
import com.proje.dosya.DosyaIslemleri; // Yeni ekledik
import com.proje.manager.SessionManager;
import com.proje.model.BirimUyesi;

public class Main {
    public static void main(String[] args) {

        // Program başladığında log tutalım
        DosyaIslemleri.logEkle("Sistem başlatıldı.");

        System.out.println("Sistem başlatılıyor...");
        DBHelper.tablolariOlustur();

        UserDAO userDAO = new UserDAO();
        userDAO.addUnit("Yazılım Ekibi");

        // Test Kullanıcısı
        BirimUyesi testUye = new BirimUyesi("2023010", "Zeynep Yılmaz", "123", "baskan", 1);
        userDAO.addUser(testUye);

        // --- GİRİŞ TESTİ VE LOGLAMA ---

        String denenenNo = "2023010";
        BirimUyesi girisYapan = userDAO.loginUser(denenenNo, "123");

        if (girisYapan != null) {
            SessionManager.getInstance().login(girisYapan);

            // Başarılı girişi dosyaya kaydet
            DosyaIslemleri.logEkle("BAŞARILI GİRİŞ: " + girisYapan.getAdSoyad() + " (" + girisYapan.getOgrenciNo() + ")");

            System.out.println("✅ Hoşgeldiniz: " + girisYapan.getAdSoyad());

        } else {
            // Başarısız denemeyi dosyaya kaydet (Güvenlik logu)
            DosyaIslemleri.logEkle("HATALI GİRİŞ DENEMESİ: " + denenenNo);
            System.out.println("❌ Giriş Başarısız!");
        }

        // Testin sonunda dosyadaki logları okuyup ekrana basalım
        DosyaIslemleri.loglariOku();
    }
}