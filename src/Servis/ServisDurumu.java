// src/Servis/ServisDurumu.java
package Servis;

// Durumları standartlaştırmak için Enum kullanımı
public enum ServisDurumu {
    KABUL_EDILDI("Kabul Edildi"),
    TAMAMLANDI("Tamamlandı");

    private final String gorunurAd;

    ServisDurumu(String gorunurAd) {
        this.gorunurAd = gorunurAd;
    }

    @Override
    public String toString() {
        return gorunurAd;
    }
}