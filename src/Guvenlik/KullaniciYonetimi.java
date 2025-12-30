package Guvenlik;

import java.util.ArrayList;
import java.util.List;

public class KullaniciYonetimi {
    private static final List<Kullanici> kullanicilar = new ArrayList<>();

    static {
        // Kullanıcıları ekliyoruz - Şifrelerde boşluk olmamasına dikkat edin
        kullanicilar.add(new Kullanici("admin", "1234", KullaniciRol.ADMIN));
        kullanicilar.add(new Kullanici("teknik", "1234", KullaniciRol.TEKNISYEN));

        // Debug: Listenin dolup dolmadığını konsola yazalım
        System.out.println("DEBUG: Kullanıcı Yönetimi yüklendi. Toplam Kullanıcı: " + kullanicilar.size());
        for (Kullanici k : kullanicilar) {
            System.out.println(" - Kayıtlı Kullanıcı: " + k.getKullaniciAdi() + " | Şifre: " + k.getSifre());
        }
    }

    public static Kullanici dogrula(String kAdi, String sifre) {
        // Null kontrolü ekleyelim
        if (kAdi == null || sifre == null) {
            System.out.println("DEBUG: Kullanıcı adı veya şifre boş geldi.");
            return null;
        }

        System.out.println("DEBUG: Doğrulama İsteği -> Gelen Ad: '" + kAdi + "' | Gelen Şifre: '" + sifre + "'");

        // Gelen verideki olası boşlukları temizleyelim (trim)
        String temizKAdi = kAdi.trim();
        // Şifrede trim() kullanmak riskli olabilir (kullanıcı bilerek boşluklu şifre koymuş olabilir)
        // ancak basit bir sistem için başta ve sonda boşlukları temizlemek kullanıcı dostudur.
        String temizSifre = sifre;

        for (Kullanici k : kullanicilar) {
            // Detaylı kontrol: Kullanıcı adı (büyük/küçük harf duyarsız) VE Şifre (birebir eşleşme)
            boolean adEslesiyor = k.getKullaniciAdi().equalsIgnoreCase(temizKAdi);
            boolean sifreEslesiyor = k.getSifre().equals(temizSifre);

            if (adEslesiyor && sifreEslesiyor) {
                System.out.println("DEBUG: Eşleşme BAŞARILI! Giriş yapan: " + k.getKullaniciAdi());
                return k;
            } else if (adEslesiyor) {
                System.out.println("DEBUG: Kullanıcı adı doğru ama şifre yanlış.");
            }
        }

        System.out.println("DEBUG: Eşleşme BAŞARISIZ. Kullanıcı bulunamadı.");
        return null;
    }
}