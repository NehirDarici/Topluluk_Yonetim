package com.proje.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {
       //UYGULAMA İLE VERİTABANI dosyası arasında köprüdür
    //static final; sabittir program çalışırken değişmez
    //String DB_URL : veritabanının açık adresidir
    // "jdbc:sqlite:..." kısmı Java'ya "Ben SQLite kullanacağım" der.

    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";

    //BAĞLANTIYI KURAN METOT

    public static Connection connect() throws SQLException {
         //DriveManger: DB URL veriyoruz, o da götürüyor.
         // Connection nesnesi geriye döndürüyor
        return DriverManager.getConnection(DB_URL);
    }
}

//Amaç her yere veri tabanı bağlama işlemini yapmamış olduk