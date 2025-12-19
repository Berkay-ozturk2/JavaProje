package Cihazlar;

import java.time.LocalDate;
import java.util.Random;
import Musteri.Musteri;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Cihaz {
    // 'serialVersionUID' kaldırıldı

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

    public abstract String toTxtFormat();

    public static Cihaz fromTxtFormat(String line) {
        try {
            String[] parcalar = line.split(";;");

            if (parcalar.length < 10) {
                return null;
            }

            String tur = parcalar[0].trim();
            String seriNo = parcalar[1].trim();
            String marka = parcalar[2].trim();
            String model = parcalar[3].trim();
            double fiyat = Double.parseDouble(parcalar[4].trim().replace(",", "."));
            LocalDate tarih = LocalDate.parse(parcalar[5].trim());
            int ekstraSure = Integer.parseInt(parcalar[6].trim());
            Musteri musteri = new Musteri(parcalar[7].trim(), parcalar[8].trim(), parcalar[9].trim());

            Cihaz cihaz = null;
            if (tur.equalsIgnoreCase("Telefon")) {
                cihaz = new Telefon(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Laptop")) {
                cihaz = new Laptop(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Tablet")) {
                boolean kalem = false;
                if (parcalar.length > 10) {
                    kalem = Boolean.parseBoolean(parcalar[10].trim());
                }
                cihaz = new Tablet(seriNo, marka, model, fiyat, tarih, kalem, musteri);
            }

            if (cihaz != null && ekstraSure > 0) {
                cihaz.garantiUzat(ekstraSure);
            }
            return cihaz;
        } catch (Exception e) {
            System.err.println("Cihaz okuma hatası (Satır: " + line + ") -> " + e.getMessage());
            return null;
        }
    }

    // --- YENİ EKLENEN MERKEZİ DOSYA OKUMA METODU ---
    public static List<Cihaz> verileriYukle(String dosyaAdi) {
        List<Cihaz> liste = new ArrayList<>();
        File dosya = new File(dosyaAdi);
        if (!dosya.exists()) return liste;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosya), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Cihaz c = fromTxtFormat(line);
                    if (c != null) liste.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        return liste;
    }
    // -----------------------------------------------

    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public LocalDate getGarantiBaslangic() { return garantiBaslangic; }
    public Musteri getSahip() { return sahip; }
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