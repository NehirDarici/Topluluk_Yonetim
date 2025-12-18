package com.proje.interfaces;

// PDF Madde 4.3: Interface Kullanımı
// PDF Madde 5.1: Generic Kullanımı (<T>)
public interface IAramaKriteri<T> {
    // Bu metot, bir nesnenin aranan kelimeye uyup uymadığını kontrol edecek
    boolean uygunMu(T veri, String arananKelime);
}