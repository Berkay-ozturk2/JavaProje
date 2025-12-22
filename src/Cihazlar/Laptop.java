package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Arayuzler.IRaporIslemleri;
import Istisnalar.GecersizDegerException;

// Cihaz sınıfından türetilen ve raporlama yeteneği kazandıran arayüzü uygulayan Laptop sınıfı.
public class Laptop extends Cihaz implements IRaporIslemleri {

    // Laptop Constructor
    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) throws GecersizDegerException {
        // Üst sınıfın (Cihaz) yapıcısını çağırarak ortak özellikleri başlatır.
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    // Laptop cihazları için özel olarak belirlenen garanti süresini döndürür.
    public int getGarantiSuresiYil() {
        return 3;
    }

    @Override
    // Polimorfizm işlemlerinde bu nesnenin "Laptop" olduğunu belirten metni döndürür.
    public String getCihazTuru() {
        return "Laptop";
    }

    @Override
    // IRaporIslemleri arayüzü gereği, cihazın teknik durumunu içeren detaylı bir rapor metni oluşturur.
    public String detayliRaporVer() {
        return "=== LAPTOP TEKNİK RAPORU ===\n" +
                "Marka/Model: " + getMarka() + " " + getModel() + "\n" +
                "Seri Numarası: " + getSeriNo() + "\n" +
                "Garanti Durumu: " + (garantiAktifMi() ? "Aktif" : "Süresi Dolmuş");
    }

    @Override
    // Rapor çıktılarında kullanılmak üzere seri numarasını içeren standart bir başlık sağlar.
    public String getRaporBasligi() {
        return "Cihaz Raporu: " + getSeriNo();
    }

    @Override
    // Listelerde gösterilmek üzere cihazın marka, model ve türünü içeren kısa bir özet döndürür.
    public String getOzetBilgi() {
        return getMarka() + " " + getModel() + " (" + getCihazTuru() + ")";
    }
}