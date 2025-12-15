// src/Cihazlar/Laptop.java (GÜNCELLENDİ: Fiyat Parametresi Eklendi)
package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;


public class Laptop extends Cihaz {


    public Laptop(String seriNo, String marka, String model, double fiyat, // Fiyat eklendi
                  LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip); // Fiyat super'a gönderildi

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