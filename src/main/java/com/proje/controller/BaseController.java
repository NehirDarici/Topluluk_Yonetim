package com.proje.controller;

import com.proje.interfaces.INavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

// Tüm Controller'lar buradan türeyecek
public abstract class BaseController implements INavigator {
    protected static BorderPane mainLayout;

    public static void setMainLayout(BorderPane layout) {
        mainLayout = layout;
    }

    @Override
    public void sayfaDegistir(String dosyaAdi) {
        try {
            if (mainLayout != null) {
                Pane view = FXMLLoader.load(getClass().getResource("/" + dosyaAdi));
                mainLayout.setCenter(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}