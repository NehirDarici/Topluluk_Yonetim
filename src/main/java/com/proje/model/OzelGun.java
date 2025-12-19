package com.proje.model;

import java.time.LocalDate;

// Takvime özel günlerin yazılmasını sağlar.
public class OzelGun extends TakvimOlayi {
    private String tur;


    public OzelGun(int id, String baslik, LocalDate tarih, String tur) {
        // TakvimOlayi ile kalıtım yapılır.
        super(baslik, tarih);
        this.tur = tur;
    }

    // Sınavlar için özel renk kodunun oluşturulması
    @Override
    public String getRenkKodu() {
        if (tur.equalsIgnoreCase("Sinav")) {
            return "#FF0000";
        }
        return "#808080";
    }

    // Getter
    public String getTur() { return tur; }
}