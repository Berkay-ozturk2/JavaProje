// src/Musteri/Musteri.java
package Musteri;

import java.io.Serializable;

public class Musteri implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ad;
    private String soyad;
    private String telefon;


    public Musteri(String ad, String soyad, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;

    }

    // Getter Metotları
    public String getAd() { return ad.toUpperCase(); }
    public String getSoyad() { return soyad.toUpperCase(); }
    public String getTelefon() { return telefon; }


    // Setter Metotları (Gerekirse)
    public void setTelefon(String telefon) { this.telefon = telefon; }


    @Override
    public String toString() {
        return ad + " " + soyad + " (" + telefon + ")";
    }
}