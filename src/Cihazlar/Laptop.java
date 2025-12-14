package Cihazlar;

public class Laptop extends Cihaz {


    private String islemci;
    private int ramGB;
    private boolean SSD;


    public Laptop(String seriNo, String marka, String model) {
        super(seriNo, marka, model);
    }


    public Laptop(String seriNo, String marka, String model, double fiyat, String islemci, int ramGB, boolean SSD) {
        super(seriNo, marka, model, fiyat, java.time.LocalDate.now());
        this.islemci = islemci;
        this.ramGB = ramGB;
        this.SSD = SSD;
    }


    @Override
    public int getGarantiSuresiYil() {
        return 3;
    }


    @Override
    public String getCihazTuru() {
        return "Laptop";
    }


    public String getIslemci() { return islemci; }
    public void setIslemci(String islemci) { this.islemci = islemci; }


    public int getRamGB() { return ramGB; }
    public void setRamGB(int ramGB) { this.ramGB = ramGB; }


    public boolean isSSD() { return SSD; }
    public void setSSD(boolean SSD) { this.SSD = SSD; }
}
