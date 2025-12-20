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
        if (!dosya.exists()) return liste;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosya), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Cihaz sınıfındaki fabrika metodunu kullanmaya devam ediyoruz
                    Cihaz c = Cihaz.fromTxtFormat(line);
                    if (c != null) liste.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        return liste;
    }

    /**
     * Cihaz listesini belirtilen dosyaya kaydeder.
     */
    public static void cihazlariKaydet(List<Cihaz> liste, String dosyaAdi) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dosyaAdi), "UTF-8"))) {
            for (Cihaz c : liste) {
                bw.write(c.toTxtFormat());
                bw.newLine();
            }
        }
    }
}