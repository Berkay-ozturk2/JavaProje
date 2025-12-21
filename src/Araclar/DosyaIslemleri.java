package Araclar;

import Cihazlar.Cihaz;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Cihaz verilerinin dosyadan okunmasını ve dosyaya kaydedilmesini sağlayan yardımcı sınıf.
public class DosyaIslemleri {


    //Belirtilen dosyadan cihaz verilerini okur ve listeye çevirir.
    public static List<Cihaz> cihazlariYukle(String dosyaAdi) {
        // Okunan cihazları bellekte tutmak için boş bir liste oluşturur.
        List<Cihaz> liste = new ArrayList<>();

        // İşlem yapılacak dosyayı temsil eden nesneyi oluşturur.
        File dosya = new File(dosyaAdi);

        // Eğer dosya fiziksel olarak diskte yoksa boş listeyi döndürür.
        if (!dosya.exists()) return liste;

        // Dosyayı UTF-8 karakter kodlamasıyla güvenli bir şekilde okumak için akışı açar.
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosya), "UTF-8"))) {

            String line;
            // Dosyadaki satırları sonuna gelene kadar tek tek okur.
            while ((line = br.readLine()) != null) {
                // Okunan satırın boş veya sadece boşluktan ibaret olmadığını kontrol eder.
                if (!line.trim().isEmpty()) {
                    // Metin halindeki satırı Formatlayici sınıfı yardımıyla Cihaz nesnesine dönüştürür.
                    Cihaz c = Formatlayici.metniCihazaDonustur(line);

                    // Dönüştürme işlemi başarılı olduysa cihazı listeye ekler.
                    if (c != null) liste.add(c);
                }
            }
        } catch (IOException e) {
            // Dosya okuma sürecinde bir hata oluşursa hatayı yakalar ve konsola yazdırır.
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        // Dosyadan başarıyla okunan tüm cihazların listesini döndürür.
        return liste;
    }


    //Cihaz listesini belirtilen dosyaya kaydeder.
    public static void cihazlariKaydet(List<Cihaz> liste, String dosyaAdi) throws IOException {
        // Verileri UTF-8 kodlamasıyla belirtilen dosyaya yazmak için bir yazıcı nesnesi oluşturur.
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(dosyaAdi), "UTF-8"))) {

            // Listedeki her bir cihaz üzerinde döngü kurar.
            for (Cihaz c : liste) {
                // Cihaz nesnesini uygun metin formatına çevirip dosyaya yazar.
                bw.write(Formatlayici.cihazMetneDonustur(c));

                // Bir sonraki kayıt için yeni bir satıra geçer.
                bw.newLine();
            }
        }
    }
}