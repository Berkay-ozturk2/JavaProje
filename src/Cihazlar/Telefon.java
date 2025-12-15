// src/Cihazlar/Telefon.java (GÜNCELLENDİ: Fiyat Parametresi Eklendi)
package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;

public class Telefon extends Cihaz {

    public Telefon(String seriNo, String marka, String model, double fiyat, // Fiyat eklendi
                   LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip); // Fiyat super'a gönderildi
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