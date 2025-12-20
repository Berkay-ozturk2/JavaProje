package Garantiler;

import java.time.LocalDate;

public class UzatilmisGaranti extends Garanti {

    // (Eski paketFiyatiHesapla metodu buradan kaldırıldı)

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
}