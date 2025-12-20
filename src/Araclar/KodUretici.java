package Araclar;

import java.util.Random;

/**
 * Sistemdeki benzersiz kodları ve seri numaralarını üretmekten sorumlu yardımcı sınıf.
 */
public class KodUretici {

    /**
     * Belirtilen cihaz türüne göre (Örn: TEL-1234) rastgele bir seri numarası üretir.
     */
    public static String rastgeleSeriNoUret(String tur) {
        String prefix = switch (tur) {
            case "Telefon" -> "TEL";
            case "Tablet" -> "TAB";
            case "Laptop" -> "LAP";
            default -> "DEV";
        };

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789";

        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return prefix + "-" + sb.toString();
    }
}