package com.proje.model;

import java.util.Objects;

// Üye bilgilerinin alındığı sınıf
public class Uye {
    private String ad;
    private String soyad;
    private String ogrenciNo;
    private String rol;

    // Üye bilgilerinin olduğu constructor
    public Uye(String ad, String soyad, String ogrenciNo, String rol) {
        this.ad = ad;
        this.soyad = soyad;
        this.ogrenciNo = ogrenciNo;
        this.rol = rol;
    }

    // Getter Metodları
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getOgrenciNo() { return ogrenciNo; }
    public String getRol() { return rol; }

    // Setter Metodları
    public void setAd(String ad) { this.ad = ad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    // Nesneyi yazdırdığında anlamlı çıktı almak için
    @Override
    public String toString() {
        return "Üye: " + ad + " " + soyad + " (" + rol + ") - No: " + ogrenciNo;
    }

    // ArrayList içinde 'contains' metodunun doğru çalışması için gereklidir.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uye uye = (Uye) o;
        return Objects.equals(ogrenciNo, uye.ogrenciNo);
    }

    // ogrenciNo bilgisini sonradan kullanmak üzere bir sayıyla eşleştirir.
    @Override
    public int hashCode() {
        return Objects.hash(ogrenciNo);
    }
}