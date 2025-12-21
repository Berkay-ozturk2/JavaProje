package Araclar;

import Cihazlar.Cihaz;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DosyaIslemleri {

    /**
     * Belirtilen dosyadan cihaz verilerini okur ve listeye çevirir.
     */
    public static List<Cihaz> cihazlariYukle(String dosyaAdi) {
        List<Cihaz> liste = new ArrayList<>();
        File dosya = new File(dosyaAdi);
        if (!dosya.exists()) return liste;//dosya mevcutsa mevcut listeyi döndürür

        //Dosyayı okuma işlemi
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosya), "UTF-8"))) {// UTF-8 Alfabetik değeri dikkate alır
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Cihaz c = Formatlayici.metniCihazaDonustur(line);
                    if (c != null) liste.add(c);
                }
            }
        } catch (IOException e) { //Dosya okumada hata olursa bu blok çalışır.
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        return liste;
    }

    /**
     * Cihaz listesini belirtilen dosyaya kaydeder.
     */
    public static void cihazlariKaydet(List<Cihaz> liste, String dosyaAdi) throws IOException {
        //Dosyaya yazdırma işlemleri
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dosyaAdi), "UTF-8"))) {
            for (Cihaz c : liste) {
                bw.write(Formatlayici.cihazMetneDonustur(c));
                bw.newLine();
            }
        }
    }
}