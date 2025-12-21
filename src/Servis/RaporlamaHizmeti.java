package Servis;

import Araclar.DosyaIslemleri;
import Araclar.RaporKutusu;
import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Arayuzler.IRaporIslemleri;
import Istisnalar.KayitBulunamadiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator; // Sıralama için eklendi
import java.util.List;

public class RaporlamaHizmeti {

    // Programcılar veya yöneticiler için konsol ekranına detaylı sistem raporu basan metot
    public static void konsolRaporuOlustur(List<Cihaz> cihazListesi) {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        // Generic (Genel Tür) sınıfımızı kullanarak cihaz listesini bir kutu içine aldık
        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir(); // Kutunun yeteneğini kullanarak listeyi yazdırdık

        System.out.println("\n[1.1] LİSTE ÖZETİ (Generic Get Metodu):");
        Cihaz ilkCihaz = cihazRaporKutusu.getIlkEleman(); // Generic get metodunu test etmek için ilk elemanı çektik

        if (ilkCihaz != null) {
            System.out.println("-> Listedeki ilk cihaz: " + ilkCihaz.getMarka() + " " + ilkCihaz.getModel());
        }

        System.out.println("\n[2] SİSTEM MESAJI (Generic Metot 1):");
        // Generic metodu test etmek için ekrana string türünde mesajlar bastık
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi aktiftir.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // --- Wildcard Test Verileri ---
        // Wildcard (?) yapısını test etmek için sayısal verileri ayrı bir listeye topladık
        List<Double> fiyatListesi = new ArrayList<>();
        for (Cihaz c : cihazListesi) {
            fiyatListesi.add(c.getFiyat());
        }
        List<Integer> servisSureleri = new ArrayList<>();
        servisSureleri.add(20);

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi); // Sadece sayı kabul eden wildcard metodunu çağırdık

        // --- YENİ EKLENEN BÖLÜM: Generic Metot 2 ve Sıralama ---
        System.out.println("\n[4] FİYAT SIRALAMASI (Generic Metot 2 & Collections.sort):");
        System.out.println("Sıralama Öncesi İlk Cihaz: " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");

        // Generic metot ve Comparator kullanarak cihazları fiyatına göre (ucuzdan pahalıya) sıraladık
        cihazRaporKutusu.genericSirala(cihazListesi, new Comparator<Cihaz>() {
            @Override
            public int compare(Cihaz c1, Cihaz c2) {
                return Double.compare(c1.getFiyat(), c2.getFiyat());
            }
        });

        System.out.println("Sıralama Sonrası İlk Cihaz (En Ucuz): " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");


        System.out.println("\n[5] DETAYLI CİHAZ RAPORLARI (Interface & Polymorphism):");
        boolean raporlanabilirCihazVarMi = false;

        // Polymorphism kullanarak listeyi geziyoruz ve sadece rapor özelliği olan cihazları buluyoruz
        for (Cihaz c : cihazListesi) {
            if (c instanceof IRaporIslemleri) { // Cihaz bu arayüzü (interface) kullanıyor mu?
                raporlanabilirCihazVarMi = true;
                IRaporIslemleri raporlanan = (IRaporIslemleri) c;
                System.out.println("\n>>> ÖZEL RAPOR (" + raporlanan.getRaporBasligi() + ") <<<");
                System.out.println(raporlanan.detayliRaporVer()); // Özel rapor metodunu çalıştırdık
                System.out.println("--------------------------------------------------");
            }
        }

        if (!raporlanabilirCihazVarMi) {
            System.out.println("Raporlanabilir cihaz bulunamadı.");
        }
    }

    /**
     * GUI (Müşteri Takip Ekranı) için kullanılan metot.
     */
    public static String musteriCihazDurumRaporuOlustur(String seriNo) throws KayitBulunamadiException {
        StringBuilder rapor = new StringBuilder(); // Metinleri birleştirmek için StringBuilder kullandık

        String dosyaYolu = System.getProperty("user.dir") + System.getProperty("file.separator") + "cihazlar.txt";
        List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(dosyaYolu); // Verileri dosyadan taze taze yüklüyoruz

        Cihaz bulunanCihaz = null;
        // Girilen seri numarasını listede arıyoruz
        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanCihaz = c;
                break;
            }
        }

        // Cihaz yoksa hata fırlatarak arayüzü uyarıyoruz
        if (bulunanCihaz == null) {
            throw new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı.");
        }

        // Rapor metnini oluşturmaya başlıyoruz
        rapor.append("=== CİHAZ BİLGİLERİ ===\n");
        rapor.append("Müşteri: ").append(bulunanCihaz.getSahip().getAd())
                .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append("\n");
        rapor.append("Cihaz: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");

        String garantiDurumu = bulunanCihaz.garantiAktifMi() ? "AKTİF" : "BİTMİŞ";
        rapor.append("Garanti: ").append(garantiDurumu).append("\n\n");

        // Cihazın detaylı rapor özelliği varsa onu da ekliyoruz
        if (bulunanCihaz instanceof IRaporIslemleri) {
            rapor.append("Özet: ").append(((IRaporIslemleri) bulunanCihaz).getOzetBilgi()).append("\n\n");
            rapor.append(((IRaporIslemleri) bulunanCihaz).detayliRaporVer()).append("\n\n");
        }

        // Servis Kayıtlarını Kontrol Et
        ServisYonetimi servisYonetimi = new ServisYonetimi(cihazlar);
        ServisKaydi bulunanKayit = null;
        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanKayit = k;
                break;
            }
        }

        // Eğer serviste kaydı varsa durumunu ve tahmini teslim tarihini ekliyoruz
        if (bulunanKayit != null) {
            rapor.append(bulunanKayit.detayliRaporVer());
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(bulunanKayit.getGirisTarihi(), 20);
            rapor.append("\n----------------------------------\n");
            rapor.append("TAHMİNİ TESLİM: ").append(tahminiTeslim).append("\n");
        } else {
            rapor.append("=== SERVİS DURUMU ===\nAktif bir servis kaydı bulunmamaktadır.\n");
        }

        return rapor.toString(); // Hazırladığımız uzun metni döndürüyoruz
    }
}