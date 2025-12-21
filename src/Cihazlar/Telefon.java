package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Istisnalar.GecersizDegerException; // Eklendi

public class Telefon extends Cihaz {

    // Telefon constructor
    public Telefon(String seriNo, String marka, String model, double fiyat,
                   LocalDate garantiBaslangic, Musteri sahip) throws GecersizDegerException {
        //Bilgileri Cihaz Ata sınıfına gönderir
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    //Telefon garanti süresi 2 yıl
    public int getGarantiSuresiYil() {
        return 2;
    }

    @Override
    public String getCihazTuru() {
        return "Telefon";
    }
}