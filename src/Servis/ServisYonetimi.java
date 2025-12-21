package Servis;

import Cihazlar.Cihaz;
import Arayuzler.IVeriIslemleri;
import Araclar.Formatlayici; // YENİ IMPORT

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServisYonetimi implements IVeriIslemleri {
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

    public ServisKaydi yeniServisKaydiOlustur(Cihaz cihaz, String hamSorunMetni) {
        String temizSorunAdi = hamSorunMetni.split("\\(")[0].trim();

        double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(
                hamSorunMetni,
                cihaz.getFiyat(),
                cihaz.getSahip().isVip()
        );

        double musteriOdeyecek = cihaz.getGaranti().sonMaliyetHesapla(hamUcret);
        Teknisyen atananTeknisyen = TeknisyenDeposu.uzmanligaGoreGetir(cihaz.getCihazTuru());

        ServisKaydi yeniKayit = new ServisKaydi(cihaz, temizSorunAdi);
        yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);
        yeniKayit.setAtananTeknisyen(atananTeknisyen);

        servisKaydiEkle(yeniKayit);
        return yeniKayit;
    }

    public List<ServisKaydi> getKayitlar() { return kayitlar; }
    public void kayitGuncelle() { Kaydet(); }

    @Override
    public void Kaydet() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (ServisKaydi k : kayitlar) {
                // DEĞİŞİKLİK: Formatlayici kullanımı
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
        this.kayitlar = new ArrayList<>();
        if (dosya.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        // DEĞİŞİKLİK: Formatlayici kullanımı
                        ServisKaydi k = Formatlayici.metniServisKaydinaDonustur(line, this.cihazListesiRef);
                        if (k != null) kayitlar.add(k);
                    }
                }
            } catch (IOException e) {
                System.err.println("Yükleme hatası.");
            } finally {
                System.out.println("Servis verileri yükleme işlemi tamamlandı.");
            }
        }
    }

    @Override
    public void verileriTemizle() {
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