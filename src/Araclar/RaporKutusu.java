package Araclar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @param <T> Bu kutunun içinde tutulacak veri tipi.
 */
// Her türden veriyi (Cihaz, ServisKaydi vb.) liste halinde tutup raporlayabilen jenerik (generic) sınıf.
public class RaporKutusu<T> {
    private List<T> veriListesi;

    // RaporKutusu Constructor
    public RaporKutusu(List<T> veriListesi) {
        // Sınıf oluşturulurken raporlanacak veri listesini alıp dahili değişkene atar.
        this.veriListesi = veriListesi;
    }

    // Listeyi terminale yazdırma Metodu
    public void listeyiKonsolaYazdir() {
        // Liste boşsa işlem yapmadan kullanıcıyı bilgilendirir.
        if (veriListesi == null || veriListesi.isEmpty()) {
            System.out.println("Liste boş.");
            return;
        }
        System.out.println("--- Rapor Kutusu İçeriği ---");

        // Listedeki tüm elemanları numaralandırarak sırayla konsola yazdırır.
        for (int i = 0; i < veriListesi.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + veriListesi.get(i).toString());
        }
    }

    // Listedeki ilk elemanı terminale yazdıran Metot.
    public T getIlkEleman() {
        // Listenin boş olup olmadığını kontrol ederek güvenli bir şekilde ilk elemanı döndürür.
        if (veriListesi != null && !veriListesi.isEmpty()) {
            return veriListesi.get(0);
        }
        return null;
    }

    // Wildcard kullanımı Ürün Fiyatlarını Terminale Yazdıran Metot
    public void wildcardTest(List<? extends Number> sayilar) {
        System.out.println("[Wildcard Test] Sayısal Veriler:");
        // Wildcard (?) kullanarak Number sınıfından türeyen (Integer, Double vb.) sayısal listeleri ekrana yazar.
        for (Number n : sayilar) {
            System.out.println("- " + n);
        }
    }

    // Terminale tek bir veri yazdıran metot(Terminale yazdırma saati)
    public <E> void tekElemanYazdir(E veri) {
        // Parametre olarak gelen herhangi bir tipteki veriyi analiz başlığıyla ekrana basar.
        if (veri != null) {
            System.out.println("Tekil Veri Analizi: " + veri.toString());
        }
    }

    // Bu metot, verilen herhangi bir listeyi ucuzdan pahalıya doğru sıralar.
    public <E> void genericSirala(List<E> liste, Comparator<E> kiyaslayici) {
        // Verilen listeyi, belirtilen karşılaştırma kuralına (Comparator) göre sıralar.
        if (liste != null && !liste.isEmpty()) {
            Collections.sort(liste, kiyaslayici);
            System.out.println(" - Liste generic metot içerisinde başarıyla sıralandı.");
        }
    }
}