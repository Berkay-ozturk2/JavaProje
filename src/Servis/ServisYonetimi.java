package Servis;

import Cihazlar.Cihaz;
import Arayuzler.IVeriIslemleri;
import Araclar.Formatlayici;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ServisYonetimi implements IVeriIslemleri {
    private List<ServisKaydi> kayitlar;
    private List<Cihaz> cihazListesiRef;

    // DEĞİŞİKLİK 1: Artık final sabit değil, değişken bir dosya yolu var.
    private String dosyaYolu;

    // DEĞİŞİKLİK 2: Constructor artık dosya yolunu dışarıdan istiyor.
    public ServisYonetimi(List<Cihaz> cihazListesi, String dosyaYolu) {
        this.cihazListesiRef = cihazListesi;
        this.dosyaYolu = dosyaYolu; // Gelen yolu hafızaya atıyoruz
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
                cihaz.getSahip().vipMi()
        );

        double musteriOdeyecek = cihaz.getGaranti().sonMaliyetHesapla(hamUcret);
        Teknisyen atananTeknisyen = TeknisyenDeposu.uzmanligaGoreGetir(cihaz.getCihazTuru());

        ServisKaydi yeniKayit = new ServisKaydi(cihaz, temizSorunAdi);
        yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);
        yeniKayit.setAtananTeknisyen(atananTeknisyen);

        if (atananTeknisyen != null) {
            yeniKayit.islemEkle("Teknisyen Atandı: " + atananTeknisyen.getAd() + " (" + atananTeknisyen.getUzmanlikAlani() + ")");
        }

        servisKaydiEkle(yeniKayit);
        return yeniKayit;
    }

    public List<ServisKaydi> getKayitlar() { return kayitlar; }
    public void kayitGuncelle() { Kaydet(); }

    @Override
    public void Kaydet() {
        // DEĞİŞİKLİK 3: Sabit isim yerine 'this.dosyaYolu' kullanıyoruz
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.dosyaYolu))) {
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
        // DEĞİŞİKLİK 4: Yüklerken de dinamik yolu kullanıyoruz
        File dosya = new File(this.dosyaYolu);
        this.kayitlar = new ArrayList<>();
        if (dosya.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                br.lines()
                        .filter(line -> !line.trim().isEmpty())
                        .map(line -> Formatlayici.metniServisKaydinaDonustur(line, this.cihazListesiRef))
                        .filter(Objects::nonNull)
                        .forEach(kayitlar::add);

            } catch (IOException e) {
                System.err.println("Yükleme hatası.");
            } finally {
                System.out.println("Veriler şuradan yüklendi: " + this.dosyaYolu);
            }
        }
    }

    @Override
    public void verileriTemizle() {
        kayitlar.clear();
        Kaydet();
        System.out.println("Tüm servis kayıtları temizlendi.");
    }

    public Set<String> getBenzersizTeknisyenIsimleri() {
        return kayitlar.stream()
                .map(ServisKaydi::getAtananTeknisyen)
                .filter(Objects::nonNull)
                .map(Teknisyen::getAd)
                .collect(Collectors.toSet());
    }
}