package Istisnalar;

//Kayıtta olmayan değer girildiğinde bu exception sınıf çalışır
public class KayitBulunamadiException extends Exception {
    public KayitBulunamadiException(String mesaj) {
        super(mesaj);
    }
}