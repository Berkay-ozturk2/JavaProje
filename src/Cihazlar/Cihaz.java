package Cihazlar;// ===== B KRİTERLERİNE UYGUN, GUI UYUMLU TEMEL SINIFLAR =====
// Paket yapısı örnek olarak verilmiştir;


import java.io.Serializable;
import java.time.LocalDate;


    // --------------------------------------------------
// ABSTRACT BASE CLASS: Cihaz
// --------------------------------------------------
    public abstract class Cihaz implements Serializable {


        private static final long serialVersionUID = 1L;


        private String seriNo;
        private String marka;
        private String model;
        private double fiyat;
        private LocalDate garantiBaslangic;


        // Constructor 1
        public Cihaz(String seriNo, String marka, String model) {
            this.seriNo = seriNo;
            this.marka = marka;
            this.model = model;
        }


        // Constructor 2 (Overload)
        public Cihaz(String seriNo, String marka, String model, double fiyat, LocalDate garantiBaslangic) {
            this(seriNo, marka, model);
            this.fiyat = fiyat;
            this.garantiBaslangic = garantiBaslangic;
}
        // Abstract method
        public abstract int getGarantiSuresiYil();
        public abstract String getCihazTuru();


        // Concrete method
        public LocalDate getGarantiBitisTarihi() {
            if (garantiBaslangic == null) return null;
            return garantiBaslangic.plusYears(getGarantiSuresiYil());
        }


        public boolean garantiGecerliMi() {
            LocalDate bitis = getGarantiBitisTarihi();
            return bitis != null && !LocalDate.now().isAfter(bitis);
        }


        // Getters & Setters (GUI binding uyumlu)
        public String getSeriNo() { return seriNo; }
        public void setSeriNo(String seriNo) { this.seriNo = seriNo; }


        public String getMarka() { return marka; }
        public void setMarka(String marka) { this.marka = marka; }


        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }


        public double getFiyat() { return fiyat; }
        public void setFiyat(double fiyat) { this.fiyat = fiyat; }


        public LocalDate getGarantiBaslangic() { return garantiBaslangic; }
        public void setGarantiBaslangic(LocalDate garantiBaslangic) {
            this.garantiBaslangic = garantiBaslangic;
        }// GUI (TableView / JList) uyumlu
        @Override
        public String toString() {
            return getCihazTuru() + " | " + marka + " " + model + " | SeriNo: " + seriNo;
        }
    }


