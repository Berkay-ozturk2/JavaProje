package Garantiler;

import java.time.LocalDate;


public class UzatilmisGaranti extends Garanti {


      //Uzatılmış Garanti kurucu metodu.
      //Standart süreye ek olarak belirlenen ekstra ay süresiyle başlatılır.
    public UzatilmisGaranti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        // Üst sınıfın constructor'ına tüm parametreleri gönderiyoruz.
        super(baslangicTarihi, sureYil, ekstraAy);
    }


    @Override
    //Servis maliyetini hesaplar.
    public double sonMaliyetHesapla(double hamUcret) {
        if (devamEdiyorMu()) {
            return 0.0; // Garanti kapsamındaysa ücret alınmaz
        } else {
            // Uzatılmış garanti alan müşteri, süresi bitse bile %10 indirim alır.
            // hamUcret * 0.90 -> %10 indirimli fiyat
            return hamUcret * 0.90;
        }
    }

    @Override
    public String garantiTuru() {
        return "Uzatılmış Garanti Paketi";
    }
}