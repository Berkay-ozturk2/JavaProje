package Garantiler;

import java.time.LocalDate;

public abstract class Garanti {

    // ENCAPSULATION
    private LocalDate baslangicTarihi;
    private int sureYil; // garanti süresi (yıl)

    // CONSTRUCTOR 1
    public Garanti(LocalDate baslangicTarihi, int sureYil) {
        this.baslangicTarihi = baslangicTarihi;
        this.sureYil = sureYil;
    }

    // CONSTRUCTOR 2 (Overloading)
    public Garanti(int sureYil) {
        this(LocalDate.now(), sureYil);
    }

    // ABSTRACT METHODLAR
    public abstract double ucretHesapla(double cihazFiyati);
    public abstract String garantiTuru();

    // CONCRETE METHODLAR
    public LocalDate bitisTarihiHesapla() {
        return baslangicTarihi.plusYears(sureYil);
    }

    public boolean garantiAktifMi() {
        return LocalDate.now().isBefore(bitisTarihiHesapla());
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

