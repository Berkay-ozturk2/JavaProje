package Garantiler;

import java.time.LocalDate;

public class StandartGaranti extends Garanti {

    public StandartGaranti(LocalDate baslangicTarihi, int sureYil) {
        super(baslangicTarihi, sureYil, 0);
    }

    @Override
    public double sonMaliyetHesapla(double hamUcret) {
        if (isDevamEdiyor()) {
            return 0.0; // Garanti kapsamındaysa ücretsiz
        } else {
            return hamUcret; // Süre bittiyse tam ücret
        }
    }

    @Override
    public String garantiTuru() {
        return "Standart Garanti";
    }

    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        return 0.0;
    }

    @Override
    public double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi) {
        return garantiAktifMi ? 0.0 : cihazFiyati;
    }

    @Override
    public String toString() {
        long kalan = getKalanGunSayisi();
        return garantiTuru() + (kalan > 0 ? " (" + kalan + " gün kaldı)" : " (Süre Doldu)");
    }
}