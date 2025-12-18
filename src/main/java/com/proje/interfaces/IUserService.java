package com.proje.interfaces;
import com.proje.model.BirimUyesi;

public interface IUserService {
    // Kullanıcı işlemleri için standart sözleşme
    BirimUyesi loginUser(String ogrenciNo, String sifre);
    boolean addUser(BirimUyesi uye);
}
