package Servis;

public class FiyatlandirmaHizmeti {

    /**
     * Arayüzdeki ComboBox'ta gösterilecek sorun listesini döner.
     * Main.java satır 144'teki "split" işlemine uygun olarak parantez içinde fiyat bilgisi içerir.
     */
    public static String[] getSorunListesi() {
        return new String[]{
                "Ekran Kırık (Cihaz Fiyatının %20'si)",
                "Batarya/Pil Değişimi (Sabit 1500 TL)",
                "Şarj Soketi Arızası (Sabit 1000 TL)",
                "Kamera Arızası (Cihaz Fiyatının %15'i)",
                "Kasa/Kapak Değişimi (Cihaz Fiyatının %10'u)",
                "Anakart/Sıvı Teması (Cihaz Fiyatının %40'ı)",
                "Yazılım/Format (Sabit 500 TL)",
                "Dokunmatik Arızası (Cihaz Fiyatının %10'u)",
                "Hoparlör/Mikrofon Sorunu (Sabit 800 TL)",
                "Genel Bakım ve Temizlik (Sabit 750 TL)"
        };
    }

    /**
     * Seçilen soruna ve cihazın piyasa değerine göre tamir ücretini hesaplar.
     * Main.java satır 140'ta kullanılmaktadır.
     *
     * @param secilenSorun ComboBox'tan seçilen string ifade
     * @param cihazFiyati Cihazın kayıtlı piyasa değeri
     * @return Hesaplanan ham servis ücreti
     */
    public static double tamirUcretiHesapla(String secilenSorun, double cihazFiyati) {
        if (secilenSorun == null) return 0.0;

        // "Ekran Kırık (Cihaz Fiyatının %20'si)" -> Sadece "Ekran Kırık" kısmına bakarak işlem yapıyoruz.
        if (secilenSorun.contains("Ekran Kırık")) {
            return cihazFiyati * 0.20;
        } else if (secilenSorun.contains("Batarya/Pil")) {
            return 1500.0;
        } else if (secilenSorun.contains("Şarj Soketi")) {
            return 1000.0;
        } else if (secilenSorun.contains("Kamera Arızası")) {
            return cihazFiyati * 0.15;
        } else if (secilenSorun.contains("Kasa/Kapak")) {
            return cihazFiyati * 0.10;
        } else if (secilenSorun.contains("Anakart")) {
            return cihazFiyati * 0.40;
        } else if (secilenSorun.contains("Yazılım")) {
            return 500.0;
        } else if (secilenSorun.contains("Dokunmatik")) {
            return cihazFiyati * 0.10;
        } else if (secilenSorun.contains("Hoparlör")) {
            return 800.0;
        } else if (secilenSorun.contains("Genel Bakım")) {
            return 750.0;
        } else {
            // Tanımlı olmayan bir durum seçilirse standart bir arıza tespit ücreti döndür
            return 250.0;
        }
    }
}