package com.proje.model;

import java.time.LocalDate;

//BİRİM GÖREVİ takvim olayo ailesinin üyesidir
//takvimOlayi sınıfındaki baslık ve tarih ozellikleri ona da miras alır


public class BirimGorevi extends TakvimOlayi {
    // TakvimOlayi'nda sadece başlık ve tarih vardı.
    // Ama BirimGorevi'nde ek olarak "Hangi birime ait?" bilgisi (ID) lazım.

    private int birimId; // 2: Etkinlik, 3: Sosyal Medya


    public BirimGorevi(String baslik, LocalDate tarih, int birimId) {
        //super ile miras aldığımız sınıfa ulaşıyoruz
        //başlığı ve tarihi sen kaydet diyoruz

        super(baslik, tarih);
        //birim ıdsini biz kaydediyoruz
        this.birimId = birimId;
    }

    // Başkan seçim yaparken veya biz görevi kontrol ederken
    // bu görevin hangi birime ait olduğunu öğrenmemiz gerekecek.
    public int getBirimId() {

        return birimId;
    }

    // POLYMORPHISM( çok biçimlilik) :  Rengi birime göre otomatik seçiyoruz
    @Override
    public String getRenkKodu() {
        if (birimId == 2) {
            return "#4169E1"; // MAVİ (Etkinlik Birimi)
        } else if (birimId == 3) {
            return "#800080"; // MOR (Sosyal Medya Birimi)
        } else {
            return "#2E8B57"; // YEŞİL (Sponsorluk veya Diğer)
        }
    }
}