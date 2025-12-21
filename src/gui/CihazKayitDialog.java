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

// Ana ekrana "yeni cihaz eklendi" haberini vermek için kullanılan arayüz
interface CihazEkleListener {
    void cihazEklendi(Cihaz cihaz);
}

public class CihazKayitDialog extends JDialog {

    private final CihazEkleListener listener; // Ana pencere ile iletişim kuracak nesne

    // UI Bileşenleri (Kullanıcının veri gireceği alanlar)
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

    // Dinamik Alanlar (Seçilen türe göre değişen kısımlar)
    private JPanel specificPanel;
    private CardLayout cardLayout;
    private JCheckBox chkCiftSim;
    private JCheckBox chkKalemDestegi;
    private JCheckBox chkSsd;

    public CihazKayitDialog(JFrame parent, CihazEkleListener listener) {
        super(parent, "Yeni Cihaz Kaydı", true); // Pencere başlığını ve modal özelliğini ayarladık
        this.listener = listener;
        initUI(); // Görsel bileşenleri yükleyen metodu çağırdık
    }

    private void initUI() {
        setSize(500, 750); // Pencerenin boyutlarını belirledik
        setLocationRelativeTo(getParent()); // Pencereyi ana ekranın ortasında açtık
        setLayout(new BorderLayout());

        // --- 1. BAŞLIK (HEADER) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        headerPanel.setBackground(new Color(52, 152, 219)); // Başlık arka planını mavi yaptık

        JLabel lblTitle = new JLabel("Cihaz Kayıt Formu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE); // Yazı rengini beyaz yaptık
        headerPanel.add(lblTitle);

        add(headerPanel, BorderLayout.NORTH); // Başlığı en üste yerleştirdik

        // --- 2. FORM ALANI (CENTER) ---
        JPanel formPanel = new JPanel(new GridBagLayout()); // Form elemanlarını hizalamak için GridBagLayout kullandık
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40)); // Kenarlardan boşluk bıraktık

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0); // Elemanlar arası boşlukları ayarladık
        gbc.fill = GridBagConstraints.HORIZONTAL; // Elemanların yatayda genişlemesini sağladık
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Müşteri Bölümü
        addSectionTitle(formPanel, gbc, "Müşteri Bilgileri"); // Bölüm başlığını ekledik

        txtMusteriAd = addFormField(formPanel, gbc, "Ad:", false); // Ad kutusunu ekledik
        txtMusteriSoyad = addFormField(formPanel, gbc, "Soyad:", false); // Soyad kutusunu ekledik
        txtMusteriTelefon = addFormField(formPanel, gbc, "Telefon ((+90)5..):", false); // Telefon kutusunu ekledik
        txtMusteriTelefon.setText("+90"); // Varsayılan ülke kodunu yazdık

        chkVip = new JCheckBox("VIP Müşteri (%20 İndirim)");
        chkVip.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkVip.setBackground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(chkVip, gbc); // VIP seçim kutusunu ekledik

        // Cihaz Bölümü
        addSeparator(formPanel, gbc); // Araya çizgi çektik
        addSectionTitle(formPanel, gbc, "Cihaz Detayları");

        formPanel.add(createLabel("Cihaz Türü:"), gbc);
        gbc.gridy++;
        String[] turler = {"Telefon", "Tablet", "Laptop"};
        cmbTur = new JComboBox<>(turler); // Tür seçimi için açılır liste oluşturduk
        styleComboBox(cmbTur);
        formPanel.add(cmbTur, gbc);
        gbc.gridy++;

        txtSeriNo = addFormField(formPanel, gbc, "Seri No (Otomatik):", true); // Seri no alanı sadece okunabilir yapıldı
        txtSeriNo.setText(KodUretici.rastgeleSeriNoUret((String) cmbTur.getSelectedItem())); // İlk açılışta rastgele kod ürettik

        formPanel.add(createLabel("Marka:"), gbc);
        gbc.gridy++;
        cmbMarka = new JComboBox<>();
        styleComboBox(cmbMarka);
        formPanel.add(cmbMarka, gbc); // Marka kutusunu ekledik
        gbc.gridy++;

        formPanel.add(createLabel("Model:"), gbc);
        gbc.gridy++;
        cmbModel = new JComboBox<>();
        styleComboBox(cmbModel);
        formPanel.add(cmbModel, gbc); // Model kutusunu ekledik
        gbc.gridy++;

        txtFiyat = addFormField(formPanel, gbc, "Fiyat (TL):", false); // Fiyat kutusunu ekledik

        chkEskiTarih = new JCheckBox("Geçmiş Tarihli Kayıt (Test Amaçlı)");
        chkEskiTarih.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkEskiTarih.setForeground(Color.GRAY);
        chkEskiTarih.setBackground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(chkEskiTarih, gbc);

        // --- Dinamik Alanlar (CardLayout) ---
        gbc.gridy++;
        cardLayout = new CardLayout(); // Paneller arası geçiş yapmak için CardLayout kullandık
        specificPanel = new JPanel(cardLayout);
        specificPanel.setBackground(Color.WHITE);
        specificPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Telefon, Tablet ve Laptop için özel panelleri oluşturduk
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

        // Panelleri ana panele ekledik
        specificPanel.add(pTelefon, "Telefon");
        specificPanel.add(pTablet, "Tablet");
        specificPanel.add(pLaptop, "Laptop");

        formPanel.add(specificPanel, gbc);

        // --- Scroll Pane (Form uzun olabilir) ---
        JScrollPane scrollPane = new JScrollPane(formPanel); // Ekrana sığmazsa kaydırma çubuğu çıksın diye panele aldık
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Kaydırma hızını ayarladık
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. ALT BUTON (FOOTER) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(new Color(245, 248, 250)); // Alt panel rengini ayarladık

        JButton btnKaydet = new JButton("Kaydı Tamamla");
        btnKaydet.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnKaydet.setBackground(new Color(46, 204, 113)); // Butonu yeşil yaptık
        btnKaydet.setForeground(Color.WHITE);
        btnKaydet.setPreferredSize(new Dimension(150, 40));
        btnKaydet.setFocusPainted(false);
        btnKaydet.putClientProperty(FlatClientProperties.STYLE, "arc: 10"); // Köşeleri yuvarlattık

        btnKaydet.addActionListener(e -> kaydet()); // Butona basınca kaydet metodunu çalıştırdık
        footerPanel.add(btnKaydet);
        add(footerPanel, BorderLayout.SOUTH);

        // --- LISTENERS ---
        setupListeners(); // Etkileşim olaylarını başlattık
    }

    // --- YARDIMCI METOTLAR (UI) ---

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(44, 62, 80));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        gbc.gridy++;
        panel.add(label, gbc); // Başlığı panele ekleyip bir alt satıra geçtik
        gbc.gridy++;
    }

    private void addSeparator(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        panel.add(sep, gbc); // Araya gri çizgi ekledik
        gbc.gridy++;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY); // Etiketleri gri ve kalın yaptık
        return lbl;
    }

    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String labelText, boolean readOnly) {
        panel.add(createLabel(labelText), gbc); // Önce etiketi ekledik
        gbc.gridy++;
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(100, 35));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (readOnly) {
            txt.setEditable(false); // Eğer sadece okunabilirse düzenlemeyi kapattık
            txt.setBackground(new Color(240, 240, 240));
        }
        // Köşeleri yuvarlat
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        panel.add(txt, gbc); // Metin kutusunu panele ekledik
        gbc.gridy++;
        return txt;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setPreferredSize(new Dimension(100, 35));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.putClientProperty(FlatClientProperties.STYLE, "arc: 10"); // Açılır kutuları modernize ettik
    }

    // --- MANTIK METOTLARI ---

    private void setupListeners() {
        cmbTur.addActionListener(e -> { // Cihaz türü değişince çalışacak kodlar
            String secilenTur = (String) cmbTur.getSelectedItem();
            guncelMarkaListesiniDoldur(secilenTur); // Markaları o türe göre güncelle
            cardLayout.show(specificPanel, secilenTur); // Özel paneli değiştir (Telefon -> Tablet vb.)
            txtSeriNo.setText(KodUretici.rastgeleSeriNoUret(secilenTur)); // Yeni seri no üret
        });

        cmbMarka.addActionListener(e -> { // Marka değişince modelleri güncelle
            String tur = (String) cmbTur.getSelectedItem();
            String marka = (String) cmbMarka.getSelectedItem();
            guncelModelListesiniDoldur(tur, marka);
        });

        cmbModel.addActionListener(e -> { // Model seçilince fiyatı otomatik getir
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
        cmbMarka.removeAllItems(); // Eski markaları temizle
        String[] markalar = CihazKatalogu.getMarkalar(tur);
        for (String m : markalar) cmbMarka.addItem(m); // Yeni markaları ekle
        if (cmbMarka.getItemCount() > 0) cmbMarka.setSelectedIndex(0);
        guncelModelListesiniDoldur(tur, (String) cmbMarka.getSelectedItem());
    }

    private void guncelModelListesiniDoldur(String tur, String marka) {
        cmbModel.removeAllItems(); // Eski modelleri temizle
        if (tur != null && marka != null) {
            String[] modeller = CihazKatalogu.getModeller(tur, marka);
            for (String m : modeller) cmbModel.addItem(m); // Yeni modelleri ekle
        }
    }

    private void kaydet() {
        try {
            // --- 1. ÖNCE FİYAT KONTROLÜ (En başa alındı) ---
            String fiyatMetni = txtFiyat.getText().trim();
            if (fiyatMetni.isEmpty()) {
                throw new GecersizDegerException("Fiyat alanı boş bırakılamaz."); // Boş fiyat kontrolü
            }

            double fiyat;
            try {
                fiyat = Double.parseDouble(fiyatMetni); // Metni sayıya çevirmeyi denedik
            } catch (NumberFormatException e) {
                throw new GecersizDegerException("Fiyat alanına geçerli bir sayı giriniz.");
            }

            // Negatiflik Kontrolü Burada Yapılıyor
            if (fiyat < 0) {
                throw new GecersizDegerException("Fiyat bilgisi negatif olamaz!"); // Eksi fiyat kontrolü
            }

            // --- 2. SONRA MÜŞTERİ BİLGİLERİ KONTROLÜ ---
            String mAd = txtMusteriAd.getText().trim();
            String mSoyad = txtMusteriSoyad.getText().trim();
            String mTelefon = txtMusteriTelefon.getText().trim();

            if (mAd.isEmpty() || mSoyad.isEmpty()) {
                throw new GecersizDegerException("Müşteri bilgileri boş bırakılamaz!"); // İsim soyisim kontrolü
            }

            if (mTelefon.isEmpty() || mTelefon.length() < 14) {
                throw new GecersizDegerException("Telefon bilgisi alanı boş bırakılamaz!\nLütfen geçerli bir numara giriniz ((+90) dahil).");
            }

            // --- 3. NESNE OLUŞTURMA VE KAYIT ---
            Musteri sahip = new Musteri(mAd, mSoyad, mTelefon); // Müşteri nesnesini oluşturduk
            sahip.setVip(chkVip.isSelected()); // VIP durumunu ayarladık

            String seriNo = txtSeriNo.getText().trim();
            String marka = (String) cmbMarka.getSelectedItem();
            String model = (String) cmbModel.getSelectedItem();
            String tur = (String) cmbTur.getSelectedItem();
            LocalDate garantiBaslangic = chkEskiTarih.isSelected() ? null : LocalDate.now(); // Garanti tarihini ayarladık

            Cihaz yeniCihaz;
            switch (tur) { // Seçilen türe göre (Telefon/Tablet/Laptop) uygun nesneyi oluşturduk
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

            listener.cihazEklendi(yeniCihaz); // Ana ekrana yeni cihazı gönderdik
            JOptionPane.showMessageDialog(this, tur + " başarıyla kaydedildi.", "İşlem Başarılı", JOptionPane.INFORMATION_MESSAGE); // Kullanıcıya başarılı mesajı verdik
            dispose(); // Pencereyi kapattık

        } catch (GecersizDegerException ex) {
            // Hata mesajını göster
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Uyarı", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // Beklenmeyen sistem hataları
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Sistem Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
}