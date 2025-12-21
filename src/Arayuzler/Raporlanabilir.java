package Arayuzler;

public interface Raporlanabilir {
    //Detaylı Rapor oluşturmak için metot
    String detayliRaporVer();
    //Hangi cihazın raporlandığını yazan metot
    String getRaporBasligi();
    String getOzetBilgi();
}