package Araclar;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TarihYardimcisi {

    // Metot "static" yapıldı, böylece 'new TarihYardimcisi()' demeden sınıf ismiyle çağrılabilir.
    public static LocalDate isGunuEkle(LocalDate baslangic, int isGunuSayisi) {
        LocalDate tarih = baslangic;
        int sayac = 0;

        while (sayac < isGunuSayisi) {
            tarih = tarih.plusDays(1);
            // Cumartesi ve Pazar değilse sayacı artır
            if (tarih.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    tarih.getDayOfWeek() != DayOfWeek.SUNDAY) {
                sayac++;
            }
        }
        return tarih;
    }
}