package Musteri;

public class Musteri {
    private String ad;
    private String soyad;
    private String telefon;

    // Mevcut Constructor
    public Musteri(String ad, String soyad, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
    }

    // GEREKSİNİM: Constructor Overloading (Aşırı Yükleme)
    // Telefonu henüz bilinmeyen müşteri için
    public Musteri(String ad, String soyad) {
        this(ad, soyad, "Belirtilmedi");
    }

    public String getAd() { return ad.toUpperCase(); }
    public String getSoyad() { return soyad.toUpperCase(); }
    public String getTelefon() { return telefon; }

    // GEREKSİNİM: Setter içinde Validasyon (Null kontrolü)
    public void setTelefon(String telefon) {
        if (telefon == null || telefon.length() < 10) {
            System.err.println("Geçersiz telefon formatı!");
            // Burada normalde throw new exception yapılır ama GUI çökmesin diye log basıyoruz.
            return;
        }
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return ad + " " + soyad + " (" + telefon + ")";
    }
}