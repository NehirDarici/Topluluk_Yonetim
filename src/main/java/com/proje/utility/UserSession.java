package com.proje.utility;

import com.proje.model.Uye;

public class UserSession {
    private static UserSession instance;
    private Uye currentUser;

    private UserSession(Uye uye) {
        this.currentUser = uye;
    }

    public static void setInstance(Uye uye) {
        instance = new UserSession(uye);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public Uye getCurrentUser() {
        return currentUser;
    }

    public static void cleanUserSession() {
        instance = null;
        // Çıkış yapıldığında burası çağrılır
    }
}