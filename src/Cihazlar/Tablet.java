// src/Cihazlar/Tablet.java (GÜNCELLENDİ: Fiyat Parametresi Eklendi)
package Cihazlar;


import java.time.LocalDate;
import Musteri.Musteri;

public class Tablet extends Cihaz {
    private boolean kalemDestegi;


    public Tablet(String seriNo, String marka, String model, double fiyat, // Fiyat eklendi
                  LocalDate garantiBaslangic, boolean kalemDestegi, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip); // Fiyat super'a gönderildi
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