package Servis;

import java.util.ArrayList;
import java.util.List;

public class TeknisyenDeposu {
    // Tüm teknisyenleri hafızada tutan ana listemiz
    private static final List<Teknisyen> mevcutTeknisyenler = new ArrayList<>();

    // Statik blok: Program çalıştırıldığı an (main başlamadan önce) bu kod bir kez çalışır ve listeyi doldurur
    static {
        mevcutTeknisyenler.add(new Teknisyen("Osman Can Küçdemir", "Laptop Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("Çağatay Oğuz", "Telefon Onarım"));
        mevcutTeknisyenler.add(new Teknisyen("İsmail Onur Koru", "Tablet Onarım"));
    }

    // Listeye dışarıdan erişmek isteyenler için getter metodu
    public static List<Teknisyen> getTumTeknisyenler() {
        return mevcutTeknisyenler;
    }

    /**
     * İsimden teknisyen bulur. Eğer listede yoksa yeni oluşturup havuza ekler.
     */
    public static Teknisyen teknisyenBulVeyaOlustur(String ad, String uzmanlik) {
        // Listeyi gezip aynı isimde biri var mı diye bakıyoruz
        for (Teknisyen t : mevcutTeknisyenler) {
            if (t.getAd().equalsIgnoreCase(ad)) {
                return t; // Varsa onu döndür
            }
        }
        // Yoksa yeni bir teknisyen oluşturup listeye ekliyoruz (Dynamik büyüme)
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
        // Gelen cihaz türüne göre hangi uzmanlığı arayacağımızı belirliyoruz
        if (cihazTuru.equalsIgnoreCase("Laptop")) aranacakUzmanlik = "Laptop";
        else if (cihazTuru.equalsIgnoreCase("Telefon")) aranacakUzmanlik = "Telefon";
        else if (cihazTuru.equalsIgnoreCase("Tablet")) aranacakUzmanlik = "Tablet";

        // Uzmanlık alanı eşleşen ilk teknisyeni bulup döndürüyoruz
        for (Teknisyen t : mevcutTeknisyenler) {
            if (t.getUzmanlikAlani().contains(aranacakUzmanlik)) {
                return t;
            }
        }
        // Eğer uygun uzman bulunamazsa, hata vermemesi için listedeki ilk kişiyi atıyoruz
        return mevcutTeknisyenler.get(0);
    }
}