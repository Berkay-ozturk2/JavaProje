package Musteri;

import Istisnalar.GecersizDegerException;

public class Musteri {
    // Müşterinin kimlik ve iletişim bilgilerini tutan değişkenler
    private String ad;
    private String soyad;
    private String telefon;
    private boolean vip;

    // Kurucu metod ile müşterinin tüm bilgilerini alarak nesneyi başlatıyoruz
    public Musteri(String ad, String soyad, String telefon) {
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.vip = false;
    }

    // Eğer telefon girilmezse varsayılan değer atayan diğer kurucu metod (Overloading)
    public Musteri(String ad, String soyad) {
        this(ad, soyad, "Belirtilmedi");
    }

    // --- VIP Getter ve Setter ---
    // Müşterinin VIP olup olmadığını döndüren metod
    public boolean vipMi() { return vip; }

    // Müşterinin VIP durumunu değiştiren metod
    public void setVip(boolean vip) { this.vip = vip; }

    // İsmi her zaman büyük harflerle döndüren metod
    public String getAd() { return ad.toUpperCase(); }
    // Soyadı her zaman büyük harflerle döndüren metod
    public String getSoyad() { return soyad.toUpperCase(); }
    // Telefon numarasını döndüren metod
    public String getTelefon() { return telefon; }

    // İsim değiştirilirken en az 2 karakter kontrolü yapıyoruz, yoksa hata fırlatıyoruz
    public void setAd(String ad) throws GecersizDegerException {
        if (ad == null || ad.length() < 2) {
            throw new GecersizDegerException("Ad en az 2 karakter olmalıdır.");
        }
        this.ad = ad;
    }

    // Soyisim için de aynı uzunluk kontrolünü yapıyoruz
    public void setSoyad(String soyad) throws GecersizDegerException {
        if (soyad == null || soyad.length() < 2) {
            throw new GecersizDegerException("Soyad en az 2 karakter olmalıdır.");
        }
        this.soyad = soyad;
    }

    // Telefon numarası geçerli mi diye basit bir uzunluk kontrolü yapıyoruz
    public void setTelefon(String telefon) {
        if (telefon == null || telefon.length() < 10) {
            System.err.println("Geçersiz telefon formatı!");
            return;
        }
        this.telefon = telefon;
    }

    // Müşteri nesnesi ekrana yazdırıldığında VIP durumuyla beraber düzgün görünmesini sağlıyoruz
    @Override
    public String toString() {
        String vipEtiketi = vip ? " [VIP]" : "";
        return ad + " " + soyad + vipEtiketi + " (" + telefon + ")";
    }
}