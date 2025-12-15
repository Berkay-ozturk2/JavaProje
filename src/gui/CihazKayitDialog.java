// src/gui/CihazKayitDialog.java (GÜNCELLENDİ: Model ComboBox ve Otomatik Seri No)
package gui;

import Cihazlar.*;
import Musteri.Musteri;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap; // YENİ IMPORT
import java.util.Map; // YENİ IMPORT
import java.util.Random; // YENİ IMPORT

// Ana pencereye Cihaz nesnesi döndürmek için arayüz
interface CihazEkleListener {
    void cihazEklendi(Cihaz cihaz);
}

public class CihazKayitDialog extends JDialog {

    private final CihazEkleListener listener;

    // Ortak Alanlar (Text Fields ve ComboBoxes)
    private JComboBox<String> cmbTur;
    private JTextField txtSeriNo; // Seri No otomatik üretileceği için JTextField'da tutulacak ve setEditable(false) olacak
    private JComboBox<String> cmbMarka;
    private JComboBox<String> cmbModel; // DÜZELTME: Model artık ComboBox
    private JTextField txtFiyat;

    // YENİ ALANLAR: MÜŞTERİ BİLGİLERİ
    private JTextField txtMusteriAd;
    private JTextField txtMusteriSoyad;
    private JTextField txtMusteriTelefon;

    // Cihaz Türlerine ve Markalara Göre Model Listeleri (YENİ YAPILANDIRMA)
    private final Map<String, Map<String, String[]>> TUM_MODELLER = new HashMap<>();

    // Cihaz Türlerine Göre Marka Listeleri (Öncekiyle aynı, ancak artık model verisi de var)
    private final String[] TELEFON_MARKALARI = {"Samsung", "Apple", "Xiaomi", "Huawei"};
    private final String[] TABLET_MARKALARI = {"Apple", "Samsung", "Lenovo","Huawei"};
    private final String[] LAPTOP_MARKALARI = {"Dell", "HP", "Lenovo", "Apple", "Asus","Msi"};

    // Cihaza Özgü Alanlar (CardLayout)
    private JPanel specificPanel;
    private CardLayout cardLayout;
    private JCheckBox chkCiftSim;       // Telefon için
    private JCheckBox chkKalemDestegi; // Tablet için
    private JCheckBox chkHariciEkranKarti; // Laptop için

    public CihazKayitDialog(JFrame parent, CihazEkleListener listener) {
        super(parent, "Yeni Cihaz Kaydı (Manuel Giriş)", true);
        this.listener = listener;
        setSize(500, 600);
        setLocationRelativeTo(parent);

        initModelData(); // Model verilerini yükle
        initUI();
    }

    /**
     * Cihaz türü ve markaya göre model verilerini doldurur.
     */
    private void initModelData() {
        // TELEFON MODELLERİ
        Map<String, String[]> telefonModelleri = new HashMap<>();
        telefonModelleri.put("Samsung", new String[]{"Galaxy S24 Ultra", "Galaxy A55", "Galaxy Flip 5", "Galaxy S23 FE"});
        telefonModelleri.put("Apple", new String[]{"iPhone 15 Pro Max", "iPhone SE", "iPhone 14", "iPhone 13"});
        telefonModelleri.put("Xiaomi", new String[]{"Redmi Note 13 Pro", "Xiaomi 14 Ultra", "Poco X6"});
        telefonModelleri.put("Huawei", new String[]{"Pura 70 Ultra", "Mate 60 RS"});
        TUM_MODELLER.put("Telefon", telefonModelleri);

        // TABLET MODELLERİ
        Map<String, String[]> tabletModelleri = new HashMap<>();
        tabletModelleri.put("Apple", new String[]{"iPad Pro (M4)", "iPad Air", "iPad Mini"});
        tabletModelleri.put("Samsung", new String[]{"Galaxy Tab S9 FE", "Galaxy Tab A9+"});
        tabletModelleri.put("Lenovo", new String[]{"Tab P12", "Yoga Tab 11", "Tab K10"});
        tabletModelleri.put("Huawei", new String[]{"MatePad Pro 13.2", "MatePad Air"});
        TUM_MODELLER.put("Tablet", tabletModelleri);

        // LAPTOP MODELLERİ
        Map<String, String[]> laptopModelleri = new HashMap<>();
        laptopModelleri.put("Dell", new String[]{"XPS 15", "Latitude 5000", "G15 Gaming"});
        laptopModelleri.put("HP", new String[]{"Spectre x360", "Pavilion 15", "Victus 16"});
        laptopModelleri.put("Lenovo", new String[]{"ThinkPad X1 Carbon", "IdeaPad 5 Pro", "Legion Pro 7i"});
        laptopModelleri.put("Apple", new String[]{"MacBook Air M3", "MacBook Pro 16"});
        laptopModelleri.put("Asus", new String[]{"ROG Zephyrus", "Zenbook 14 OLED", "TUF Gaming F15"});
        laptopModelleri.put("Msi", new String[]{"Stealth 16", "Titan GT77", "Katana 17"});
        TUM_MODELLER.put("Laptop", laptopModelleri);
    }

    /**
     * Cihaz türüne özgü rastgele bir seri numarası üretir.
     * Örn: TEL-A1B2C3D4E5
     */
    private static String generateRandomSeriNo(String tur) {
        String prefix;
        switch (tur) {
            case "Telefon":
                prefix = "TEL";
                break;
            case "Tablet":
                prefix = "TAB";
                break;
            case "Laptop":
                prefix = "LAP";
                break;
            default:
                prefix = "DEV";
                break;
        }

        // Rastgele 10 haneli alfanümerik kısım
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return prefix + "-" + sb.toString();
    }


    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel generalPanel = new JPanel(new GridLayout(10, 2, 5, 5));

        // =====================================
        // --- Müşteri Bilgileri ---
        // =====================================
        txtMusteriAd = new JTextField();
        generalPanel.add(new JLabel("Müşteri Adı:"));
        generalPanel.add(txtMusteriAd);

        txtMusteriSoyad = new JTextField();
        generalPanel.add(new JLabel("Müşteri Soyadı:"));
        generalPanel.add(txtMusteriSoyad);

        txtMusteriTelefon = new JTextField();
        generalPanel.add(new JLabel("Telefon:"));
        generalPanel.add(txtMusteriTelefon);

        //
        // =====================================
        // --- Cihaz Ortak Alanları ---
        // =====================================
        // --- 1. Cihaz Türü Seçimi ---
        generalPanel.add(new JLabel("Cihaz Türü:"));
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler);
        generalPanel.add(cmbTur);

        // --- 2. Ortak Alanlar Girişi ---

        // DÜZELTME: Seri No Otomatik Üretim
        txtSeriNo = new JTextField(generateRandomSeriNo((String) cmbTur.getSelectedItem()));
        txtSeriNo.setEditable(false); // Kullanıcının değiştirmesini engelle
        generalPanel.add(new JLabel("Seri No: (Otomatik)"));
        generalPanel.add(txtSeriNo);

        // --- MARKA SEÇİMİ ---
        cmbMarka = new JComboBox<>();
        generalPanel.add(new JLabel("Marka:"));
        generalPanel.add(cmbMarka);

        // --- MODEL SEÇİMİ (YENİ) ---
        cmbModel = new JComboBox<>();
        generalPanel.add(new JLabel("Model:"));
        generalPanel.add(cmbModel);


        // Cihaz türü değiştikçe marka listesini ve seri no'yu güncelle
        cmbTur.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            guncelMarkaListesiniDoldur(secilenTur);
            cardLayout.show(specificPanel, secilenTur);

            // Seri No'yu yeni türe göre güncelle
            txtSeriNo.setText(generateRandomSeriNo(secilenTur));
        });

        // Marka değiştikçe Model listesini güncelle (YENİ)
        cmbMarka.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            String secilenMarka = (String) cmbMarka.getSelectedItem();
            guncelModelListesiniDoldur(secilenTur, secilenMarka);
        });
//yorum satırı

        // Fiyat Girişi
        txtFiyat = new JTextField();
        generalPanel.add(new JLabel("Fiyat (TL):"));
        generalPanel.add(txtFiyat);


        // --- 3. Cihaza Özgü Alanlar (CardLayout ile Yönetilir) ---
        cardLayout = new CardLayout();
        specificPanel = new JPanel(cardLayout);

        // Telefon Alanı
        JPanel telPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkCiftSim = new JCheckBox("Çift SIM Desteği Var mı?");
        telPanel.add(chkCiftSim);
        specificPanel.add(telPanel, "Telefon");

        // Tablet Alanı
        JPanel tabletPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkKalemDestegi = new JCheckBox("Kalem Desteği Var mı?");
        tabletPanel.add(chkKalemDestegi);
        specificPanel.add(tabletPanel, "Tablet");

        // Laptop Alanı
        JPanel laptopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkHariciEkranKarti = new JCheckBox("Harici Ekran Kartı Var mı?");
        laptopPanel.add(chkHariciEkranKarti);
        specificPanel.add(laptopPanel, "Laptop");


        // --- Başlangıç Ayarları ---
        // Başlangıçta ilk seçili türün markasını ve modelini yükle
        guncelMarkaListesiniDoldur((String) cmbTur.getSelectedItem());
        cardLayout.show(specificPanel, (String) cmbTur.getSelectedItem());

        // --- 4. Kaydet Butonu ---
        JButton btnKaydet = new JButton("Cihazı Kaydet");
        btnKaydet.addActionListener(e -> kaydet());

        // Layout Birleştirme
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(generalPanel, BorderLayout.NORTH);
        contentPanel.add(specificPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        add(btnKaydet, BorderLayout.SOUTH);
    }

    /**
     * Seçilen cihaza göre marka ComboBox'ını doldurur.
     */
    private void guncelMarkaListesiniDoldur(String tur) {
        cmbMarka.removeAllItems(); // Önceki öğeleri temizle

        String[] markalar;
        switch (tur) {
            case "Telefon":
                markalar = TELEFON_MARKALARI;
                break;
            case "Tablet":
                markalar = TABLET_MARKALARI;
                break;
            case "Laptop":
                markalar = LAPTOP_MARKALARI;
                break;
            default:
                markalar = new String[]{};
                break;
        }

        for (String marka : markalar) {
            cmbMarka.addItem(marka);
        }

        // YENİ: Marka listesi yüklendikten sonra ilk markanın model listesini de yükle
        if (cmbMarka.getItemCount() > 0) {
            cmbMarka.setSelectedIndex(0); // İlk öğeyi otomatik seç
            guncelModelListesiniDoldur(tur, (String) cmbMarka.getSelectedItem());
        } else {
            guncelModelListesiniDoldur(tur, null); // Model listesini temizle
        }
    }

    /**
     * Seçilen marka ve türe göre model ComboBox'ını doldurur. (YENİ)
     */
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
            // Müşteri Bilgileri
            String mAd = txtMusteriAd.getText().trim();
            String mSoyad = txtMusteriSoyad.getText().trim();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty() || mTelefon.isEmpty()) {
                throw new IllegalArgumentException("Müşteri Adı, Soyadı ve Telefon bilgileri zorunludur.");
            }

            // Müşteri nesnesi oluştur
            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon );

            // Cihaz Bilgileri
            String seriNo = txtSeriNo.getText().trim(); // Otomatik üretilen seri no
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem(); // ComboBox'tan al

            if (marka == null || model == null || marka.isEmpty() || model.isEmpty()) {
                throw new IllegalArgumentException("Marka ve Model seçimi zorunludur.");
            }

            // Fiyat değerini al ve kontrol et
            double fiyat = Double.parseDouble(txtFiyat.getText().trim());
            if (fiyat <= 0) {
                throw new IllegalArgumentException("Cihaz fiyatı pozitif bir değer olmalıdır.");
            }

            String tur = (String) cmbTur.getSelectedItem();
            LocalDate garantiBaslangic = LocalDate.now();


            Cihaz yeniCihaz;

            switch (tur) {
                case "Telefon":
                    // chkCiftSim'in durumu Telefon constructor'ında kullanılmadığı için dikkate alınmaz
                    yeniCihaz = new Telefon(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                case "Tablet":
                    boolean kalemDestegi = chkKalemDestegi.isSelected();
                    yeniCihaz = new Tablet(seriNo, marka, model, fiyat, garantiBaslangic, kalemDestegi, sahip);
                    break;
                case "Laptop":
                    // chkHariciEkranKarti'nın durumu Laptop constructor'ında kullanılmadığı için dikkate alınmaz
                    yeniCihaz = new Laptop(seriNo, marka, model, fiyat, garantiBaslangic, sahip);
                    break;
                default:
                    throw new IllegalArgumentException("Geçersiz cihaz türü seçimi.");
            }

            listener.cihazEklendi(yeniCihaz);
            JOptionPane.showMessageDialog(this, tur + " başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Pencereyi kapat

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fiyat alanı geçerli bir sayı olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}