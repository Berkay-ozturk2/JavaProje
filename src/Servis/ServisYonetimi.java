package Servis;

import Cihazlar.Cihaz; // --- EKLENDİ: Cihaz sınıfını kullanabilmek için ---
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServisYonetimi {
    private List<ServisKaydi> kayitlar;
    private static final String DOSYA_ADI = "servisler.txt";

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
                // System.out.println("Servis kayıtları yüklendi: " + kayitlar.size());
            } catch (IOException e) {
                System.err.println("Servis kayıtları yüklenirken hata oluştu.");
            }
        }
    }

    // --- YENİ EKLENEN METOT ---
    // Bu metot, servisler.txt'den gelen "Bilinmiyor" müşterili kayıtları,
    // cihazlar.txt'den gelen "Gerçek" müşterili cihazlarla değiştirir.
    public void cihazBilgileriniEslestir(List<Cihaz> guncelCihazListesi) {
        for (ServisKaydi kayit : kayitlar) {
            String servisSeriNo = kayit.getCihaz().getSeriNo();

            // Cihaz listesinde bu seri numarasına sahip cihazı bul
            for (Cihaz gercekCihaz : guncelCihazListesi) {
                if (gercekCihaz.getSeriNo().equalsIgnoreCase(servisSeriNo)) {
                    // Servis kaydındaki eksik cihazı, müşteri bilgisi olan cihazla değiştir
                    kayit.setCihaz(gercekCihaz);
                    break;
                }
            }
        }
    }
}