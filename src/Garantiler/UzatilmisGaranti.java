package Garantiler;

public class UzatilmisGaranti extends Garanti {

    private double oran; // cihaz fiyatının yüzdesi

    public UzatilmisGaranti(int sureYil, double oran) {
        super(sureYil);
        this.oran = oran;
    }

    @Override
    public double ucretHesapla(double cihazFiyati) {
        return cihazFiyati * oran;
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
