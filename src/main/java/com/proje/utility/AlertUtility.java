package com.proje.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtility {

    // Hata Mesajı Gösterir (Kırmızı İkonlu)
    public static void showError(String baslik, String mesaj) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    // Bilgi Mesajı Gösterir (Mavi İkonlu)
    public static void showInfo(String baslik, String mesaj) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    // Uyarı Mesajı Gösterir (Sarı İkonlu)
    public static void showWarning(String baslik, String mesaj) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
