package Cihazlar;

import java.time.LocalDate;
import java.util.Random;
import Musteri.Musteri;
import Garantiler.*;
import Istisnalar.GecersizDegerException; // EKLENDİ

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Cihaz {
    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private Musteri sahip;

    protected Garanti garanti;
    private static final Random rand = new Random();

    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic, Musteri sahip) {
        this.seriNo = seriNo;
        this.marka = marka;
        this.model = model;
        this.fiyat = fiyat;
        this.sahip = sahip;

        LocalDate baslangic;
        if (garantiBaslangic == null) {
            baslangic = randomGarantiBaslangic(getGarantiSuresiYil());
        } else {
            baslangic = garantiBaslangic;
        }
        this.garanti = new StandartGaranti(baslangic, getGarantiSuresiYil());
    }

    private LocalDate randomGarantiBaslangic(int sureYil) {
        int maxRandomDays = (sureYil + 1) * 365;
        int randomDaysInPast = rand.nextInt(maxRandomDays + 1);
        return LocalDate.now().minusDays(randomDaysInPast);
    }

    public abstract String toTxtFormat();

    // ... (Önceki importlar ve kodlar aynı)

    public static Cihaz fromTxtFormat(String line) {
        try {
            String[] parcalar = line.split(";;");
            if (parcalar.length < 10) return null;

            String tur = parcalar[0].trim();
            String seriNo = parcalar[1].trim();
            String marka = parcalar[2].trim();
            String model = parcalar[3].trim();
            double fiyat = Double.parseDouble(parcalar[4].trim().replace(",", "."));
            LocalDate tarih = LocalDate.parse(parcalar[5].trim());
            int ekstraSure = Integer.parseInt(parcalar[6].trim());

            // Müşteriyi oluştur
            Musteri musteri = new Musteri(parcalar[7].trim(), parcalar[8].trim(), parcalar[9].trim());

            // --- YENİ: VIP OKUMA MANTIĞI (Geriye dönük uyumluluk ile) ---
            boolean isVip = false;
            boolean kalem = false; // Tablet için

            if (tur.equalsIgnoreCase("Tablet")) {
                // Tablet formatı eskiden 11 parçaydı (sonuncusu kalem), şimdi 12 olacak (VIP + Kalem)
                if (parcalar.length > 11) {
                    // Yeni Format: ...;;Tel;;VIP;;Kalem
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                    kalem = Boolean.parseBoolean(parcalar[11].trim());
                } else if (parcalar.length == 11) {
                    // Eski Format: ...;;Tel;;Kalem
                    kalem = Boolean.parseBoolean(parcalar[10].trim());
                }
            } else {
                // Laptop ve Telefon
                if (parcalar.length > 10) {
                    // Yeni Format: ...;;Tel;;VIP
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                }
            }

            musteri.setVip(isVip);
            // -----------------------------------------------------------

            Cihaz cihaz = null;
            if (tur.equalsIgnoreCase("Telefon")) {
                cihaz = new Telefon(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Laptop")) {
                cihaz = new Laptop(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Tablet")) {
                cihaz = new Tablet(seriNo, marka, model, fiyat, tarih, kalem, musteri);
            }

            if (cihaz != null && ekstraSure > 0) {
                cihaz.garantiUzat(ekstraSure);
            }
            return cihaz;
        } catch (Exception e) {
            System.err.println("Cihaz okuma hatası: " + e.getMessage());
            return null;
        }
    }
    // ... (Geri kalan kodlar aynı)

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

    // --- GETTER METOTLARI ---
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public Musteri getSahip() { return sahip; }
    public Garanti getGaranti() { return garanti; }
    public LocalDate getGarantiBaslangic() { return garanti.getBaslangicTarihi(); }
    public LocalDate getGarantiBitisTarihi() { return garanti.getBitisTarihi(); }
    public boolean isGarantiAktif() { return garanti.isDevamEdiyor(); }

    // --- EKLENEN SETTER METOTLARI (Gereksinim: Validasyon ve Exception) ---

    // Setter 1: Fiyat kontrolü
    public void setFiyat(double fiyat) throws GecersizDegerException {
        if (fiyat < 0) {
            throw new GecersizDegerException("Fiyat negatif olamaz!");
        }
        this.fiyat = fiyat;
    }

    // Setter 2: Marka boş olamaz kontrolü
    public void setMarka(String marka) throws GecersizDegerException {
        if (marka == null || marka.trim().isEmpty()) {
            throw new GecersizDegerException("Marka alanı boş bırakılamaz.");
        }
        this.marka = marka;
    }

    // Setter 3: Model boş olamaz kontrolü
    public void setModel(String model) throws GecersizDegerException {
        if (model == null || model.trim().isEmpty()) {
            throw new GecersizDegerException("Model alanı boş bırakılamaz.");
        }
        this.model = model;
    }

    public int getEkstraGarantiSuresiAy() {
        long standartBitisEpoch = garanti.getBaslangicTarihi().plusYears(getGarantiSuresiYil()).toEpochDay();
        long guncelBitisEpoch = garanti.getBitisTarihi().toEpochDay();
        if (guncelBitisEpoch > standartBitisEpoch) {
            long farkGun = guncelBitisEpoch - standartBitisEpoch;
            return (int) (farkGun / 30);
        }
        return 0;
    }

    public void garantiUzat(int ay) {
        this.garanti.sureUzat(ay);
        if (this.garanti instanceof StandartGaranti) {
            int toplamEkstraSure = getEkstraGarantiSuresiAy();
            this.garanti = new UzatilmisGaranti(
                    this.garanti.getBaslangicTarihi(),
                    getGarantiSuresiYil(),
                    toplamEkstraSure
            );
        }
    }

    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();

    @Override
    public String toString() {
        String garantiDurumu = isGarantiAktif() ? "Aktif" : "Sona Ermiş";
        String ekstraBilgi = getEkstraGarantiSuresiAy() > 0 ? " (+" + getEkstraGarantiSuresiAy() + " Ay Uzatıldı)" : "";
        return String.format("%s [%s - %s] (Seri No: %s) - Garanti: %s%s",
                getCihazTuru(), marka, model, seriNo, garantiDurumu, ekstraBilgi);
    }
}