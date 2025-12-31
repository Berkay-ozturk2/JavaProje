package Servis;

import Araclar.RaporKutusu;
import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Arayuzler.IRaporIslemleri;
import Istisnalar.KayitBulunamadiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RaporlamaHizmeti {

    // Bu metot zaten listeyi dışarıdan aldığı için sorunsuz, aynen kalabilir.
    public static void konsolRaporuOlustur(List<Cihaz> cihazListesi) {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir();

        System.out.println("\n[1.1] LİSTE ÖZETİ (Stream findFirst):");
        cihazListesi.stream()
                .findFirst()
                .ifPresent(ilkCihaz -> System.out.println("-> Listedeki ilk cihaz: " + ilkCihaz.getMarka() + " " + ilkCihaz.getModel()));

        System.out.println("\n[2] SİSTEM MESAJI (Generic Metot 1):");
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi aktiftir.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        List<Double> fiyatListesi = cihazListesi.stream()
                .map(Cihaz::getFiyat)
                .collect(Collectors.toList());

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi);

        System.out.println("\n[4] FİYAT SIRALAMASI (Generic Metot 2 & Collections.sort):");
        if(!cihazListesi.isEmpty()) {
            cihazRaporKutusu.genericSirala(cihazListesi, Comparator.comparingDouble(Cihaz::getFiyat));
        }

        System.out.println("\n[5] DETAYLI CİHAZ RAPORLARI (Interface & Polymorphism & Stream API):");
        boolean raporVar = cihazListesi.stream().anyMatch(c -> c instanceof IRaporIslemleri);

        if (raporVar) {
            cihazListesi.stream()
                    .filter(c -> c instanceof IRaporIslemleri)
                    .map(c -> (IRaporIslemleri) c)
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
     * GÜNCELLENDİ: Artık dosya yüklemiyor, verileri parametre olarak istiyor.
     * Böylece hem PC hem Android'de hatasız çalışır.
     */
    public static String musteriCihazDurumRaporuOlustur(String seriNo, List<Cihaz> cihazlar, ServisYonetimi servisYonetimi) throws KayitBulunamadiException {
        StringBuilder rapor = new StringBuilder();

        // 1. Cihazı Listeden Bul
        Cihaz bulunanCihaz = cihazlar.stream()
                .filter(c -> c.getSeriNo().equalsIgnoreCase(seriNo))
                .findFirst()
                .orElseThrow(() -> new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı."));

        // 2. Rapor Metnini Oluştur
        rapor.append("=== CİHAZ BİLGİLERİ ===\n");
        rapor.append("Müşteri: ").append(bulunanCihaz.getSahip().getAd())
                .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append("\n");
        rapor.append("Cihaz: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");

        String garantiDurumu = bulunanCihaz.garantiAktifMi() ? "AKTİF" : "BİTMİŞ";
        rapor.append("Garanti: ").append(garantiDurumu).append("\n\n");

        if (bulunanCihaz instanceof IRaporIslemleri) {
            rapor.append("Özet: ").append(((IRaporIslemleri) bulunanCihaz).getOzetBilgi()).append("\n\n");
            rapor.append(((IRaporIslemleri) bulunanCihaz).detayliRaporVer()).append("\n\n");
        }

        // 3. Servis Kayıtlarını Kontrol Et (Parametre gelen yönetimden bakıyoruz)
        ServisKaydi bulunanKayit = servisYonetimi.getKayitlar().stream()
                .filter(k -> k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo))
                .findFirst()
                .orElse(null);

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