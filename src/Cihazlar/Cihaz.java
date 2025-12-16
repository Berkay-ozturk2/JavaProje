package Cihazlar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;
import Musteri.Musteri;

public abstract class Cihaz implements Serializable {
    private static final long serialVersionUID = 20240520L;

    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private LocalDate garantiBaslangic;
    private static final Random rand = new Random();
    private Musteri sahip;

    // Sonradan satın alınan ek garanti süresi (Ay cinsinden)
    private int ekstraGarantiSuresiAy = 0;

    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic, Musteri sahip) {
        this.seriNo = seriNo;
        this.marka = marka;
        this.model = model;
        this.fiyat = fiyat;
        this.sahip = sahip;

        // Eğer garantiBaslangic null gelirse (txt okumalarında gelmez ama tedbir)
        if (garantiBaslangic == null) {
            this.garantiBaslangic = randomGarantiBaslangic(getGarantiSuresiYil());
        } else {
            this.garantiBaslangic = garantiBaslangic;
        }
    }

    private LocalDate randomGarantiBaslangic(int sureYil) {
        int maxRandomDays = (sureYil + 1) * 365;
        int randomDaysInPast = rand.nextInt(maxRandomDays + 1);
        return LocalDate.now().minusDays(randomDaysInPast);
    }

    // --- TXT DÖNÜŞÜM METOTLARI (YENİ) ---
    public abstract String toTxtFormat();

    public static Cihaz fromTxtFormat(String line) {
        try {
            String[] parcalar = line.split(";;");
            // Beklenen: TUR;;SERINO;;MARKA;;MODEL;;FIYAT;;TARIH;;EKSTRA_SURE;;MUSTERI_AD;;MUSTERI_SOYAD;;MUSTERI_TEL;;[OZEL]

            String tur = parcalar[0];
            String seriNo = parcalar[1];
            String marka = parcalar[2];
            String model = parcalar[3];
            double fiyat = Double.parseDouble(parcalar[4]);
            LocalDate tarih = LocalDate.parse(parcalar[5]);
            int ekstraSure = Integer.parseInt(parcalar[6]);

            Musteri musteri = new Musteri(parcalar[7], parcalar[8], parcalar[9]);

            Cihaz cihaz = null;
            if (tur.equals("Telefon")) {
                cihaz = new Telefon(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equals("Laptop")) {
                cihaz = new Laptop(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equals("Tablet")) {
                boolean kalem = Boolean.parseBoolean(parcalar[10]);
                cihaz = new Tablet(seriNo, marka, model, fiyat, tarih, kalem, musteri);
            }

            if (cihaz != null && ekstraSure > 0) {
                cihaz.garantiUzat(ekstraSure);
            }
            return cihaz;
        } catch (Exception e) {
            System.err.println("Cihaz ayrıştırma hatası: " + line);
            return null;
        }
    }

    // Getter Metotları
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public LocalDate getGarantiBaslangic() { return garantiBaslangic; }
    public Musteri getSahip() { return sahip; }

    // YENİ GETTER (Txt kaydı için gerekli)
    public int getEkstraGarantiSuresiAy() { return ekstraGarantiSuresiAy; }

    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();

    public LocalDate getGarantiBitisTarihi() {
        return garantiBaslangic
                .plusYears(getGarantiSuresiYil())
                .plusMonths(ekstraGarantiSuresiAy);
    }

    public boolean isGarantiAktif() {
        return LocalDate.now().isBefore(getGarantiBitisTarihi());
    }

    public void garantiUzat(int ay) {
        this.ekstraGarantiSuresiAy += ay;
    }

    @Override
    public String toString() {
        String garantiDurumu = isGarantiAktif() ? "Aktif" : "Sona Ermiş";
        String ekstraBilgi = ekstraGarantiSuresiAy > 0 ? " (+" + ekstraGarantiSuresiAy + " Ay Uzatıldı)" : "";

        return String.format("%s [%s - %s] (Seri No: %s, Fiyat: %.2f TL) - Garanti: %s%s",
                getCihazTuru(), marka, model, seriNo, fiyat, garantiDurumu, ekstraBilgi);
    }
}