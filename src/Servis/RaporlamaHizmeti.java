package Servis;

import Araclar.RaporKutusu;
import Cihazlar.Cihaz;
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
}