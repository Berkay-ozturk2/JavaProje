package Araclar;

import Cihazlar.*;
import Musteri.Musteri;
import Musteri.MusteriDeposu;
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

        if (cihaz instanceof Tablet) {
            return temel + ";;" + ((Tablet) cihaz).getKalemDestegi();
        }

        return temel;
    }

    // Dosyadan okunan satırı Cihaz nesnesine çevirir.
    public static Cihaz metniCihazaDonustur(String satir) {
        try {
            String[] parcalar = satir.split(";;");
            if (parcalar.length < 10) return null;

            String tur = parcalar[0].trim();
            String seriNo = parcalar[1].trim();
            String marka = parcalar[2].trim();
            String model = parcalar[3].trim();
            double fiyat = Double.parseDouble(parcalar[4].trim().replace(",", "."));
            LocalDate tarih = LocalDate.parse(parcalar[5].trim());
            int ekstraSure = Integer.parseInt(parcalar[6].trim());

            Musteri musteri = MusteriDeposu.musteriBulVeyaOlustur(
                    parcalar[7].trim(), // Ad
                    parcalar[8].trim(), // Soyad
                    parcalar[9].trim()  // Telefon
            );

            boolean isVip = false;
            boolean kalem = false;

            if (tur.equalsIgnoreCase("Tablet")) {
                if (parcalar.length > 11) {
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                    kalem = Boolean.parseBoolean(parcalar[11].trim());
                } else if (parcalar.length == 11) {
                    try {
                        isVip = Boolean.parseBoolean(parcalar[10].trim());
                    } catch (Exception e) {
                        kalem = Boolean.parseBoolean(parcalar[10].trim());
                    }
                }
            } else {
                if (parcalar.length > 10) {
                    isVip = Boolean.parseBoolean(parcalar[10].trim());
                }
            }

            musteri.setVip(isVip);

            Cihaz cihaz = null;
            if (tur.equalsIgnoreCase("Telefon")) {
                cihaz = new Telefon(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Laptop")) {
                cihaz = new Laptop(seriNo, marka, model, fiyat, tarih, musteri);
            } else if (tur.equalsIgnoreCase("Tablet")) {
                cihaz = new Tablet(seriNo, marka, model, fiyat, tarih, kalem, musteri);
            }

            if (cihaz != null && ekstraSure > 0) {
                cihaz.garantiUzat(ekstraSure);
            }
            return cihaz;

        } catch (Exception e) {
            System.err.println("Cihaz ayrıştırma hatası (" + satir + "): " + e.getMessage());
            return null;
        }
    }

    // --- SERVİS KAYDI İŞLEMLERİ ---

    public static String servisKaydiMetneDonustur(ServisKaydi kayit) {
        String tekAd = (kayit.getAtananTeknisyen() != null) ? kayit.getAtananTeknisyen().getAd() : "Yok";
        String tekUzmanlik = (kayit.getAtananTeknisyen() != null) ? kayit.getAtananTeknisyen().getUzmanlikAlani() : "Yok";
        String bitisTarihiStr = (kayit.getTamamlamaTarihi() != null) ? kayit.getTamamlamaTarihi().toString() : "Yok";

        // YENİ: İşlem geçmişini "%%" ile birleştirip tek bir string yapıyoruz
        StringBuilder sbGecmis = new StringBuilder();
        if (kayit.getIslemGecmisi() != null) {
            for (String islem : kayit.getIslemGecmisi()) {
                sbGecmis.append(islem).append("%%");
            }
        }
        String gecmisStr = sbGecmis.toString();
        if (gecmisStr.isEmpty()) gecmisStr = "Yok";

        // En sona geçmiş stringini ekledik
        return String.format("%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%s;;%.2f;;%s;;%s",
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
                bitisTarihiStr,
                gecmisStr);
    }

    public static ServisKaydi metniServisKaydinaDonustur(String satir, List<Cihaz> guncelCihazListesi) {
        try {
            String[] p = satir.split(";;");
            if(p.length < 10) return null;

            String seriNo = p[0].trim();
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
                String tur = p[1].trim();
                String marka = p[2].trim();
                String model = p[3].trim();

                if(tur.equalsIgnoreCase("Telefon"))
                    gercekCihaz = new Telefon(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else if(tur.equalsIgnoreCase("Laptop"))
                    gercekCihaz = new Laptop(seriNo, marka, model, 0, gecmisTarih, dummyMusteri);
                else
                    gercekCihaz = new Tablet(seriNo, marka, model, 0, gecmisTarih, false, dummyMusteri);
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
                kayit.setTamamlamaTarihi(LocalDate.parse(p[10].trim()));
            }

            // YENİ: GEÇMİŞİ YÜKLEME
            if (p.length > 11 && !p[11].equals("Yok")) {
                String[] gecmisSatirlar = p[11].split("%%");
                // Constructor otomatik bir log oluşturuyor, onu temizleyip dosyadan gelenleri yükleyelim
                kayit.getIslemGecmisi().clear();
                for (String islem : gecmisSatirlar) {
                    if (!islem.trim().isEmpty()) {
                        kayit.getIslemGecmisi().add(islem);
                    }
                }
            }

            return kayit;
        } catch (Exception e) {
            System.err.println("Servis kaydı okuma hatası: " + e.getMessage());
            return null;
        }
    }
}