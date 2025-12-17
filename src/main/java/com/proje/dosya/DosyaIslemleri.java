package com.proje.dosya;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DosyaIslemleri {

    private static final String LOG_DOSYASI = "sistem_loglari.txt";

    // 1. LOG TUTMA METODU (Dosyaya Yazma - Append Mode)
    // Bu metot her çağrıldığında dosyaya yeni bir satır ekler.
    public static void logEkle(String mesaj) {
        // Tarih formatı: Gün-Ay-Yıl Saat:Dakika:Saniye
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String zamanDamgasi = dtf.format(LocalDateTime.now());

        // Yazılacak format: [Tarih] Mesaj
        String logSatiri = "[" + zamanDamgasi + "] " + mesaj;

        // try-with-resources yapısı (Dosyayı otomatik kapatır)
        // FileWriter(..., true) -> true parametresi "üzerine yazma, sonuna ekle" demektir.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_DOSYASI, true))) {
            writer.write(logSatiri);
            writer.newLine(); // Alt satıra geç
            // System.out.println("Log kaydedildi."); // Konsolu kirletmeyelim, kapalı kalsın
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // 2. DOSYA OKUMA METODU (Test amaçlı)
    // Log dosyasının içeriğini konsola basar.
    public static void loglariOku() {
        File dosya = new File(LOG_DOSYASI);
        if (!dosya.exists()) {
            System.out.println("Henüz log dosyası oluşmamış.");
            return;
        }

        System.out.println("\n--- GEÇMİŞ LOG KAYITLARI ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_DOSYASI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                System.out.println(satir);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        System.out.println("----------------------------\n");
    }
}
