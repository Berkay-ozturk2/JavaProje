package Araclar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Generic bir sınıf örneği (Madde 5.1).
 * @param <T> Bu kutunun içinde tutulacak veri tipi.
 */
public class RaporKutusu<T> {
    private List<T> veriListesi;

    // Constructor
    public RaporKutusu(List<T> veriListesi) {
        this.veriListesi = veriListesi;
    }

    public void listeyiKonsolaYazdir() {
        if (veriListesi == null || veriListesi.isEmpty()) {
            System.out.println("Liste boş.");
            return;
        }
        System.out.println("--- Rapor Kutusu İçeriği ---");

        // Standart for döngüsü (int i = 0; ...)
        for (int i = 0; i < veriListesi.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + veriListesi.get(i).toString());
        }
    }

    public T getIlkEleman() {
        if (veriListesi != null && !veriListesi.isEmpty()) {
            return veriListesi.get(0);
        }
        return null;
    }

    // --- WILDCARD KULLANIMI (Madde 5.1 - List<? extends Number>) ---
    public void wildcardTest(List<? extends Number> sayilar) {
        System.out.println("[Wildcard Test] Sayısal Veriler:");
        for (Number n : sayilar) {
            System.out.println("- " + n);
        }
    }

    // --- GENERIC METOT 1 (Madde 5.1) ---
    // Sınıfın T tipinden bağımsız olarak çalışabilir (<E>).
    public <E> void tekElemanYazdir(E veri) {
        if (veri != null) {
            System.out.println("Tekil Veri Analizi: " + veri.toString());
        }
    }

    // --- GENERIC METOT 2 (Madde 5.1) ve SIRALAMA (Madde 5.2) ---
    // GEREKSİNİM: İkinci generic metot ve Collections.sort kullanımı.
    // Bu metot, verilen herhangi bir listeyi (<E>), verilen kurala (Comparator) göre sıralar.
    public <E> void genericSirala(List<E> liste, Comparator<E> kiyaslayici) {
        if (liste != null && !liste.isEmpty()) {
            Collections.sort(liste, kiyaslayici); // Madde 5.2: Sıralama
            System.out.println("-> Liste generic metot içerisinde başarıyla sıralandı.");
        }
    }
}