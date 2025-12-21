package Araclar;

import java.util.Random;

//Sistemdeki benzersiz kodları ve seri numaralarını üretmekten sorumlu yardımcı sınıf.
public class KodUretici {

    // Belirtilen cihaz türüne göre rastgele bir seri numarası üretir.
    public static String rastgeleSeriNoUret(String tur) {
        String cihaz = switch (tur) {
            case "Telefon" -> "TEL";
            case "Tablet" -> "TAB";
            case "Laptop" -> "LAP";
            default -> "DEV";
        };

        Random rnd = new Random();
        //StringBuilder sürekli değişen metinler için kullanılır
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789";

        //4 Tane random sayı üreten do-while döngüsü
        int i = 0;
        do {
            //append() sona ekleme yapar
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
            i++;
        } while (i < 4);

        //cihaz türünü ve random 4 sayıyı birleştirir
        return cihaz + "-" + sb.toString();
    }
}