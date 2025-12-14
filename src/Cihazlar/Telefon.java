package Cihazlar;


public class Telefon extends Cihaz {


    private int kameraMP;
    private boolean ciftHat;


    public Telefon(String seriNo, String marka, String model) {
        super(seriNo, marka, model);
    }


    public Telefon(String seriNo, String marka, String model, double fiyat, int kameraMP, boolean ciftHat) {
        super(seriNo, marka, model, fiyat, java.time.LocalDate.now());
        this.kameraMP = kameraMP;
        this.ciftHat = ciftHat;
    }


    @Override
    public int getGarantiSuresiYil() {
        return 2;
    }


    @Override
    public String getCihazTuru() {
        return "Telefon";
    }


    public int getKameraMP() { return kameraMP; }
    public void setKameraMP(int kameraMP) { this.kameraMP = kameraMP; }


    public boolean isCiftHat() { return ciftHat; }
    public void setCiftHat(boolean ciftHat) { this.ciftHat = ciftHat; }
}