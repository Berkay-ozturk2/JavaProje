package gui;

import Cihazlar.*;
import Musteri.Musteri;
import Istisnalar.GecersizDegerException;
import Araclar.KodUretici;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

// Listener arayüzü
interface CihazEkleListener {
    void cihazEklendi(Cihaz cihaz);
}

public class CihazKayitDialog extends JDialog {

    private final CihazEkleListener listener;

    // UI Bileşenleri
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

    // Dinamik Alanlar
    private JPanel specificPanel;
    private CardLayout cardLayout;
    private JCheckBox chkCiftSim;
    private JCheckBox chkKalemDestegi;
    private JCheckBox chkSsd;

    public CihazKayitDialog(JFrame parent, CihazEkleListener listener) {
        super(parent, "Yeni Cihaz Kaydı", true);
        this.listener = listener;
        initUI();
    }

    private void initUI() {
        setSize(500, 750);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- 1. BAŞLIK (HEADER) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        headerPanel.setBackground(new Color(52, 152, 219)); // Mavi Başlık

        JLabel lblTitle = new JLabel("Cihaz Kayıt Formu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. FORM ALANI (CENTER) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0); // Satır boşlukları
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Müşteri Bölümü
        addSectionTitle(formPanel, gbc, "Müşteri Bilgileri");

        txtMusteriAd = addFormField(formPanel, gbc, "Ad:", false);
        txtMusteriSoyad = addFormField(formPanel, gbc, "Soyad:", false);
        txtMusteriTelefon = addFormField(formPanel, gbc, "Telefon ((+90)5..):", false);
        txtMusteriTelefon.setText("+90");

        chkVip = new JCheckBox("VIP Müşteri (%20 İndirim)");
        chkVip.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkVip.setBackground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(chkVip, gbc);

        // Cihaz Bölümü
        addSeparator(formPanel, gbc);
        addSectionTitle(formPanel, gbc, "Cihaz Detayları");

        formPanel.add(createLabel("Cihaz Türü:"), gbc);
        gbc.gridy++;
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler);
        styleComboBox(cmbTur);
        formPanel.add(cmbTur, gbc);
        gbc.gridy++;

        txtSeriNo = addFormField(formPanel, gbc, "Seri No (Otomatik):", true);
        txtSeriNo.setText(KodUretici.rastgeleSeriNoUret((String) cmbTur.getSelectedItem()));

        formPanel.add(createLabel("Marka:"), gbc);
        gbc.gridy++;
        cmbMarka = new JComboBox<>();
        styleComboBox(cmbMarka);
        formPanel.add(cmbMarka, gbc);
        gbc.gridy++;

        formPanel.add(createLabel("Model:"), gbc);
        gbc.gridy++;
        cmbModel = new JComboBox<>();
        styleComboBox(cmbModel);
        formPanel.add(cmbModel, gbc);
        gbc.gridy++;

        txtFiyat = addFormField(formPanel, gbc, "Fiyat (TL):", false);

        chkEskiTarih = new JCheckBox("Geçmiş Tarihli Kayıt (Test Amaçlı)");
        chkEskiTarih.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkEskiTarih.setForeground(Color.GRAY);
        chkEskiTarih.setBackground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(chkEskiTarih, gbc);

        // --- Dinamik Alanlar (CardLayout) ---
        gbc.gridy++;
        cardLayout = new CardLayout();
        specificPanel = new JPanel(cardLayout);
        specificPanel.setBackground(Color.WHITE);
        specificPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Panelleri Oluştur
        JPanel pTelefon = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTelefon.setBackground(Color.WHITE);
        chkCiftSim = new JCheckBox("Çift Sim Desteği");
        chkCiftSim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkCiftSim.setBackground(Color.WHITE);
        pTelefon.add(chkCiftSim);

        JPanel pTablet = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTablet.setBackground(Color.WHITE);
        chkKalemDestegi = new JCheckBox("Kalem Desteği Var");
        chkKalemDestegi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkKalemDestegi.setBackground(Color.WHITE);
        pTablet.add(chkKalemDestegi);

        JPanel pLaptop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pLaptop.setBackground(Color.WHITE);
        chkSsd = new JCheckBox("Harici Ekran Kartı");
        chkSsd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkSsd.setBackground(Color.WHITE);
        pLaptop.add(chkSsd);

        specificPanel.add(pTelefon, "Telefon");
        specificPanel.add(pTablet, "Tablet");
        specificPanel.add(pLaptop, "Laptop");

        formPanel.add(specificPanel, gbc);

        // --- Scroll Pane (Form uzun olabilir) ---
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. ALT BUTON (FOOTER) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(new Color(245, 248, 250));

        JButton btnKaydet = new JButton("Kaydı Tamamla");
        btnKaydet.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnKaydet.setBackground(new Color(46, 204, 113)); // Yeşil Buton
        btnKaydet.setForeground(Color.WHITE);
        btnKaydet.setPreferredSize(new Dimension(150, 40));
        btnKaydet.setFocusPainted(false);
        btnKaydet.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        btnKaydet.addActionListener(e -> kaydet());
        footerPanel.add(btnKaydet);
        add(footerPanel, BorderLayout.SOUTH);

        // --- LISTENERS ---
        setupListeners();
    }

    // --- YARDIMCI METOTLAR (UI) ---

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(44, 62, 80));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
    }

    private void addSeparator(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        panel.add(sep, gbc);
        gbc.gridy++;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String labelText, boolean readOnly) {
        panel.add(createLabel(labelText), gbc);
        gbc.gridy++;
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(100, 35)); // Yükseklik artırıldı
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (readOnly) {
            txt.setEditable(false);
            txt.setBackground(new Color(240, 240, 240));
        }
        // Köşeleri yuvarlat
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        panel.add(txt, gbc);
        gbc.gridy++;
        return txt;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setPreferredSize(new Dimension(100, 35));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    // --- MANTIK METOTLARI (Eskisiyle aynı) ---

    private void setupListeners() {
        cmbTur.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            guncelMarkaListesiniDoldur(secilenTur);
            cardLayout.show(specificPanel, secilenTur);
            txtSeriNo.setText(KodUretici.rastgeleSeriNoUret(secilenTur));
        });

        cmbMarka.addActionListener(e -> {
            String tur = (String) cmbTur.getSelectedItem();
            String marka = (String) cmbMarka.getSelectedItem();
            guncelModelListesiniDoldur(tur, marka);
        });

        cmbModel.addActionListener(e -> {
            String model = (String) cmbModel.getSelectedItem();
            if (model != null && CihazKatalogu.fiyatMevcutMu(model)) {
                txtFiyat.setText(String.valueOf(CihazKatalogu.getFiyat(model)));
            }
        });

        // Başlangıç verileri
        guncelMarkaListesiniDoldur((String) cmbTur.getSelectedItem());
        cardLayout.show(specificPanel, (String) cmbTur.getSelectedItem());
    }

    private void guncelMarkaListesiniDoldur(String tur) {
        cmbMarka.removeAllItems();
        String[] markalar = CihazKatalogu.getMarkalar(tur);
        for (String m : markalar) cmbMarka.addItem(m);
        if (cmbMarka.getItemCount() > 0) cmbMarka.setSelectedIndex(0);
        guncelModelListesiniDoldur(tur, (String) cmbMarka.getSelectedItem());
    }

    private void guncelModelListesiniDoldur(String tur, String marka) {
        cmbModel.removeAllItems();
        if (tur != null && marka != null) {
            String[] modeller = CihazKatalogu.getModeller(tur, marka);
            for (String m : modeller) cmbModel.addItem(m);
        }
    }

    private void kaydet() {
        try {
            String mAd = txtMusteriAd.getText().trim();
            String mSoyad = txtMusteriSoyad.getText().trim();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty()) {
                throw new GecersizDegerException("Müşteri bilgileri boş bırakılamaz!");
            }

            if (mTelefon.isEmpty() || mTelefon.equals("+90") || mTelefon.length() < 14) {
                throw new GecersizDegerException("Telefon bilgisi alanı boş bırakılamaz!\nLütfen geçerli bir numara giriniz.");
            }

            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon);
            sahip.setVip(chkVip.isSelected());

            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem();
            double fiyat = Double.parseDouble(txtFiyat.getText().trim());
            String tur = (String) cmbTur.getSelectedItem();
            LocalDate garantiBaslangic = chkEskiTarih.isSelected() ? null : LocalDate.now();

            Cihaz yeniCihaz;
            switch (tur) {
                case "Telefon": yeniCihaz = new Telefon(seriNo, marka, model, fiyat, garantiBaslangic, sahip); break;
                case "Tablet": yeniCihaz = new Tablet(seriNo, marka, model, fiyat, garantiBaslangic, chkKalemDestegi.isSelected(), sahip); break;
                case "Laptop": yeniCihaz = new Laptop(seriNo, marka, model, fiyat, garantiBaslangic, sahip); break;
                default: throw new GecersizDegerException("Geçersiz tür.");
            }

            listener.cihazEklendi(yeniCihaz);
            JOptionPane.showMessageDialog(this, tur + " başarıyla kaydedildi.", "İşlem Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}