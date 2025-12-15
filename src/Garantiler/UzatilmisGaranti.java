package Garantiler;

public class UzatilmisGaranti extends Garanti {

    // Fiyatlandırma Oranları (Cihaz Fiyatının Yüzdesi)
    private static final double ORAN_6_AY = 0.05;  // %5
    private static final double ORAN_12_AY = 0.07; // %7
    private static final double ORAN_24_AY = 0.10; // %10

    // Constructor: Abstract sınıfın (Garanti) constructor'ını çağırır
    public UzatilmisGaranti(int sureYil) {
        super(sureYil);
    }

    /**
     * Statik Metot: Seçilen aya göre paket fiyatını hesaplar.
     */
    public static double paketFiyatiHesapla(double cihazFiyati, int ay) {
        switch (ay) {
            case 6:
                return cihazFiyati * ORAN_6_AY;
            case 12:
                return cihazFiyati * ORAN_12_AY;
            case 24:
                return cihazFiyati * ORAN_24_AY;
            default:
                return 0.0;
        }
    }

    @Override
    public double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi) {
        // Uzatılmış garanti aktifse ücretsiz, değilse %10 indirimli tamir
        return garantiAktifMi ? 0.0 : cihazFiyati * 0.10;
    }

    @Override
    public double garantiUcretiHesapla(double cihazFiyati) {
        // Bu metot abstract olduğu için override edilmek zorundadır.
        // Ancak biz hesaplamayı yukarıdaki statik metotla yapıyoruz.
        // O yüzden burası varsayılan bir değer dönebilir.
        return 0.0;
    }

    @Override
    public String garantiTuru() {
        return "Uzatılmış Garanti Paketi";
    }
}