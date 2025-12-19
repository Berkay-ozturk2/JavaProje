package Servis;

import Arayuzler.Raporlanabilir;
import Cihazlar.*;
import Musteri.Musteri;
import java.time.LocalDate;
import java.time.LocalDateTime; // GEREKSİNİM: İkinci Tarih sınıfı
import java.util.List;

public class ServisKaydi implements Raporlanabilir {

    private Cihaz cihaz;
    private String sorunAciklamasi;
    private LocalDate girisTarihi;
    private LocalDateTime islemZamani; // Yeni alan
    private double tahminiTamirUcreti;
    private ServisDurumu durum;
    private LocalDate tamamlamaTarihi;
    private Teknisyen atananTeknisyen;

    public ServisKaydi(Cihaz cihaz, String sorunAciklamasi) {
        this.cihaz = cihaz;
        this.sorunAciklamasi = sorunAciklamasi;
        this.girisTarihi = LocalDate.now();
        this.islemZamani = LocalDateTime.now(); // Anlık zaman damgası
        this.durum = ServisDurumu.KABUL_EDILDI;
        this.tamamlamaTarihi = null;
        this.tahminiTamirUcreti = 0.0;
        this.atananTeknisyen = null;
    }

    // ... (toTxtFormat ve fromTxtFormat metotları aynen kalabilir, islemZamani'ni kaydetmeye gerek yok) ...
    // ... Buraya mevcut kodunuzdaki toTxtFormat ve fromTxtFormat metotlarını olduğu gibi yapıştırın ...
    // ... Getter ve Setter'lar aynen kalacak ...

    public String toTxtFormat() {
        // Mevcut kodunuzdaki haliyle aynen kullanın
        String tekAd = (atananTeknisyen != null) ? atananTeknisyen.getAd() : "Yok";
        String tekUzmanlik = (atananTeknisyen != null) ? atananTeknisyen.getUzmanlikAlani() : "Yok";
        String bitisTarihiStr = (tamamlamaTarihi != null) ? tamamlamaTarihi.toString() : "Yok";

        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f;;%s",
                cihaz.getSeriNo(), cihaz.getCihazTuru(), cihaz.getMarka(), cihaz.getModel(),
                sorunAciklamasi, girisTarihi.toString(), durum.toString(),
                tekAd, tekUzmanlik, tahminiTamirUcreti, bitisTarihiStr);
    }

    public static ServisKaydi fromTxtFormat(String line, List<Cihaz> guncelCihazListesi) {
        // Mevcut kodunuzdaki fromTxtFormat metodunu aynen buraya kopyalayın
        // Sadece sınıfın üst tarafındaki import ve field değişiklikleri önemli.
        // ... (Kod tekrarı olmaması için burayı kısaltıyorum) ...
        // Dosyadan okurken LocalDateTime set etmeye gerek yok, default now() kalabilir veya null geçilebilir.
        try {
            String[] p = line.split(";;");
            if(p.length < 10) return null;

            // ... (Aynı ayrıştırma mantığı) ...

            // Tek fark: yukarıda tanımladığımız nesne LocalDateTime ile oluşuyor.
            // Bu metot içinde bir değişiklik yapmanıza gerek yok.
            // Sadece return kayit; demeniz yeterli.

            // (Mevcut kodunuzu buraya yapıştırın)

            // Referans olması için kısa versiyon:
            String seriNo = p[0].trim();
            // ... (diğer parse işlemleri) ...
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
                // Basit dummy creation logic...
                if(p[1].trim().equalsIgnoreCase("Telefon"))
                    gercekCihaz = new Telefon(seriNo, p[2].trim(), p[3].trim(), 0, gecmisTarih, dummyMusteri);
                else if(p[1].trim().equalsIgnoreCase("Laptop"))
                    gercekCihaz = new Laptop(seriNo, p[2].trim(), p[3].trim(), 0, gecmisTarih, dummyMusteri);
                else
                    gercekCihaz = new Tablet(seriNo, p[2].trim(), p[3].trim(), 0, gecmisTarih, false, dummyMusteri);
            }

            ServisKaydi kayit = new ServisKaydi(gercekCihaz, p[4].trim());
            kayit.setGirisTarihi(LocalDate.parse(p[5].trim()));

            for(ServisDurumu d : ServisDurumu.values()) {
                if(d.toString().equalsIgnoreCase(p[6].trim())) {
                    kayit.setDurum(d);
                    break;
                }
            }

            String tekAd = p[7].trim();
            String tekUzmanlik = p[8].trim();
            if(!tekAd.equals("Yok")) {
                kayit.setAtananTeknisyen(TeknisyenDeposu.teknisyenBulVeyaOlustur(tekAd, tekUzmanlik));
            }

            kayit.setTahminiTamirUcreti(Double.parseDouble(p[9].replace(",", ".").trim()));
            if (p.length > 10 && !p[10].trim().equals("Yok")) {
                kayit.tamamlamaTarihi = LocalDate.parse(p[10].trim());
            }

            return kayit;
        } catch (Exception e) {
            return null;
        }
    }

    // ... Getter ve Setterlar (Mevcutlar) ...
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

    public void setDurum(ServisDurumu durum) {
        this.durum = durum;
        if (durum == ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = LocalDate.now();
        } else if (this.tamamlamaTarihi != null && durum != ServisDurumu.TAMAMLANDI) {
            this.tamamlamaTarihi = null;
        }
    }

    public void setCihaz(Cihaz cihaz) { this.cihaz = cihaz; }

    // --- INTERFACE METOTLARI (YENİ EKLENENLER) ---
    @Override
    public String detayliRaporVer() {
        // Mevcut kodunuz
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