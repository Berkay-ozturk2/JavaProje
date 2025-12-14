package Cihazlar;

public class Tablet extends Cihaz {


    private double ekranBoyutu;
    private boolean kalemDestegi;


    public Tablet(String seriNo, String marka, String model) {
        super(seriNo, marka, model);
    }


    public Tablet(String seriNo, String marka, String model, double fiyat, double ekranBoyutu, boolean kalemDestegi) {
        super(seriNo, marka, model, fiyat, java.time.LocalDate.now());
        this.ekranBoyutu = ekranBoyutu;
        this.kalemDestegi = kalemDestegi;
    }


    @Override
    public int getGarantiSuresiYil() {
        return 2;
    }


    @Override
    public String getCihazTuru() {
        return "Tablet";
    }


    public double getEkranBoyutu() { return ekranBoyutu; }
    public void setEkranBoyutu(double ekranBoyutu) { this.ekranBoyutu = ekranBoyutu; }


    public boolean isKalemDestegi() { return kalemDestegi; }
    public void setKalemDestegi(boolean kalemDestegi) { this.kalemDestegi = kalemDestegi; }
}
