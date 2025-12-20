package com.proje.utility;
// kalabalık topluluklarda kişileri aramak için kullanılır
// farklı yerlerde kullanırsa AramaMotoru<X> diyerek kullanacağız

import com.proje.interfaces.IAramaKriteri;
import java.util.ArrayList;
import java.util.List;

/*<T> KISMI  generic sınıftır, her türlü nesneyle çalışır
 */

public class AramaMotoru<T> {


    // . analiste: içinde arama yapılacak tümm liste
    //aranan kullanıcının yazdığı metin
    //arama kuralı neye göre aramalı?

    public List<T> ara(List<T> anaListe, String aranan, IAramaKriteri<T> kriter) {

        //kullanıcı bir şey yazmadıysa
        if (aranan == null || aranan.trim().isEmpty()) {
            return new ArrayList<>(anaListe); // Arama boşsa hepsini döndür
        }
        // AhmET= ahmet yazsa da bul
        //aranan kelimeyi küçük harflere çevirip, boşlukları trim ile siliyoruz
        String arananKucuk = aranan.toLowerCase().trim();
        List<T> sonucListesi = new ArrayList<>(); // PDF Madde 5.2: ArrayList kullanımı

        //Ana listedeki her elemanı (T) ile teker teker geziyoruz
        for (T eleman : anaListe) {
            //ad, soyad ? neye bakılacağı kriter nesnesine sorulur
            //kriter.uygunMu metodu eşleştirme yapar ve true olursa
            if (kriter.uygunMu(eleman, arananKucuk)) {

                sonucListesi.add(eleman);
            }
        }
        //içinde sadece eşleşenlerin oludğu listeyi geri döndürür
        return sonucListesi;
    }
}