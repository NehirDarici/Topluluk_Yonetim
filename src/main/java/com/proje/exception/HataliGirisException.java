package com.proje.exception;

public class HataliGirisException extends Exception {
    public HataliGirisException(String mesaj) {
        super(mesaj);
    }
}

// Login ve veri doğrulama kısmındaki hatalar için kullanılmaktadır.