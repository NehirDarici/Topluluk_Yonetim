package com.proje.model;

public class Gorev {
    private int id;
    private String baslik;
    private String aciklama;
    private String tarih;
    private String durum; // "BEKLIYOR", "TAMAMLANDI"
    private String renkKodu; // Arayüzde renklendirme için
    private int atananBirimId;

    public Gorev() {}

    public Gorev(String baslik, String aciklama, String tarih, String renkKodu, int atananBirimId) {
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tarih = tarih;
        this.renkKodu = renkKodu;
        this.atananBirimId = atananBirimId;
        this.durum = "BEKLIYOR"; // Varsayılan değer
    }

    // Getter ve Setterlar
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public String getTarih() { return tarih; }
    public void setTarih(String tarih) { this.tarih = tarih; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public String getRenkKodu() { return renkKodu; }
    public void setRenkKodu(String renkKodu) { this.renkKodu = renkKodu; }

    public int getAtananBirimId() { return atananBirimId; }
    public void setAtananBirimId(int atananBirimId) { this.atananBirimId = atananBirimId; }
}
