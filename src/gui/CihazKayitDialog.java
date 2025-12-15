// src/gui/CihazKayitDialog.java (GÜNCELLENDİ)
package gui;

import Cihazlar.*;
import Musteri.Musteri; // YENİ IMPORT
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

// Ana pencereye Cihaz nesnesi döndürmek için arayüz
interface CihazEkleListener {
    void cihazEklendi(Cihaz cihaz);
}

public class CihazKayitDialog extends JDialog {

    private final CihazEkleListener listener;

    // Ortak Alanlar (Text Fields ve ComboBoxes)
    private JComboBox<String> cmbTur;
    private JTextField txtSeriNo;
    private JComboBox<String> cmbMarka;
    private JTextField txtModel;


    // YENİ ALANLAR: MÜŞTERİ BİLGİLERİ
    private JTextField txtMusteriAd;
    private JTextField txtMusteriSoyad;
    private JTextField txtMusteriTelefon;

    // Cihaz Türlerine Göre Marka Listeleri
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
        setSize(500, 600); // Boyut büyütüldü
        setLocationRelativeTo(parent);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel generalPanel = new JPanel(new GridLayout(10, 2, 5, 5)); // Satır sayısı artırıldı

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


        // =====================================
        // --- Cihaz Ortak Alanları ---
        // =====================================
        // --- 1. Cihaz Türü Seçimi ---
        generalPanel.add(new JLabel("Cihaz Türü:"));
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler);
        generalPanel.add(cmbTur);

        // --- 2. Ortak Alanlar Girişi ---
        txtSeriNo = new JTextField();
        generalPanel.add(new JLabel("Seri No:"));
        generalPanel.add(txtSeriNo);

        // --- MARKA SEÇİMİ (YENİ) ---
        cmbMarka = new JComboBox<>();
        generalPanel.add(new JLabel("Marka:"));
        generalPanel.add(cmbMarka); // Marka artık ComboBox

        // Cihaz türü değiştikçe marka listesini güncelle
        cmbTur.addActionListener(e -> {
            String secilenTur = (String) cmbTur.getSelectedItem();
            guncelMarkaListesiniDoldur(secilenTur);
            cardLayout.show(specificPanel, secilenTur);
        });

        txtModel = new JTextField();
        generalPanel.add(new JLabel("Model:"));
        generalPanel.add(txtModel);


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
        // Başlangıçta ilk seçili türün markasını yükle
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
            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = txtModel.getText().trim();

            String tur = (String) cmbTur.getSelectedItem();
            LocalDate garantiBaslangic = LocalDate.now();



            Cihaz yeniCihaz;

            switch (tur) {
                case "Telefon":
                    boolean ciftSim = chkCiftSim.isSelected();
                    yeniCihaz = new Telefon(seriNo, marka, model, fiyat, garantiBaslangic, ciftSim, sahip); // Müşteri eklendi
                    break;
                case "Tablet":
                    boolean kalemDestegi = chkKalemDestegi.isSelected();
                    yeniCihaz = new Tablet(seriNo, marka, model, fiyat, garantiBaslangic, kalemDestegi, sahip); // Müşteri eklendi
                    break;
                case "Laptop":
                    boolean hariciEkranKarti = chkHariciEkranKarti.isSelected();
                    yeniCihaz = new Laptop(seriNo, marka, model, fiyat, garantiBaslangic, hariciEkranKarti, sahip); // Müşteri eklendi
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