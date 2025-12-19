package Servis;

// 'import java.io.Serializable;' kaldırıldı

public class Teknisyen {
    // 'serialVersionUID' kaldırıldı

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
        return ad + " (" + uzmanlikAlani + ")";
    }
}