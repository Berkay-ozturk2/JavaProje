// src/Cihazlar/Laptop.java (GÜNCELLENDİ)
package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri; // YENİ IMPORT


public class Laptop extends Cihaz {



    public Laptop(String seriNo, String marka, String model,
                  LocalDate garantiBaslangic, boolean hariciEkranKarti, Musteri sahip) { // CONSTRUCTOR GÜNCELLENDİ
        super(seriNo, marka, model, garantiBaslangic, sahip);

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