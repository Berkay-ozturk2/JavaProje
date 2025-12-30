package Musteri;

import java.util.HashMap;
import java.util.Map;

public class MusteriDeposu {
    // Telefon numarasını ANAHTAR (Key) olarak kullanıp müşterileri tekil tutuyoruz
    private static final Map<String, Musteri> musteriHavuzu = new HashMap<>();

    /**
     * Bu metot, verilen bilgilerle bir müşteri arar.
     * Eğer telefon numarası sistemde kayıtlıysa O MÜŞTERİYİ döndürür (ID aynı kalır).
     * Eğer yoksa YENİ MÜŞTERİ oluşturur, havuza ekler ve onu döndürür.
     */
    public static Musteri musteriBulVeyaOlustur(String ad, String soyad, String telefon) {
        // Telefon numarasındaki boşlukları temizleyelim ki hatalı eşleşme olmasın
        String temizTel = telefon.trim().replace(" ", "");

        if (musteriHavuzu.containsKey(temizTel)) {
            // Müşteri zaten var! Var olan nesneyi döndür (Böylece ID değişmez ve artmaz)
            Musteri mevcutMusteri = musteriHavuzu.get(temizTel);

            // İsim güncellemesi yapılmış olabilir, güncellemek isterseniz:
            // try { mevcutMusteri.setAd(ad); mevcutMusteri.setSoyad(soyad); } catch (Exception e) {}

            return mevcutMusteri;
        } else {
            // Müşteri yok, yeni oluşturuyoruz
            Musteri yeniMusteri = new Musteri(ad, soyad, telefon);
            musteriHavuzu.put(temizTel, yeniMusteri);
            return yeniMusteri;
        }
    }

    // Sistemi temizlemek gerekirse (Opsiyonel)
    public static void depoyuTemizle() {
        musteriHavuzu.clear();
    }
}