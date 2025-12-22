package Servis;

// Durumları standartlaştırmak için Enum kullandık (Sadece belirli seçenekler olsun diye)
public enum ServisDurumu {
    KABUL_EDILDI("Kabul Edildi"), // Cihaz servise ilk geldiğinde bu durumu alacak
    TAMAMLANDI("Tamamlandı");     // Tamir bitince bu duruma geçecek

    private final String gorunurAd; // Ekranda kullanıcıya göstereceğimiz Türkçe isim

    // Enum oluşturulurken parantez içindeki ismi bu değişkene atayan kurucu metot
    ServisDurumu(String gorunurAd) {
        this.gorunurAd = gorunurAd;
    }

    // Ekrana yazdırınca BÜYÜK_HARFLİ kod adı yerine, parantez içindeki ismi yazsın
    @Override
    public String toString() {
        return gorunurAd;
    }
}