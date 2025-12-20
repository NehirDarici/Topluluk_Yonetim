package com.proje.model;

//kullanıcıya izin verdiğimiz seçenekler sunulur
//ekstra seçeneklerin gelir- gider olduğunu da ayrıştırır

//enum: sabit seçenekler listesi
public enum IslemTuru {

    // her seçenek sınıftan oluşturulmuş ,değişmez nesnedir
    //parantez içindeki yapıcıya gider

    //kasa artar gelir
    SPONSORLUK("Sponsorluk", true),
    AIDAT("Üye Aidatı", true),
    ETKINLIK_GELIRI("Etkinlik Bileti", true),

    //kasa azalır gider
    YEMEK("Yemek/İkram", false),
    ULASIM("Ulaşım", false),
    EKIPMAN("Ekipman/Malzeme", false),
    KIRTASIYE("Kırtasiye", false),
    DIGER("Diğer Giderler", false);


    //ekranda aidat yerineüye aidatı yazsın şeklinde

    private final String etiket;

    //hesap yaparken toplama mı çıkarma mı?
    private final boolean gelirMi;

    IslemTuru(String etiket, boolean gelirMi) {
        this.etiket = etiket;
        this.gelirMi = gelirMi;
    }
    // gelir?gider --> bakiye güncellemesi
    public boolean isGelirMi() {
        return gelirMi;
    }

    //to string
    //açılır liste comboBoxta ekranda ne yazsın
    //bunu yazmazsak ETKİNLIK_GELIRI VS YAZAR
    //bunu yazdığımız için Etkinlik Bileti yazar

    @Override
    public String toString() {
        return etiket;
    }
}