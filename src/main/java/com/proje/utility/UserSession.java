package com.proje.utility;

import com.proje.model.Uye;

/*kimlikakrtı sistemi gibi, kullanıcı giriş yaptığında
onun bilgilerini verir.
 */


public class UserSession {

    //static; nesneye özel değil, sınıfa özel olduğunu belirtir, tek ve ortak
    private static UserSession instance;
    //giriş yapmış kullanıcının tğm bilgilerini tutar
    private Uye currentUser;

    //private ile new UserSession şeklinde yeni bir oturum açma engellendi
    private UserSession(Uye uye) {
        this.currentUser = uye;
    }
    //kullanıcı adı ve şifreesi doğruysa, LoginController bu metodu çağırır

    public static void setInstance(Uye uye) {
        //tek ve ortak oturum nesnesi oluşur
        instance = new UserSession(uye);
    }

    //şu an kim giriş yapmış bilgisini alır
    public static UserSession getInstance() {
        return instance;
    }
    //giriş yapmış kulanıcının kendisini verir, u.getAd vs kullanılabilir
    public Uye getCurrentUser() {
        return currentUser;
    }
    // Oturum kapatma kısmı
    // 'btnCikisYap' butonuna basıldığında bu çağrılır.
    // 'instance = null' diyerek hafızadaki o kimlik kartını siler.
    public static void cleanUserSession() {
        instance = null;
        // Çıkış yapıldığında burası çağrılır
    }
}