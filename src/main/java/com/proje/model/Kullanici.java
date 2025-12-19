package com.proje.model;

// ABSTRACT Class: Bu sınıftan doğrudan nesne üretilemez, miras alınması gerekir.
public abstract class Kullanici {
    private String ogrenciNo;
    private String adSoyad;
    private String sifre;
    private String rol;

    // Boş Constructor
    public Kullanici() {
    }

    // Dolu Constructor (Overloading örneği)
    public Kullanici(String ogrenciNo, String adSoyad, String sifre, String rol) {
        this.ogrenciNo=ogrenciNo;
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.rol = rol;
    }

    // Ortak Getter ve Setterlar
    public String getOgrenciNo() { return ogrenciNo; }
    public void setOgrenciNo(String ogrenciNo ) { this.ogrenciNo = ogrenciNo; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

}