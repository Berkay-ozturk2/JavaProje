package gui;

import Cihazlar.*;
import Musteri.Musteri;
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

    // YENİ EKLENEN KUTUCUK
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
        super(parent, "Yeni Cihaz Kaydı", true); // Başlık sadeleştirildi
        this.listener = listener;
        setSize(500, 650); // Yükseklik biraz artırıldı
        setLocationRelativeTo(parent);

        initModelData();
        initUI();
    }

    // ... (initModelData ve generateRandomSeriNo metodları aynen kalacak, buraya tekrar yazmıyorum) ...
    // Sadece initModelData() ve generateRandomSeriNo() kısmını değiştirmeyin, orası orijinal kodunuzla aynı kalsın.

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
        JPanel generalPanel = new JPanel(new GridLayout(11, 2, 5, 5)); // Satır sayısı artırıldı (11)

        txtMusteriAd = new JTextField();
        generalPanel.add(new JLabel("Müşteri Adı:"));
        generalPanel.add(txtMusteriAd);

        txtMusteriSoyad = new JTextField();
        generalPanel.add(new JLabel("Müşteri Soyadı:"));
        generalPanel.add(txtMusteriSoyad);

        txtMusteriTelefon = new JTextField();
        generalPanel.add(new JLabel("Telefon (Sadece Rakam):"));
        generalPanel.add(txtMusteriTelefon);

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

        // ... Listenerlar aynı ...
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

        // --- SİZİN İÇİN EKLENEN ÖZEL KISIM ---
        // Bu kutucuk seçilirse null gönderip sizin rastgele tarih kodunuzu çalıştıracağız.
        chkEskiTarih = new JCheckBox("Geçmiş Tarihli Kayıt (Test Amaçlı)");
        // Varsayılan olarak boş gelsin, böylece normal kayıt "bugün" olur.
        // Test verisi girmek istediğinizde bunu işaretlersiniz.
        generalPanel.add(new JLabel("Garanti Durumu:"));
        generalPanel.add(chkEskiTarih);
        // -------------------------------------

        cardLayout = new CardLayout();
        specificPanel = new JPanel(cardLayout);

        // Paneller aynı
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

    // Marka ve Model doldurma metodları aynı kalabilir...
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

    // --- KRİTİK DEĞİŞİKLİKLER BURADA ---
    private void kaydet() {
        try {
            String mAd = txtMusteriAd.getText().trim().toUpperCase();
            String mSoyad = txtMusteriSoyad.getText().trim().toUpperCase();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty() || mTelefon.isEmpty()) {
                throw new IllegalArgumentException("Müşteri bilgileri zorunludur.");
            }

            // Telefon formatı kontrolü (Sadece rakam olsun)
            if (!mTelefon.matches("\\d+")) {
                throw new IllegalArgumentException("Telefon numarası sadece rakamlardan oluşmalıdır.");
            }

            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon);
            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem();

            if (marka == null || model == null) {
                throw new IllegalArgumentException("Marka ve Model seçimi zorunludur.");
            }

            double fiyat = Double.parseDouble(txtFiyat.getText().trim());
            if (fiyat <= 0) throw new IllegalArgumentException("Cihaz fiyatı pozitif olmalıdır.");

            String tur = (String) cmbTur.getSelectedItem();

            // --- SİZİN İSTEDİĞİNİZ MANTIK ---
            // Eğer kutucuk seçiliyse NULL gönderiyoruz (Cihaz sınıfı rastgele tarih atıyor).
            // Seçili değilse BUGÜN tarihini gönderiyoruz (Sıfır cihaz).
            LocalDate garantiBaslangic = chkEskiTarih.isSelected() ? null : LocalDate.now();

            Cihaz yeniCihaz;

            switch (tur) {
                case "Telefon":
                    yeniCihaz = new Telefon(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                case "Tablet":
                    yeniCihaz = new Tablet(seriNo, marka, model, fiyat, garantiBaslangic, chkKalemDestegi.isSelected(),sahip);
                    break;
                case "Laptop":
                    yeniCihaz = new Laptop(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                default:
                    throw new IllegalArgumentException("Geçersiz tür.");
            }

            listener.cihazEklendi(yeniCihaz);
            JOptionPane.showMessageDialog(this, tur + " başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fiyat geçerli bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}