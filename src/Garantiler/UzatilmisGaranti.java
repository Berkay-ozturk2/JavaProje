package Garantiler;

public class UzatilmisGaranti extends Garanti {

    private double oran; // cihaz fiyatının yüzdesi (Uzatılmış Garanti Ücreti için)
    private static final double GARANTI_BITIS_SERVIS_ORANI = 0.10; // Uzatılmış garanti bitince onarım %10 fiyatında


    public UzatilmisGaranti(int sureYil, double oran) {
        super(sureYil);
        this.oran = oran;
    }

    /**
     * Uzatılmış Garanti Ücreti Hesaplama (Ek Ücret)
     * Ücret = Cihaz Fiyatı * Oran * Uzatılan Yıl Sayısı
     */
    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        // Uzatılmış garanti alabilmek için ek ücret (cihaz fiyatının yüzdesi * yıl)
        return cihazFiyati * oran * super.getSureYil();
    }

    /**
     * Servis ücreti hesaplar. Uzatılmış Garanti aktifse 0, değilse cihaz fiyatının %10'u onarım ücreti alınır.
     */
    @Override
    public double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi) {
        if (garantiAktifMi) {
            return 0.0; // Uzatılmış Garanti aktifse servis ücretsiz
        } else {
            // Uzatılmış Garanti bitmişse cihaz fiyatının %10'u onarım ücreti alınır
            return cihazFiyati * GARANTI_BITIS_SERVIS_ORANI;
        }
    }

    @Override
    public String garantiTuru() {
        return "Uzatılmış Garanti";
    }

    public double getOran() {
        return oran;
    }

    public void setOran(double oran) {
        if (oran <= 0) {
            throw new IllegalArgumentException("Oran 0 veya negatif olamaz.");
        }
        this.oran = oran;
    }
}