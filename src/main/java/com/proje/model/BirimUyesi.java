package com.proje.model;

public class BirimUyesi {
    //'private' yapsaydık, bu sınıftan türeyen 'BirimBaskani' bu verilere doğrudan erişemezdi.

    // Veritabanı sütunlarına karşılık gelen değişkenler
    // PROTECTED sayesinde miras alınanlar için rahatlıkla kullanırlar
    protected int id;   //veritabanında satır numarası
    protected String ogrenciNo;
    protected String adSoyad;
    protected String sifre;
    protected String rol;
    protected int birimId; // Bu alan yönlendirmeyi sağlar

    //userDAO sınıfı veritabanından veriyi çektiğinde bu metodu kullanır.
    //veri tabanından gelen bilgileri alır, tek bir BirimUyesi paketi haline getirir

    public BirimUyesi(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        this.id = id;
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.rol = rol;
        this.birimId = birimId;
    }

    /* Dışarıdaki sınıflar (controller kısımları) bu üyenin bilgilerini
    öğrenmek istediğinde bunları kullanır
     */

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
        return rol; // yetki kontrolü sürecinde lazım( admin paneline girebilme durumu)
    }

    // LoginController'da 'if (user.getBirimId() == 2)' kontrolü için gerekli:
    public int getBirimId() {
        return birimId;
    }

    // Üye profilini düzenlemek isterse bu metotlar kullanılır.
    // ID ve OgrenciNo için Setter yok, çünkü onlar genelde değişmez.

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }


    //DeBug için kullanılan kısımm, hata çıkarsa doğru bir üyeyi çekip çekilmediğini anlamak için kullanılrı

    @Override
    public String toString() {
        return "BirimUyesi{" +
                "ad='" + adSoyad + '\'' +
                ", rol='" + rol + '\'' +
                ", birim=" + birimId +
                '}';
    }
}