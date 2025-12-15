package Garantiler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Gün farkını hesaplamak için gerekli

public abstract class Garanti {

    // ENCAPSULATION
    private LocalDate baslangicTarihi;
    private int sureYil; // garanti süresi (yıl)

    // CONSTRUCTOR 1: Belirli bir başlangıç tarihi ile
    public Garanti(LocalDate baslangicTarihi, int sureYil) {
        this.baslangicTarihi = baslangicTarihi;
        this.sureYil = sureYil;
    }

    // CONSTRUCTOR 2: Başlangıç tarihi verilmezse "Bugün" kabul edilir
    public Garanti(int sureYil) {
        this(LocalDate.now(), sureYil);
    }

    public abstract double servisUcretiHesapla(double cihazFiyati, boolean garantiAktifMi);
    public abstract double garantiUcretiHesapla(double cihazFiyati);
    public abstract String garantiTuru();

    // CONCRETE METHOD: Bitiş tarihini hesaplar
    public LocalDate bitisTarihiHesapla() {
        return baslangicTarihi.plusYears(sureYil);
    }

    // Bugünden itibaren kalan gün sayısını hesaplar
    public long getKalanGunSayisi() {
        LocalDate bugun = LocalDate.now();
        LocalDate bitis = bitisTarihiHesapla();

        // Bugün ile bitiş tarihi arasındaki gün farkını al
        return ChronoUnit.DAYS.between(bugun, bitis);
    }

    // GETTER - SETTER
    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public int getSureYil() {
        return sureYil;
    }

    public void setSureYil(int sureYil) {
        if (sureYil <= 0) {
            throw new IllegalArgumentException("Garanti süresi 0 veya negatif olamaz.");
        }
        this.sureYil = sureYil;
    }
}