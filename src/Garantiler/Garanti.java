package Garantiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Garanti {

    protected LocalDate baslangicTarihi;
    protected LocalDate bitisTarihi;

    // Constructor: Standart ve Uzatılmış garanti için ortak yapı
    public Garanti(LocalDate baslangicTarihi, int sureYil, int ekstraAy) {
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = baslangicTarihi.plusYears(sureYil).plusMonths(ekstraAy);
    }

    // Garanti devam ediyor mu kontrolü
    public boolean isDevamEdiyor() {
        return LocalDate.now().isBefore(bitisTarihi) || LocalDate.now().isEqual(bitisTarihi);
    }

    // Main sınıfında kullanılan temel maliyet hesaplama metodu (KALACAK)
    public abstract double sonMaliyetHesapla(double hamUcret);

    public abstract String garantiTuru();

    // Getter ve Yardımcı Metotlar
    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }

    public long getKalanGunSayisi() {
        return ChronoUnit.DAYS.between(LocalDate.now(), bitisTarihi);
    }

    // Süre uzatma metodu
    public void sureUzat(int ay) {
        this.bitisTarihi = this.bitisTarihi.plusMonths(ay);
    }

    // SİLİNEN METOTLAR: garantiUcretiHesapla, servisUcretiHesapla
}