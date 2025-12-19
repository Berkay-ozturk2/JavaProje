package Araclar;

import java.util.List;

// GEREKSİNİM: En az 1 Generic Sınıf (<T>)
public class RaporKutusu<T> {
    private List<T> veriListesi;

    public RaporKutusu(List<T> veriListesi) {
        this.veriListesi = veriListesi;
    }

    // GEREKSİNİM: Generic Metot 1
    public void listeyiKonsolaYazdir() {
        if (veriListesi == null || veriListesi.isEmpty()) {
            System.out.println("Liste boş.");
            return;
        }
        System.out.println("--- Rapor Kutusu İçeriği ---");
        // GEREKSİNİM: for-each döngüsü
        for (T eleman : veriListesi) {
            System.out.println(eleman.toString());
        }
    }

    // GEREKSİNİM: Generic Metot 2
    public T getIlkEleman() {
        if (veriListesi != null && !veriListesi.isEmpty()) {
            return veriListesi.get(0);
        }
        return null;
    }
}