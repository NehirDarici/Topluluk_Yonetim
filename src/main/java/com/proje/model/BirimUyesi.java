package com.proje.model;

public class BirimUyesi extends Kullanici {
    private int id;        // Veritabanındaki 'kullanici_id' (Primary Key)
    private int birimId;   // Veritabanındaki 'birim_id' (Foreign Key)

    // 1. Boş Constructor (Veritabanından veri çekerken lazım)
    public BirimUyesi() {
        super();
    }

    // 2. Dolu Constructor (Veritabanından okurken ID dahil her şeyi set ederiz)
    public BirimUyesi(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        // Ata sınıfın (Kullanici) yapıcısını çağırıyoruz. Sıralama ÖNEMLİ!
        super(ogrenciNo, adSoyad, sifre, rol);
        this.id = id;
        this.birimId = birimId;
    }

    // 3. Kayıt Constructor'ı (Yeni kayıt yaparken ID henüz yoktur)
    public BirimUyesi(String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        super(ogrenciNo, adSoyad, sifre, rol);
        this.birimId = birimId;
    }

    // Getter ve Setter Metotları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBirimId() { return birimId; }
    public void setBirimId(int birimId) { this.birimId = birimId; }
}