package com.proje.ui;

import com.proje.database.DBHelper;

public class Launcher {
    public static void main(String[] args) {
        DBHelper.tablolariKontrolEt();
        // Asıl uygulamayı buradan çağırıyoruz
        App.main(args);
    }
}