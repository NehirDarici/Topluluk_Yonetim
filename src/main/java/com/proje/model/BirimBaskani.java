package com.proje.model;

// KALITIM: BirimBaskani extends BirimUyesi
public class BirimBaskani extends BirimUyesi {

    public BirimBaskani(int id, String ogrenciNo, String adSoyad, String sifre, String rol, int birimId) {
        super(id, ogrenciNo, adSoyad, sifre, rol, birimId);
    }

    // Başkana özel yetenek
    public void gorevAta(String gorevAdi) {
        System.out.println("BAŞKAN: " + super.getAdSoyad() + " görev atadı: " + gorevAdi);
    }
}