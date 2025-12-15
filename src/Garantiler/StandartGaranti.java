package Garantiler;

public class StandartGaranti extends Garanti {

    public StandartGaranti(int sureYil) {
        super(sureYil);
    }

    @Override
    public double ucretHesapla(double cihazFiyati) {
        // Standart garanti genelde ücretsiz
        return 0.0;
    }

    @Override
    public String garantiTuru() {
        return "Standart Garanti";
    }
}
