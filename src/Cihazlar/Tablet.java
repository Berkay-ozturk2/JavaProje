package Cihazlar;


import java.time.LocalDate;
import Musteri.Musteri;

public class Tablet extends Cihaz {

    public Tablet(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);

    }





    @Override
    public int getGarantiSuresiYil() {
        return 2;
    }


    @Override
    public String getCihazTuru() {
        return "Tablet";
    }
}