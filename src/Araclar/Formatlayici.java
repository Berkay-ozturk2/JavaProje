package Araclar;

import Cihazlar.*;
import Musteri.Musteri;
import Musteri.MusteriDeposu; // YENİ: Depo sınıfını import ettik
import Servis.ServisDurumu;
import Servis.ServisKaydi;
import Servis.TeknisyenDeposu;

import java.time.LocalDate;
import java.util.List;

/* Dosya okuma ve yazma işlemleri için metin formatlama ve ayrıştırma
 işlemlerini yürüten yardımcı sınıftır.*/
public class Formatlayici {

    // --- CİHAZ İŞLEMLERİ ---

    // Bir Cihaz nesnesini dosya formatına uygun String haline getirir.
    public static String cihazMetneDonustur(Cihaz cihaz) {
        // Tüm cihazlarda ortak olan temel bilgileri formatlar.
        String temel = String.format("%s;;%s;;%s;;%s;;%.2f;;%s;;%d;;%s;;%s;;%s;;%b",
                cihaz.getCihazTuru(),
                cihaz.getSeriNo(),
                cihaz.getMarka(),
                cihaz.getModel(),
                cihaz.getFiyat(),
                cihaz.getGarantiBaslangic(),
                cihaz.getEkstraGarantiSuresiAy(),
                cihaz.getSahip().getAd(),
                cihaz.getSahip().getSoyad(),
                cihaz.getSahip().getTelefon(),
                cihaz.getSahip().vipMi());

        // Cihaz Tablet ise kalem desteği bilgisini de metnin sonuna ekler.
        if (cihaz instanceof Tablet) {
            return temel + ";;" + ((Tablet) cihaz).getKalemDestegi();
        }

        // Laptop ve Telefon için ekstra özel alan şimdilik yok, temel format yeterli.
        return temel;
    }

    // Dosyadan okunan satırı Cihaz nesnesine çevirir.
    public static Cihaz metniCihazaDonustur(String satir) {
        try {
            // Satırı ';;' ayracına göre parçalayarak veri dizisine dönüştürür.
            String[] parcalar = satir.split(";;");

            // Eksik veri varsa işlem yapmadan null döndürür.
            if (parcalar.length < 10) return null;

            // Temel veri alanlarını ayrıştırır.
            String tur = parcalar[0].trim();
            String seriNo = parcalar[1].trim();
            String marka = parcalar[2].trim();
            String model = parcalar[3].trim();
            double fiyat = Double.parseDouble(parcalar[4].trim().replace(",", "."));
            LocalDate tarih = LocalDate.parse(parcalar[5].trim());
            int ekstraSure = Integer.parseInt(parcalar[6].trim());

            // --- DEĞİŞİKLİK BAŞLANGICI ---
            // Eskiden doğrudan 'new Musteri' diyorduk. Şimdi depoya soruyoruz.
            // Eğer bu telefon numarasıyla kayıtlı biri varsa onu getiriyor (ID korunuyor).
            // Yoksa yeni oluşturup ID veriyor.
            Musteri musteri = MusteriDeposu.musteriBulVeyaOlustur(
                    parcalar[7].trim(), // Ad
                    parcalar[8].trim(), // Soyad
                    parcalar[9].trim()  // Telefon
            );
            // --- DEĞİŞİKLİK BİTİŞİ ---

            boolean isVip = false;
            boolean kalem = false;

            // Cihaz türüne göre (Tablet mi diğerleri mi) opsiyonel alanları kontrol eder.
            if (tur.equalsIgnoreCase("Tablet")) {
                if (parcalar.length > 11) {
                    // Hem VIP hem Kalem bilgisi varsa ikisini de okur.
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                    kalem = Boolean.parseBoolean(parcalar[11].trim());
                } else if (parcalar.length == 11) {
                    // Eski kayıt formatıyla uyumluluk sağlamak için hata yönetimiyle okur.
                    try {
                        isVip = Boolean.parseBoolean(parcalar[10].trim());
                    } catch (Exception e) {
                        kalem = Boolean.parseBoolean(parcalar[10].trim());
                    }
                }
            } else {
                // Tablet değilse sadece VIP bilgisini kontrol eder.
                if (parcalar.length > 10) {
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                }
            }

            // Müşterinin VIP durumunu günceller.
            musteri.setVip(isVip);

            Cihaz cihaz = null;
            // Okunan türe göre ilgili alt sınıftan (Telefon, Laptop, Tablet) nesne üretir.
            if (tur.equalsIgnoreCase("Telefon")) {
                cihaz = new Telefon(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Laptop")) {
                cihaz = new Laptop(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Tablet")) {
                cihaz = new Tablet(seriNo, marka, model, fiyat, tarih, kalem, musteri);
            }

            // Cihaz başarıyla oluşturulduysa ve ekstra garanti süresi varsa ekler.
            if (cihaz != null && ekstraSure > 0) {
                cihaz.garantiUzat(ekstraSure);
            }
            return cihaz;

        } catch (Exception e) {
            // Herhangi bir ayrıştırma hatasında konsola bilgi basar ve null döndürür.
            System.err.println("Cihaz ayrıştırma hatası (" + satir + "): " + e.getMessage());
            return null;
        }
    }

    // --- SERVİS KAYDI İŞLEMLERİ ---

    public static String servisKaydiMetneDonustur(ServisKaydi kayit) {
        // Teknisyen veya tarih bilgisi null ise "Yok" yazarak hatayı önler.
        String tekAd = (kayit.getAtananTeknisyen() != null) ? kayit.getAtananTeknisyen().getAd() : "Yok";
        String tekUzmanlik = (kayit.getAtananTeknisyen() != null) ? kayit.getAtananTeknisyen().getUzmanlikAlani() : "Yok";
        String bitisTarihiStr = (kayit.getTamamlamaTarihi() != null) ? kayit.getTamamlamaTarihi().toString() : "Yok";

        // Tüm servis bilgilerini sıralı bir şekilde birleştirip döndürür.
        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f;;%s",
                kayit.getCihaz().getSeriNo(),
                kayit.getCihaz().getCihazTuru(),
                kayit.getCihaz().getMarka(),
                kayit.getCihaz().getModel(),
                kayit.getSorunAciklamasi(),
                kayit.getGirisTarihi().toString(),
                kayit.getDurum().toString(),
                tekAd,
                tekUzmanlik,
                kayit.getOdenecekTamirUcreti(),
                bitisTarihiStr);
    }

    public static ServisKaydi metniServisKaydinaDonustur(String satir, List<Cihaz> guncelCihazListesi) {
        try {
            // Satırı parçalayıp en az veri uzunluğunu kontrol eder.
            String[] p = satir.split(";;");
            if(p.length < 10) return null;

            String seriNo = p[0].trim();

            // Seri numarasını kullanarak listedeki gerçek cihaz nesnesini arar.
            Cihaz gercekCihaz = null;
            if (guncelCihazListesi != null) {
                for (Cihaz c : guncelCihazListesi) {
                    if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                        gercekCihaz = c;
                        break;
                    }
                }
            }

            // Cihaz silinmişse veya bulunamazsa kaydın bozulmaması için "Hayalet" cihaz oluşturur.
            if (gercekCihaz == null) {
                Musteri dummyMusteri = new Musteri("Bilinmiyor", "", "");
                LocalDate gecmisTarih = LocalDate.now().minusYears(10);
                String tur = p[1].trim();
                String marka = p[2].trim();
                String model = p[3].trim();

                // Türüne göre geçici bir cihaz nesnesi yaratır.
                if(tur.equalsIgnoreCase("Telefon"))
                    gercekCihaz = new Telefon(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else if(tur.equalsIgnoreCase("Laptop"))
                    gercekCihaz = new Laptop(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else
                    gercekCihaz = new Tablet(seriNo, marka, model, 0, gecmisTarih, false, dummyMusteri);
            }

            // Bulunan veya oluşturulan cihaz ile servis kaydını başlatır.
            ServisKaydi kayit = new ServisKaydi(gercekCihaz, p[4].trim());
            kayit.setGirisTarihi(LocalDate.parse(p[5].trim()));

            // Metin olarak gelen servis durumunu Enum yapısına (ServisDurumu) çevirir.
            for(ServisDurumu d : ServisDurumu.values()) {
                if(d.toString().equalsIgnoreCase(p[6].trim())) {
                    kayit.setDurum(d);
                    break;
                }
            }

            // Teknisyen bilgisi varsa ilgili teknisyeni bulur veya oluşturur.
            String tekAd = p[7].trim();
            String tekUzmanlik = p[8].trim();
            if(!tekAd.equals("Yok")) {
                kayit.setAtananTeknisyen(TeknisyenDeposu.teknisyenBulVeyaOlustur(tekAd, tekUzmanlik));
            }

            // Tamir ücretini sayısal veriye çevirir.
            kayit.setTahminiTamirUcreti(Double.parseDouble(p[9].replace(",", ".").trim()));

            // Varsa tamamlama tarihini okur ve kayda işler.
            if (p.length > 10 && !p[10].trim().equals("Yok")) {
                kayit.setTamamlamaTarihi(LocalDate.parse(p[10].trim()));
            }

            return kayit;
        } catch (Exception e) {
            // Hata durumunda konsola bilgi verip null döndürür.
            System.err.println("Servis kaydı okuma hatası: " + e.getMessage());
            return null;
        }
    }
}