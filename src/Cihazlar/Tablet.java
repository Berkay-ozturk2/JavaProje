package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;

public class Tablet extends Cihaz {
    private boolean kalemDestegi;

    public Tablet(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, boolean kalemDestegi, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
        this.kalemDestegi = kalemDestegi;
    }

    // Private getter yerine sınıf içi erişim de mümkün ama dışarıdan erişim için:
    public boolean getKalemDestegi() {
        return kalemDestegi;
    }

    @Override
    public String toTxtFormat() {
        return String.format("Tablet;;%s;;%s;;%s;;%.2f;;%s;;%d;;%s;;%s;;%s;;%b",
                getSeriNo(), getMarka(), getModel(), getFiyat(), getGarantiBaslangic(),
                getEkstraGarantiSuresiAy(),
                getSahip().getAd(), getSahip().getSoyad(), getSahip().getTelefon(),
                kalemDestegi);
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