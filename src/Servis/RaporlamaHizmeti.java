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

    public static void konsolRaporuOlustur(List<Cihaz> cihazListesi) {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        // Generic sınıfın oluşturulması
        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir();

        System.out.println("\n[1.1] LİSTE ÖZETİ (Generic Get Metodu):");
        Cihaz ilkCihaz = cihazRaporKutusu.getIlkEleman();

        if (ilkCihaz != null) {
            System.out.println("-> Listedeki ilk cihaz: " + ilkCihaz.getMarka() + " " + ilkCihaz.getModel());
        }

        System.out.println("\n[2] SİSTEM MESAJI (Generic Metot 1):");
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi aktiftir.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // --- Wildcard Test Verileri ---
        List<Double> fiyatListesi = new ArrayList<>();
        for (Cihaz c : cihazListesi) {
            fiyatListesi.add(c.getFiyat());
        }
        List<Integer> servisSureleri = new ArrayList<>();
        servisSureleri.add(20);

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi);

        // --- YENİ EKLENEN BÖLÜM: Generic Metot 2 ve Sıralama ---
        System.out.println("\n[4] FİYAT SIRALAMASI (Generic Metot 2 & Collections.sort):");
        System.out.println("Sıralama Öncesi İlk Cihaz: " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");

        // Generic metot kullanılarak sıralama yapılıyor (Fiyata göre artan)
        cihazRaporKutusu.genericSirala(cihazListesi, new Comparator<Cihaz>() {
            @Override
            public int compare(Cihaz c1, Cihaz c2) {
                return Double.compare(c1.getFiyat(), c2.getFiyat());
            }
        });

        System.out.println("Sıralama Sonrası İlk Cihaz (En Ucuz): " + cihazListesi.get(0).getModel() + " (" + cihazListesi.get(0).getFiyat() + " TL)");


        System.out.println("\n[5] DETAYLI CİHAZ RAPORLARI (Interface & Polymorphism):");
        boolean raporlanabilirCihazVarMi = false;

        for (Cihaz c : cihazListesi) {
            if (c instanceof IRaporIslemleri) {
                raporlanabilirCihazVarMi = true;
                IRaporIslemleri raporlanan = (IRaporIslemleri) c;
                System.out.println("\n>>> ÖZEL RAPOR (" + raporlanan.getRaporBasligi() + ") <<<");
                System.out.println(raporlanan.detayliRaporVer());
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
        StringBuilder rapor = new StringBuilder();

        String dosyaYolu = System.getProperty("user.dir") + System.getProperty("file.separator") + "cihazlar.txt";
        List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(dosyaYolu);

        Cihaz bulunanCihaz = null;
        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanCihaz = c;
                break;
            }
        }

        if (bulunanCihaz == null) {
            throw new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı.");
        }

        rapor.append("=== CİHAZ BİLGİLERİ ===\n");
        rapor.append("Müşteri: ").append(bulunanCihaz.getSahip().getAd())
                .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append("\n");
        rapor.append("Cihaz: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");

        String garantiDurumu = bulunanCihaz.garantiAktifMi() ? "AKTİF" : "BİTMİŞ";
        rapor.append("Garanti: ").append(garantiDurumu).append("\n\n");

        if (bulunanCihaz instanceof IRaporIslemleri) {
            // Burada da arayüzün getOzetBilgi metodunu rapora ekleyebiliriz
            rapor.append("Özet: ").append(((IRaporIslemleri) bulunanCihaz).getOzetBilgi()).append("\n\n");
            rapor.append(((IRaporIslemleri) bulunanCihaz).detayliRaporVer()).append("\n\n");
        }

        // Servis Kayıtlarını Bul
        ServisYonetimi servisYonetimi = new ServisYonetimi(cihazlar);
        ServisKaydi bulunanKayit = null;
        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanKayit = k;
                break;
            }
        }

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