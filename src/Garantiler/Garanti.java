package Garantiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Tüm garanti türleri (Standart, Ekstra vb.) için temel (base) soyut sınıf.
 * Garanti süreleri, tarih hesaplamaları ve maliyet şablonlarını yönetir.
 */
public abstract class Garanti {

    // Garantinin başladığı ve biteceği tarihleri tutan alanlar.
    protected LocalDate baslangicTarihi;
    protected LocalDate bitisTarihi;

    // Garanti constructor
    public Garanti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        this.baslangicTarihi = baslangicTarihi;
        // Verilen standart yıl ve ekstra ay bilgisini toplayarak kesin bitiş tarihini hesaplar.
        this.bitisTarihi = baslangicTarihi.plusYears(sureYil).plusMonths(ekstraAy);
    }

    // Test verisi oluşturmak için geçmişe dönük rastgele bir başlangıç tarihi üretir.
    public static LocalDate rastgeleBaslangicOlustur(int sureYil) {
        Random rand = new Random();

        // Garanti süresinin biraz fazlası kadar gün aralığı belirler.
        int maxRandomDays = (sureYil + 1) * 365;

        // Belirlenen aralıkta rastgele bir gün sayısı seçer.
        int randomDays = rand.nextInt(maxRandomDays + 1);

        // Simülasyon gereği bugünden geçmişe giderek başlangıç tarihini döndürür.
        return LocalDate.now().minusDays(randomDays);
    }

    /* Mevcut garantinin, standart bir garanti süresine göre ne kadar uzatıldığını hesaplar. */
    public int hesaplaEkstraSureAy(int standartSureYil) {
        // Standart sürenin bitiş tarihini gün (epoch) cinsinden hesaplar.
        long standartBitisEpoch = baslangicTarihi.plusYears(standartSureYil).toEpochDay();

        // Mevcut (uzatılmış olabilir) bitiş tarihini gün cinsinden hesaplar.
        long guncelBitisEpoch = bitisTarihi.toEpochDay();

        // Eğer güncel tarih standart tarihi geçiyorsa aradaki farkı aya çevirir.
        if (guncelBitisEpoch > standartBitisEpoch) {
            long farkGun = guncelBitisEpoch - standartBitisEpoch;
            return (int) (farkGun / 30);
        }
        // Ekstra süre yoksa 0 döndürür.
        return 0;
    }

    // Garantinin şu anki tarih itibarıyla geçerli olup olmadığını kontrol eder.
    public boolean devamEdiyorMu() {
        // Bugünün tarihi, bitiş tarihinden önceyse veya ona eşitse garanti aktiftir.
        return LocalDate.now().isBefore(bitisTarihi) || LocalDate.now().isEqual(bitisTarihi);
    }

    // --- SOYUT METOTLAR (Alt sınıflar bunları ezmek/implement etmek zorundadır) ---

    // Garantinin türüne göre (Standart/Uzatılmış) indirimli veya tam maliyeti hesaplar.
    public abstract double sonMaliyetHesapla(double hamUcret);

    // Garantinin türünü ayırt etmek için metin etiketi döndürür.
    public abstract String garantiTuru();


    // GETTER VE YARDIMCI METOTLAR
    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }

    // Garantinin bitmesine tam olarak kaç gün kaldığını hesaplar.
    public long getKalanGunSayisi() {
        // İki tarih arasındaki gün farkını (ChronoUnit.DAYS) kullanarak bulur.
        return ChronoUnit.DAYS.between(LocalDate.now(), bitisTarihi);
    }

    // Garantinin bitiş tarihini belirtilen ay kadar ileri atar.
    public void sureUzat(int ay) {
        // Mevcut bitiş tarihine ay ekleyerek tarihi günceller.
        this.bitisTarihi = this.bitisTarihi.plusMonths(ay);
    }
}