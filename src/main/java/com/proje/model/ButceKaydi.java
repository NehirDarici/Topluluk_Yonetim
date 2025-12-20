package com.proje.model;

public class ButceKaydi {
    //ayrı id olur ama toplamKayıt hepsi için ortak ve tek yerdedir
    //amacı kaç tane bütçe nesnesi oluşturduğumuzu saymak
    public static int toplamKayitSayisi = 0;


    //fişlerde de olabilecek özellikler
    private int id;
    private String aciklama;
    private double miktar;
    private String tarih;
    private IslemTuru tur;   //Enum( gelir-gider)

    // Constructor--> Veritabanından çekerken
    //veritabanından eski bir kaydı çekerken bunu kullanırız
    public ButceKaydi(int id, String aciklama, double miktar, String tarih, IslemTuru tur) {
        this.id = id;
        this.aciklama = aciklama;
        this.miktar = miktar;
        this.tarih = tarih;
        this.tur = tur;

        //her yeni nesne oluşturulduğunda ortak sayac artar
        toplamKayitSayisi++; // Her nesne oluştuğunda sayaç artar
    }


    //overloading: aynı isim farklı parametre alan metotlar yazmak demektir

    // Yeni kayıt eklerken ID'ye ihtiyaç duymadan oluşturmak için kullanılrı

    public ButceKaydi(String aciklama, double miktar, IslemTuru tur) {
        this.aciklama = aciklama;

        //mmiktarı doğrudan eşitlemek yerine set metodunu çağırdık
        setMiktar(miktar);
        this.tur = tur;
        // Tarih verilmezse bugünü atayabiliriz vs.
    }


    //GETTER METOTLARI - OKUMA İŞLEMLERİ

    public int getId() { return id; }
    public String getAciklama() { return aciklama; }
    public double getMiktar() { return miktar; }
    public String getTarih() { return tarih; }
    public IslemTuru getTur() { return tur; }

    // MADDE 3: Setter içinde Validasyon(KONTROL) ve Exception(HATA FIRLATMA)

    public void setMiktar(double miktar) {
        //kontol : para eksi olamaz
        if (miktar < 0) {
            //para eksi gelirse hata verir
            throw new IllegalArgumentException("Miktar negatif olamaz!");
        }
        this.miktar = miktar;
    }
}