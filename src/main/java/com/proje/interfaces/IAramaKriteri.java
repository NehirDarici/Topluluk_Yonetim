package com.proje.interfaces;

//isme göre arama metodu
//soyisme göre arama metodu
//numaraya göre arama metodu

//<T> Üye, Etkinlik vs... kullanım alanının arttırır

public interface IAramaKriteri<T> {
    //ınterface soru sorar , nasıl olacağınını interfaceyi kullanan sınıflar yazar

    boolean uygunMu(T veri, String arananKelime);
}