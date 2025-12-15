package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;

public class Telefon extends Cihaz {

    //Telefon Constructor
    public Telefon(String seriNo, String marka, String model, double fiyat,
                   LocalDate garantiBaslangic, Musteri sahip) {
        //Değerler Cihaz Sinifina gönderildi
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    public int getGarantiSuresiYil() {
        return 2;
    }

    @Override
    public String getCihazTuru() {
        return "Telefon";
    }
}