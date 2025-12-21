package Garantiler;

import java.time.LocalDate;


public class StandartGaranti extends Garanti {


    //Standart Garanti kurucu metodu.
    public StandartGaranti(LocalDate baslangicTarihi, int sureYil) {
        // Garanti ata sınıfından super çağrılır
        super(baslangicTarihi, sureYil, 0);
    }


    @Override
    //Servis maliyetini hesaplar.
    public double sonMaliyetHesapla(double hamUcret) {
        if (devamEdiyorMu()) {
            return 0.0; // Garanti kapsamındaysa ücret alınmaz
        } else {
            return hamUcret; // Süre bittiyse tam ücret ödenir
        }
    }


    @Override
    public String garantiTuru() {
        return "Standart Garanti";
    }


    @Override
    //Garantinin bitmesine kalan günü stringe dönüştürür
    public String toString() {
        long kalan = getKalanGunSayisi();
        return garantiTuru() + (kalan > 0 ? " (" + kalan + " gün kaldı)" : " (Süre Doldu)");
    }
}