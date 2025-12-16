package Cihazlar;
//YORUM SATIRI
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;
import Musteri.Musteri;

public abstract class Cihaz implements Serializable {
    // Sınıf yapısı değiştiği için versiyon ID'si ekliyoruz
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

        // Rastgele bir geçmiş tarih oluşturma
        this.garantiBaslangic = randomGarantiBaslangic(getGarantiSuresiYil());
    }

    private LocalDate randomGarantiBaslangic(int sureYil) {
        int maxRandomDays = (sureYil + 1) * 365;
        int randomDaysInPast = rand.nextInt(maxRandomDays + 1);
        return LocalDate.now().minusDays(randomDaysInPast);
    }

    // Getter Metotları
    public String getSeriNo() {
        return seriNo;
    }
    public String getMarka() {
        return marka;
    }
    public String getModel() {
        return model;
    }
    public double getFiyat() {
        return fiyat;
    }
    public LocalDate getGarantiBaslangic() {
        return garantiBaslangic;
    }
    public Musteri getSahip() {
        return sahip;
    }

    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();

    /**
     * Garanti bitiş tarihini hesaplarken standart süreye eklenen süreyi de dahil eder.
     */
    public LocalDate getGarantiBitisTarihi() {
        return garantiBaslangic
                .plusYears(getGarantiSuresiYil())
                .plusMonths(ekstraGarantiSuresiAy);
    }

    public boolean isGarantiAktif() {
        return LocalDate.now().isBefore(getGarantiBitisTarihi());
    }

    /**
     * Garantiyi belirtilen ay kadar uzatır.
     */
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