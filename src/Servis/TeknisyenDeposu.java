package Servis;

import java.util.ArrayList;
import java.util.List;

public class TeknisyenDeposu {
    private static final List<Teknisyen> mevcutTeknisyenler = new ArrayList<>();

    // Statik blok: Program açılınca teknisyenler bir kere oluşturulur
    static {
        mevcutTeknisyenler.add(new Teknisyen("Osman Can Küçdemir", "Laptop Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("Çağatay Oğuz", "Telefon Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("İsmail Onur Koru", "Tablet Onarım"));
    }

    public static List<Teknisyen> getTumTeknisyenler() {
        return mevcutTeknisyenler;
    }

    /**
     * İsimden teknisyen bulur. Eğer listede yoksa yeni oluşturup havuza ekler.
     */
    public static Teknisyen teknisyenBulVeyaOlustur(String ad, String uzmanlik) {
        for (Teknisyen t : mevcutTeknisyenler) {
            if (t.getAd().equalsIgnoreCase(ad)) {
                return t;
            }
        }
        Teknisyen yeni = new Teknisyen(ad, uzmanlik);
        mevcutTeknisyenler.add(yeni);
        return yeni;
    }

    /**
     * Cihaz türüne göre uygun uzmanlığa sahip teknisyeni döndürür.
     * Main sınıfındaki mantık buraya taşınarak merkezileştirilmiştir.
     */
    public static Teknisyen uzmanligaGoreGetir(String cihazTuru) {
        String aranacakUzmanlik = "";
        if (cihazTuru.equalsIgnoreCase("Laptop")) aranacakUzmanlik = "Laptop";
        else if (cihazTuru.equalsIgnoreCase("Telefon")) aranacakUzmanlik = "Telefon";
        else if (cihazTuru.equalsIgnoreCase("Tablet")) aranacakUzmanlik = "Tablet";

        // Uzmanlık alanı eşleşen ilk teknisyeni bul
        for (Teknisyen t : mevcutTeknisyenler) {
            if (t.getUzmanlikAlani().contains(aranacakUzmanlik)) {
                return t;
            }
        }
        // Bulunamazsa varsayılan olarak ilk teknisyeni döndür
        return mevcutTeknisyenler.get(0);
    }
}