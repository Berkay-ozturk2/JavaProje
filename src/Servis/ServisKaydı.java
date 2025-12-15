package Servis;

import Cihazlar.Cihaz;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Servis kaydı nesnesini temsil eder ve dosyaya yazılabilmesi için Serializable arayüzünü uygular.
 */
public class ServisKaydı implements Serializable {

    // Serileştirme uyumluluğu için sabit bir sürüm kimliği (ID) eklenmesi önerilir.
    private static final long serialVersionUID = 1L;

    private final Cihaz cihaz;
    private final String sorunAciklamasi;
    private final LocalDate girisTarihi;
    private String durum; // Örneğin: "Kabul Edildi", "İncelemede", "Tamamlandı"
    private LocalDate tamamlamaTarihi;

    public ServisKaydı(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.durum = "Kabul Edildi";
        this.tamamlamaTarihi = null;
    }

    // Getter Metotları
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public String getDurum() { return durum; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }

    // Durum Güncelleme Metodu
    public void setDurum(String durum) {
        this.durum = durum;
        if (durum.equals("Tamamlandı")) {
            this.tamamlamaTarihi = LocalDate.now();
        }
    }

    @Override
    public String toString() {
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Giriş: %s, Durum: %s",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, girisTarihi, durum);
    }
}