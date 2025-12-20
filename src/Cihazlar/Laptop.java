package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;

public class Laptop extends Cihaz {

    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    public String toTxtFormat() {
        return String.format("Laptop;;%s;;%s;;%s;;%.2f;;%s;;%d;;%s;;%s;;%s;;%b",
                getSeriNo(), getMarka(), getModel(), getFiyat(), getGarantiBaslangic(),
                getEkstraGarantiSuresiAy(),
                getSahip().getAd(), getSahip().getSoyad(), getSahip().getTelefon(),
                getSahip().isVip()); // EKLENDİ
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