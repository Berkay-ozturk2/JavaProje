package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;


public class Laptop extends Cihaz {


    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);

    }


    @Override
    public int getGarantiSuresiYil() {
        return 3;
    }


    @Override
    public String getCihazTuru() {
        return "Laptop";
    }
}