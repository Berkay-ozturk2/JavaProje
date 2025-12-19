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

    public int getTeknisyenId() { return teknisyenId; }
    public String getAd() { return ad; }
    public String getUzmanlikAlani() { return uzmanlikAlani; }

    @Override
    public String toString() {
        // ÇIKTI FORMATI DEĞİŞTİ: Artık ID de görünüyor.
        // Örn: "[1] Osman Can Küçdemir (Laptop Onarım)"
        return "[" + teknisyenId + "] " + ad + " (" + uzmanlikAlani + ")";
    }
}