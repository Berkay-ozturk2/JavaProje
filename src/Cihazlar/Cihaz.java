
package Cihazlar;
import java.io.Serializable;
import java.time.LocalDate;


public abstract class Cihaz implements Serializable {
    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private LocalDate garantiBaslangic;


    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic) {
        this.seriNo = seriNo;
        this.marka = marka;
        this.model = model;
        this.fiyat = fiyat;
        this.garantiBaslangic = garantiBaslangic;
    }


    // Encapsulation
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public LocalDate getGarantiBaslangic() { return garantiBaslangic; }


    public void setFiyat(double fiyat) {
        if (fiyat <= 0) {
            throw new IllegalArgumentException("Fiyat sifirdan buyuk olmalidir");
        }
        this.fiyat = fiyat;
    }


    // Abstract methods (polymorphism)
    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();


    // Concrete method
    public LocalDate getGarantiBitisTarihi() {
        return garantiBaslangic.plusYears(getGarantiSuresiYil());
    }


    @Override
    public String toString() {
        return String.format("%s [%s - %s] (%s)", getCihazTuru(), marka, model, seriNo);
    }
}