// src/Cihazlar/Telefon.java (GÜNCELLENDİ)
package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri; // YENİ IMPORT

public class Telefon extends Cihaz {



    public Telefon(String seriNo, String marka, String model,
                   LocalDate garantiBaslangic, Musteri sahip) { // CONSTRUCTOR GÜNCELLENDİ
        super(seriNo, marka, model,  garantiBaslangic, sahip);

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