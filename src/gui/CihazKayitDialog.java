package gui;

import Cihazlar.*;
import Musteri.Musteri;
import Istisnalar.GecersizDegerException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    // --- EKLENEN: VIP Seçim Kutusu ---
    private JCheckBox chkVip;

    private JCheckBox chkEskiTarih;

    private final Map<String, Map<String, String[]>> TUM_MODELLER = new HashMap<>();
    private final Map<String, Double> MODEL_FIYATLARI = new HashMap<>();

    private final String[] TELEFON_MARKALARI = {"Samsung", "Apple", "Xiaomi", "Huawei"};
    private final String[] TABLET_MARKALARI = {"Apple", "Samsung", "Lenovo","Huawei"};
    private final String[] LAPTOP_MARKALARI = {"Dell", "HP", "Lenovo", "Apple", "Asus","Msi"};

    private JPanel specificPanel;
    private CardLayout cardLayout;
    private JCheckBox chkCiftSim;
    private JCheckBox chkKalemDestegi;
    private JCheckBox chkSsd;

    public CihazKayitDialog(JFrame parent, CihazEkleListener listener) {
        super(parent, "Yeni Cihaz Kaydı", true);
        this.listener = listener;
        setSize(500, 700); // Boyut biraz artırıldı
        setLocationRelativeTo(parent);

        initModelData();
        initUI();
    }

    private void initModelData() {
        // TELEFON
        Map<String, String[]> telefonModelleri = new HashMap<>();
        telefonModelleri.put("Samsung", new String[]{"Galaxy S24 Ultra", "Galaxy A55", "Galaxy Flip 5", "Galaxy S23 FE"});
        MODEL_FIYATLARI.put("Galaxy S24 Ultra", 70000.0);
        MODEL_FIYATLARI.put("Galaxy A55", 20000.0);
        MODEL_FIYATLARI.put("Galaxy Flip 5", 40000.0);
        MODEL_FIYATLARI.put("Galaxy S23 FE", 25000.0);

        telefonModelleri.put("Apple", new String[]{"iPhone 15 Pro Max", "iPhone SE", "iPhone 14", "iPhone 13"});
        MODEL_FIYATLARI.put("iPhone 15 Pro Max", 85000.0);
        MODEL_FIYATLARI.put("iPhone SE", 28000.0);
        MODEL_FIYATLARI.put("iPhone 14", 45000.0);
        MODEL_FIYATLARI.put("iPhone 13", 38000.0);

        telefonModelleri.put("Xiaomi", new String[]{"Redmi Note 13 Pro", "Xiaomi 14 Ultra", "Poco X6"});
        MODEL_FIYATLARI.put("Redmi Note 13 Pro", 18000.0);
        MODEL_FIYATLARI.put("Xiaomi 14 Ultra", 55000.0);
        MODEL_FIYATLARI.put("Poco X6", 15000.0);

        telefonModelleri.put("Huawei", new String[]{"Pura 70 Ultra", "Mate 60 RS"});
        MODEL_FIYATLARI.put("Pura 70 Ultra", 60000.0);
        MODEL_FIYATLARI.put("Mate 60 RS", 75000.0);

        TUM_MODELLER.put("Telefon", telefonModelleri);

        // TABLET
        Map<String, String[]> tabletModelleri = new HashMap<>();
        tabletModelleri.put("Apple", new String[]{"iPad Pro (M4)", "iPad Air", "iPad Mini"});
        MODEL_FIYATLARI.put("iPad Pro (M4)", 45000.0);
        MODEL_FIYATLARI.put("iPad Air", 25000.0);
        MODEL_FIYATLARI.put("iPad Mini", 20000.0);

        tabletModelleri.put("Samsung", new String[]{"Galaxy Tab S9 FE", "Galaxy Tab A9+"});
        MODEL_FIYATLARI.put("Galaxy Tab S9 FE", 12000.0);
        MODEL_FIYATLARI.put("Galaxy Tab A9+", 7000.0);

        tabletModelleri.put("Lenovo", new String[]{"Tab P12", "Yoga Tab 11", "Tab K10"});
        MODEL_FIYATLARI.put("Tab P12", 11000.0);
        MODEL_FIYATLARI.put("Yoga Tab 11", 9000.0);
        MODEL_FIYATLARI.put("Tab K10", 6000.0);

        tabletModelleri.put("Huawei", new String[]{"MatePad Pro 13.2", "MatePad Air"});
        MODEL_FIYATLARI.put("MatePad Pro 13.2", 30000.0);
        MODEL_FIYATLARI.put("MatePad Air", 18000.0);

        TUM_MODELLER.put("Tablet", tabletModelleri);

        // LAPTOP
        Map<String, String[]> laptopModelleri = new HashMap<>();
        laptopModelleri.put("Dell", new String[]{"XPS 15", "Latitude 5000", "G15 Gaming"});
        MODEL_FIYATLARI.put("XPS 15", 90000.0);
        MODEL_FIYATLARI.put("Latitude 5000", 40000.0);
        MODEL_FIYATLARI.put("G15 Gaming", 35000.0);

        laptopModelleri.put("HP", new String[]{"Spectre x360", "Pavilion 15", "Victus 16"});
        MODEL_FIYATLARI.put("Spectre x360", 60000.0);
        MODEL_FIYATLARI.put("Pavilion 15", 25000.0);
        MODEL_FIYATLARI.put("Victus 16", 32000.0);

        laptopModelleri.put("Lenovo", new String[]{"ThinkPad X1 Carbon", "IdeaPad 5 Pro", "Legion Pro 7i"});
        MODEL_FIYATLARI.put("ThinkPad X1 Carbon", 80000.0);
        MODEL_FIYATLARI.put("IdeaPad 5 Pro", 30000.0);
        MODEL_FIYATLARI.put("Legion Pro 7i", 95000.0);

        laptopModelleri.put("Apple", new String[]{"MacBook Air M3", "MacBook Pro 16"});
        MODEL_FIYATLARI.put("MacBook Air M3", 45000.0);
        MODEL_FIYATLARI.put("MacBook Pro 16", 90000.0);

        laptopModelleri.put("Asus", new String[]{"ROG Zephyrus", "Zenbook 14 OLED", "TUF Gaming F15"});
        MODEL_FIYATLARI.put("ROG Zephyrus", 70000.0);
        MODEL_FIYATLARI.put("Zenbook 14 OLED", 40000.0);
        MODEL_FIYATLARI.put("TUF Gaming F15", 30000.0);

        laptopModelleri.put("Msi", new String[]{"Stealth 16", "Titan GT77", "Katana 17"});
        MODEL_FIYATLARI.put("Stealth 16", 85000.0);
        MODEL_FIYATLARI.put("Titan GT77", 150000.0);
        MODEL_FIYATLARI.put("Katana 17", 55000.0);

        TUM_MODELLER.put("Laptop", laptopModelleri);
    }

    private static String generateRandomSeriNo(String tur) {
        String prefix = switch (tur) {
            case "Telefon" -> "TEL";
            case "Tablet" -> "TAB";
            case "Laptop" -> "LAP";
            default -> "DEV";
        };
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789";
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return prefix + "-" + sb.toString();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        // Satır sayısını 1 artırdık (11 -> 12)
        JPanel generalPanel = new JPanel(new GridLayout(12, 2, 5, 5));

        txtMusteriAd = new JTextField();
        generalPanel.add(new JLabel("Müşteri Adı:"));
        generalPanel.add(txtMusteriAd);

        txtMusteriSoyad = new JTextField();
        generalPanel.add(new JLabel("Müşteri Soyadı:"));
        generalPanel.add(txtMusteriSoyad);

        txtMusteriTelefon = new JTextField("+90");
        generalPanel.add(new JLabel("Telefon ((+90)5..):"));
        generalPanel.add(txtMusteriTelefon);

        // --- EKLENEN: VIP Müşteri Checkbox ---
        chkVip = new JCheckBox("VIP Müşteri (%20 İndirim)");
        generalPanel.add(new JLabel("Müşteri Statüsü:"));
        generalPanel.add(chkVip);
        // -------------------------------------

        generalPanel.add(new JLabel("Cihaz Türü:"));
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler);
        generalPanel.add(cmbTur);

        txtSeriNo = new JTextField(generateRandomSeriNo((String) cmbTur.getSelectedItem()));
        txtSeriNo.setEditable(false);
        generalPanel.add(new JLabel("Seri No: (Otomatik)"));
        generalPanel.add(txtSeriNo);

        cmbMarka = new JComboBox<>();
        generalPanel.add(new JLabel("Marka:"));
        generalPanel.add(cmbMarka);

        cmbModel = new JComboBox<>();
        generalPanel.add(new JLabel("Model:"));
        generalPanel.add(cmbModel);

        cmbTur.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            guncelMarkaListesiniDoldur(secilenTur);
            cardLayout.show(specificPanel, secilenTur);
            txtSeriNo.setText(generateRandomSeriNo(secilenTur));
        });

        cmbMarka.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            String secilenMarka = (String) cmbMarka.getSelectedItem();
            guncelModelListesiniDoldur(secilenTur, secilenMarka);
        });

        cmbModel.addActionListener(e -> {
            String secilenModel = (String) cmbModel.getSelectedItem();
            if (secilenModel != null && MODEL_FIYATLARI.containsKey(secilenModel)) {
                txtFiyat.setText(String.valueOf(MODEL_FIYATLARI.get(secilenModel)));
            }
        });

        txtFiyat = new JTextField();
        generalPanel.add(new JLabel("Fiyat (TL):"));
        generalPanel.add(txtFiyat);

        chkEskiTarih = new JCheckBox("Geçmiş Tarihli Kayıt (Test Amaçlı)");
        generalPanel.add(new JLabel("Garanti Durumu:"));
        generalPanel.add(chkEskiTarih);

        cardLayout = new CardLayout();
        specificPanel = new JPanel(cardLayout);

        JPanel telefonPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkCiftSim = new JCheckBox("Çift Sim Mi?");
        telefonPanel.add(chkCiftSim);
        specificPanel.add(telefonPanel, "Telefon");

        JPanel tabletPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkKalemDestegi = new JCheckBox("Kalem Desteği Var Mı?");
        tabletPanel.add(chkKalemDestegi);
        specificPanel.add(tabletPanel, "Tablet");

        JPanel laptopPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        chkSsd = new JCheckBox("Harici Ekran Kartı Var Mı?");
        laptopPanel.add(chkSsd);
        specificPanel.add(laptopPanel, "Laptop");

        guncelMarkaListesiniDoldur((String) cmbTur.getSelectedItem());
        cardLayout.show(specificPanel, (String) cmbTur.getSelectedItem());

        JButton btnKaydet = new JButton("Cihazı Kaydet");
        btnKaydet.addActionListener(e -> kaydet());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(generalPanel, BorderLayout.NORTH);
        contentPanel.add(specificPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        add(btnKaydet, BorderLayout.SOUTH);
    }

    private void guncelMarkaListesiniDoldur(String tur) {
        cmbMarka.removeAllItems();
        String[] markalar;
        switch (tur) {
            case "Telefon": markalar = TELEFON_MARKALARI; break;
            case "Tablet": markalar = TABLET_MARKALARI; break;
            case "Laptop": markalar = LAPTOP_MARKALARI; break;
            default: markalar = new String[]{}; break;
        }
        for (String marka : markalar) {
            cmbMarka.addItem(marka);
        }
        if (cmbMarka.getItemCount() > 0) {
            cmbMarka.setSelectedIndex(0);
            guncelModelListesiniDoldur(tur, (String) cmbMarka.getSelectedItem());
        } else {
            guncelModelListesiniDoldur(tur, null);
        }
    }

    private void guncelModelListesiniDoldur(String tur, String marka) {
        cmbModel.removeAllItems();
        if (tur != null && marka != null && TUM_MODELLER.containsKey(tur) && TUM_MODELLER.get(tur).containsKey(marka)) {
            String[] modeller = TUM_MODELLER.get(tur).get(marka);
            for (String model : modeller) {
                cmbModel.addItem(model);
            }
        }
    }

    private void kaydet() {
        try {
            String mAd = txtMusteriAd.getText().trim();
            String mSoyad = txtMusteriSoyad.getText().trim();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty() || mTelefon.isEmpty()) {
                throw new GecersizDegerException("Müşteri bilgileri boş bırakılamaz!");
            }

            if (!mTelefon.startsWith("+90")) {
                throw new GecersizDegerException("Telefon numarası +90 ile başlamalıdır.");
            }

            if (mTelefon.length() != 13 || !mTelefon.substring(1).matches("\\d+")) {
                throw new GecersizDegerException("Telefon numarası +905XXXXXXXXX formatında olmalıdır.");
            }

            // Müşteri nesnesi oluşturma
            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon);

            // --- EKLENEN: VIP Durumunu Set Etme ---
            sahip.setVip(chkVip.isSelected());
            // -------------------------------------

            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem();

            if (marka == null || model == null) {
                throw new GecersizDegerException("Marka ve Model seçimi zorunludur.");
            }

            double fiyat;
            try {
                fiyat = Double.parseDouble(txtFiyat.getText().trim());
            } catch (NumberFormatException e) {
                throw new GecersizDegerException("Fiyat geçerli bir sayı olmalıdır.");
            }

            if (fiyat <= 0) throw new GecersizDegerException("Cihaz fiyatı pozitif olmalıdır.");

            String tur = (String) cmbTur.getSelectedItem();
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
            dispose();

        } catch (GecersizDegerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Giriş Hatası", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}