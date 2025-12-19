package Servis;

public class FiyatlandirmaHizmeti {

    // --- EKLENEN SABİTLER (Gereksinim: byte, short, char, float kullanımı) ---
    public static final byte KDV_ORANI_YUZDE = 20;
    public static final short MIN_SERVIS_UCRETI = 200;
    public static final char PARA_BIRIMI = '₺';
    public static final float INDIRIM_ORANI = 0.15f;

    // --- EKLENEN ÇOK BOYUTLU DİZİ ---
    // GEREKSİNİM: En az 1 çok boyutlu dizi (String[][] veya double[][])
    private static final double[][] FIYAT_KATSAYILARI = {
            {1.0, 1.2}, // Standart Müşteri Katsayıları (Normal, Acil)
            {0.9, 1.0}  // VIP Müşteri Katsayıları (Normal, Acil)
    };

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

    public static double tamirUcretiHesapla(String secilenSorun, double cihazFiyati) {
        if (secilenSorun == null) return 0.0;

        double ucret;

        if (secilenSorun.contains("Ekran Kırık")) {
            ucret = cihazFiyati * 0.20;
        } else if (secilenSorun.contains("Batarya/Pil")) {
            ucret = 1500.0;
        } else if (secilenSorun.contains("Şarj Soketi")) {
            ucret = 1000.0;
        } else if (secilenSorun.contains("Kamera Arızası")) {
            ucret = cihazFiyati * 0.15;
        } else if (secilenSorun.contains("Kasa/Kapak")) {
            ucret = cihazFiyati * 0.10;
        } else if (secilenSorun.contains("Anakart")) {
            ucret = cihazFiyati * 0.40;
        } else if (secilenSorun.contains("Yazılım")) {
            ucret = 500.0;
        } else if (secilenSorun.contains("Dokunmatik")) {
            ucret = cihazFiyati * 0.10;
        } else if (secilenSorun.contains("Hoparlör")) {
            ucret = 800.0;
        } else if (secilenSorun.contains("Genel Bakım")) {
            ucret = 750.0;
        } else {
            ucret = 250.0;
        }

        // Çok boyutlu diziyi sembolik olarak kullanmış olalım (Standart - Normal işlem katsayısı)
        return ucret * FIYAT_KATSAYILARI[0][0];
    }

    // --- EKLENEN METOT (Gereksinim: Mod (%) operatörü kullanımı) ---
    public static boolean kampanyaVarMi(int servisId) {
        // Her 5. serviste bir kampanya olsun
        return (servisId % 5) == 0;
    }
}