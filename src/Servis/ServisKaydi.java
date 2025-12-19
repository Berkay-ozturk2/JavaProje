package Servis;

import Arayuzler.Raporlanabilir;
// --- YENİ EKLENDİ: Tarih aracını import ediyoruz ---
import Araclar.TarihYardimcisi;
import Cihazlar.*;
import Musteri.Musteri;
// import java.time.DayOfWeek; // Bu import artık gereksiz, silebilirsiniz.
import java.time.LocalDate;
import java.util.List;

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

    // --- SİLİNDİ: 'private LocalDate isGunuEkle(...)' metodu buradan kaldırıldı ---
    // Artık bu işi 'TarihYardimcisi' sınıfı yapıyor.

    public String toTxtFormat() {
        String tekAd = (atananTeknisyen != null) ? atananTeknisyen.getAd() : "Yok";
        String tekUzmanlik = (atananTeknisyen != null) ? atananTeknisyen.getUzmanlikAlani() : "Yok";
        String bitisTarihiStr = (tamamlamaTarihi != null) ? tamamlamaTarihi.toString() : "Yok";

        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f;;%s",
                cihaz.getSeriNo(), cihaz.getCihazTuru(), cihaz.getMarka(), cihaz.getModel(),
                sorunAciklamasi, girisTarihi.toString(), durum.toString(),
                tekAd, tekUzmanlik, tahminiTamirUcreti, bitisTarihiStr);
    }

    public static ServisKaydi fromTxtFormat(String line, List<Cihaz> guncelCihazListesi) {
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

            Cihaz gercekCihaz = null;
            if (guncelCihazListesi != null) {
                for (Cihaz c : guncelCihazListesi) {
                    if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                        gercekCihaz = c;
                        break;
                    }
                }
            }

            if (gercekCihaz == null) {
                Musteri dummyMusteri = new Musteri("Bilinmiyor", "", "");
                LocalDate gecmisTarih = LocalDate.now().minusYears(10);
                if(tur.equalsIgnoreCase("Telefon"))
                    gercekCihaz = new Telefon(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else if(tur.equalsIgnoreCase("Laptop"))
                    gercekCihaz = new Laptop(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else
                    gercekCihaz = new Tablet(seriNo, marka, model, 0, gecmisTarih, false, dummyMusteri);
            }

            ServisKaydi kayit = new ServisKaydi(gercekCihaz, sorun);
            kayit.setGirisTarihi(giris);

            for(ServisDurumu d : ServisDurumu.values()) {
                if(d.toString().equalsIgnoreCase(durumStr)) {
                    kayit.setDurum(d);
                    break;
                }
            }

            if(!tekAd.equals("Yok")) {
                kayit.setAtananTeknisyen(TeknisyenDeposu.teknisyenBulVeyaOlustur(tekAd, tekUzmanlik));
            }

            kayit.setTahminiTamirUcreti(ucret);

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

    public void setDurum(ServisDurumu durum) {
        this.durum = durum;

        if (durum == ServisDurumu.TAMAMLANDI) {
            if (this.tamamlamaTarihi == null) {
                // --- DEĞİŞİKLİK: Tarih hesaplaması artık Utility sınıfından çağrılıyor ---
                this.tamamlamaTarihi = TarihYardimcisi.isGunuEkle(this.girisTarihi, 20);
            }
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
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