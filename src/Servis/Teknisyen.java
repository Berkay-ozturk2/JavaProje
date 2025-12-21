package Servis;

public class Teknisyen {

    private String ad;
    private String uzmanlikAlani;
    private int teknisyenId;      // Her teknisyenin kendine ait benzersiz numarası
    private static int nextId = 1; // Tüm teknisyenlerin paylaştığı ortak sayaç (Static)

    // İsim ve uzmanlık alanını alarak teknisyeni oluşturan ana kurucu metot
    public Teknisyen(String ad, String uzmanlikAlani) {
        this.teknisyenId = nextId++; // Önce numarayı ver, sonra sayacı bir artır
        this.ad = ad;
        this.uzmanlikAlani = uzmanlikAlani;
    }

    // --- EKLENEN CONSTRUCTOR (Overloading) ---
    // Eğer uzmanlık girilmezse varsayılan olarak "Genel Bakım" atayan alternatif kurucu
    public Teknisyen(String ad) {
        this(ad, "Genel Bakım"); // Diğer kurucuyu çağırarak kod tekrarını önledik
    }

    // Bilgilere dışarıdan erişmek için kullanılan Getter metotları
    public int getTeknisyenId() { return teknisyenId; }
    public String getAd() { return ad; }
    public String getUzmanlikAlani() { return uzmanlikAlani; }

    // Teknisyen nesnesini ekrana yazdırdığımızda ID ve isim düzgün görünsün diye yaptık
    @Override
    public String toString() {
        return "[" + teknisyenId + "] " + ad + " (" + uzmanlikAlani + ")";
    }
}