package com.proje.service;

import java.util.List;

/**
 * PDF Madde 1.1: Yardımcı Hizmet Sınıfı
 */
public class BirimServisi {

    // PDF Madde 9: En az 1 çok boyutlu dizi (2D Array) kullanılmalıdır.
    // [BirimID][Oda Numarası] eşleşmesi
    private int[][] birimLokasyonlari = {
            {1, 101}, // Yönetim Birimi (ID:1) -> Oda 101
            {2, 201}, // Etkinlik Birimi (ID:2) -> Oda 201
            {3, 202}  // Sosyal Medya Birimi (ID:3) -> Oda 202
    };

    /**
     * PDF Madde 5.1: Wildcard (List<?>) kullanımı.
     * Bu metot, ne tür bir liste gelirse gelsin eleman sayısını raporlar.
     */
    public void elemanSayisiniRaporla(List<?> herhangiBirListe) {
        System.out.println("LOG: Listede toplam " + herhangiBirListe.size() + " kayıt bulundu.");
    }

    /**
     * PDF Madde 9: Çok boyutlu diziden veri okuma.
     */
    public int odaBul(int birimId) {
        for (int i = 0; i < birimLokasyonlari.length; i++) {
            if (birimLokasyonlari[i][0] == birimId) {
                return birimLokasyonlari[i][1];
            }
        }
        return 0; // Bulunamazsa
    }
}