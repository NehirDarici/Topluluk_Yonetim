package com.proje.model;

// KALITIM: BirimBaskani extends BirimUyesi AMA ekstra şeyleri yapar

/*
Bu sınıfta birim üyesindeki bilgileri tekrar yazmıyoruz
onları miras alıyoruz
 */
public class BirimBaskani extends BirimUyesi {


    public BirimBaskani(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        //super üst sınıf, gelen bilgileri alır birim üyesine gönderir
        //çünkü ad, soyad vs orada tanımlandı
        super(id, ogrenciNo, adSoyad, sifre, rol, birimId);
    }

    //Bu metot sadece BirimBakanı sınıfında var, Normal birim üyesi bunu yapamaz

    public void gorevAta(String gorevAdi) {
        System.out.println("BAŞKAN: " + super.getAdSoyad() + " görev atadı: " + gorevAdi);
    }
}