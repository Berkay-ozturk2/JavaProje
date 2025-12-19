package Servis;

import Arayuzler.Raporlanabilir;
import Cihazlar.*;
import Musteri.Musteri;
import java.time.DayOfWeek; // EKLENDİ: Gün kontrolü için gerekli
import java.time.LocalDate;

public class ServisKaydi implements Raporlanabilir {

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

    // --- YARDIMCI METOT: İş Günü Hesaplama ---
    private LocalDate isGunuEkle(LocalDate baslangic, int isGunuSayisi) {
        LocalDate tarih = baslangic;
        int sayac = 0;

        while (sayac < isGunuSayisi) {
            tarih = tarih.plusDays(1);
            // Cumartesi ve Pazar değilse sayacı artır
            if (tarih.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    tarih.getDayOfWeek() != DayOfWeek.SUNDAY) {
                sayac++;
            }
        }
        return tarih;
    }

    public String toTxtFormat() {
        String tekAd = (atananTeknisyen != null) ? atananTeknisyen.getAd() : "Yok";
        String tekUzmanlik = (atananTeknisyen != null) ? atananTeknisyen.getUzmanlikAlani() : "Yok";
        String bitisTarihiStr = (tamamlamaTarihi != null) ? tamamlamaTarihi.toString() : "Yok";

        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f;;%s",
                cihaz.getSeriNo(), cihaz.getCihazTuru(), cihaz.getMarka(), cihaz.getModel(),
                sorunAciklamasi, girisTarihi.toString(), durum.toString(),
                tekAd, tekUzmanlik, tahminiTamirUcreti, bitisTarihiStr);
    }

    public static ServisKaydi fromTxtFormat(String line) {
        try {
            String[] p = line.split(";;");
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
            double ucret = Double.parseDouble(p[9].replace(",", ".").trim());
            String bitisTarihiStr = (p.length > 10) ? p[10].trim() : "Yok";

            Musteri dummyMusteri = new Musteri("Bilinmiyor", "", "");
            Cihaz tempCihaz = null;
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
                if(d.toString().equalsIgnoreCase(durumStr)) {
                    kayit.setDurum(d); // Burada setDurum içindeki tarih mantığı çalışır ama aşağıda override ediyoruz
                    break;
                }
            }

            if(!tekAd.equals("Yok")) {
                kayit.setAtananTeknisyen(TeknisyenDeposu.teknisyenBulVeyaOlustur(tekAd, tekUzmanlik));
            }

            kayit.setTahminiTamirUcreti(ucret);

            // Dosyadan okurken kayıtlı tarihi esas al (tekrar hesaplama yapma)
            if (!bitisTarihiStr.equals("Yok")) {
                kayit.tamamlamaTarihi = LocalDate.parse(bitisTarihiStr);
            }

            return kayit;
        } catch (Exception e) {
            System.err.println("Servis kaydı okuma hatası: " + e.getMessage());
            return null;
        }
    }

    // Getter & Setterlar
    public Cihaz getCihaz() { return cihaz; }
    public String getSorunAciklamasi() { return sorunAciklamasi; }
    public LocalDate getGirisTarihi() { return girisTarihi; }
    public ServisDurumu getDurum() { return durum; }
    public double getTahminiTamirUcreti() { return tahminiTamirUcreti; }
    public Teknisyen getAtananTeknisyen() { return atananTeknisyen; }
    public LocalDate getTamamlamaTarihi() { return tamamlamaTarihi; }

    public double getOdenecekTamirUcreti() { return tahminiTamirUcreti; }

    public void setTahminiTamirUcreti(double tahminiTamirUcreti) {
        this.tahminiTamirUcreti = tahminiTamirUcreti;
    }
    public void setAtananTeknisyen(Teknisyen atananTeknisyen) {
        this.atananTeknisyen = atananTeknisyen;
    }
    public void setGirisTarihi(LocalDate girisTarihi) {
        this.girisTarihi = girisTarihi;
    }

    // --- GÜNCELLENEN METOT: Tarihi 20 iş günü sonrasına ayarlar ---
    public void setDurum(ServisDurumu durum) {
        this.durum = durum;

        if (durum == ServisDurumu.TAMAMLANDI) {
            // Eğer tarih daha önce atanmamışsa hesapla
            if (this.tamamlamaTarihi == null) {
                this.tamamlamaTarihi = isGunuEkle(this.girisTarihi, 20);
            }
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            // Tamamlandı'dan geri alınıyorsa tarihi temizle
            this.tamamlamaTarihi = null;
        }
    }

    public void setCihaz(Cihaz cihaz) {
        this.cihaz = cihaz;
    }

    @Override
    public String toString() {
        return String.format("Servis Kaydı [%s] - Cihaz: %s, Sorun: %s, Durum: %s",
                cihaz.getSeriNo(), cihaz.toString(), sorunAciklamasi, durum.toString());
    }

    @Override
    public String detayliRaporVer() {
        return "=== SERVİS DETAY RAPORU ===\n" +
                "Cihaz: " + cihaz.getMarka() + " " + cihaz.getModel() + "\n" +
                "Sorun: " + sorunAciklamasi + "\n" +
                "Durum: " + durum + "\n" +
                "Tahmini/Ödenecek Ücret: " + tahminiTamirUcreti + " TL\n" +
                "Tamamlanma/Teslim Tarihi: " + (tamamlamaTarihi != null ? tamamlamaTarihi : "Hesaplanmadı") + "\n" +
                "Teknisyen: " + (atananTeknisyen != null ? atananTeknisyen.toString() : "Atanmadı");
    }
}