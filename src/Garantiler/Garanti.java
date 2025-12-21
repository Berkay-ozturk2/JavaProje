package Garantiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Tüm garanti türleri (Standart, Ekstra vb.) için temel (base) soyut sınıf.
 * Garanti süreleri, tarih hesaplamaları ve maliyet şablonlarını yönetir.
 */
public abstract class Garanti {

    // Garantinin başladığı ve biteceği tarihleri tutan alanlar
    protected LocalDate baslangicTarihi;
    protected LocalDate bitisTarihi;

    //Garanti constructor
    public Garanti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        this.baslangicTarihi = baslangicTarihi;
        // Bitiş tarihi hesaplaması: Başlangıç + Yıl + Ekstra Ay
        this.bitisTarihi = baslangicTarihi.plusYears(sureYil).plusMonths(ekstraAy);
    }

    //Test verisi oluşturmak için geçmişe dönük rastgele bir başlangıç tarihi üretir.
    public static LocalDate rastgeleBaslangicOlustur(int sureYil) {
        Random rand = new Random();
        // Verilen yıl kadar günü hesaplar
        int maxRandomDays = (sureYil + 1) * 365;
        // 0 ile maxRandomDays arasında rastgele bir gün sayısı seçer
        int randomDays = rand.nextInt(maxRandomDays + 1);

        // Bugünden o kadar gün geriye giderek tarihi döndürür
        return LocalDate.now().minusDays(randomDays);
    }

    /* Mevcut garantinin, standart bir garanti süresine göre ne kadar uzatıldığını hesaplar.
    (Örn: Standart 2 yıl ise ama garanti 2 yıl 6 ay sürüyorsa, 6 döner).
    Epoch = 1 Ocak 1970 tarihinden bugüne kadar sayar*/
    public int hesaplaEkstraSureAy(int standartSureYil) {
        // Standart sürenin biteceği tarihi epoch (gün sayısı) cinsinden bulur
        long standartBitisEpoch = baslangicTarihi.plusYears(standartSureYil).toEpochDay();
        // Mevcut garantinin biteceği tarihi epoch cinsinden bulur
        long guncelBitisEpoch = bitisTarihi.toEpochDay();

        // Eğer mevcut garanti, standart süreden daha uzunsa farkı hesapla
        if (guncelBitisEpoch > standartBitisEpoch) {
            long farkGun = guncelBitisEpoch - standartBitisEpoch;
            // Gün farkını aya çevir
            return (int) (farkGun / 30);
        }
        return 0; // Ekstra süre yoksa 0 döner
    }

    //Garantinin şu anki tarih itibarıyla geçerli olup olmadığını kontrol eder.
    public boolean devamEdiyorMu() {
        // Şu anki tarih, bitiş tarihinden önceyse veya eşitse garanti devam ediyordur.
        return LocalDate.now().isBefore(bitisTarihi) || LocalDate.now().isEqual(bitisTarihi);
    }

    // --- SOYUT METOTLAR (Alt sınıflar bunları ezmek/implement etmek zorundadır) ---


    //Garantinin türüne göre son maliyeti hesaplar.
    public abstract double sonMaliyetHesapla(double hamUcret);

    //Garantinin türünü metin olarak döndürür (Örn: "Standart", "Ekstra").
    public abstract String garantiTuru();


    //GETTER VE YARDIMCI METOTLAR
    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }


    //Garantinin bitmesine kaç gün kaldığını hesaplar.
    public long getKalanGunSayisi() {
        return ChronoUnit.DAYS.between(LocalDate.now(), bitisTarihi);
    }


    //Garantinin bitiş tarihini belirtilen ay kadar ileri atar.
    public void sureUzat(int ay) {
        this.bitisTarihi = this.bitisTarihi.plusMonths(ay);
    }
}