package gui;

import Araclar.DosyaIslemleri;
import Cihazlar.Cihaz;
import Servis.RaporlamaHizmeti;
import Servis.ServisDurumu;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Istisnalar.KayitBulunamadiException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtAramaGirdisi;
    private JComboBox<String> cmbAramaTuru;
    private JTextArea txtBilgiEkrani;
    private JProgressBar progressBar;
    private JLabel lblDurumMesaji;

    // PC tarafı için dosya yolları
    private static final String CIHAZ_DOSYA_YOLU = System.getProperty("user.dir") +
            System.getProperty("file.separator") + "cihazlar.txt";
    private static final String SERVIS_DOSYA_YOLU = System.getProperty("user.dir") +
            System.getProperty("file.separator") + "servisler.txt";

    public MusteriTakipEkrani() {
        initUI();
    }

    private void initUI() {
        setTitle("Müşteri Cihaz Sorgulama Sistemi");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // HEADER
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 0));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Cihaz ve Servis Durumu", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblDesc = new JLabel("Seri numarası veya Telefon numarası ile sorgulama yapabilirsiniz.", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(lblDesc);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CENTER
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);

        // Arama Paneli
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setOpaque(false);

        cmbAramaTuru = new JComboBox<>(new String[]{"Seri No ile", "Telefon No ile"});
        cmbAramaTuru.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cmbAramaTuru.setPreferredSize(new Dimension(110, 35));

        txtAramaGirdisi = new JTextField(15);
        txtAramaGirdisi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAramaGirdisi.setPreferredSize(new Dimension(200, 35));
        txtAramaGirdisi.putClientProperty("JTextField.placeholderText", "Giriş yapınız...");

        JButton btnSorgula = createButton("Sorgula", new Color(52, 152, 219));
        JButton btnTemizle = createButton("Temizle", new Color(149, 165, 166));

        searchPanel.add(cmbAramaTuru);
        searchPanel.add(txtAramaGirdisi);
        searchPanel.add(btnSorgula);
        searchPanel.add(btnTemizle);

        // Sonuç Ekranı
        txtBilgiEkrani = new JTextArea();
        txtBilgiEkrani.setEditable(false);
        txtBilgiEkrani.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBilgiEkrani.setBackground(Color.WHITE);
        txtBilgiEkrani.setForeground(new Color(40, 40, 40));
        txtBilgiEkrani.setMargin(new Insets(15, 15, 15, 15));
        txtBilgiEkrani.setText("\n\n      Lütfen yukarıdan arama türünü seçip bilginizi giriniz.");

        JScrollPane scrollPane = new JScrollPane(txtBilgiEkrani);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Progress Bar Paneli
        JPanel statusPanel = new JPanel(new BorderLayout(0, 5));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setPreferredSize(new Dimension(100, 25));
        progressBar.setVisible(false);

        lblDurumMesaji = new JLabel("İşlem Durumu Bekleniyor...", SwingConstants.CENTER);
        lblDurumMesaji.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDurumMesaji.setForeground(Color.GRAY);
        lblDurumMesaji.setVisible(false);

        statusPanel.add(lblDurumMesaji, BorderLayout.NORTH);
        statusPanel.add(progressBar, BorderLayout.CENTER);

        JPanel contentContainer = new JPanel(new BorderLayout(0, 10));
        contentContainer.setOpaque(false);
        contentContainer.add(searchPanel, BorderLayout.NORTH);
        contentContainer.add(scrollPane, BorderLayout.CENTER);
        contentContainer.add(statusPanel, BorderLayout.SOUTH);

        mainPanel.add(contentContainer, BorderLayout.CENTER);

        // ACTIONS
        btnSorgula.addActionListener(e -> sorgulaIslemi());
        txtAramaGirdisi.addActionListener(e -> sorgulaIslemi());

        btnTemizle.addActionListener(e -> {
            txtAramaGirdisi.setText("");
            txtBilgiEkrani.setText("\n\n      Ekran temizlendi.");
            progressBar.setVisible(false);
            lblDurumMesaji.setVisible(false);
        });

        cmbAramaTuru.addActionListener(e -> {
            txtAramaGirdisi.setText("");
            if (cmbAramaTuru.getSelectedIndex() == 0) {
                txtAramaGirdisi.putClientProperty("JTextField.placeholderText", "Örn: TEL-1234");
            } else {
                txtAramaGirdisi.putClientProperty("JTextField.placeholderText", "Örn: 5551234567");
            }
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void sorgulaIslemi() {
        String girdi = txtAramaGirdisi.getText().trim();
        if (girdi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen arama kutusunu boş bırakmayınız.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtBilgiEkrani.setText("Sorgulanıyor...\nLütfen bekleyiniz.");
        progressBar.setVisible(false);
        lblDurumMesaji.setVisible(false);

        if (cmbAramaTuru.getSelectedIndex() == 0) {
            seriNoIleAra(girdi);
        } else {
            telefonIleAra(girdi);
        }
    }

    private void telefonIleAra(String telefon) {
        // Cihazları yükle (PC yolunu kullanıyoruz)
        List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(CIHAZ_DOSYA_YOLU);

        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== MÜŞTERİ CİHAZ LİSTESİ ===\n\n");
        sonuc.append("Sorgulanan Telefon: ").append(telefon).append("\n");
        sonuc.append("-----------------------------------------\n");

        int cihazSayisi = 0;
        String arananTelTemiz = telefon.replaceAll("\\s+", "");

        for (Cihaz c : cihazlar) {
            String kayitliTelTemiz = c.getSahip().getTelefon().replaceAll("\\s+", "");
            if (kayitliTelTemiz.contains(arananTelTemiz)) {
                cihazSayisi++;
                sonuc.append(cihazSayisi).append(". CİHAZ\n");
                sonuc.append("Model: ").append(c.getMarka()).append(" ").append(c.getModel()).append("\n");
                sonuc.append("Seri No: ").append(c.getSeriNo()).append("\n");
                sonuc.append("Sahibi: ").append(c.getSahip().getAd()).append(" ").append(c.getSahip().getSoyad()).append("\n");
                sonuc.append("Garanti: ").append(c.garantiAktifMi() ? "Devam Ediyor" : "Bitti").append("\n");

                // DEĞİŞİKLİK: Servis Yönetimi başlatırken dosya yolunu veriyoruz
                ServisYonetimi sy = new ServisYonetimi(cihazlar, SERVIS_DOSYA_YOLU);

                boolean servisteMi = false;
                for(ServisKaydi k : sy.getKayitlar()) {
                    if(k.getCihaz().getSeriNo().equals(c.getSeriNo()) && k.getDurum() != ServisDurumu.TAMAMLANDI) {
                        sonuc.append("SERVİS DURUMU: ").append(k.getDurum()).append("\n");
                        servisteMi = true;
                    }
                }
                if(!servisteMi) sonuc.append("SERVİS DURUMU: Serviste Değil\n");

                sonuc.append("-----------------------------------------\n");
            }
        }

        if (cihazSayisi == 0) {
            txtBilgiEkrani.setForeground(new Color(192, 57, 43));
            txtBilgiEkrani.setText("\n!!! KAYIT BULUNAMADI !!!\n\nBu telefon numarasına kayıtlı cihaz yok.");
        } else {
            txtBilgiEkrani.setForeground(new Color(40, 40, 40));
            txtBilgiEkrani.setText(sonuc.toString());
        }
    }

    private void seriNoIleAra(String seriNo) {
        try {
            // ÖNCE GEREKLİ VERİLERİ YÜKLÜYORUZ (PC Yollarını Kullanarak)
            List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(CIHAZ_DOSYA_YOLU);
            ServisYonetimi sy = new ServisYonetimi(cihazlar, SERVIS_DOSYA_YOLU);

            // GÜNCELLEME: Raporlama servisine verileri biz gönderiyoruz
            String rapor = RaporlamaHizmeti.musteriCihazDurumRaporuOlustur(seriNo, cihazlar, sy);

            StringBuilder susluRapor = new StringBuilder();
            susluRapor.append("=========================================\n");
            susluRapor.append("          DETAYLI CİHAZ RAPORU           \n");
            susluRapor.append("=========================================\n\n");
            susluRapor.append(rapor);

            txtBilgiEkrani.setForeground(new Color(40, 40, 40));
            txtBilgiEkrani.setText(susluRapor.toString());

            ServisDurumu durum = servisDurumunuBul(seriNo);
            gorselDurumuGuncelle(durum);

        } catch (KayitBulunamadiException ex) {
            txtBilgiEkrani.setForeground(new Color(192, 57, 43));
            txtBilgiEkrani.setText("\n!!! KAYIT BULUNAMADI !!!\n\n" + ex.getMessage());
            gorselDurumuGuncelle(null);
        } catch (Exception ex) {
            txtBilgiEkrani.setText("Hata: " + ex.getMessage());
        }
    }

    private ServisDurumu servisDurumunuBul(String seriNo) {
        // Dosyaları yükle (PC yolunu kullanıyoruz)
        List<Cihaz> cihazlar = DosyaIslemleri.cihazlariYukle(CIHAZ_DOSYA_YOLU);

        // DEĞİŞİKLİK: ServisYonetimi başlatırken dosya yolunu veriyoruz
        ServisYonetimi sy = new ServisYonetimi(cihazlar, SERVIS_DOSYA_YOLU);

        for (ServisKaydi k : sy.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(seriNo)) {
                return k.getDurum();
            }
        }
        return null;
    }

    private void gorselDurumuGuncelle(ServisDurumu durum) {
        if (durum == null) {
            progressBar.setVisible(false);
            lblDurumMesaji.setVisible(true);
            lblDurumMesaji.setText("Bu cihaz şu an serviste değil.");
            lblDurumMesaji.setForeground(Color.GRAY);
            return;
        }

        progressBar.setVisible(true);
        lblDurumMesaji.setVisible(true);

        if (durum == ServisDurumu.KABUL_EDILDI) {
            progressBar.setValue(30);
            progressBar.setString("%30 - İşleme Alındı");
            progressBar.setForeground(new Color(241, 196, 15));
            lblDurumMesaji.setText("Cihazınız teknisyen tarafından inceleniyor.");
            lblDurumMesaji.setForeground(new Color(211, 84, 0));
        } else if (durum == ServisDurumu.TAMAMLANDI) {
            progressBar.setValue(100);
            progressBar.setString("%100 - Tamamlandı");
            progressBar.setForeground(new Color(46, 204, 113));
            lblDurumMesaji.setText("Cihazınızın işlemleri bitti. Teslim alabilirsiniz.");
            lblDurumMesaji.setForeground(new Color(39, 174, 96));
        }
    }
}