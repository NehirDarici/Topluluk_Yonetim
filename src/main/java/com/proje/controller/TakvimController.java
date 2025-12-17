package com.proje.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TakvimController {

    @FXML
    private Button btnDuzenle; // SceneBuilder'da butona verdiğin fx:id

    // Bu metodun amacı: Yetki varsa butonu göster, yoksa gizle
    public void yetkiAyarla(boolean tamYetki) {
        if (btnDuzenle != null) {
            btnDuzenle.setVisible(tamYetki);
            btnDuzenle.setManaged(tamYetki); // Gizlenince yer kaplamasın
        }
    }
}