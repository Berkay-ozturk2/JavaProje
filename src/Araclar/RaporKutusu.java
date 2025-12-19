package Araclar;

import java.util.List;

public class RaporKutusu<T> {
    private List<T> veriListesi;

    public RaporKutusu(List<T> veriListesi) {
        this.veriListesi = veriListesi;
    }

    public void listeyiKonsolaYazdir() {
        if (veriListesi == null || veriListesi.isEmpty()) {
            System.out.println("Liste boş.");
            return;
        }
        System.out.println("--- Rapor Kutusu İçeriği ---");
        for (T eleman : veriListesi) {
            System.out.println(eleman.toString());
        }
    }

    public T getIlkEleman() {
        if (veriListesi != null && !veriListesi.isEmpty()) {
            return veriListesi.get(0);
        }
        return null;
    }

    // --- EKLENEN WILDCARD (?) KULLANIMI ---
    // GEREKSİNİM: List<?> veya List<? extends Number> kullanımı
    public void wildcardTest(List<? extends Number> sayilar) {
        System.out.println("[Wildcard Test] Sayısal Veriler:");
        for (Number n : sayilar) {
            System.out.println("- " + n);
        }
    }
}