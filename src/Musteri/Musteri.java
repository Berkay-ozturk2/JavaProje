package Musteri;

// 'import java.io.Serializable;' kaldırıldı

public class Musteri {
    // 'serialVersionUID' kaldırıldı

    private String ad;
    private String soyad;
    private String telefon;

    public Musteri(String ad, String soyad, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
    }

    public String getAd() { return ad.toUpperCase(); }
    public String getSoyad() { return soyad.toUpperCase(); }
    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public String toString() {
        return ad + " " + soyad + " (" + telefon + ")";
    }
}