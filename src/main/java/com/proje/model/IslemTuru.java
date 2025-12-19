package com.proje.model;

public enum IslemTuru {
    // Tür Adı("Ekranda Görünecek İsim", Gelir mi?)
    SPONSORLUK("Sponsorluk", true),
    AIDAT("Üye Aidatı", true),
    ETKINLIK_GELIRI("Etkinlik Bileti", true),

    YEMEK("Yemek/İkram", false),
    ULASIM("Ulaşım", false),
    EKIPMAN("Ekipman/Malzeme", false),
    KIRTASIYE("Kırtasiye", false),
    DIGER("Diğer Giderler", false);

    private final String etiket;
    private final boolean gelirMi;

    IslemTuru(String etiket, boolean gelirMi) {
        this.etiket = etiket;
        this.gelirMi = gelirMi;
    }

    public boolean isGelirMi() {
        return gelirMi;
    }

    @Override
    public String toString() {
        return etiket;
    }
}