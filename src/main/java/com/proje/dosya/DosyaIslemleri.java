package com.proje.dosya;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; // PDF Madde 8 için gerekli

/**
 * PDF Madde 1.1: Yardımcı / Utility Sınıfı
 */
public class DosyaIslemleri {

    // PDF Madde 10: Platform bağımsız dosya yolu yönetimi
    // file.separator kullanımı işletim sistemi farklarını (Windows/Linux) ortadan kaldırır.
    private static final String AYIRICI = System.getProperty("file.separator");
    private static final String LOG_DOSYASI = "sistem_loglari.txt";

    /**
     * PDF Madde 7: 'throws' ifadesi ile exception'ı çağıran tarafa iletir.
     * PDF Madde 8: BufferedWriter ve FileWriter yapısını kullanır.
     */
    public static void logEkle(String mesaj) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String zamanDamgasi = dtf.format(LocalDateTime.now());
        String logSatiri = "[" + zamanDamgasi + "] " + mesaj;

        // try-with-resources: Dosyayı otomatik kapatır.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_DOSYASI, true))) {
            writer.write(logSatiri);
            writer.newLine();
        } catch (IOException e) {
            // PDF Madde 7: Hata yönetimi
            System.err.println("Log yazılırken bir hata oluştu: " + e.getMessage());
            throw e; // Hatayı fırlatarak üst katmana bildirir
        } finally {
            // PDF Madde 7: En az bir yerde finally bloğu kullanılmalıdır.
            // Dosya kapansa bile bu blok çalışır.
            System.out.println("Dosya yazma işlemi denemesi sonlandırıldı.");
        }
    }

    /**
     * PDF Madde 8: Scanner ile dosyadan okuma işlemi.
     * PDF Madde 8: NoSuchElementException durumunu hasNextLine() ile önler.
     */
    public static void loglariOku() {
        // PDF Madde 8: Relative path kullanımı (proje kök dizinindeki dosya)
        File dosya = new File(LOG_DOSYASI);

        if (!dosya.exists()) {
            System.out.println("Okunacak log dosyası henüz mevcut değil: " + LOG_DOSYASI);
            return;
        }

        // PDF Madde 8: Scanner(File) yapısı kullanımı
        try (Scanner okuyucu = new Scanner(dosya)) {
            System.out.println("\n--- GEÇMİŞ LOG KAYITLARI (Scanner ile Okundu) ---");

            // PDF Madde 8: hasNextLine() kontrolü ile güvenli okuma
            while (okuyucu.hasNextLine()) {
                String satir = okuyucu.nextLine();
                System.out.println(satir);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dosya okuma sırasında hata: " + e.getMessage());
        } finally {
            // PDF Madde 7: İkinci bir finally bloğu örneği
            System.out.println("----------------------------\n");
        }
    }
}