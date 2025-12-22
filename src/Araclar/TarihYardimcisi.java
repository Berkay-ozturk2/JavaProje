package Araclar;

import java.time.DayOfWeek;
import java.time.LocalDate;

// Tarih hesaplamaları ve iş günü takibi için kullanılan yardımcı sınıf.
public class TarihYardimcisi {

    // Verilen tarihe hafta sonlarını atlayarak belirtilen sayıda iş günü ekler.
    public static LocalDate isGunuEkle(LocalDate baslangic, int isGunuSayisi) {
        // Hesaplama sırasında başlangıç tarihini değiştirmemek için yeni bir değişkene atar.
        LocalDate tarih = baslangic;

        int sayac = 0;

        // Hedeflenen iş günü sayısına ulaşana kadar döngüyü çalıştırır.
        while (sayac < isGunuSayisi) {
            // Tarihi takvim üzerinde bir gün ileri alır.
            tarih = tarih.plusDays(1);

            // O günün Cumartesi veya Pazar olup olmadığını kontrol eder.
            if (tarih.getDayOfWeek() != DayOfWeek.SATURDAY && tarih.getDayOfWeek() != DayOfWeek.SUNDAY) {
                // Eğer gün hafta sonu değilse, sayacı bir artırarak iş günü olarak işler.
                sayac++;
            }
        }
        // Hesaplanan tahmini bitiş tarihini döndürür.
        return tarih;
    }
}