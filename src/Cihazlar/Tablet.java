package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Istisnalar.GecersizDegerException; // Eklendi

public class Tablet extends Cihaz {
    private boolean kalemDestegi;

    // throws GecersizDegerException eklendi
    public Tablet(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, boolean kalemDestegi, Musteri sahip) throws GecersizDegerException {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
        this.kalemDestegi = kalemDestegi;
    }

    public boolean getKalemDestegi() {
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