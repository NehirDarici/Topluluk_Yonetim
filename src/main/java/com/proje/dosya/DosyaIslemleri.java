package com.proje.dosya;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; // PDF Madde 8 için gerekli

// Dosya işlemleri için oluşturulan yardımcı sınıf
public class DosyaIslemleri {

    // file.separator kullanımı işletim sistemi farklarını (Windows/Linux) ortadan kaldırır.
    private static final String AYIRICI = System.getProperty("file.separator");
    // Logların tutulacağı dosya adı
    private static final String LOG_DOSYASI = "sistem_loglari.txt";


    // Sisteme yapılan işlemleri tarih ve saat bilgisiyle kaydeder.
    public static void logEkle(String mesaj) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String zamanDamgasi = dtf.format(LocalDateTime.now());
        String logSatiri = "[" + zamanDamgasi + "] " + mesaj;

        // try-with-resources: Dosyayı otomatik kapatır.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_DOSYASI, true))) {
            writer.write(logSatiri);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Log yazılırken bir hata oluştu: " + e.getMessage());
            // Hatayı fırlatarak üst katmana bildirir.
            throw e;
        } finally {
            // Dosya kapansa bile bu blok çalışır.
            System.out.println("Dosya yazma işlemi denemesi sonlandırıldı.");
        }
    }

    // Log dosyasındaki geçmiş kayıtları okur ve ekrana yazdırır.
    public static void loglariOku() {
        File dosya = new File(LOG_DOSYASI);

        if (!dosya.exists()) {
            System.out.println("Okunacak log dosyası henüz mevcut değil: " + LOG_DOSYASI);
            return;
        }

        // Dosya Okuma
        try (Scanner okuyucu = new Scanner(dosya)) {
            System.out.println("\n--- GEÇMİŞ LOG KAYITLARI (Scanner ile Okundu) ---");

            // Dosyanın sonuna gelinip gelinmediğini kontrol eder.
            while (okuyucu.hasNextLine()) {
                String satir = okuyucu.nextLine();
                System.out.println(satir);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dosya okuma sırasında hata: " + e.getMessage());
        } finally {
            System.out.println("----------------------------\n");
        }
    }
}