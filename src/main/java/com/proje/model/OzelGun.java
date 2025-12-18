package com.proje.model;

public class OzelGun extends TakvimOlayi {
    private String tur; // "Sinav" veya "Tatil"

    public OzelGun(int id, String baslik, String tarih, String tur) {
        super(id, baslik, tarih);
        this.tur = tur;
    }

    @Override
    public String getRenkKodu() {
        if(tur.equals("Sinav")) return "#FF0000"; // Kırmızı
        return "#808080"; // Gri
    }
}