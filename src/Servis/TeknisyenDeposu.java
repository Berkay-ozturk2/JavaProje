package Servis;

import java.util.ArrayList;
import java.util.List;

public class TeknisyenDeposu {
    private static final List<Teknisyen> mevcutTeknisyenler = new ArrayList<>();

    // Statik blok: Program açılınca teknisyenler bir kere oluşturulur ve ID'leri sabitlenir (1, 2, 3)
    static {
        mevcutTeknisyenler.add(new Teknisyen("Osman Can Küçdemir", "Laptop Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("Çağatay Oğuz", "Telefon Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("İsmail Onur Koru", "Tablet Onarım"));
    }

    public static List<Teknisyen> getTumTeknisyenler() {
        return mevcutTeknisyenler;
    }

    /**
     * İsimden teknisyen bulur. Eğer listede yoksa (yeni personel veya eski kayıt)
     * yeni oluşturup havuza ekler.
     */
    public static Teknisyen teknisyenBulVeyaOlustur(String ad, String uzmanlik) {
        for (Teknisyen t : mevcutTeknisyenler) {
            if (t.getAd().equalsIgnoreCase(ad)) {
                return t; // Mevcut nesneyi (ve sabit ID'sini) döndür
            }
        }
        // Listede yoksa yeni oluştur ve ekle (ID kaldığı yerden devam eder)
        Teknisyen yeni = new Teknisyen(ad, uzmanlik);
        mevcutTeknisyenler.add(yeni);
        return yeni;
    }
}