package Cihazlar;

import java.util.HashMap;
import java.util.Map;

// Cihaz türleri, markaları, modelleri ve fiyatlarını içeren statik bir veri kataloğu sınıfı.
public class CihazKatalogu {

    // Tüm cihaz hiyerarşisini (Tür -> Marka -> Modeller) tutan iç içe geçmiş (Nested Map) harita yapısı.
    private static final Map<String, Map<String, String[]>> TUM_MODELLER = new HashMap<>();

    // Her bir modelin baz fiyatını tutan harita yapısı.
    private static final Map<String, Double> MODEL_FIYATLARI = new HashMap<>();

    // Sınıf yüklendiğinde verileri bir kez hazırlamak için çalışan statik blok.
    static {
        initData();
    }

    // Veri yapılarını (Map) hardcoded (sabit) verilerle dolduran hazırlık metodu.
    private static void initData() {
        // --- TELEFON VERİLERİ ---
        Map<String, String[]> telefonModelleri = new HashMap<>();

        // Samsung modellerini ve fiyatlarını tanımlar.
        telefonModelleri.put("Samsung", new String[]{"Galaxy S24 Ultra", "Galaxy A55", "Galaxy Flip 5", "Galaxy S23 FE"});
        MODEL_FIYATLARI.put("Galaxy S24 Ultra", 70000.0);
        MODEL_FIYATLARI.put("Galaxy A55", 20000.0);
        MODEL_FIYATLARI.put("Galaxy Flip 5", 40000.0);
        MODEL_FIYATLARI.put("Galaxy S23 FE", 25000.0);

        // Apple modellerini ve fiyatlarını tanımlar.
        telefonModelleri.put("Apple", new String[]{"iPhone 15 Pro Max", "iPhone SE", "iPhone 14", "iPhone 13"});
        MODEL_FIYATLARI.put("iPhone 15 Pro Max", 85000.0);
        MODEL_FIYATLARI.put("iPhone SE", 28000.0);
        MODEL_FIYATLARI.put("iPhone 14", 45000.0);
        MODEL_FIYATLARI.put("iPhone 13", 38000.0);

        // Xiaomi modellerini ve fiyatlarını tanımlar.
        telefonModelleri.put("Xiaomi", new String[]{"Redmi Note 13 Pro", "Xiaomi 14 Ultra", "Poco X6"});
        MODEL_FIYATLARI.put("Redmi Note 13 Pro", 18000.0);
        MODEL_FIYATLARI.put("Xiaomi 14 Ultra", 55000.0);
        MODEL_FIYATLARI.put("Poco X6", 15000.0);

        // Huawei modellerini ve fiyatlarını tanımlar.
        telefonModelleri.put("Huawei", new String[]{"Pura 70 Ultra", "Mate 60 RS"});
        MODEL_FIYATLARI.put("Pura 70 Ultra", 60000.0);
        MODEL_FIYATLARI.put("Mate 60 RS", 75000.0);

        // Hazırlanan telefon haritasını ana kataloğa ekler.
        TUM_MODELLER.put("Telefon", telefonModelleri);

        // --- TABLET VERİLERİ ---
        Map<String, String[]> tabletModelleri = new HashMap<>();

        // Apple tabletlerini ve fiyatlarını tanımlar.
        tabletModelleri.put("Apple", new String[]{"iPad Pro (M4)", "iPad Air", "iPad Mini"});
        MODEL_FIYATLARI.put("iPad Pro (M4)", 45000.0);
        MODEL_FIYATLARI.put("iPad Air", 25000.0);
        MODEL_FIYATLARI.put("iPad Mini", 20000.0);

        // Samsung tabletlerini ve fiyatlarını tanımlar.
        tabletModelleri.put("Samsung", new String[]{"Galaxy Tab S9 FE", "Galaxy Tab A9+"});
        MODEL_FIYATLARI.put("Galaxy Tab S9 FE", 12000.0);
        MODEL_FIYATLARI.put("Galaxy Tab A9+", 7000.0);

        // Lenovo tabletlerini ve fiyatlarını tanımlar.
        tabletModelleri.put("Lenovo", new String[]{"Tab P12", "Yoga Tab 11", "Tab K10"});
        MODEL_FIYATLARI.put("Tab P12", 11000.0);
        MODEL_FIYATLARI.put("Yoga Tab 11", 9000.0);
        MODEL_FIYATLARI.put("Tab K10", 6000.0);

        // Huawei tabletlerini ve fiyatlarını tanımlar.
        tabletModelleri.put("Huawei", new String[]{"MatePad Pro 13.2", "MatePad Air"});
        MODEL_FIYATLARI.put("MatePad Pro 13.2", 30000.0);
        MODEL_FIYATLARI.put("MatePad Air", 18000.0);

        // Hazırlanan tablet haritasını ana kataloğa ekler.
        TUM_MODELLER.put("Tablet", tabletModelleri);

        // --- LAPTOP VERİLERİ ---
        Map<String, String[]> laptopModelleri = new HashMap<>();

        // Dell laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("Dell", new String[]{"XPS 15", "Latitude 5000", "G15 Gaming"});
        MODEL_FIYATLARI.put("XPS 15", 90000.0);
        MODEL_FIYATLARI.put("Latitude 5000", 40000.0);
        MODEL_FIYATLARI.put("G15 Gaming", 35000.0);

        // HP laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("HP", new String[]{"Spectre x360", "Pavilion 15", "Victus 16"});
        MODEL_FIYATLARI.put("Spectre x360", 60000.0);
        MODEL_FIYATLARI.put("Pavilion 15", 25000.0);
        MODEL_FIYATLARI.put("Victus 16", 32000.0);

        // Lenovo laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("Lenovo", new String[]{"ThinkPad X1 Carbon", "IdeaPad 5 Pro", "Legion Pro 7i"});
        MODEL_FIYATLARI.put("ThinkPad X1 Carbon", 80000.0);
        MODEL_FIYATLARI.put("IdeaPad 5 Pro", 30000.0);
        MODEL_FIYATLARI.put("Legion Pro 7i", 95000.0);

        // Apple laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("Apple", new String[]{"MacBook Air M3", "MacBook Pro 16"});
        MODEL_FIYATLARI.put("MacBook Air M3", 45000.0);
        MODEL_FIYATLARI.put("MacBook Pro 16", 90000.0);

        // Asus laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("Asus", new String[]{"ROG Zephyrus", "Zenbook 14 OLED", "TUF Gaming F15"});
        MODEL_FIYATLARI.put("ROG Zephyrus", 70000.0);
        MODEL_FIYATLARI.put("Zenbook 14 OLED", 40000.0);
        MODEL_FIYATLARI.put("TUF Gaming F15", 30000.0);

        // MSI laptoplarını ve fiyatlarını tanımlar.
        laptopModelleri.put("Msi", new String[]{"Stealth 16", "Titan GT77", "Katana 17"});
        MODEL_FIYATLARI.put("Stealth 16", 85000.0);
        MODEL_FIYATLARI.put("Titan GT77", 150000.0);
        MODEL_FIYATLARI.put("Katana 17", 55000.0);

        // Hazırlanan laptop haritasını ana kataloğa ekler.
        TUM_MODELLER.put("Laptop", laptopModelleri);
    }

    // Belirtilen cihaz türüne (Örn: Telefon) ait markaların listesini döndürür.
    public static String[] getMarkalar(String tur) {
        // İstenen türün katalogda olup olmadığını kontrol eder.
        if (TUM_MODELLER.containsKey(tur)) {
            // O türe ait markaları (Map key'lerini) bir diziye çevirip döndürür.
            return TUM_MODELLER.get(tur).keySet().toArray(new String[0]);
        }
        // Tür bulunamazsa boş bir dizi döndürür.
        return new String[0];
    }

    // Belirtilen tür ve markaya ait modellerin listesini döndürür.
    public static String[] getModeller(String tur, String marka) {
        // Hem türün hem de o türün içinde markanın varlığını doğrular.
        if (TUM_MODELLER.containsKey(tur) && TUM_MODELLER.get(tur).containsKey(marka)) {
            // İlgili markanın model dizisini döndürür.
            return TUM_MODELLER.get(tur).get(marka);
        }
        return new String[0];
    }

    // Model ismine göre fiyat bilgisini döndürür.
    public static double getFiyat(String model) {
        // Eğer model haritada varsa fiyatı, yoksa varsayılan olarak 0.0 döndürür.
        return MODEL_FIYATLARI.getOrDefault(model, 0.0);
    }

    // Verilen model isminin fiyat listesinde kayıtlı olup olmadığını kontrol eder.
    public static boolean fiyatMevcutMu(String model) {
        // MODEL_FIYATLARI haritasında bu modelin anahtar olarak bulunup bulunmadığını sorgular.
        return MODEL_FIYATLARI.containsKey(model);
    }
}