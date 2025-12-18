package com.proje.utility;

import com.proje.interfaces.IAramaKriteri;
import java.util.ArrayList;
import java.util.List;

// PDF Madde 5.1: En az 1 generic sınıf (AramaMotoru<T>)
public class AramaMotoru<T> {

    // PDF Madde 5.1: Generic metot
    public List<T> ara(List<T> anaListe, String aranan, IAramaKriteri<T> kriter) {
        // PDF Madde 2.2: String işlemleri (null kontrolü ve boşluk silme - trim)
        if (aranan == null || aranan.trim().isEmpty()) {
            return new ArrayList<>(anaListe); // Arama boşsa hepsini döndür
        }

        String arananKucuk = aranan.toLowerCase().trim();
        List<T> sonucListesi = new ArrayList<>(); // PDF Madde 5.2: ArrayList kullanımı

        // PDF Madde 9: Döngüler (for-each)
        for (T eleman : anaListe) {
            // Kriter interface'i üzerinden kontrol (Polimorfizm mantığına katkı)
            if (kriter.uygunMu(eleman, arananKucuk)) {
                sonucListesi.add(eleman);
            }
        }
        return sonucListesi;
    }
}