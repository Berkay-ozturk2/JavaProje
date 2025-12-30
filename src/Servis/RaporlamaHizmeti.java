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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RaporlamaHizmeti {

    // Programcılar veya yöneticiler için konsol ekranına detaylı sistem raporu basan metot
    public static void konsolRaporuOlustur(List<Cihaz> cihazListesi) {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        // Generic (Genel Tür) sınıfımızı kullanarak cihaz listesini bir kutu içine aldık
        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir(); // Kutunun yeteneğini kullanarak listeyi yazdırdık

        System.out.println("\n[1.1] LİSTE ÖZETİ (Stream findFirst):");
        // STREAM API KULLANIMI: Listenin ilk elemanını güvenli bir şekilde alma
        cihazListesi.stream()
                .findFirst()
                .ifPresent(ilkCihaz -> System.out.println("-> Listedeki ilk cihaz: " + ilkCihaz.getMarka() + " " + ilkCihaz.getModel()));


        System.out.println("\n[2] SİSTEM MESAJI (Generic Metot 1):");
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi aktiftir.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // --- Wildcard Test Verileri ---
        // STREAM API KULLANIMI: Fiyat listesini stream ile oluşturma
        List<Double> fiyatListesi = cihazListesi.stream()
                .map(Cihaz::getFiyat)
                .collect(Collectors.toList());

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi);

        // --- YENİ EKLENEN BÖLÜM: Generic Metot 2 ve Sıralama ---
        System.out.println("\n[4] FİYAT SIRALAMASI (Generic Metot 2 & Collections.sort):");
        // Stream ile sıralama yapmak da mümkündür ancak mevcut generic metodumuzu test ediyoruz.
        if(!cihazListesi.isEmpty()) {
            System.out.println("Sıralama Öncesi İlk Cihaz: " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");

            // Generic metot ve Comparator kullanarak cihazları fiyatına göre (ucuzdan pahalıya) sıraladık
            cihazRaporKutusu.genericSirala(cihazListesi, Comparator.comparingDouble(Cihaz::getFiyat));

            System.out.println("Sıralama Sonrası İlk Cihaz (En Ucuz): " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");
        }


        System.out.println("\n[5] DETAYLI CİHAZ RAPORLARI (Interface & Polymorphism & Stream API):");

        // STREAM API KULLANIMI: Filtreleme ve döngüyü stream ile yapıyoruz
        boolean raporVar = cihazListesi.stream()
                .anyMatch(c -> c instanceof IRaporIslemleri); // Hiç raporlanabilir cihaz var mı kontrolü

        if (raporVar) {
            cihazListesi.stream()
                    .filter(c -> c instanceof IRaporIslemleri) // Sadece interface'i uygulayanları filtrele
                    .map(c -> (IRaporIslemleri) c) // Tür dönüşümü yap
                    .forEach(raporlanan -> {
                        System.out.println("\n>>> ÖZEL RAPOR (" + raporlanan.getRaporBasligi() + ") <<<");
                        System.out.println(raporlanan.detayliRaporVer());
                        System.out.println("--------------------------------------------------");
                    });
        } else {
            System.out.println("Raporlanabilir cihaz bulunamadı.");
        }
    }

    /**
     * GUI (Müşteri Takip Ekranı) için kullanılan metot.
     */
    public static String musteriCihazDurumRaporuOlustur(String seriNo) throws KayitBulunamadiException {
        StringBuilder rapor = new StringBuilder();

        String dosyaYolu = System.getProperty("user.dir") + System.getProperty("file.separator") + "cihazlar.txt";
        List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(dosyaYolu);

        // STREAM API KULLANIMI: Seri numarasına göre cihazı bulma
        Cihaz bulunanCihaz = cihazlar.stream()
                .filter(c -> c.getSeriNo().equalsIgnoreCase(seriNo))
                .findFirst()
                .orElseThrow(() -> new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı."));


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

        // STREAM API KULLANIMI: Servis kayıtlarında arama yapma
        ServisKaydi bulunanKayit = servisYonetimi.getKayitlar().stream()
                .filter(k -> k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo))
                .findFirst()
                .orElse(null);

        // Eğer serviste kaydı varsa durumunu ve tahmini teslim tarihini ekliyoruz
        if (bulunanKayit != null) {
            rapor.append(bulunanKayit.detayliRaporVer());
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(bulunanKayit.getGirisTarihi(), 20);
            rapor.append("\n----------------------------------\n");
            rapor.append("TAHMİNİ TESLİM: ").append(tahminiTeslim).append("\n");
        } else {
            rapor.append("=== SERVİS DURUMU ===\nAktif bir servis kaydı bulunmamaktadır.\n");
        }

        return rapor.toString();
    }
}