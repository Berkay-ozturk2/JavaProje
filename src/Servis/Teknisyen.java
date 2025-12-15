// src/Servis/Teknisyen.java (GÜNCELLENDİ)
package Servis;

import java.io.Serializable;

public class Teknisyen implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ad;
    private String uzmanlikAlani; // Örneğin: Telefon, Laptop
    private int teknisyenId;
    private static int nextId = 1; // Basit bir ID ataması için

    public Teknisyen(String ad, String uzmanlikAlani) {
        this.teknisyenId = nextId++;
        this.ad = ad;
        this.uzmanlikAlani = uzmanlikAlani;
    }

    // Getter Metotları
    public int getTeknisyenId() { return teknisyenId; }
    public String getAd() { return ad; }
    public String getUzmanlikAlani() { return uzmanlikAlani; }

    @Override
    public String toString() {
        return ad + " (" + uzmanlikAlani + ")";
    }
}