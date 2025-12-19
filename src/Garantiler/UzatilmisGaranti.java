package Garantiler;

import java.time.LocalDate;

public class UzatilmisGaranti extends Garanti {

    // Paket satış fiyatı hesaplama (Statik olarak kalıyor)
    public static double paketFiyatiHesapla(double cihazFiyati, int ay) {
        switch (ay) {
            case 6: return cihazFiyati * 0.05;
            case 12: return cihazFiyati * 0.07;
            case 24: return cihazFiyati * 0.10;
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

    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        return 0.0;
    }

    @Override
    public double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi) {
        return 0.0;
    }
}