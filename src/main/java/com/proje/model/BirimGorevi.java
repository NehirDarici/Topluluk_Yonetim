package com.proje.model;

public class BirimGorevi extends TakvimOlayi {
    private int birimId;

    public BirimGorevi(int id, String baslik, String tarih, int birimId) {
        super(id, baslik, tarih);
        this.birimId = birimId;
    }

    @Override
    public String getRenkKodu() {
        return "#800080"; // Mor Renk
    }
}
