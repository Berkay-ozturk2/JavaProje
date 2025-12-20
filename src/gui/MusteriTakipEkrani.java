package gui;

import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Istisnalar.KayitBulunamadiException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    // Performans için verileri bir kez yüklemek daha iyidir,
    // ancak gerçek zamanlılık gerekiyorsa metot içinde kalabilir.
    // SRP gereği business logic (rapor oluşturma) UI'dan ayrıldı.

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

        // ActionListener sadece UI işlemini tetikler, mantığı bilmez
        btnSorgula.addActionListener(e -> sorgulaIslemi());
        txtSeriNo.addActionListener(e -> sorgulaIslemi());

        add(panelArama, BorderLayout.NORTH);
        add(new JScrollPane(txtBilgiEkrani), BorderLayout.CENTER);
    }

    private void sorgulaIslemi() {
        String arananSeriNo = txtSeriNo.getText().trim();
        if (arananSeriNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası Giriniz.");
            return;
        }

        txtBilgiEkrani.setText("Sorgulanıyor...");

        // UI Thread'i dondurmamak için işlem ayrı yapılabilir ama basitlik adına direkt çağırıyoruz.
        try {
            String rapor = cihazDurumuRaporla(arananSeriNo);
            txtBilgiEkrani.setText(rapor);
        } catch (KayitBulunamadiException ex) {
            txtBilgiEkrani.setText(ex.getMessage());
        } catch (Exception ex) {
            txtBilgiEkrani.setText("Beklenmeyen bir hata oluştu: " + ex.getMessage());
        }
    }

    // --- AYRIŞTIRILMIŞ İŞ MANTIĞI (Business Logic) ---
    // Bu metot artık UI bileşenlerine dokunmaz, sadece veri üretir.
    private String cihazDurumuRaporla(String seriNo) throws Exception {
        StringBuilder rapor = new StringBuilder();

        // Veri Yükleme (Burası idealde bir Servis sınıfına aittir)
        List<Cihaz> cihazlar = Cihaz.verileriYukle("cihazlar.txt");
        Cihaz bulunanCihaz = null;

        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(seriNo)) {
                bulunanCihaz = c;
                break;
            }
        }

        if (bulunanCihaz == null) {
            throw new KayitBulunamadiException("HATA: Bu seri numarasına ait bir cihaz bulunamadı.\nLütfen numarayı kontrol ediniz.");
        }

        // Cihaz Bilgileri
        rapor.append("=== CİHAZ BİLGİLERİ ===\n");
        rapor.append("Sayın ").append(bulunanCihaz.getSahip().getAd())
                .append(" ").append(bulunanCihaz.getSahip().getSoyad()).append(",\n");
        rapor.append("Marka/Model: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");
        rapor.append("Tür: ").append(bulunanCihaz.getCihazTuru()).append("\n");

        String garantiDurumu = bulunanCihaz.isGarantiAktif() ? "AKTİF" : "BİTMİŞ";
        rapor.append("Garanti Durumu: ").append(garantiDurumu).append("\n");
        rapor.append("Garanti Bitiş: ").append(bulunanCihaz.getGarantiBitisTarihi()).append("\n\n");

        // Servis Kayıtları
        ServisYonetimi servisYonetimi = new ServisYonetimi(cihazlar);
        ServisKaydi bulunanKayit = null;
        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo)) {
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

        return rapor.toString();
    }
}