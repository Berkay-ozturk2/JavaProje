package gui;

import Araclar.TarihYardimcisi; // EKLENDİ
import Cihazlar.Cihaz;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate; // EKLENDİ
import java.util.List;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    public MusteriTakipEkrani() {
        setTitle("Cihaz Durum Sorgulama");
        setSize(500, 450); // Tarih eklenince sığması için boyutu biraz artırdık
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelArama = new JPanel(new FlowLayout());
        panelArama.add(new JLabel("Cihaz Seri No:"));
        txtSeriNo = new JTextField(15);
        JButton btnSorgula = new JButton("Sorgula");

        panelArama.add(txtSeriNo);
        panelArama.add(btnSorgula);

        txtBilgiEkrani = new JTextArea();
        txtBilgiEkrani.setEditable(false);
        txtBilgiEkrani.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBilgiEkrani.setMargin(new Insets(10, 10, 10, 10));

        // Mevcut: Butona tıklama aksiyonu
        btnSorgula.addActionListener(e -> sorgula());

        // --- EKLENEN: Enter tuşuna basınca sorgulama aksiyonu ---
        txtSeriNo.addActionListener(e -> sorgula());

        add(panelArama, BorderLayout.NORTH);
        add(new JScrollPane(txtBilgiEkrani), BorderLayout.CENTER);
    }

    private void sorgula() {
        String arananSeriNo = txtSeriNo.getText().trim();
        if (arananSeriNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası Giriniz.");
            return;
        }

        txtBilgiEkrani.setText("Sorgulanıyor...");
        StringBuilder rapor = new StringBuilder();
        boolean cihazBulundu = false;

        // 1. ADIM: Cihazları Yükle
        List<Cihaz> cihazlar = Cihaz.verileriYukle("cihazlar.txt");
        Cihaz bulunanCihaz = null;

        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(arananSeriNo)) {
                bulunanCihaz = c;
                cihazBulundu = true;
                break;
            }
        }

        if (bulunanCihaz != null) {
            rapor.append("=== CİHAZ BİLGİLERİ ===\n");
            rapor.append("Sayın ").append(bulunanCihaz.getSahip().getAd())
                    .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append(",\n");
            rapor.append("Marka/Model: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");
            rapor.append("Tür: ").append(bulunanCihaz.getCihazTuru()).append("\n");

            String garantiDurumu = bulunanCihaz.isGarantiAktif() ? "AKTİF" : "BİTMİŞ";
            rapor.append("Garanti Durumu: ").append(garantiDurumu).append("\n");
            rapor.append("Garanti Bitiş: ").append(bulunanCihaz.getGarantiBitisTarihi()).append("\n\n");
        }

        // 2. ADIM: Servis Kayıtlarını Yükle ve Eşleştir
        ServisYonetimi servisYonetimi = new ServisYonetimi(cihazlar);

        ServisKaydi bulunanKayit = null;
        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(arananSeriNo)) {
                bulunanKayit = k;
                break;
            }
        }

        if (bulunanKayit != null) {
            rapor.append(bulunanKayit.detayliRaporVer());

            // --- YENİ EKLENEN KISIM: TAHMİNİ TESLİM TARİHİ ---
            // Main'de yaptığımız gibi giriş tarihine 20 iş günü ekliyoruz.
            // Dosyaya kaydetmesek bile her sorgulamada yeniden hesaplayarak müşteriye gösteriyoruz.
            LocalDate giris = bulunanKayit.getGirisTarihi();
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(giris, 20);

            rapor.append("\n----------------------------------\n");
            rapor.append("TAHMİNİ TESLİM TARİHİ: ").append(tahminiTeslim).append("\n");
            rapor.append("(İşlem süresi standart 20 iş günüdür.)\n");
            // ------------------------------------------------

        } else if (cihazBulundu) {
            rapor.append("=== SERVİS DURUMU ===\n");
            rapor.append("Bu cihaz için aktif bir servis kaydı bulunmamaktadır.\n");
        }

        if (!cihazBulundu) {
            txtBilgiEkrani.setText("HATA: Bu seri numarasına ait bir cihaz bulunamadı.\nLütfen numarayı kontrol ediniz.");
        } else {
            txtBilgiEkrani.setText(rapor.toString());
        }
    }
}