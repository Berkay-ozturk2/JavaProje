package Cihazlar;


import java.time.LocalDate;

public class Telefon extends Cihaz {
    private boolean ciftSim;


    public Telefon(String seriNo, String marka, String model, double fiyat,
                   LocalDate garantiBaslangic, boolean ciftSim) {
        super(seriNo, marka, model, fiyat, garantiBaslangic);
        this.ciftSim = ciftSim;
    }


    public boolean isCiftSim() {
        return ciftSim;
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