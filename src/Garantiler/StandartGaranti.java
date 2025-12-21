package Garantiler;

import java.time.LocalDate;

// Cihazın ilk satın alımında geçerli olan ve ek süre içermeyen temel garanti türüdür.
public class StandartGaranti extends Garanti {

    // Standart Garanti kurucu metodu.
    public StandartGaranti(LocalDate baslangicTarihi, int sureYil) {
        // Ekstra süre parametresini 0 olarak gönderip ana sınıftaki (super) yapıyı başlatır.
        super(baslangicTarihi, sureYil, 0);
    }

    @Override
    // Servis maliyetini hesaplar.
    public double sonMaliyetHesapla(double hamUcret) {
        // Garanti hala aktifse müşteri herhangi bir ücret ödemez.
        if (devamEdiyorMu()) {
            return 0.0; // Garanti kapsamındaysa ücret alınmaz
        } else {
            // Garanti süresi dolduysa tamir ücretinin tamamı yansıtılır.
            return hamUcret; // Süre bittiyse tam ücret ödenir
        }
    }

    @Override
    // Raporlarda görüntülenmek üzere garantinin tür ismini döndürür.
    public String garantiTuru() {
        return "Standart Garanti";
    }

    @Override
    // Garantinin bitmesine kalan günü stringe dönüştürür
    public String toString() {
        // Kalan gün sayısını hesaplayıp kullanıcıya anlaşılır bir durum mesajı oluşturur.
        long kalan = getKalanGunSayisi();
        return garantiTuru() + (kalan > 0 ? " (" + kalan + " gün kaldı)" : " (Süre Doldu)");
    }
}