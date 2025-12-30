package Musteri;

import Istisnalar.GecersizDegerException;

public class Musteri {
    // --- YENİ EKLENEN KISIM ---
    private int id;
    private static int sayac = 1; // ID'leri otomatikleştirmek için sayaç
    // ---------------------------

    private String ad;
    private String soyad;
    private String telefon;
    private boolean vip;

    public Musteri(String ad, String soyad, String telefon) {
        // Her yeni müşteri oluşturulduğunda ID otomatik atanacak
        this.id = sayac++;

        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.vip = false;
    }

    public Musteri(String ad, String soyad) {
        this(ad, soyad, "Belirtilmedi");
    }

    // --- ID İÇİN GETTER ---
    public int getId() {
        return id;
    }

    public boolean vipMi() { return vip; }
    public void setVip(boolean vip) { this.vip = vip; }

    public String getAd() { return ad.toUpperCase(); }
    public String getSoyad() { return soyad.toUpperCase(); }
    public String getTelefon() { return telefon; }

    public void setAd(String ad) throws GecersizDegerException {
        if (ad == null || ad.length() < 2) {
            throw new GecersizDegerException("Ad en az 2 karakter olmalıdır.");
        }
        this.ad = ad;
    }

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
        String vipEtiketi = vip ? " [VIP]" : "";
        // Çıktıya ID bilgisini de ekledik
        return "[" + id + "] " + ad + " " + soyad + vipEtiketi + " (" + telefon + ")";
    }
}