package Araclar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @param <T> Bu kutunun içinde tutulacak veri tipi.
 */
public class RaporKutusu<T> {
    private List<T> veriListesi;

    // RaporKutusu Constructor
    public RaporKutusu(List<T> veriListesi) {
        this.veriListesi = veriListesi;
    }

    // Listeyi terminale yazdırma Metodu
    public void listeyiKonsolaYazdir() {
        if (veriListesi == null || veriListesi.isEmpty()) {
            System.out.println("Liste boş.");
            return;
        }
        System.out.println("--- Rapor Kutusu İçeriği ---");

        //Cihazları 1'den başlayarak sırayla terminale yazdırır
        for (int i = 0; i < veriListesi.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + veriListesi.get(i).toString());
        }
    }

    //Listedeki ilk elemanı terminale yazdıran Metot.
    public T getIlkEleman() {
        if (veriListesi != null && !veriListesi.isEmpty()) {
            return veriListesi.get(0);
        }
        return null;
    }

    // Wildcard kullanımı Ürün Fiyatlarını Terminale Yazdıran Metot
    public void wildcardTest(List<? extends Number> sayilar) {
        System.out.println("[Wildcard Test] Sayısal Veriler:");
        for (Number n : sayilar) {
            System.out.println("- " + n);
        }
    }

    //Terminale tek bir veri yazdıran metot(Terminale yazdırma saati)
    public <E> void tekElemanYazdir(E veri) {
        if (veri != null) {
            System.out.println("Tekil Veri Analizi: " + veri.toString());
        }
    }

    // Bu metot, verilen herhangi bir listeyi ucuzdan pahalıya doğru sıralar.
    public <E> void genericSirala(List<E> liste, Comparator<E> kiyaslayici) {
        if (liste != null && !liste.isEmpty()) {
            Collections.sort(liste, kiyaslayici);
            System.out.println(" - Liste generic metot içerisinde başarıyla sıralandı.");
        }
    }
}