// src/Cihazlar/Cihaz.java (GÜNCELLENDİ)
package Cihazlar;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

import Musteri.Musteri; // YENİ IMPORT

public abstract class Cihaz implements Serializable {
    private String seriNo;
    private String marka;
    private String model;

    private LocalDate garantiBaslangic;
    private static final Random RND = new Random();
    private Musteri sahip; // YENİ ALAN: Cihazın sahibi

    // CONSTRUCTOR GÜNCELLENDİ
    public Cihaz(String seriNo, String marka, String model, LocalDate garantiBaslangic, Musteri sahip) {
        this.seriNo = seriNo;
        this.marka = marka;
        this.model = model;
        this.sahip = sahip; // Sahip bilgisi atandı

        // Garanti başlangıç tarihi parametresini dikkate almadan,
        // cihazın garantisinin bitmiş olma ihtimalini simüle etmek için rastgele oluştur.
        this.garantiBaslangic = generateRandomGarantiBaslangic(getGarantiSuresiYil());
    }

    /**
     * Cihazın garanti süresinin bitmiş olma ihtimalini simüle etmek için
     * rastgele bir başlangıç tarihi üretir.
     */
    private LocalDate generateRandomGarantiBaslangic(int sureYil) {
        // Garanti süresinden 1 yıl fazlasına kadar geçmiş bir tarih belirleyerek
        // garantinin bitmiş olma veya bitmeye yakın olma ihtimalini simüle eder.
        int maxRandomDays = (sureYil + 1) * 365;

        // Şu andan itibaren 0 gün ile maxRandomDays gün arası rastgele bir gün seç
        int randomDaysInPast = RND.nextInt(maxRandomDays + 1);

        return LocalDate.now().minusDays(randomDaysInPast);
    }

    // Encapsulation
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }

    public LocalDate getGarantiBaslangic() { return garantiBaslangic; }
    public Musteri getSahip() { return sahip; } // YENİ GETTER


    // Abstract methods (polymorphism)
    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();


    // Concrete method
    public LocalDate getGarantiBitisTarihi() {
        return garantiBaslangic.plusYears(getGarantiSuresiYil());
    }

    /**
     * Garanti süresinin aktif olup olmadığını kontrol eder.
     * @return boolean
     */
    public boolean isGarantiAktif() {
        return LocalDate.now().isBefore(getGarantiBitisTarihi());
    }

    //
    @Override
    public String toString() {
        // Garanti durumunu da ekle
        String garantiDurumu = isGarantiAktif() ? "Aktif" : "Sona Ermiş";
        return String.format("%s [%s - %s] (%s) - Sahibi: %s - Garanti: %s", getCihazTuru(), marka, model, seriNo, sahip.toString(), garantiDurumu);
    }
}