package com.proje.model; // Paket ismine dikkat et

import java.util.Objects;

public class Uye {
    private String ad;
    private String soyad;
    private String tcNo;
    private String rol; // Örn: Başkan, Üye, Sekreter

    // Constructor
    public Uye(String ad, String soyad, String tcNo, String rol) {
        this.ad = ad;
        this.soyad = soyad;
        this.tcNo = tcNo;
        this.rol = rol;
    }

    // --- Getter Metodları (Hataları Çözen Kısım) ---
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getTcNo() { return tcNo; }
    public String getRol() { return rol; }

    // --- Setter Metodları ---
    public void setAd(String ad) { this.ad = ad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    // TC No genellikle değiştirilmez ama gerekirse eklenebilir.

    // toString: Nesneyi yazdırdığında anlamlı çıktı almak için
    @Override
    public String toString() {
        return "Üye: " + ad + " " + soyad + " (" + rol + ") - TC: " + tcNo;
    }

    // ÖNEMLİ: ArrayList içinde 'contains' metodunun doğru çalışması için gereklidir.
    // İki üyenin TC numarası aynıysa, bu kişiler aynıdır mantığı:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uye uye = (Uye) o;
        return Objects.equals(tcNo, uye.tcNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcNo);
    }
}