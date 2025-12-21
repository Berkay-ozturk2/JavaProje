package Istisnalar;

//Geçersiz değer girildiğinde bu exception sınıf çalışır
public class GecersizDegerException extends Exception {
    public GecersizDegerException(String mesaj) {
        super(mesaj);
    }
}