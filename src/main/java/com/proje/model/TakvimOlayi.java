package com.proje.model;

// PDF GEREKSİNİMİ: Abstract Sınıf
public abstract class TakvimOlayi {
    protected int id;
    protected String baslik;
    protected String tarih;

    public TakvimOlayi(int id, String baslik, String tarih) {
        this.id = id;
        this.baslik = baslik;
        this.tarih = tarih;
    }

    // Abstract Metot: Her alt sınıf rengini kendi belirlemek ZORUNDA
    public abstract String getRenkKodu();

    public String getBaslik() { return baslik; }
    public String getTarih() { return tarih; }
}