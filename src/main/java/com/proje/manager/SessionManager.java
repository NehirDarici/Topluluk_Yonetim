package com.proje.manager;

import com.proje.model.BirimUyesi;

// Sınıfın işlevi, her controller o anki giriş yapan kullanıcıyı görmesini sağlamaktır.
public class SessionManager {
    // Singleton Örneği
    private static SessionManager instance;

    // O an giriş yapmış olan kullanıcıyı tutacak değişken
    private BirimUyesi currentUser;

    // Constructor private yapılır ki dışarıdan 'new' ile üretilemesin.
    private SessionManager() {
    }

    // Projenin her yerinde aynı nesnenin kullanılmasını sağlar.
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Kullanıcı giriş yapınca bu metot çağrılır.
    public void login(BirimUyesi user) {
        this.currentUser = user;
        System.out.println("OTURUM AÇILDI: " + user.getAdSoyad() + " (" + user.getRol() + ")");
    }

    // Çıkış yapınca bu çağrılır.
    public void logout() {
        System.out.println("OTURUM KAPATILDI: " + (currentUser != null ? currentUser.getAdSoyad() : "Bilinmiyor"));
        this.currentUser = null;
    }

    // Şu anki kullanıcıyı döndürür.
    public BirimUyesi getCurrentUser() {
        return currentUser;
    }

    // Kullanıcının içeride olup olmadığını kontrol eder.
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
