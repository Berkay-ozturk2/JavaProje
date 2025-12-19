package Servis;

import Cihazlar.Cihaz;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Arayuzler.VeriIslemleri;

public class ServisYonetimi implements VeriIslemleri {
    private List<ServisKaydi> kayitlar;
    // --- DÜZELTME: Cihaz listesini referans olarak tutuyoruz ---
    private List<Cihaz> cihazListesiRef;
    private static final String DOSYA_ADI = "servisler.txt";

    // --- DÜZELTME: Constructor parametre alıyor ---
    public ServisYonetimi(List<Cihaz> cihazListesi) {
        this.cihazListesiRef = cihazListesi;
        this.kayitlar = new ArrayList<>();
        Yukle();
    }

    public void servisKaydiEkle(ServisKaydi kayit) {
        kayitlar.add(kayit);
        Kaydet();
    }

    public List<ServisKaydi> getKayitlar() {
        return kayitlar;
    }

    public void kayitGuncelle() {
        Kaydet();
    }

    @Override
    public void Kaydet() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (ServisKaydi k : kayitlar) {
                bw.write(k.toTxtFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Servis kayıtları kaydedilirken hata oluştu: " + e.getMessage());
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
                        // --- DÜZELTME: Listeyi parametre olarak gönderiyoruz ---
                        ServisKaydi k = ServisKaydi.fromTxtFormat(line, this.cihazListesiRef);
                        if (k != null) kayitlar.add(k);
                    }
                }
            } catch (IOException e) {
                System.err.println("Servis kayıtları yüklenirken hata oluştu.");
            }
        }
    }

    // 'cihazBilgileriniEslestir' metodu tamamen silindi.
}