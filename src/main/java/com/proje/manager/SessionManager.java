package com.proje.manager;

import com.proje.model.BirimUyesi;

public class SessionManager {
    // 1. Singleton Örneği (Uygulama boyunca tek bir tane olacak)
    private static SessionManager instance;

    // 2. O an giriş yapmış olan kullanıcıyı tutacak değişken
    private BirimUyesi currentUser;

    // Constructor private yapılır ki dışarıdan 'new' ile üretilemesin
    private SessionManager() {
    }

    // 3. Tekil örneğe ulaşmak için kullanılan metot
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // 4. Kullanıcı giriş yapınca bu metot çağrılır
    public void login(BirimUyesi user) {
        this.currentUser = user;
        System.out.println("OTURUM AÇILDI: " + user.getAdSoyad() + " (" + user.getRol() + ")");
    }

    // 5. Çıkış yapınca bu çağrılır
    public void logout() {
        System.out.println("OTURUM KAPATILDI: " + (currentUser != null ? currentUser.getAdSoyad() : "Bilinmiyor"));
        this.currentUser = null;
    }

    // 6. Şu anki kullanıcıyı döndürür (Diğer sınıflar buradan erişir)
    public BirimUyesi getCurrentUser() {
        return currentUser;
    }

    // Kullanıcının içeride olup olmadığını kontrol eder
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
