package com.proje.model;

public class BirimUyesi {
    protected int id;
    protected String ogrenciNo;
    protected String adSoyad;
    protected String sifre;
    protected String rol;
    protected int birimId;

    // 6 Parametreli Constructor (UserDAO bunu kullanır)
    public BirimUyesi(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        this.id = id;
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.rol = rol;
        this.birimId = birimId;
    }

    // Getter Metotları (Private alanlara erişim için)
    public int getId() { return id; }
    public String getOgrenciNo() { return ogrenciNo; }
    public String getAdSoyad() { return adSoyad; }
    public String getSifre() { return sifre; }
    public String getRol() { return rol; }
    public int getBirimId() { return birimId; }

    public void takvimiGoruntule() {
        System.out.println(adSoyad + " takvimi görüntülüyor.");
    }
}