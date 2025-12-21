package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Garantiler.*;
import Istisnalar.GecersizDegerException;

public abstract class Cihaz {

    //Alanlar
    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private Musteri sahip;

    protected Garanti garanti;// Garanti sınıfından bir garanti nesnesi oluşturma

    // Cihaz Constructor (GecersizDegerException sınıfını kullanır)
    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic, Musteri sahip) throws GecersizDegerException {
        this.seriNo = seriNo;

        //setMarka, setModel ve setFiyat metotlarının çalışması için set olarak değer alıyor.
        setMarka(marka);
        setModel(model);
        setFiyat(fiyat);

        this.sahip = sahip;

        LocalDate baslangic;
        if (garantiBaslangic == null) {
            //garantiBaslangic boşsa ratsgele bir tarih üretir
            baslangic = Garanti.rastgeleBaslangicOlustur(getGarantiSuresiYil());
        } else {
            //Eğer bir tarih varsa direkt o tarihi alır
            baslangic = garantiBaslangic;
        }
        //garanti nesnesine garanti başlangıç değerini atar
        this.garanti = new StandartGaranti(baslangic, getGarantiSuresiYil());
    }

    // Getter & Setter
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public Musteri getSahip() { return sahip; }
    public Garanti getGaranti() { return garanti; }
    public LocalDate getGarantiBaslangic() { return garanti.getBaslangicTarihi(); }
    public LocalDate getGarantiBitisTarihi() { return garanti.getBitisTarihi(); }
    public boolean garantiAktifMi() { return garanti.devamEdiyorMu(); } //Garanti Devam ediyor mu

    //Fiyat değeri atama setterı
    public void setFiyat(double fiyat) throws GecersizDegerException {
        if (fiyat < 0) throw new GecersizDegerException("Fiyat negatif olamaz!");
        this.fiyat = fiyat;
    }

    //Marka bilgisi setterı
    public void setMarka(String marka) throws GecersizDegerException {
        if (marka == null || marka.trim().isEmpty()) throw new GecersizDegerException("Marka alanı boş bırakılamaz.");
        this.marka = marka;
    }

    //Model bilgisi setterı
    public void setModel(String model) throws GecersizDegerException {
        if (model == null || model.trim().isEmpty()) throw new GecersizDegerException("Model alanı boş bırakılamaz.");
        this.model = model;
    }

    //Ekstra garanti süresi ekleme metodunu çağıran getter
    public int getEkstraGarantiSuresiAy() {
        return garanti.hesaplaEkstraSureAy(getGarantiSuresiYil());
    }

    //Aylık garanti uzatma metodu
    public void garantiUzat(int ay) {
        this.garanti.sureUzat(ay);
        // garantin nesnesinin StandartGaranti sınıfına ait olup olmadığını kontrol eder
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
        //Garantinin aktif olup oladığını test eder.
        String garantiDurumu = garantiAktifMi() ? "Aktif" : "Sona Ermiş";
        //Garanti süresi uzatıldıysa kaç ay uzatıldığını yazar.
        String ekstraBilgi = getEkstraGarantiSuresiAy() > 0 ? " (+" + getEkstraGarantiSuresiAy() + " Ay Uzatıldı)" : "";
        //Cihaz hakkındaki bilgileri verir.
        return String.format("%s [%s - %s] (Seri No: %s) - Garanti: %s%s",
                getCihazTuru(), marka, model, seriNo, garantiDurumu, ekstraBilgi);
    }
}