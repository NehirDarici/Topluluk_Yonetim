package com.proje.service;

import java.util.List;

//VERİTABANINA GİTMEDEN kod içinde bazı işlemleri yapar ve tutar
public class BirimServisi {
    //2D array mantığı exceldeki tablo gibi çalışır
    // [BirimID][Oda Numarası] eşleşmesi
    private int[][] birimLokasyonlari = {

            {1, 101}, // Yönetim Birimi (ID:1) -> Oda 101
            {2, 201}, // Etkinlik Birimi (ID:2) -> Oda 201
            {3, 202}  // Sosyal Medya Birimi (ID:3) -> Oda 202
    };

    // List<?> String,Uye, Integer da gelse bu metot çalışır
    //sadece sayım yapacağından içeriğinin türü önemli değildir
    public void elemanSayisiniRaporla(List<?> herhangiBirListe) {
        //gelen listeyi size metodunu çağırıp ekrana yazar
        System.out.println("LOG: Listede toplam " + herhangiBirListe.size() + " kayıt bulundu.");
    }

    /**
     BİRİM İD VER, NEREDE OLDUĞUNU SÖYLEYEYİM DER
     */
    public int odaBul(int birimId) {
        //Tablonun satırlarını teker teker geziyoruz

        for (int i = 0; i < birimLokasyonlari.length; i++) {
            /**eğer aradığımız ID satırda varsa oda numarası ile temsil ettiğimiz
             * sayıyu döndürür
             */
            if (birimLokasyonlari[i][0] == birimId) {
                return birimLokasyonlari[i][1];
            }
        }

        return 0; // Bulunamazsa
    }
}