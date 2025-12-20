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

    // --- GUI'DEN TAŞINAN İŞ MANTIĞI ---
    /**
     * Cihaz ve sorun bilgisini alıp, tüm hesaplamaları yaparak (Fiyat, Teknisyen, vb.)
     * yeni bir servis kaydı oluşturur ve sisteme ekler.
     */
    public ServisKaydi yeniServisKaydiOlustur(Cihaz cihaz, String hamSorunMetni) {
        // 1. Sorun adını temizle (Örn: "Ekran Kırık (Fiyat %20)" -> "Ekran Kırık")
        String temizSorunAdi = hamSorunMetni.split("\\(")[0].trim();

        // 2. Fiyat Hesapla
        double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(
                hamSorunMetni,
                cihaz.getFiyat(),
                cihaz.getSahip().isVip()
        );

        // 3. Garanti Kontrolü ve Son Fiyat
        double musteriOdeyecek = cihaz.getGaranti().sonMaliyetHesapla(hamUcret);

        // 4. Teknisyen Ata
        Teknisyen atananTeknisyen = TeknisyenDeposu.uzmanligaGoreGetir(cihaz.getCihazTuru());

        // 5. Kaydı Oluştur
        ServisKaydi yeniKayit = new ServisKaydi(cihaz, temizSorunAdi);
        yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);
        yeniKayit.setAtananTeknisyen(atananTeknisyen);

        // 6. Listeye Ekle ve Kaydet
        servisKaydiEkle(yeniKayit);

        return yeniKayit;
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
        Kaydet();
        System.out.println("Tüm servis kayıtları ve dosya içeriği temizlendi.");
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