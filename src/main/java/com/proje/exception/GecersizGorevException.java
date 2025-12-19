package com.proje.exception;


public class GecersizGorevException extends Exception {
    public GecersizGorevException(String mesaj) {
        super(mesaj);
    }
}


// Görevlerle ilgili çıkan teknik olmayan hataları yakalamak için kullanılır.