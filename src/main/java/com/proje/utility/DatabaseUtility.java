package com.proje.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {

    // Veritabanı adresi sadece burada yazacak. Değişirse tek yerden değişirsin.
    private static final String DB_URL = "jdbc:sqlite:topluluk_yonetim.db";

    // Static Metot: Nesne oluşturmadan (new demeden) çağrılabilir.
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}