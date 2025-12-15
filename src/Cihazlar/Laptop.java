package Cihazlar;

import java.time.LocalDate;


public class Laptop extends Cihaz {
    private boolean hariciEkranKarti;


    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, boolean hariciEkranKarti) {
        super(seriNo, marka, model, fiyat, garantiBaslangic);
        this.hariciEkranKarti = hariciEkranKarti;
    }


    public boolean isHariciEkranKarti() {
        return hariciEkranKarti;
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