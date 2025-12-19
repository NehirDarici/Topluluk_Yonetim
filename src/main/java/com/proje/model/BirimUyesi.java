package com.proje.model;

public class BirimUyesi {

    // Veritabanı sütunlarına karşılık gelen değişkenler
    protected int id;
    protected String ogrenciNo;
    protected String adSoyad;
    protected String sifre;
    protected String rol;
    protected int birimId; // Bu alan yönlendirme (LoginController) için kritiktir

    // --- 6 PARAMETRELİ CONSTRUCTOR (UserDAO Hatasını Çözen Kısım) ---
    // UserDAO sınıfında 'new BirimUyesi(...)' dediğinde burası çalışır.
    public BirimUyesi(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        this.id = id;
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.rol = rol;
        this.birimId = birimId;
    }

    // --- Getter Metotları (Verilere erişmek için) ---
    public int getId() {
        return id;
    }

    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getSifre() {
        return sifre;
    }

    public String getRol() {
        return rol;
    }

    // LoginController'da 'if (user.getBirimId() == 2)' kontrolü için gerekli:
    public int getBirimId() {
        return birimId;
    }

    // Opsiyonel: Setterlar (İleride güncelleme yapmak istersen diye)
    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    // Test amaçlı yazdırma metodu
    @Override
    public String toString() {
        return "BirimUyesi{" +
                "ad='" + adSoyad + '\'' +
                ", rol='" + rol + '\'' +
                ", birim=" + birimId +
                '}';
    }
}