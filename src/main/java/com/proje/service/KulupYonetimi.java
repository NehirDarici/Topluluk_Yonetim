package com.proje.service;

// Model sınıfını içeri aktarıyoruz (Hatanın asıl çözümü bu satır)
import com.proje.model.Uye;
import com.proje.exception.HataliGirisException; // Eğer exception paketini yaptıysan

import java.util.*;

public class KulupYonetimi {

    // İSTER: En az 3 koleksiyon türü kullanımı
    private List<Uye> uyeListesi;          // 1. ArrayList (Sıralı liste tutmak için)
    private Map<String, Uye> uyeMap;       // 2. HashMap (TC ile hızlı arama yapmak için)
    private Set<String> etkinlikTurleri;   // 3. HashSet (Benzersiz etkinlik türleri için)

    public KulupYonetimi() {
        // İSTER: Referans tipi arayüz kullanımı (List<...> = new ArrayList...)
        this.uyeListesi = new ArrayList<>();
        this.uyeMap = new HashMap<>();
        this.etkinlikTurleri = new HashSet<>();
    }

    // --- 1. EKLEME (add / put) ---
    public void uyeEkle(Uye yeniUye) {
        // Listeye ekle
        uyeListesi.add(yeniUye);

        // Map'e ekle (Anahtar: TC, Değer: Uye Nesnesi)
        uyeMap.put(yeniUye.getTcNo(), yeniUye);

        System.out.println(yeniUye.getAd() + " kulübe başarıyla eklendi.");
    }

    public void etkinlikTuruEkle(String tur) {
        // Set özelliği: Aynı eleman varsa tekrar eklemez
        if (etkinlikTurleri.add(tur)) {
            System.out.println("Yeni etkinlik türü eklendi: " + tur);
        } else {
            System.out.println("Bu etkinlik türü zaten mevcut.");
        }
    }

    // --- 2. SİLME (remove) ---
    public void uyeSil(String tcNo) {
        if (uyeMap.containsKey(tcNo)) {
            Uye silinecekUye = uyeMap.get(tcNo);

            // Hem listeden hem mapten siliyoruz
            uyeListesi.remove(silinecekUye);
            uyeMap.remove(tcNo);

            System.out.println("Üye silindi: " + silinecekUye.getAd());
        } else {
            System.out.println("Silinecek üye bulunamadı.");
        }
    }

    // --- 3. GÜNCELLEME (set / replace) ---
    public void uyeGuncelle(String tcNo, String yeniAd, String yeniSoyad) {
        if (uyeMap.containsKey(tcNo)) {
            Uye uye = uyeMap.get(tcNo);

            // Bilgileri güncelle
            uye.setAd(yeniAd);
            uye.setSoyad(yeniSoyad);

            // Map üzerinde replace (Nesne referansı aynı olsa da metot örneği olsun)
            uyeMap.replace(tcNo, uye);

            System.out.println("Üye bilgileri güncellendi: " + tcNo);
        } else {
            System.out.println("Güncellenecek üye bulunamadı.");
        }
    }

    // --- 4. ARAMA (contains, get, indexOf) ---
    public void uyeAra(String tcNo) {
        // Map ile en hızlı arama (O(1))
        if (uyeMap.containsKey(tcNo)) {
            System.out.println("BULUNDU: " + uyeMap.get(tcNo).toString());
        } else {
            System.out.println("Kayıtlı üye yok.");
        }
    }

    public void listedeVarMiKontrolu(Uye uye) {
        // ArrayList üzerinde 'contains' kullanımı
        // Uye sınıfındaki equals() metodu sayesinde çalışır
        if (uyeListesi.contains(uye)) {
            System.out.println(uye.getAd() + " listede mevcut.");
            System.out.println("Sıra Numarası (Index): " + uyeListesi.indexOf(uye));
        } else {
            System.out.println("Liste içinde bulunamadı.");
        }
    }

    // --- 5. SIRALAMA (Collections.sort) ---
    public void uyeleriIsmeGoreSirala() {
        // İsim (Ad) özelliğine göre A'dan Z'ye sıralama
        Collections.sort(uyeListesi, new Comparator<Uye>() {
            @Override
            public int compare(Uye u1, Uye u2) {
                // String sınıfının compareTo metodu kullanılır
                return u1.getAd().compareToIgnoreCase(u2.getAd());
            }
        });

        System.out.println("\n--- İsim Sırasına Göre Üyeler ---");
        for (Uye u : uyeListesi) {
            System.out.println(u);
        }
    }
}