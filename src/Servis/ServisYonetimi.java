package Servis;

import Cihazlar.Cihaz;
import Arayuzler.IVeriIslemleri;
import Araclar.Formatlayici;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ServisYonetimi implements IVeriIslemleri {
    private List<ServisKaydi> kayitlar; // Tüm servis geçmişini tutan ana liste
    private List<Cihaz> cihazListesiRef; // Cihazları seri no'dan bulmak için ana listenin bir kopyasını (referansını) tutuyoruz

    // Kayıtların tutulacağı dosyanın yolunu dinamik olarak belirliyoruz
    private static final String DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "servisler.txt";

    public ServisYonetimi(List<Cihaz> cihazListesi) {
        this.cihazListesiRef = cihazListesi; // Cihaz listesini dışarıdan alıp bağlıyoruz
        this.kayitlar = new ArrayList<>();
        Yukle(); // Program başlarken eski kayıtları dosyadan yüklüyoruz
    }

    public void servisKaydiEkle(ServisKaydi kayit) {
        kayitlar.add(kayit); // Listeye ekle
        Kaydet(); // Hemen dosyaya yaz ki elektrik kesilirse veri kaybolmasın
    }

    // Bu metot projenin kalbi: Fiyatı hesaplar, ustayı atar ve kaydı oluşturur
    public ServisKaydi yeniServisKaydiOlustur(Cihaz cihaz, String hamSorunMetni) {
        // "Ekran Kırık (Fiyat %20)" gibi gelen metni temizleyip sadece "Ekran Kırık" kısmını alıyoruz
        String temizSorunAdi = hamSorunMetni.split("\\(")[0].trim();

        // Fiyatlandırma sınıfına sorarak indirimsiz ham ücreti hesaplatıyoruz
        double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(
                hamSorunMetni,
                cihaz.getFiyat(),
                cihaz.getSahip().vipMi()
        );

        // Garantiyi devreye sokarak müşterinin ödeyeceği son tutarı buluyoruz
        double musteriOdeyecek = cihaz.getGaranti().sonMaliyetHesapla(hamUcret);
        // Cihazın türüne göre (Telefon ise telefoncu, Laptop ise laptopçu) uygun teknisyeni buluyoruz
        Teknisyen atananTeknisyen = TeknisyenDeposu.uzmanligaGoreGetir(cihaz.getCihazTuru());

        // Yeni kayıt nesnesini oluşturup bilgileri giriyoruz
        ServisKaydi yeniKayit = new ServisKaydi(cihaz, temizSorunAdi);
        yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);
        yeniKayit.setAtananTeknisyen(atananTeknisyen);

        servisKaydiEkle(yeniKayit); // Listeye ve dosyaya ekliyoruz
        return yeniKayit;
    }

    public List<ServisKaydi> getKayitlar() { return kayitlar; }
    public void kayitGuncelle() { Kaydet(); } // Bir değişiklik olduğunda dosyayı güncellemek için kısayol

    @Override
    public void Kaydet() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            // STREAM API KULLANIMI: Döngü yerine forEach kullanımı
            // Checked Exception (IOException) stream içinde yönetmek zor olduğu için
            // burada klasik döngü daha güvenli olabilir ama stream ile yazmak istersek
            // try-catch bloğunu içine almamız gerekir.
            // Dosya işlemlerinde klasik for-each bazen daha temizdir, ancak mantığı göstermek adına:
            for (ServisKaydi k : kayitlar) {
                bw.write(Formatlayici.servisKaydiMetneDonustur(k));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }

    @Override
    public void Yukle() {
        File dosya = new File(DOSYA_ADI);
        this.kayitlar = new ArrayList<>(); // Listeyi sıfırlıyoruz
        if (dosya.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                // STREAM API KULLANIMI: BufferedReader'dan satırları akış olarak alıp işliyoruz
                // lines() metodu Stream<String> döner.
                br.lines()
                        .filter(line -> !line.trim().isEmpty()) // Boş satırları filtrele
                        .map(line -> Formatlayici.metniServisKaydinaDonustur(line, this.cihazListesiRef)) // Nesneye dönüştür
                        .filter(Objects::nonNull) // Null dönenleri (hatalı satırları) filtrele
                        .forEach(kayitlar::add); // Listeye ekle

            } catch (IOException e) {
                System.err.println("Yükleme hatası.");
            } finally {
                System.out.println("Servis verileri yükleme işlemi tamamlandı (Stream API).");
            }
        }
    }

    @Override
    public void verileriTemizle() {
        kayitlar.clear(); // Listeyi boşalt
        Kaydet(); // Boş halini dosyaya yaz (yani dosyayı da temizle)
        System.out.println("Tüm servis kayıtları ve dosya içeriği temizlendi.");
    }

    // Set yapısı kullanarak aynı ismin listede tekrar etmesini engelliyoruz
    public Set<String> getBenzersizTeknisyenIsimleri() {
        // STREAM API KULLANIMI:
        // 1. Kayıtlardan akış oluştur
        // 2. Teknisyen nesnesini al
        // 3. Null olanları filtrele (Atanmamış olabilir)
        // 4. Teknisyen ismini al (Map)
        // 5. Set olarak topla (Collect)
        return kayitlar.stream()
                .map(ServisKaydi::getAtananTeknisyen)
                .filter(Objects::nonNull)
                .map(Teknisyen::getAd)
                .collect(Collectors.toSet());
    }
}