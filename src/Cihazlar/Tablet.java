// src/Cihazlar/Tablet.java (GÜNCELLENDİ)
package Cihazlar;


import java.time.LocalDate;
import Musteri.Musteri; // YENİ IMPORT

public class Tablet extends Cihaz {
    private boolean kalemDestegi;


    public Tablet(String seriNo, String marka, String model,
                  LocalDate garantiBaslangic, boolean kalemDestegi, Musteri sahip) { // CONSTRUCTOR GÜNCELLENDİ
        super(seriNo, marka, model,  garantiBaslangic, sahip);
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