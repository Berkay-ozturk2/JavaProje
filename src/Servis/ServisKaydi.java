package Servis;

import Arayuzler.Raporlanabilir;
import Cihazlar.*;
import Musteri.Musteri;
import java.io.Serializable;
import java.time.LocalDate;

public class ServisKaydi implements Serializable, Raporlanabilir {
    private static final long serialVersionUID = 3L;

    // 'final' ifadeleri kaldırılarak sonradan set edilebilir hale getirildi
    private Cihaz cihaz;
    private String sorunAciklamasi;
    private LocalDate girisTarihi;
    private double tahminiTamirUcreti;
    private ServisDurumu durum;
    private LocalDate tamamlamaTarihi;
    private Teknisyen atananTeknisyen;

    public ServisKaydi(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.durum = ServisDurumu.KABUL_EDILDI;
        this.tamamlamaTarihi = null;
        this.tahminiTamirUcreti = 0.0;
        this.atananTeknisyen = null;
    }

    // --- TXT DÖNÜŞÜM METOTLARI ---
    public String toTxtFormat() {
        String tekAd = (atananTeknisyen != null) ? atananTeknisyen.getAd() : "Yok";
        String tekUzmanlik = (atananTeknisyen != null) ? atananTeknisyen.getUzmanlikAlani() : "Yok";

        // Format: SERINO;;CIHAZ_TURU;;MARKA;;MODEL;;SORUN;;GIRIS_TARIHI;;DURUM;;TEK_AD;;TEK_UZMAN;;UCRET
        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f",
                cihaz.getSeriNo(), cihaz.getCihazTuru(), cihaz.getMarka(), cihaz.getModel(),
                sorunAciklamasi, girisTarihi.toString(), durum.toString(),
                tekAd, tekUzmanlik, tahminiTamirUcreti);
    }

    public static ServisKaydi fromTxtFormat(String line) {
        try {
            String[] p = line.split(";;");
            // Güvenlik kontrolleri
            if(p.length < 10) return null;

            String seriNo = p[0].trim();
            String tur = p[1].trim();
            String marka = p[2].trim();
            String model = p[3].trim();
            String sorun = p[4].trim();
            LocalDate giris = LocalDate.parse(p[5].trim());
            String durumStr = p[6].trim();
            String tekAd = p[7].trim();
            String tekUzmanlik = p[8].trim();

            // FİYAT DÜZELTME
            double ucret = Double.parseDouble(p[9].replace(",", ".").trim());

            Musteri dummyMusteri = new Musteri("Bilinmiyor", "", "");
            Cihaz tempCihaz = null;

            // Cihazı geçmiş tarihli oluşturuyoruz ki garanti bitmiş varsayılsın ve kayıtlı ücret geçerli olsun.
            LocalDate gecmisTarih = LocalDate.now().minusYears(10);

            if(tur.equalsIgnoreCase("Telefon"))
                tempCihaz = new Telefon(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
            else if(tur.equalsIgnoreCase("Laptop"))
                tempCihaz = new Laptop(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
            else
                tempCihaz = new Tablet(seriNo, marka, model, 0, gecmisTarih, false, dummyMusteri);

            ServisKaydi kayit = new ServisKaydi(tempCihaz, sorun);
            kayit.setGirisTarihi(giris);

            for(ServisDurumu d : ServisDurumu.values()) {
                if(d.toString().equalsIgnoreCase(durumStr)) { // equalsIgnoreCase yaptık
                    kayit.setDurum(d);
                    break;
                }
            }

            if(!tekAd.equals("Yok")) {
                kayit.setAtananTeknisyen(new Teknisyen(tekAd, tekUzmanlik));
            }
            kayit.setTahminiTamirUcreti(ucret);

            return kayit;
        } catch (Exception e) {
            System.err.println("Servis kaydı okuma hatası: " + e.getMessage());
            return null;
        }
    }

    // Getterlar
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public ServisDurumu getDurum() { return durum; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }
    public Teknisyen getAtananTeknisyen() { return atananTeknisyen; }

    public boolean isGarantiKapsaminda() {
        return girisTarihi.isBefore(cihaz.getGarantiBitisTarihi());
    }

    public double getOdenecekTamirUcreti() {
        if (isGarantiKapsaminda()) {
            return 0.0;
        } else {
            return tahminiTamirUcreti;
        }
    }

    // Setterlar
    public void setTahminiTamirUcreti(double tahminiTamirUcreti) {
        this.tahminiTamirUcreti = tahminiTamirUcreti;
    }
    public void setAtananTeknisyen(Teknisyen atananTeknisyen) {
        this.atananTeknisyen = atananTeknisyen;
    }
    public void setGirisTarihi(LocalDate girisTarihi) { // Yeni Setter
        this.girisTarihi = girisTarihi;
    }

    public void setDurum(ServisDurumu durum) {
        this.durum = durum;
        if (durum == ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = LocalDate.now();
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = null;
        }
    }
    // ... Diğer setter metodlarının yanına ekleyin ...

    // YENİ EKLENECEK METOT:
    // Txt okumalarında geçici cihaz verisini gerçek verilerle değiştirmek için gerekli.
    public void setCihaz(Cihaz cihaz) {
        this.cihaz = cihaz;
    }

    @Override
    public String toString() {
        String garantiDurumu = isGarantiKapsaminda() ? "EVET" : "HAYIR";
        String teknisyenAd = atananTeknisyen != null ? atananTeknisyen.getAd() : "Yok";
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Durum: %s",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, durum.toString());
    }
    @Override
    public String detayliRaporVer() {
        return "=== SERVİS DETAY RAPORU ===\n" +
                "Cihaz: " + cihaz.getMarka() + " " + cihaz.getModel() + "\n" +
                "Sorun: " + sorunAciklamasi + "\n" +
                "Durum: " + durum + "\n" +
                "Tahmini Ücret: " + tahminiTamirUcreti + " TL\n" +
                "Teknisyen: " + (atananTeknisyen != null ? atananTeknisyen.getAd() : "Atanmadı");
    }
}