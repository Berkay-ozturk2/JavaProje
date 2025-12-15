package Servis;

import Cihazlar.Cihaz;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Servis kaydı nesnesini temsil eder ve dosyaya yazılabilmesi için Serializable arayüzünü uygular.
 */
public class ServisKaydı implements Serializable {

    // Serileştirme uyumluluğu için sabit bir sürüm kimliği (ID) eklenmesi önerilir.
    private static final long serialVersionUID = 2L; // Sürüm ID'yi güncelledik

    private final Cihaz cihaz;
    private final String sorunAciklamasi;
    private final LocalDate girisTarihi;
    private double tahminiTamirUcreti; // YENİ: Tamir için belirlenen tahmini ücret
    private String durum; // Örneğin: "Kabul Edildi", "İncelemede", "Tamamlandı"
    private LocalDate tamamlamaTarihi;

    // YENİ CONSTRUCTOR
    public ServisKaydı(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.durum = "Kabul Edildi";
        this.tamamlamaTarihi = null;
        this.tahminiTamirUcreti = tahminiTamirUcreti;
    }

    // Getter Metotları
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public String getDurum() { return durum; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }

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

    // Durum Güncelleme Metodu
    public void setDurum(String durum) {
        this.durum = durum;
        if (durum.equals("Tamamlandı")) {
            this.tamamlamaTarihi = LocalDate.now();
        }
    }

    @Override
    public String toString() {
        String garantiDurumu = isGarantiKapsaminda() ? "EVET" : "HAYIR";
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Giriş: %s, Durum: %s, Garanti: %s, Ödenecek Ücret: %.2f TL",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, girisTarihi, durum, garantiDurumu, getOdenecekTamirUcreti());
    }
}