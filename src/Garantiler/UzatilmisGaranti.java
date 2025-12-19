package Garantiler;

import java.time.LocalDate;

public class UzatilmisGaranti extends Garanti {

    // Paket satış fiyatı hesaplama (Statik olarak kalıyor, Main içinde kullanılıyor)
    public static double paketFiyatiHesapla(double cihazFiyati, int ay) {
        switch (ay) {
            case 6: return cihazFiyati * 0.05;
            case 12: return cihazFiyati * 0.07;
            case 24: return cihazFiyati * 0.10;
            case 36: return cihazFiyati * 0.15; // EKLENEN 4. CASE
            default: return 0.0;
        }
    }

    public UzatilmisGaranti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        super(baslangicTarihi, sureYil, ekstraAy);
    }

    @Override
    public double sonMaliyetHesapla(double hamUcret) {
        if (isDevamEdiyor()) {
            return 0.0;
        } else {
            // JEST: Uzatılmış garanti alan müşteri, süresi bitse bile %10 indirim alır.
            return hamUcret * 0.90;
        }
    }

    @Override
    public String garantiTuru() {
        return "Uzatılmış Garanti Paketi";
    }

    // SİLİNEN METOTLAR: garantiUcretiHesapla, servisUcretiHesapla
}