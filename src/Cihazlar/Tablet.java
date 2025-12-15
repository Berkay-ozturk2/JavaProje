package Cihazlar;


import java.time.LocalDate;

public class Tablet extends Cihaz {
    private boolean kalemDestegi;


    public Tablet(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, boolean kalemDestegi) {
        super(seriNo, marka, model, fiyat, garantiBaslangic);
        this.kalemDestegi = kalemDestegi;
    }


    public boolean isKalemDestegi() {
        return kalemDestegi;
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
