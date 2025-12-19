package Servis;

import Cihazlar.Cihaz;
import Arayuzler.VeriIslemleri;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet; // GEREKSİNİM: 3. Koleksiyon Türü
import java.util.List;
import java.util.Set;

public class ServisYonetimi implements VeriIslemleri {
    private List<ServisKaydi> kayitlar;
    private List<Cihaz> cihazListesiRef;
    private static final String DOSYA_ADI = "servisler.txt";

    public ServisYonetimi(List<Cihaz> cihazListesi) {
        this.cihazListesiRef = cihazListesi;
        this.kayitlar = new ArrayList<>();
        Yukle();
    }

    public void servisKaydiEkle(ServisKaydi kayit) {
        kayitlar.add(kayit);
        Kaydet();
    }

    public List<ServisKaydi> getKayitlar() { return kayitlar; }
    public void kayitGuncelle() { Kaydet(); }

    @Override
    public void Kaydet() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (ServisKaydi k : kayitlar) {
                bw.write(k.toTxtFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }

    @Override
    public void Yukle() {
        File dosya = new File(DOSYA_ADI);
        this.kayitlar = new ArrayList<>();
        if (dosya.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        ServisKaydi k = ServisKaydi.fromTxtFormat(line, this.cihazListesiRef);
                        if (k != null) kayitlar.add(k);
                    }
                }
            } catch (IOException e) {
                System.err.println("Yükleme hatası.");
            }
        }
    }

    // GEREKSİNİM: Interface'in 3. metodu
    @Override
    public void verileriTemizle() {
        // do-while döngüsü örneği (Gereksinim 9)
        int i = 0;
        do {
            // Simülasyon: Veriler bellekte temizleniyor...
            i++;
        } while (i < 1);

        kayitlar.clear();
        System.out.println("Bellekteki servis kayıtları temizlendi.");
    }

    // GEREKSİNİM: HashSet Kullanımı (ArrayList ve HashMap dışında 3. tür)
    public Set<String> getBenzersizTeknisyenIsimleri() {
        Set<String> teknisyenler = new HashSet<>();
        for (ServisKaydi sk : kayitlar) {
            if (sk.getAtananTeknisyen() != null) {
                teknisyenler.add(sk.getAtananTeknisyen().getAd());
            }
        }
        return teknisyenler;
    }
}