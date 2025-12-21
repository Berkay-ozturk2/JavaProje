package Servis;

import Arayuzler.IRaporIslemleri;
import Cihazlar.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Servisteki her bir işlem kaydını temsil eden ve raporlama yeteneği olan sınıf
public class ServisKaydi implements IRaporIslemleri {

    private Cihaz cihaz;                // Tamir edilecek cihaz nesnesi
    private String sorunAciklamasi;     // Müşterinin beyan ettiği sorun
    private LocalDate girisTarihi;      // Servise kayıt tarihi
    private LocalDateTime islemZamani;  // İşlem saati (Loglama için)
    private double tahminiTamirUcreti;  // Hesaplanan ücret
    private ServisDurumu durum;         // Cihazın durumu (Kabul Edildi / Tamamlandı)
    private LocalDate tamamlamaTarihi;  // Tamir bittiyse bitiş tarihi
    private Teknisyen atananTeknisyen;  // Bu işi hangi usta yapıyor

    // Yeni bir kayıt açılırken sadece cihazı ve sorunu alıyoruz, gerisini otomatik dolduruyoruz
    public ServisKaydi(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();      // Tarih otomatik olarak "bugün" ayarlanır
        this.islemZamani = LocalDateTime.now();  // Saat şu anki saat olur
        this.durum = ServisDurumu.KABUL_EDILDI;  // İlk durumu varsayılan olarak ayarlanır
        this.tamamlamaTarihi = null;             // Henüz bitmediği için null
        this.tahminiTamirUcreti = 0.0;
        this.atananTeknisyen = null;             // Henüz bir usta atanmadı
    }

    // --- KALDIRILAN METOTLAR ---
    // toTxtFormat ve fromTxtFormat -> Araclar.Formatlayici sınıfına taşındı.

    // Getter ve Setterlar (Verilere erişmek ve değiştirmek için)
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public ServisDurumu getDurum() { return durum; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }
    public Teknisyen getAtananTeknisyen() { return atananTeknisyen; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }
    public double getOdenecekTamirUcreti() { return tahminiTamirUcreti; } // Ödeme ekranı için isim değişikliği

    public void setTahminiTamirUcreti(double tahminiTamirUcreti) { this.tahminiTamirUcreti = tahminiTamirUcreti; }
    public void setAtananTeknisyen(Teknisyen atananTeknisyen) { this.atananTeknisyen = atananTeknisyen; }
    public void setGirisTarihi(LocalDate girisTarihi) { this.girisTarihi = girisTarihi; }

    // Formatlayici'nın dosyadan okurken tarihi set edebilmesi için gerekli
    public void setTamamlamaTarihi(LocalDate tamamlamaTarihi) { this.tamamlamaTarihi = tamamlamaTarihi; }

    // Durumu güncellerken ek mantık: Eğer "Tamamlandı" seçilirse bitiş tarihini otomatik atar
    public void setDurum(ServisDurumu durum) {
        this.durum = durum;
        if (durum == ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = LocalDate.now(); // Bugün bitti olarak işaretle
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = null; // Yanlışlıkla tamamlandı dendiyse tarihi geri al
        }
    }

    public void setCihaz(Cihaz cihaz) { this.cihaz = cihaz; }

    // --- INTERFACE METOTLARI (IRaporIslemleri'nden gelen zorunlu metotlar) ---
    @Override
    public String detayliRaporVer() {
        return "=== SERVİS DETAY RAPORU ===\n" +
                "Cihaz: " + cihaz.getMarka() + " " + cihaz.getModel() + "\n" +
                "Sorun: " + sorunAciklamasi + "\n" +
                "Durum: " + durum + "\n" +
                "Tahmini/Ödenecek Ücret: " + tahminiTamirUcreti + " TL\n" +
                "Tamamlanma/Teslim Tarihi: " + (tamamlamaTarihi != null ? tamamlamaTarihi : "Devam Ediyor") + "\n" +
                "Teknisyen: " + (atananTeknisyen != null ? atananTeknisyen.toString() : "Atanmadı");
    }

    @Override
    public String getRaporBasligi() {
        return "Servis Kaydı #" + cihaz.getSeriNo(); // Rapor başlığı olarak seri numarasını kullanıyoruz
    }

    @Override
    public String getOzetBilgi() {
        return cihaz.getModel() + " - " + durum; // Listelerde görünmesi için kısa özet
    }

    // Nesneyi ekrana yazdırdığımızda (System.out.println) anlamlı bir metin çıksın diye ezdik
    @Override
    public String toString() {
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Durum: %s",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, durum.toString());
    }
}