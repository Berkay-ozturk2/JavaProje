package Servis;

import Cihazlar.Cihaz;
import Arayuzler.VeriIslemleri;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServisYonetimi implements VeriIslemleri {
    private List<ServisKaydi> kayitlar;
    private List<Cihaz> cihazListesiRef;

    // --- GÜNCELLENEN DOSYA YOLU (GEREKSİNİM: Platform Bağımsızlık) ---
    private static final String DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "servisler.txt";

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
            } finally {
                // --- EKLENEN FINALLY BLOĞU (GEREKSİNİM) ---
                System.out.println("Servis verileri yükleme işlemi tamamlandı (Başarılı veya Hatalı).");
            }
        }
    }

    @Override
    public void verileriTemizle() {
        int i = 0;
        do {
            i++;
        } while (i < 1);
        kayitlar.clear();
        System.out.println("Bellekteki servis kayıtları temizlendi.");
    }

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