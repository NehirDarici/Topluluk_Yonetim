package com.proje.exception;

// PDF Madde 1.1: Özel Exception Sınıfı
public class GecersizGorevException extends Exception {
    public GecersizGorevException(String mesaj) {
        super(mesaj);
    }
}