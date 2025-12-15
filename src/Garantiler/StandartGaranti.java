package Garantiler;

public class StandartGaranti extends Garanti {

    private static final double SERVIS_FIYAT_ORANI = 0.15; // Garanti bitince onarım cihaz fiyatının %15'i

    public StandartGaranti(int sureYil) {
        super(sureYil);
    }

    // Garanti ücreti: Standart garanti ücretsizdir
    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        return 0.0;
    }

    /**
     * Servis ücreti hesaplar. Garanti aktifse 0, değilse cihaz fiyatının %15'i onarım ücreti alınır.
     */
    @Override
    public double servisUcretiHesapla( boolean garantiAktifMi) {
        if (garantiAktifMi) {
            return 0.0; // Garanti aktifse servis ücretsiz
        } else {
            // Garanti bitmişse cihaz fiyatının %15'i onarım ücreti alınır
            return cihazFiyati * SERVIS_FIYAT_ORANI;
        }
    }

    @Override
    public String garantiTuru() {
        return "Standart Garanti";
    }
}