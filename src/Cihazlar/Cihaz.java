package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Garantiler.*;
import Istisnalar.GecersizDegerException;

public abstract class Cihaz {
    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private Musteri sahip;

    protected Garanti garanti;

    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic, Musteri sahip) {
        this.seriNo = seriNo;
        this.marka = marka;
        this.model = model;
        this.fiyat = fiyat;
        this.sahip = sahip;

        LocalDate baslangic;
        if (garantiBaslangic == null) {
            baslangic = Garanti.rastgeleBaslangicOlustur(getGarantiSuresiYil());
        } else {
            baslangic = garantiBaslangic;
        }
        this.garanti = new StandartGaranti(baslangic, getGarantiSuresiYil());
    }

    // --- KALDIRILAN METOTLAR ---
    // rastgeleSeriNoUret -> Araclar.KodUretici sınıfına taşındı.
    // toTxtFormat ve fromTxtFormat -> Araclar.Formatlayici sınıfına taşındı.

    // Getter & Setter
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public Musteri getSahip() { return sahip; }
    public Garanti getGaranti() { return garanti; }
    public LocalDate getGarantiBaslangic() { return garanti.getBaslangicTarihi(); }
    public LocalDate getGarantiBitisTarihi() { return garanti.getBitisTarihi(); }
    public boolean isGarantiAktif() { return garanti.isDevamEdiyor(); }

    public void setFiyat(double fiyat) throws GecersizDegerException {
        if (fiyat < 0) throw new GecersizDegerException("Fiyat negatif olamaz!");
        this.fiyat = fiyat;
    }

    public void setMarka(String marka) throws GecersizDegerException {
        if (marka == null || marka.trim().isEmpty()) throw new GecersizDegerException("Marka alanı boş bırakılamaz.");
        this.marka = marka;
    }

    public void setModel(String model) throws GecersizDegerException {
        if (model == null || model.trim().isEmpty()) throw new GecersizDegerException("Model alanı boş bırakılamaz.");
        this.model = model;
    }

    public int getEkstraGarantiSuresiAy() {
        return garanti.hesaplaEkstraSureAy(getGarantiSuresiYil());
    }

    public void garantiUzat(int ay) {
        this.garanti.sureUzat(ay);
        if (this.garanti instanceof StandartGaranti) {
            int toplamEkstraSure = getEkstraGarantiSuresiAy();
            this.garanti = new UzatilmisGaranti(
                    this.garanti.getBaslangicTarihi(),
                    getGarantiSuresiYil(),
                    toplamEkstraSure
            );
        }
    }

    public abstract int getGarantiSuresiYil();
    public abstract String getCihazTuru();

    @Override
    public String toString() {
        String garantiDurumu = isGarantiAktif() ? "Aktif" : "Sona Ermiş";
        String ekstraBilgi = getEkstraGarantiSuresiAy() > 0 ? " (+" + getEkstraGarantiSuresiAy() + " Ay Uzatıldı)" : "";
        return String.format("%s [%s - %s] (Seri No: %s) - Garanti: %s%s",
                getCihazTuru(), marka, model, seriNo, garantiDurumu, ekstraBilgi);
    }
}