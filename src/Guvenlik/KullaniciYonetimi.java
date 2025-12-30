package Guvenlik;

import java.util.ArrayList;
import java.util.List;

public class KullaniciYonetimi {
    private static final List<Kullanici> kullanicilar = new ArrayList<>();

    static {
        // Kullanıcıları ekliyoruz
        kullanicilar.add(new Kullanici("admin", "1234", KullaniciRol.ADMIN));
        kullanicilar.add(new Kullanici("teknik", "1234", KullaniciRol.TEKNISYEN));

        // Debug: Listenin dolup dolmadığını konsola yazalım
        System.out.println("DEBUG: Kullanıcı Yönetimi yüklendi. Toplam Kullanıcı: " + kullanicilar.size());
    }

    public static Kullanici dogrula(String kAdi, String sifre) {
        if (kAdi == null || sifre == null) return null;

        String temizKAdi = kAdi.trim();
        String temizSifre = sifre.trim();

        for (Kullanici k : kullanicilar) {
            if (k.getKullaniciAdi().equalsIgnoreCase(temizKAdi)) {
                if (k.getSifre().equals(temizSifre)) {
                    return k;
                }
            }
        }
        return null;
    }
}