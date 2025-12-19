package com.proje.model;

public class ButceKaydi {
    // MADDE 10: Statik Alan (Sınıfa özgü, nesneden bağımsız sayaç)
    public static int toplamKayitSayisi = 0;

    private int id;
    private String aciklama;
    private double miktar;
    private String tarih;
    private IslemTuru tur;

    // 1. Constructor (Veritabanından çekerken)
    public ButceKaydi(int id, String aciklama, double miktar, String tarih, IslemTuru tur) {
        this.id = id;
        this.aciklama = aciklama;
        this.miktar = miktar;
        this.tarih = tarih;
        this.tur = tur;
        toplamKayitSayisi++; // Her nesne oluştuğunda sayaç artar
    }

    // 2. Constructor Overloading (MADDE 4.4 - Polymorphism başlangıcı)
    // Yeni kayıt eklerken ID'ye ihtiyaç duymadan oluşturmak için
    public ButceKaydi(String aciklama, double miktar, IslemTuru tur) {
        this.aciklama = aciklama;
        setMiktar(miktar); // Validasyonlu setter kullanımı
        this.tur = tur;
        // Tarih verilmezse bugünü atayabiliriz vs.
    }

    public int getId() { return id; }
    public String getAciklama() { return aciklama; }
    public double getMiktar() { return miktar; }
    public String getTarih() { return tarih; }
    public IslemTuru getTur() { return tur; }

    // MADDE 3: Setter içinde Validasyon ve Exception
    public void setMiktar(double miktar) {
        if (miktar < 0) {
            throw new IllegalArgumentException("Miktar negatif olamaz!");
        }
        this.miktar = miktar;
    }
}