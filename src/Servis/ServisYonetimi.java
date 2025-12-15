package Servis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServisYonetimi {
    private List<ServisKaydı> kayitlar;
    private static final String DOSYA_ADI = "servis_kayitlari.ser"; // Kayıtların tutulacağı dosya adı

    @SuppressWarnings("unchecked")
    public ServisYonetimi() {
        this.kayitlar = new ArrayList<>();
        yukle(); // Yöneticinin oluşturulmasıyla birlikte kayıtları yüklemeyi dene
    }

    public void servisKaydiEkle(ServisKaydı kayit) {
        kayitlar.add(kayit);
        kaydet(); // Her eklemeden sonra dosyaya kaydet
    }

    public List<ServisKaydı> getKayitlar() {
        return kayitlar;
    }

    // Gerekirse bir kaydın durumunu güncelledikten sonra kaydetmek için
    public void kayitGuncelle() {
        kaydet();
    }

    /**
     * Servis kayıtlarını dosyaya kaydeder (Serialization).
     */
    public void kaydet() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DOSYA_ADI))) {
            oos.writeObject(kayitlar);
            System.out.println("Servis kayıtları başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("Servis kayıtları kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    /**
     * Servis kayıtlarını dosyadan yükler (Deserialization).
     */
    public void yukle() {
        File dosya = new File(DOSYA_ADI);
        if (dosya.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dosya))) {
                // List<ServisKaydı> tipine güvenli dönüşüm
                this.kayitlar = (List<ServisKaydı>) ois.readObject();
                System.out.println("Servis kayıtları başarıyla yüklendi. Toplam kayıt: " + kayitlar.size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Servis kayıtları yüklenirken hata oluştu. Yeni liste oluşturuluyor.");
                this.kayitlar = new ArrayList<>();
            }
        } else {
            System.out.println("Servis kayıt dosyası bulunamadı. Yeni bir servis listesi oluşturuluyor.");
        }
    }
}