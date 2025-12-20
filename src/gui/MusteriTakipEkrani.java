package gui;

import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Istisnalar.KayitBulunamadiException; // KayitBulunamadiException import edildi

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    public MusteriTakipEkrani() {
        setTitle("Cihaz Durum Sorgulama");
        setSize(500, 450);
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

        btnSorgula.addActionListener(e -> sorgula());
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

        try { // Hata yönetimi için try-catch bloğu eklendi
            StringBuilder rapor = new StringBuilder();

            // 1. ADIM: Cihazları Yükle
            List<Cihaz> cihazlar = Cihaz.verileriYukle("cihazlar.txt");
            Cihaz bulunanCihaz = null;

            for (Cihaz c : cihazlar) {
                if (c.getSeriNo().equalsIgnoreCase(arananSeriNo)) {
                    bulunanCihaz = c;
                    break;
                }
            }

            // --- KayitBulunamadiException Kullanımı ---
            if (bulunanCihaz == null) {
                // Seri No eşleşmezse özel istisna fırlatılır
                throw new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı.\nLütfen numarayı kontrol ediniz.");
            }

            // Cihaz bulunduysa rapor oluşturulur
            rapor.append("=== CİHAZ BİLGİLERİ ===\n");
            rapor.append("Sayın ").append(bulunanCihaz.getSahip().getAd())
                    .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append(",\n");
            rapor.append("Marka/Model: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");
            rapor.append("Tür: ").append(bulunanCihaz.getCihazTuru()).append("\n");

            String garantiDurumu = bulunanCihaz.isGarantiAktif() ? "AKTİF" : "BİTMİŞ";
            rapor.append("Garanti Durumu: ").append(garantiDurumu).append("\n");
            rapor.append("Garanti Bitiş: ").append(bulunanCihaz.getGarantiBitisTarihi()).append("\n\n");

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

                LocalDate giris = bulunanKayit.getGirisTarihi();
                LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(giris, 20);

                rapor.append("\n----------------------------------\n");
                rapor.append("TAHMİNİ TESLİM TARİHİ: ").append(tahminiTeslim).append("\n");
                rapor.append("(İşlem süresi standart 20 iş günüdür.)\n");

            } else {
                rapor.append("=== SERVİS DURUMU ===\n");
                rapor.append("Bu cihaz için aktif bir servis kaydı bulunmamaktadır.\n");
            }

            txtBilgiEkrani.setText(rapor.toString());

        } catch (KayitBulunamadiException ex) {
            // Yakalanan özel hata mesajı bilgi ekranına yazdırılır
            txtBilgiEkrani.setText(ex.getMessage());
        } catch (Exception ex) {
            // Diğer beklenmedik hatalar için genel yakalama
            txtBilgiEkrani.setText("Beklenmeyen bir hata oluştu: " + ex.getMessage());
        }
    }
}