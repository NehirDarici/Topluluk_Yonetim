package com.proje.service;
import java.util.ArrayList;
import java.util.List;

// İSTER 1: Generic Sınıf Tanımlama (Depo<T>)
public class Depo<T> {
    private List<T> veriListesi;

    public Depo() {
        this.veriListesi = new ArrayList<>();
    }

    // İSTER 2 (a): Generic Metot 1 (Ekleme)
    public void ekle(T veri) {
        veriListesi.add(veri);
        System.out.println("Veri depoya eklendi.");
    }

    // İSTER 2 (b): Generic Metot 2 (Getirme)
    public T getir(int index) {
        if (index >= 0 && index < veriListesi.size()) {
            return veriListesi.get(index);
        }
        return null;
    }

    public List<T> getList() {
        return veriListesi;
    }

    // İSTER 3: Wildcard Kullanımı (List<?> veya ? extends ...)
    // Bu metot, ne tür bir liste gelirse gelsin sadece eleman sayısını basar.
    public static void elemanSayisiniYazdir(List<?> herhangiBirListe) {
        System.out.println("Listeki eleman sayısı: " + herhangiBirListe.size());
    }
}
