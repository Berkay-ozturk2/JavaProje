package Garantiler;

import java.time.LocalDate;

public class StandartGaranti extends Garanti {

    private static final double SERVIS_FIYAT_ORANI = 0.15; // Garanti bitince onarım cihaz fiyatının %15'i

    // Yapıcı metodlar
    public StandartGaranti(LocalDate baslangicTarihi, int sureYil) {
        super(baslangicTarihi, sureYil);
    }

    public StandartGaranti(int sureYil) {
        super(sureYil);
    }

    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        return 0.0; // Standart garanti ücretsizdir
    }

    @Override
    public double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi) {
        if (garantiAktifMi) {
            return 0.0; // Garanti aktifse servis ücretsiz
        } else {
            return cihazFiyati * SERVIS_FIYAT_ORANI;
        }
    }

    @Override
    public String garantiTuru() {
        return "Standart Garanti";
    }

    // YENİ: Kalan günü gösterecek şekilde metin çıktısı (toString) düzenlendi
    @Override
    public String toString() {
        long kalanGun = getKalanGunSayisi();
        String durumMesaji;

        if (kalanGun > 0) {
            durumMesaji = kalanGun + " Gün Kaldı";
        } else if (kalanGun == 0) {
            durumMesaji = "Son Gün!";
        } else {
            // Negatif sayı sürenin ne kadar geçtiğini gösterir, mutlak değerini alıyoruz
            durumMesaji = "Süre Doldu (" + Math.abs(kalanGun) + " gün geçti)";
        }

        return String.format("%s - %s", garantiTuru(), durumMesaji);
    }
}