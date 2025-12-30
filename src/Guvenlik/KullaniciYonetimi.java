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
        // Null kontrolü
        if (kAdi == null || sifre == null) {
            System.out.println("DEBUG: Kullanıcı adı veya şifre boş geldi.");
            return null;
        }

        // Hem kullanıcı adı hem şifredeki boşlukları temizliyoruz (Daha kullanıcı dostu)
        String temizKAdi = kAdi.trim();
        String temizSifre = sifre.trim();

        System.out.println("DEBUG: Doğrulama İsteği -> Gelen Ad: '" + temizKAdi + "'");

        for (Kullanici k : kullanicilar) {
            // Kullanıcı adı (büyük/küçük harf duyarsız)
            boolean adEslesiyor = k.getKullaniciAdi().equalsIgnoreCase(temizKAdi);

            if (adEslesiyor) {
                // Şifre kontrolü (birebir eşleşme)
                boolean sifreEslesiyor = k.getSifre().equals(temizSifre);

                if (sifreEslesiyor) {
                    System.out.println("DEBUG: Eşleşme BAŞARILI! Giriş yapan: " + k.getKullaniciAdi());
                    return k;
                } else {
                    System.out.println("DEBUG: Kullanıcı adı doğru ama şifre yanlış.");
                    // Güvenlik gereği burada null dönüyoruz ama loglara hatayı yazdık
                }
            }
        }

        System.out.println("DEBUG: Eşleşme BAŞARISIZ. Kayıt bulunamadı.");
        return null;
    }
}