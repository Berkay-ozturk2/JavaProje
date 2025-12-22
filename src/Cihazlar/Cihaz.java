package Cihazlar;

import java.time.LocalDate;
import Musteri.Musteri;
import Garantiler.*;
import Istisnalar.GecersizDegerException;

// Tüm cihaz türleri için ortak özellikleri tanımlayan soyut ana sınıf.
public abstract class Cihaz {

    //Alanlar
    private String seriNo;
    private String marka;
    private String model;
    private double fiyat;
    private Musteri sahip;

    // Her cihazın bir garanti nesnesine sahip olmasını sağlayan kompozisyon yapısı.
    protected Garanti garanti;

    // Cihaz Constructor (GecersizDegerException sınıfını kullanır)
    public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic, Musteri sahip) throws GecersizDegerException {
        this.seriNo = seriNo;

        // Veri bütünlüğünü korumak için değerleri doğrudan atamak yerine kontrol mekanizması olan setter'ları kullanır.
        setMarka(marka);
        setModel(model);
        setFiyat(fiyat);

        this.sahip = sahip;

        LocalDate baslangic;
        // Eğer garanti başlangıç tarihi verilmediyse, rastgele bir tarih üretir.
        if (garantiBaslangic == null) {
            baslangic = Garanti.rastgeleBaslangicOlustur(getGarantiSuresiYil());
        } else {
            // Eğer geçerli bir tarih verildiyse onu başlangıç kabul eder.
            baslangic = garantiBaslangic;
        }
        // Cihaz oluşturulduğunda varsayılan olarak Standart Garanti başlatır.
        this.garanti = new StandartGaranti(baslangic, getGarantiSuresiYil());
    }

    // Getter & Setter
    public String getSeriNo() { return seriNo; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public double getFiyat() { return fiyat; }
    public Musteri getSahip() { return sahip; }
    public Garanti getGaranti() { return garanti; }

    // Garanti nesnesi üzerinden başlangıç tarihine erişen yönlendirici metot.
    public LocalDate getGarantiBaslangic() { return garanti.getBaslangicTarihi(); }

    // Garanti nesnesi üzerinden bitiş tarihini hesaplayıp döndüren metot.
    public LocalDate getGarantiBitisTarihi() { return garanti.getBitisTarihi(); }

    // Garantinin şu anki tarihe göre geçerli olup olmadığını kontrol eder.
    public boolean garantiAktifMi() { return garanti.devamEdiyorMu(); }

    // Fiyat değeri atama setterı
    public void setFiyat(double fiyat) throws GecersizDegerException {
        // Fiyatın negatif girilmesini engeller
        if (fiyat < 0) throw new GecersizDegerException("Fiyat negatif olamaz!");
        this.fiyat = fiyat;
    }

    // Marka bilgisi setterı
    public void setMarka(String marka) throws GecersizDegerException {
        // Marka alanının boş bırakılmasını engelleyen doğrulama kontrolü.
        if (marka == null || marka.trim().isEmpty()) throw new GecersizDegerException("Marka alanı boş bırakılamaz.");
        this.marka = marka;
    }

    // Model bilgisi setterı
    public void setModel(String model) throws GecersizDegerException {
        // Model alanının boş bırakılmasını engelleyen doğrulama kontrolü.
        if (model == null || model.trim().isEmpty()) throw new GecersizDegerException("Model alanı boş bırakılamaz.");
        this.model = model;
    }

    // Ekstra garanti süresi ekleme metodunu çağıran getter
    public int getEkstraGarantiSuresiAy() {
        // Toplam süreden standart süreyi çıkararak sadece eklenen ayı hesaplar.
        return garanti.hesaplaEkstraSureAy(getGarantiSuresiYil());
    }

    // Aylık garanti uzatma metodu
    public void garantiUzat(int ay) {
        // Mevcut garanti süresine belirtilen ayı ekler.
        this.garanti.sureUzat(ay);

        // Eğer garanti nesnesi StandartGaranti sınıfı içinde ise, uzatma işlemi yapıldığı için onu Uzatılmış Garanti sınıfına yükseltir.
        if (this.garanti instanceof StandartGaranti) {
            int toplamEkstraSure = getEkstraGarantiSuresiAy();
            this.garanti = new UzatilmisGaranti(
                    this.garanti.getBaslangicTarihi(),
                    getGarantiSuresiYil(),
                    toplamEkstraSure
            );
        }
    }

    // Alt sınıfların kendi garanti sürelerini belirtmesi için zorunlu metot.
    public abstract int getGarantiSuresiYil();

    // Alt sınıfların kendi tür isimlerini döndürmesi için zorunlu metot.
    public abstract String getCihazTuru();


    @Override
    public String toString() {
        // Garantinin aktif olup olmadığını kontrol ederek yazdırır.
        String garantiDurumu = garantiAktifMi() ? "Aktif" : "Sona Ermiş";

        // Eğer süre uzatılmışsa, ne kadar uzatıldığını parantez içinde belirtir.
        String ekstraBilgi = getEkstraGarantiSuresiAy() > 0 ? " (+" + getEkstraGarantiSuresiAy() + " Ay Uzatıldı)" : "";

        // Cihazın marka, model, seri no ve garanti durumunu tek satırda özetleyen çıktıyı oluşturur.
        return String.format("%s [%s - %s] (Seri No: %s) - Garanti: %s%s",
                getCihazTuru(), marka, model, seriNo, garantiDurumu, ekstraBilgi);
    }
}