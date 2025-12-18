package com.proje.model;

import java.time.LocalDate;

// PDF Gereksinimi: Abstract Class
public abstract class TakvimOlayi {

    // --- YENİ EKLENEN KISIM: ID ALANI ---
    protected int id;

    protected String baslik;
    protected LocalDate tarih;

    public TakvimOlayi(String baslik, LocalDate tarih) {
        this.baslik = baslik;
        this.tarih = tarih;
    }

    // --- YENİ EKLENEN METOTLAR (ID için) ---
    // GorevDAO'nun silme işlemi yapabilmesi için bu metotlar ŞART!
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