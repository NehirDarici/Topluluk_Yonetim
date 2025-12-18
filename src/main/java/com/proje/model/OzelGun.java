package com.proje.model;

import java.time.LocalDate; // 1. BU KÜTÜPHANEYİ EKLEMELİSİN

public class OzelGun extends TakvimOlayi {
    private String tur; // "Sinav" veya "Tatil"

    // 2. DEĞİŞİKLİK: 'String tarih' yerine 'LocalDate tarih' yaptık
    public OzelGun(int id, String baslik, LocalDate tarih, String tur) {

        // 3. DEĞİŞİKLİK: 'id' parametresini sildik.
        // Çünkü TakvimOlayi (Baba sınıf) sadece (baslik, tarih) istiyor.
        super(baslik, tarih);

        this.tur = tur;
    }

    @Override
    public String getRenkKodu() {
        // equalsIgnoreCase: Büyük-küçük harf hatasını önler ("sinav" yazsan da kabul eder)
        if (tur.equalsIgnoreCase("Sinav")) {
            return "#FF0000"; // Kırmızı
        }
        return "#808080"; // Gri
    }

    // Getter eklemek istersen (İsteğe bağlı)
    public String getTur() { return tur; }
}