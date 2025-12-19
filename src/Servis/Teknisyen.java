package Servis;

public class Teknisyen {

    private String ad;
    private String uzmanlikAlani;
    private int teknisyenId;
    private static int nextId = 1;

    public Teknisyen(String ad, String uzmanlikAlani) {
        this.teknisyenId = nextId++;
        this.ad = ad;
        this.uzmanlikAlani = uzmanlikAlani;
    }

    // --- EKLENEN CONSTRUCTOR (Overloading) ---
    // Uzmanlık alanı belirtilmezse varsayılan olarak "Genel Bakım" atanır.
    public Teknisyen(String ad) {
        this(ad, "Genel Bakım");
    }

    public int getTeknisyenId() { return teknisyenId; }
    public String getAd() { return ad; }
    public String getUzmanlikAlani() { return uzmanlikAlani; }

    @Override
    public String toString() {
        return "[" + teknisyenId + "] " + ad + " (" + uzmanlikAlani + ")";
    }
}