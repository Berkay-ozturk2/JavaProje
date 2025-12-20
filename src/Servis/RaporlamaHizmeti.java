package Servis;

import Araclar.RaporKutusu;
import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Istisnalar.KayitBulunamadiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RaporlamaHizmeti {

    public static void konsolRaporuOlustur(List<Cihaz> cihazListesi) {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir();

        System.out.println("\n[1.1] LİSTE ÖZETİ (Generic Get Metodu Testi):");
        Cihaz ilkCihaz = cihazRaporKutusu.getIlkEleman();

        if (ilkCihaz != null) {
            System.out.println("-> Listedeki ilk cihaz bulundu.");
            System.out.println("-> Seri No: " + ilkCihaz.getSeriNo());
            System.out.println("-> Model: " + ilkCihaz.getMarka() + " " + ilkCihaz.getModel());
        } else {
            System.out.println("-> Liste boş, ilk eleman getirilemedi.");
        }

        System.out.println("\n[2] SİSTEM MESAJI:");
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi başarıyla başlatıldı.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        List<Double> fiyatListesi = new ArrayList<>();
        for (Cihaz c : cihazListesi) {
            fiyatListesi.add(c.getFiyat());
        }

        List<Integer> servisSureleri = new ArrayList<>();
        servisSureleri.add(20);

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi);
        cihazRaporKutusu.wildcardTest(servisSureleri);
    }

    /**
     * GUI'den taşınan metot.
     * Müşteri takip ekranı için detaylı durum raporu üretir.
     */
    public static String musteriCihazDurumRaporuOlustur(String seriNo) throws KayitBulunamadiException {
        StringBuilder rapor = new StringBuilder();

        // Veri Yükleme
        // Not: Normalde dosya yolu dışarıdan parametre gelmeli ama yapıyı bozmamak için sabit bırakıyoruz.
        String dosyaYolu = System.getProperty("user.dir") + System.getProperty("file.separator") + "cihazlar.txt";
        List<Cihaz> cihazlar = Cihaz.verileriYukle(dosyaYolu);
        Cihaz bulunanCihaz = null;

        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanCihaz = c;
                break;
            }
        }

        if (bulunanCihaz == null) {
            throw new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı.\nLütfen numarayı kontrol ediniz.");
        }

        // Cihaz Bilgileri
        rapor.append("=== CİHAZ BİLGİLERİ ===\n");
        rapor.append("Sayın ").append(bulunanCihaz.getSahip().getAd())
                .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append(",\n");
        rapor.append("Marka/Model: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");
        rapor.append("Tür: ").append(bulunanCihaz.getCihazTuru()).append("\n");

        String garantiDurumu = bulunanCihaz.isGarantiAktif() ? "AKTİF" : "BİTMİŞ";
        rapor.append("Garanti Durumu: ").append(garantiDurumu).append("\n");
        rapor.append("Garanti Bitiş: ").append(bulunanCihaz.getGarantiBitisTarihi()).append("\n\n");

        // Servis Kayıtları
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
            LocalDate giris = bulunanKayit.getGirisTarihi();
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(giris, 20);

            rapor.append("\n----------------------------------\n");
            rapor.append("TAHMİNİ TESLİM TARİHİ: ").append(tahminiTeslim).append("\n");
            rapor.append("(İşlem süresi standart 20 iş günüdür.)\n");
        } else {
            rapor.append("=== SERVİS DURUMU ===\n");
            rapor.append("Bu cihaz için aktif bir servis kaydı bulunmamaktadır.\n");
        }

        return rapor.toString();
    }
}