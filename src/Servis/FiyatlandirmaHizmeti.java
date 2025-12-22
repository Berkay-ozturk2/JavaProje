package Servis;

public class FiyatlandirmaHizmeti {

    // Vergi oranı, en düşük ücret ve para birimi gibi değişmeyen sabitleri burada tanımladık
    public static final byte KDV_ORANI_YUZDE = 20;
    public static final short MIN_SERVIS_UCRETI = 200;
    public static final char PARA_BIRIMI = '₺';
    public static final float INDIRIM_ORANI = 0.15f;

    // Normal ve VIP müşteriler için farklı fiyat çarpanlarını iki boyutlu dizide tuttuk
    // Satır 0: Normal Müşteri, Satır 1: VIP Müşteri
    private static final double[][] FIYAT_KATSAYILARI = {
            {1.0, 1.2}, // Standart Müşteri
            {0.8, 1.0}  // VIP Müşteri (%20 İndirimli)
    };

    // Ekranda gösterilecek arıza seçeneklerini bir liste olarak geri döndürüyoruz
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
                "Genel Bakım ve Temizlik (Sabit 750 TL)" };
    }

    // --- EKLENEN METOT (Refactoring Sonrası Buraya Taşındı) ---
    // Seçilen garanti süresine göre cihaz fiyatı üzerinden ek paket ücretini hesaplıyoruz
    public static double paketFiyatiHesapla(double cihazFiyati, int ay) {
        switch (ay) {
            case 6: return cihazFiyati * 0.05;
            case 12: return cihazFiyati * 0.07;
            case 24: return cihazFiyati * 0.10;
            case 36: return cihazFiyati * 0.15;
            default: return 0.0;
        }
    }

    // İmzayı değiştirdik: boolean isVip parametresi eklendi
    // Sorunun türüne ve müşterinin VIP olup olmadığına bakarak tamir ücretini hesaplıyoruz
    public static double tamirUcretiHesapla(String secilenSorun, double cihazFiyati, boolean isVip) {
        if (secilenSorun == null) return 0.0;

        double ucret;

        // Seçilen sorunun içinde geçen kelimeye göre fiyatlandırma mantığını çalıştırıyoruz
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

        // DİNAMİK KATSAYI SEÇİMİ
        // Müşteri VIP ise 1. satır (indirimli), değilse 0. satır (standart) bilgilerini seçiyoruz
        int musteriTipiIndex = isVip ? 1 : 0;

        // İki boyutlu diziden belirlediğimiz katsayı ile ücreti çarpıp sonucu döndürüyoruz
        return ucret * FIYAT_KATSAYILARI[musteriTipiIndex][0];
    }

    // Her 5. servise özel kampanya yapmak için mod alma işlemi kullandık
    public static boolean kampanyaVarMi(int servisId) {
        return (servisId % 5) == 0;
    }
}