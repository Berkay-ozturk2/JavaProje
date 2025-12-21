package Servis;

import Arayuzler.IRaporIslemleri;
import Cihazlar.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServisKaydi implements IRaporIslemleri {

    private Cihaz cihaz;
    private String sorunAciklamasi;
    private LocalDate girisTarihi;
    private LocalDateTime islemZamani;
    private double tahminiTamirUcreti;
    private ServisDurumu durum;
    private LocalDate tamamlamaTarihi;
    private Teknisyen atananTeknisyen;

    public ServisKaydi(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.islemZamani = LocalDateTime.now();
        this.durum = ServisDurumu.KABUL_EDILDI;
        this.tamamlamaTarihi = null;
        this.tahminiTamirUcreti = 0.0;
        this.atananTeknisyen = null;
    }

    // --- KALDIRILAN METOTLAR ---
    // toTxtFormat ve fromTxtFormat -> Araclar.Formatlayici sınıfına taşındı.

    // Getter ve Setterlar
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public ServisDurumu getDurum() { return durum; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }
    public Teknisyen getAtananTeknisyen() { return atananTeknisyen; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }
    public double getOdenecekTamirUcreti() { return tahminiTamirUcreti; }

    public void setTahminiTamirUcreti(double tahminiTamirUcreti) { this.tahminiTamirUcreti = tahminiTamirUcreti; }
    public void setAtananTeknisyen(Teknisyen atananTeknisyen) { this.atananTeknisyen = atananTeknisyen; }
    public void setGirisTarihi(LocalDate girisTarihi) { this.girisTarihi = girisTarihi; }

    // Formatlayici'nın kullanması için gerekli yeni setter
    public void setTamamlamaTarihi(LocalDate tamamlamaTarihi) { this.tamamlamaTarihi = tamamlamaTarihi; }

    public void setDurum(ServisDurumu durum) {
        this.durum = durum;
        if (durum == ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = LocalDate.now();
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = null;
        }
    }

    public void setCihaz(Cihaz cihaz) { this.cihaz = cihaz; }

    // --- INTERFACE METOTLARI ---
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
        return "Servis Kaydı #" + cihaz.getSeriNo();
    }

    @Override
    public String getOzetBilgi() {
        return cihaz.getModel() + " - " + durum;
    }

    @Override
    public String toString() {
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Durum: %s",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, durum.toString());
    }
}