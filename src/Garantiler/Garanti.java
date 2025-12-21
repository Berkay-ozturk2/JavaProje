package Garantiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random; // Random import edildi

public abstract class Garanti {

    protected LocalDate baslangicTarihi;
    protected LocalDate bitisTarihi;

    // Constructor
    public Garanti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = baslangicTarihi.plusYears(sureYil).plusMonths(ekstraAy);
    }

    // --- YENİ EKLENEN STATİK METOT ---
    // Bu metot Cihaz sınıfından taşındı. Artık Garanti sınıfı kendi rastgele tarihini üretebilir.
    public static LocalDate rastgeleBaslangicOlustur(int sureYil) {
        Random rand = new Random();
        int maxRandomDays = (sureYil + 1) * 365;
        int randomDaysInPast = rand.nextInt(maxRandomDays + 1);
        return LocalDate.now().minusDays(randomDaysInPast);
    }

    // --- YENİ EKLENEN YARDIMCI METOT ---
    // Cihaz sınıfındaki matematiksel hesaplamayı buraya alarak mantığı merkezileştiriyoruz.
    public int hesaplaEkstraSureAy(int standartSureYil) {
        long standartBitisEpoch = baslangicTarihi.plusYears(standartSureYil).toEpochDay();
        long guncelBitisEpoch = bitisTarihi.toEpochDay();

        if (guncelBitisEpoch > standartBitisEpoch) {
            long farkGun = guncelBitisEpoch - standartBitisEpoch;
            return (int) (farkGun / 30);
        }
        return 0;
    }

    // Garanti devam ediyor mu kontrolü
    public boolean devamEdiyorMu() {
        return LocalDate.now().isBefore(bitisTarihi) || LocalDate.now().isEqual(bitisTarihi);
    }

    public abstract double sonMaliyetHesapla(double hamUcret);
    public abstract String garantiTuru();

    // Getter ve Yardımcı Metotlar
    public LocalDate getBaslangicTarihi() { return baslangicTarihi; }
    public LocalDate getBitisTarihi() { return bitisTarihi; }

    public long getKalanGunSayisi() {
        return ChronoUnit.DAYS.between(LocalDate.now(), bitisTarihi);
    }

    public void sureUzat(int ay) {
        this.bitisTarihi = this.bitisTarihi.plusMonths(ay);
    }
}