package gui;

import Cihazlar.*;
import Musteri.Musteri;
import Istisnalar.GecersizDegerException;
import Araclar.KodUretici;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

// Listener arayüzünü aynı dosya içinde tutuyoruz
interface CihazEkleListener {
    void cihazEklendi(Cihaz cihaz);
}

public class CihazKayitDialog extends JDialog {

    private final CihazEkleListener listener;

    private JComboBox<String> cmbTur;
    private JTextField txtSeriNo;
    private JComboBox<String> cmbMarka;
    private JComboBox<String> cmbModel;
    private JTextField txtFiyat;
    private JTextField txtMusteriAd;
    private JTextField txtMusteriSoyad;
    private JTextField txtMusteriTelefon;
    private JCheckBox chkVip;
    private JCheckBox chkEskiTarih;
    private JPanel specificPanel;
    private CardLayout cardLayout;

    // Türlere özel bileşenler
    private JCheckBox chkCiftSim;
    private JCheckBox chkKalemDestegi;
    private JCheckBox chkSsd;

    public CihazKayitDialog(JFrame parent, CihazEkleListener listener) {
        super(parent, "Yeni Cihaz Kaydı", true);
        this.listener = listener;
        setSize(500, 700);
        setLocationRelativeTo(parent);

        // Eski initModelData() metodunu kaldırdık çünkü veriler artık CihazKatalogu'ndan geliyor.
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel generalPanel = new JPanel(new GridLayout(12, 2, 5, 5));

        // --- Müşteri Bilgileri ---
        txtMusteriAd = new JTextField();
        generalPanel.add(new JLabel("Müşteri Adı:"));
        generalPanel.add(txtMusteriAd);

        txtMusteriSoyad = new JTextField();
        generalPanel.add(new JLabel("Müşteri Soyadı:"));
        generalPanel.add(txtMusteriSoyad);

        txtMusteriTelefon = new JTextField("+90");
        generalPanel.add(new JLabel("Telefon ((+90)5..):"));
        generalPanel.add(txtMusteriTelefon);

        chkVip = new JCheckBox("VIP Müşteri (%20 İndirim)");
        generalPanel.add(new JLabel("Müşteri Statüsü:"));
        generalPanel.add(chkVip);

        // --- Cihaz Bilgileri ---
        generalPanel.add(new JLabel("Cihaz Türü:"));
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler);
        generalPanel.add(cmbTur);

        // Seri No alanı (Otomatik üretilir)
        txtSeriNo = new JTextField(KodUretici.rastgeleSeriNoUret((String) cmbTur.getSelectedItem()));
        txtSeriNo.setEditable(false);
        generalPanel.add(new JLabel("Seri No: (Otomatik)"));
        generalPanel.add(txtSeriNo);

        // Marka ve Model ComboBox'ları
        cmbMarka = new JComboBox<>();
        generalPanel.add(new JLabel("Marka:"));
        generalPanel.add(cmbMarka);

        cmbModel = new JComboBox<>();
        generalPanel.add(new JLabel("Model:"));
        generalPanel.add(cmbModel);

        // Cihaz Türü Değişince Çalışacak Listener
        cmbTur.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();

            // Marka listesini kataloğa göre güncelle
            guncelMarkaListesiniDoldur(secilenTur);

            // İlgili özel paneli göster (örn: Tablet için kalem desteği)
            cardLayout.show(specificPanel, secilenTur);

            // Yeni türe uygun seri numarası üret
            txtSeriNo.setText(KodUretici.rastgeleSeriNoUret(secilenTur));
        });

        // Marka Değişince Modelleri Güncelle
        cmbMarka.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            String secilenMarka = (String) cmbMarka.getSelectedItem();
            guncelModelListesiniDoldur(secilenTur, secilenMarka);
        });

        txtFiyat = new JTextField();
        generalPanel.add(new JLabel("Fiyat (TL):"));
        generalPanel.add(txtFiyat);

        // Model Seçilince Fiyatı Otomatik Getir
        cmbModel.addActionListener(e -> {
            String secilenModel = (String) cmbModel.getSelectedItem();
            // CihazKatalogu üzerinden fiyatı çekiyoruz
            if (secilenModel != null && CihazKatalogu.fiyatMevcutMu(secilenModel)) {
                txtFiyat.setText(String.valueOf(CihazKatalogu.getFiyat(secilenModel)));
            }
        });

        chkEskiTarih = new JCheckBox("Geçmiş Tarihli Kayıt (Test Amaçlı)");
        generalPanel.add(new JLabel("Garanti Durumu:"));
        generalPanel.add(chkEskiTarih);

        // --- Dinamik Paneller (CardLayout) ---
        cardLayout = new CardLayout();
        specificPanel = new JPanel(cardLayout);

        // Telefon Paneli
        JPanel telefonPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkCiftSim = new JCheckBox("Çift Sim Mi?");
        telefonPanel.add(chkCiftSim);
        specificPanel.add(telefonPanel, "Telefon");

        // Tablet Paneli
        JPanel tabletPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkKalemDestegi = new JCheckBox("Kalem Desteği Var Mı?");
        tabletPanel.add(chkKalemDestegi);
        specificPanel.add(tabletPanel, "Tablet");

        // Laptop Paneli
        JPanel laptopPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkSsd = new JCheckBox("Harici Ekran Kartı Var Mı?");
        laptopPanel.add(chkSsd);
        specificPanel.add(laptopPanel, "Laptop");

        // Açılışta varsayılan verileri doldur
        guncelMarkaListesiniDoldur((String) cmbTur.getSelectedItem());
        cardLayout.show(specificPanel, (String) cmbTur.getSelectedItem());

        // --- Kaydet Butonu ---
        JButton btnKaydet = new JButton("Cihazı Kaydet");
        btnKaydet.addActionListener(e -> kaydet());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(generalPanel, BorderLayout.NORTH);
        contentPanel.add(specificPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        add(btnKaydet, BorderLayout.SOUTH);
    }

    // --- GÜNCELLENEN METOTLAR (CihazKatalogu Kullanımı) ---

    private void guncelMarkaListesiniDoldur(String tur) {
        cmbMarka.removeAllItems();
        // Verileri merkezi katalogdan çekiyoruz
        String[] markalar = CihazKatalogu.getMarkalar(tur);

        for (String marka : markalar) {
            cmbMarka.addItem(marka);
        }

        if (cmbMarka.getItemCount() > 0) cmbMarka.setSelectedIndex(0);

        // Marka değiştiği için modelleri de tetikle
        guncelModelListesiniDoldur(tur, (String) cmbMarka.getSelectedItem());
    }

    private void guncelModelListesiniDoldur(String tur, String marka) {
        cmbModel.removeAllItems();
        // Verileri merkezi katalogdan çekiyoruz
        if (tur != null && marka != null) {
            String[] modeller = CihazKatalogu.getModeller(tur, marka);
            for (String model : modeller) {
                cmbModel.addItem(model);
            }
        }
    }

    private void kaydet() {
        try {
            // Validasyonlar
            String mAd = txtMusteriAd.getText().trim();
            String mSoyad = txtMusteriSoyad.getText().trim();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty() || mTelefon.isEmpty()) {
                throw new GecersizDegerException("Müşteri bilgileri boş bırakılamaz!");
            }

            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon);
            sahip.setVip(chkVip.isSelected());

            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem();

            if (marka == null || model == null) throw new GecersizDegerException("Marka/Model seçilmeli.");

            double fiyat = Double.parseDouble(txtFiyat.getText().trim());

            String tur = (String) cmbTur.getSelectedItem();
            // Test amaçlı geçmiş tarihli kayıt (garantisi bitmiş cihaz simülasyonu)
            LocalDate garantiBaslangic = chkEskiTarih.isSelected() ? null : LocalDate.now();

            Cihaz yeniCihaz;
            switch (tur) {
                case "Telefon":
                    yeniCihaz = new Telefon(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                case "Tablet":
                    yeniCihaz = new Tablet(seriNo, marka, model, fiyat, garantiBaslangic, chkKalemDestegi.isSelected(), sahip);
                    break;
                case "Laptop":
                    yeniCihaz = new Laptop(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                default:
                    throw new GecersizDegerException("Geçersiz tür.");
            }

            listener.cihazEklendi(yeniCihaz);
            JOptionPane.showMessageDialog(this, tur + " başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Pencereyi kapat

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}