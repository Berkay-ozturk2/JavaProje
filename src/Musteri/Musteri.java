package Musteri;

import Istisnalar.GecersizDegerException; // EKLENDİ

public class Musteri {
    private String ad;
    private String soyad;
    private String telefon;

    public Musteri(String ad, String soyad, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
    }

    public Musteri(String ad, String soyad) {
        this(ad, soyad, "Belirtilmedi");
    }

    public String getAd() { return ad.toUpperCase(); }
    public String getSoyad() { return soyad.toUpperCase(); }
    public String getTelefon() { return telefon; }

    // --- EKLENEN SETTER METOTLARI ---

    // Setter 4: Ad Validasyonu
    public void setAd(String ad) throws GecersizDegerException {
        if (ad == null || ad.length() < 2) {
            throw new GecersizDegerException("Ad en az 2 karakter olmalıdır.");
        }
        this.ad = ad;
    }

    // Setter 5: Soyad Validasyonu
    public void setSoyad(String soyad) throws GecersizDegerException {
        if (soyad == null || soyad.length() < 2) {
            throw new GecersizDegerException("Soyad en az 2 karakter olmalıdır.");
        }
        this.soyad = soyad;
    }

    public void setTelefon(String telefon) {
        if (telefon == null || telefon.length() < 10) {
            System.err.println("Geçersiz telefon formatı!");
            return;
        }
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return ad + " " + soyad + " (" + telefon + ")";
    }
}