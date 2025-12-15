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
        // Standart garanti başlangıç tarihi için varsayılan olarak bugün kullanılır.
        // Cihaz objesi, garanti başlangıç tarihini rastgele oluşturduğu için buradaki date değeri
        // Cihaz objesi içindeki tarih ile çakışmaz.
        this(LocalDate.now(), sureYil);
    }

    // YENİ ABSTRACT METHOD: Servis/Onarım Ücreti Hesapla
    // Garanti kapsamındaki bir servisin ücretini hesaplar.
    // Garanti aktifse 0, değilse bir ücret döndürür.
    public abstract double servisUcretiHesapla( boolean garantiAktifMi);

    // ESKİ ABSTRACT METHODUN YENİ İSMİ: Garanti Ücreti Hesapla
    // Uzatılmış garanti gibi ek ücret gerektiren durumlarda kullanılır.


    public abstract String garantiTuru();

    // CONCRETE METHODLAR
    public LocalDate bitisTarihiHesapla() {
        return baslangicTarihi.plusYears(sureYil);
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