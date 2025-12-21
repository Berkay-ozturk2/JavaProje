package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Arayuzler.IRaporIslemleri;
import Istisnalar.GecersizDegerException; // Eklendi

public class Laptop extends Cihaz implements IRaporIslemleri {

    //Laptop constructor
    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) throws GecersizDegerException {
        //Bilgileri Cihaz Ata sınıfına gönderir
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    //Laptop garanti süresi 3 yıl
    public int getGarantiSuresiYil() {
        return 3;
    }

    @Override
    public String getCihazTuru() {
        return "Laptop";
    }

    @Override
    //IRaporIslemlerini implement ettiği için detayliRaporVer metodunu override eder.
    //Listedeki Laptoplar için detaylı rapor oluşturur
    public String detayliRaporVer() {
        return "=== LAPTOP TEKNİK RAPORU ===\n" +
                "Marka/Model: " + getMarka() + " " + getModel() + "\n" +
                "Seri Numarası: " + getSeriNo() + "\n" +
                "Garanti Durumu: " + (garantiAktifMi() ? "Aktif" : "Süresi Dolmuş");
    }

    @Override
    //SeriNo'ya göre Cihaz Raporu başlığını koyar
    public String getRaporBasligi() {
        return "Cihaz Raporu: " + getSeriNo();
    }

    @Override
    //Laptop için tek satırlık özet rapor bilgisi oluşturur.
    public String getOzetBilgi() {
        return getMarka() + " " + getModel() + " (" + getCihazTuru() + ")";
    }
}