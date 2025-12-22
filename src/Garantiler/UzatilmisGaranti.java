package Garantiler;

import java.time.LocalDate;

// Standart garantiye ek süre ve indirim sağlayan gelişmiş garanti sınıfı.
public class UzatilmisGaranti extends Garanti {

    // Uzatılmış Garanti kurucu metodu; standart süreye eklenen ayları da hesaba katarak nesneyi oluşturur.
    public UzatilmisGaranti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        // Üst sınıfın yapıcısını çağırarak hem standart yılı hem de ekstra ayları işler.
        super(baslangicTarihi, sureYil, ekstraAy);
    }

    @Override
    // Servis maliyetini hesaplar; uzatılmış garanti sahiplerine özel indirim mantığı içerir.
    public double sonMaliyetHesapla(double hamUcret) {
        // Garanti süresi devam ediyorsa tamir tamamen ücretsizdir.
        if (devamEdiyorMu()) {
            return 0.0;
        } else {
            // Süre bitmiş olsa dahi, uzatılmış garanti alan müşteriye %10 indirim uygulanır.
            return hamUcret * 0.90;
        }
    }

    @Override
    // Sistemin diğer bölümlerinde (Raporlar vb.) görünmesi için bu garantinin özel adını döndürür.
    public String garantiTuru() {
        return "Uzatılmış Garanti Paketi";
    }
}