package Servis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServisYonetimi {
    private List<ServisKaydi> kayitlar;
    private static final String DOSYA_ADI = "servisler.txt"; // TXT olarak güncellendi

    public ServisYonetimi() {
        this.kayitlar = new ArrayList<>();
        yukle();
    }

    public void servisKaydiEkle(ServisKaydi kayit) {
        kayitlar.add(kayit);
        kaydet();
    }

    public List<ServisKaydi> getKayitlar() {
        return kayitlar;
    }

    public void kayitGuncelle() {
        kaydet();
    }

    public void kaydet() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (ServisKaydi k : kayitlar) {
                bw.write(k.toTxtFormat());
                bw.newLine();
            }
            // System.out.println("Servis kayıtları txt olarak kaydedildi.");
        } catch (IOException e) {
            System.err.println("Servis kayıtları kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    public void yukle() {
        File dosya = new File(DOSYA_ADI);
        this.kayitlar = new ArrayList<>();

        if (dosya.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        ServisKaydi k = ServisKaydi.fromTxtFormat(line);
                        if(k != null) kayitlar.add(k);
                    }
                }
                System.out.println("Servis kayıtları yüklendi: " + kayitlar.size());
            } catch (IOException e) {
                System.err.println("Servis kayıtları yüklenirken hata oluştu.");
            }
        }
    }
}