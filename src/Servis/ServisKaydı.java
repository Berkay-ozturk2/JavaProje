// src/Servis/ServisKaydı.java (GÜNCELLENDİ)
package Servis;

import Cihazlar.Cihaz;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Servis kaydı nesnesini temsil eder ve dosyaya yazılabilmesi için Serializable arayüzünü uygular.
 */
public class ServisKaydı implements Serializable {

    // Serileştirme uyumluluğu için sabit bir sürüm kimliği (ID) eklenmesi önerilir.
    private static final long serialVersionUID = 3L; // Sürüm ID'yi güncelledik

    private final Cihaz cihaz;
    private final String sorunAciklamasi;
    private final LocalDate girisTarihi;
    private double tahminiTamirUcreti; // YENİ: Tamir için belirlenen tahmini ücret
    private ServisDurumu durum; // GÜNCELLENDİ: String yerine Enum
    private LocalDate tamamlamaTarihi;
    private Teknisyen atananTeknisyen; // YENİ ALAN: Atanan teknisyen

    // YENİ CONSTRUCTOR
    public ServisKaydı(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.durum = ServisDurumu.KABUL_EDILDI; // Enum ile başlangıç durumu
        this.tamamlamaTarihi = null;
        this.tahminiTamirUcreti = 0.0;
        this.atananTeknisyen = null;
    }

    // Getter Metotları
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public ServisDurumu getDurum() { return durum; } // GÜNCELLENDİ
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }
    public Teknisyen getAtananTeknisyen() { return atananTeknisyen; } // YENİ GETTER

    /**
     * Cihazın standart garanti kapsamında olup olmadığını kontrol eder.
     * Cihazın servise giriş tarihi, cihazın garanti bitiş tarihinden önce ise garanti kapsamındadır.
     */
    public boolean isGarantiKapsaminda() {
        // Cihaz sınıfındaki getGarantiBitisTarihi() metodu kullanılır.
        return girisTarihi.isBefore(cihaz.getGarantiBitisTarihi());
    }

    /**
     * Ödenecek tamir ücretini hesaplar.
     * Garanti kapsamında ise 0.0 TL, değilse tahmini tamir ücreti tahsil edilir.
     */
    public double getOdenecekTamirUcreti() {
        if (isGarantiKapsaminda()) {
            return 0.0;
        } else {
            return tahminiTamirUcreti;
        }
    }

    // Setter Metotları (Yeni)
    public void setTahminiTamirUcreti(double tahminiTamirUcreti) {
        if (tahminiTamirUcreti < 0) {
            throw new IllegalArgumentException("Tahmini tamir ücreti negatif olamaz.");
        }
        this.tahminiTamirUcreti = tahminiTamirUcreti;
    }

    public void setAtananTeknisyen(Teknisyen atananTeknisyen) {
        this.atananTeknisyen = atananTeknisyen;
    }


    // Durum Güncelleme Metodu
    public void setDurum(ServisDurumu durum) { // GÜNCELLENDİ: Enum kabul eder
        this.durum = durum;
        if (durum == ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = LocalDate.now();
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = null;
        }
    }

    @Override
    public String toString() {
        String garantiDurumu = isGarantiKapsaminda() ? "EVET" : "HAYIR";
        String teknisyenAd = atananTeknisyen != null ? atananTeknisyen.getAd() : "Yok";
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Giriş: %s, Durum: %s, Atanan: %s, Garanti: %s, Ödenecek Ücret: %.2f TL",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, girisTarihi, durum.toString(), teknisyenAd, garantiDurumu, getOdenecekTamirUcreti());
    }
}