package com.proje.model;

import java.time.LocalDate;

// Kalıtım: BirimGorevi "bir" TakvimOlayi'dır.
public class BirimGorevi extends TakvimOlayi {
    private int birimId; // 2: Etkinlik, 3: Sosyal Medya

    public BirimGorevi(String baslik, LocalDate tarih, int birimId) {
        super(baslik, tarih);
        this.birimId = birimId;
    }

    // --- YENİ EKLENEN METOT (GETTER) ---
    // Başkan seçim yaparken veya biz görevi kontrol ederken
    // bu görevin hangi birime ait olduğunu öğrenmemiz gerekecek.
    public int getBirimId() {
        return birimId;
    }

    // POLYMORPHISM: Rengi birime göre otomatik seçiyoruz
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