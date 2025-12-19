package com.proje.model;

import java.time.LocalDate;

// Takvime başlık ve tarih girilmesini sağlayan abstract sınıf
public abstract class TakvimOlayi {
    protected int id;
    protected String baslik;
    protected LocalDate tarih;

    // Başlık ve tarihler için constructor
    public TakvimOlayi(String baslik, LocalDate tarih) {
        this.baslik = baslik;
        this.tarih = tarih;
    }

    // GorevDAO için oluşturulan metodlar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Mevcut Getter Metotları
    public LocalDate getTarih() { return tarih; }
    public String getBaslik() { return baslik; }

    // Abstract Metot
    public abstract String getRenkKodu();
}