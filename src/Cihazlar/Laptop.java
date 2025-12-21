package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Arayuzler.Raporlanabilir; // Mevcut interface import edildi

// GEREKSİNİM: Hem kalıtım (extends) hem arayüz (implements) bir arada kullanıldı
public class Laptop extends Cihaz implements Raporlanabilir {

    public Laptop(String seriNo, String marka, String model, double fiyat,
                  LocalDate garantiBaslangic, Musteri sahip) {
        super(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
    }

    @Override
    public int getGarantiSuresiYil() {
        return 3;
    }

    @Override
    public String getCihazTuru() {
        return "Laptop";
    }

    // --- Raporlanabilir Arayüzünden Gelen Metotlar ---

    @Override
    public String detayliRaporVer() {
        return "=== LAPTOP TEKNİK RAPORU ===\n" +
                "Marka/Model: " + getMarka() + " " + getModel() + "\n" +
                "Seri Numarası: " + getSeriNo() + "\n" +
                "Garanti Durumu: " + (isGarantiAktif() ? "Aktif" : "Süresi Dolmuş");
    }

    @Override
    public String getRaporBasligi() {
        return "Cihaz Raporu: " + getSeriNo();
    }

    @Override
    public String getOzetBilgi() {
        return getMarka() + " " + getModel() + " (" + getCihazTuru() + ")";
    }
}