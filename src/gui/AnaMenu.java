package gui;

import Araclar.DosyaIslemleri;
import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Servis.FiyatlandirmaHizmeti;
import Servis.RaporlamaHizmeti;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import com.formdev.flatlaf.FlatClientProperties; // FlatLaf özellikleri

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Ana ekranımız burası, hem tabloyu hem butonları yönetiyor ve Cihaz Ekleme olaylarını dinliyor
public class AnaMenu extends JFrame implements CihazEkleListener {

    private JTabbedPane tabbedPane; // Sekmeli yapı (Telefon, Tablet vs. ayrımı için)
    private Map<String, DefaultTableModel> tableModels = new HashMap<>(); // Tablo modellerini isme göre tutuyoruz
    private Map<String, JTable> tables = new HashMap<>(); // Tabloları isme göre tutuyoruz

    private List<Cihaz> cihazListesi = new ArrayList<>(); // Tüm cihazların tutulduğu ana liste
    // Dosya yolunu dinamik olarak alıyoruz ki her bilgisayarda çalışsın
    private static final String CIHAZ_DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "cihazlar.txt";

    private ServisYonetimi servisYonetimi; // Servis işlemlerini yapan sınıf

    // --- RENK PALETİ (TUTARLI TASARIM İÇİN) ---
    // Renkleri burada sabitliyorum ki her yerde aynı tonu kullanabileyim
    private static final Color COLOR_ACTION_PRIMARY = new Color(52, 152, 219); // Açık Mavi (İşlemler)
    private static final Color COLOR_ACTION_SECONDARY = new Color(52, 73, 94); // Koyu Lacivert (Rapor/Takip)
    private static final Color COLOR_DANGER = new Color(231, 76, 60);          // Kırmızı (Silme)
    private static final Color COLOR_NEUTRAL = new Color(149, 165, 166);       // Gri (Çıkış)

    public AnaMenu() {
        // Program açılırken verileri dosyadan yüklüyoruz
        cihazListesi = DosyaIslemleri.cihazlariYukle(CIHAZ_DOSYA_ADI);
        servisYonetimi = new ServisYonetimi(cihazListesi);

        initUI(); // Ekran tasarımını başlatıyoruz
        cihazListesiniTabloyaDoldur(cihazListesi); // Tabloları verilerle dolduruyoruz

        System.out.println("Sistem başlatıldı. Cihaz sayısı: " + cihazListesi.size());
    }

    private void initUI() {
        setTitle("Teknik Servis Yönetim Paneli"); // Pencere başlığı
        setSize(1280, 800); // Pencere boyutu
        setLocationRelativeTo(null); // Ortada açılması için
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Kapatınca program dursun

        // Ana Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(new Color(245, 248, 250)); // Arka planı açık gri yaptık
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Kenar boşlukları verdik
        setContentPane(mainPanel);

        // --- 1. ÜST BAŞLIK (HEADER) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); // Arka planı şeffaf yaptık

        JLabel lblTitle = new JLabel("Cihaz ve Servis Yönetimi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblSub = new JLabel("Aktif Cihazlar ve Servis İşlemleri Listesi");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblSub);

        headerPanel.add(textPanel, BorderLayout.WEST); // Yazıları sola yasladık
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. ORTA BÖLÜM (TABLOLAR) ---
        tabbedPane = new JTabbedPane(); // Sekmeleri oluşturduk
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.putClientProperty(FlatClientProperties.STYLE, "tabSeparators: true");

        createTab("Tümü");
        createTab("Telefon");
        createTab("Tablet");
        createTab("Laptop");

        mainPanel.add(tabbedPane, BorderLayout.CENTER); // Tabloları ortaya yerleştirdik

        // --- 3. ALT BÖLÜM (BUTON PANELİ) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // GRUP 1: Operasyonel İşlemler (Hepsi Mavi)
        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftActions.setOpaque(false);

        JButton btnEkle = createStyledButton("Yeni Cihaz", COLOR_ACTION_PRIMARY);
        btnEkle.addActionListener(e -> {
            CihazKayit dialog = new CihazKayit(this, this); // Kayıt penceresini açıyoruz
            dialog.setVisible(true);
        });

        JButton btnServis = createStyledButton("Servis Kaydı", COLOR_ACTION_PRIMARY);
        btnServis.addActionListener(e -> servisKaydiOlusturIslemi()); // Servis işlemini başlatıyoruz

        JButton btnGaranti = createStyledButton("Garanti Uzat", COLOR_ACTION_PRIMARY);
        btnGaranti.addActionListener(e -> garantiUzatmaIslemi()); // Garanti uzatma işlemini başlatıyoruz

        leftActions.add(btnEkle);
        leftActions.add(btnServis);
        leftActions.add(btnGaranti);

        // GRUP 2: İzleme ve Raporlama (Hepsi Koyu Gri-Mavi)
        JPanel centerActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerActions.setOpaque(false);

        JButton btnTakip = createStyledButton("Servis Takip Ekranı", COLOR_ACTION_SECONDARY);
        btnTakip.addActionListener(e -> new ServisTakipEkrani(servisYonetimi).setVisible(true)); // Takip ekranını açıyoruz

        JButton btnRapor = createStyledButton("Rapor Al", COLOR_ACTION_SECONDARY);
        btnRapor.addActionListener(e -> konsolRaporuOlustur()); // Raporu konsola basıyoruz

        centerActions.add(btnTakip);
        centerActions.add(btnRapor);

        // GRUP 3: Sistem ve Silme (Kırmızı ve Gri)
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);

        JButton btnSil = createStyledButton("Cihazı Sil", COLOR_DANGER);
        btnSil.addActionListener(e -> cihazSilIslemi()); // Silme işlemini başlatıyoruz

        JButton btnCikis = createStyledButton("Çıkış Yap", COLOR_NEUTRAL);
        btnCikis.addActionListener(e -> {
            new GirisEkrani().setVisible(true); // Giriş ekranına dönüyoruz
            this.dispose(); // Bu penceryi kapatıyoruz
        });

        rightActions.add(btnSil);
        rightActions.add(btnCikis);

        bottomPanel.add(leftActions, BorderLayout.WEST);
        bottomPanel.add(centerActions, BorderLayout.CENTER);
        bottomPanel.add(rightActions, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- YARDIMCI METOTLAR ---

    private void createTab(String title) {
        // Tablo modelini oluşturup başlıkları ekliyoruz ve düzenlemeyi kapatıyoruz
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Müşteri", "Fiyat (TL)", "Garanti Bitiş"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);

        // Tablo Görünümü (Renkler ve boyutlar)
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(220, 230, 240)); // Seçili satır rengi
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader(); // Tablo başlığını süslüyoruz
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 100, 100));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        tableModels.put(title, model); // Modeli map'e ekledik
        tables.put(title, table); // Tabloyu map'e ekledik

        JScrollPane scrollPane = new JScrollPane(table); // Kaydırma çubuğu ekledik
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tabbedPane.addTab(title, scrollPane); // Sekmeyi panele ekledik
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text); // Butonu oluşturduk
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor); // Parametre olarak gelen rengi verdik
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(145, 40));

        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10"); // Köşeleri yuvarlattık

        // Hover (Mouse üzerine gelince renk açma efekti)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    // --- İŞ MANTIĞI ---

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz); // Listeye ekledik
        cihazListesiniTabloyaDoldur(cihazListesi); // Tabloyu güncelledik
        cihazKaydet(cihazListesi); // Dosyaya kaydettik
    }

    private void cihazListesiniTabloyaDoldur(List<Cihaz> liste) {
        for (DefaultTableModel model : tableModels.values()) {
            model.setRowCount(0); // Önce tüm tabloları temizliyoruz
        }
        for (Cihaz c : liste) {
            String musteriBilgisi = c.getSahip().toString().toUpperCase();
            if (c.getSahip().vipMi()) musteriBilgisi += " [VIP]"; // VIP müşteriyi belirtiyoruz

            Object[] rowData = new Object[]{
                    c.getCihazTuru(), c.getMarka(), c.getModel(), c.getSeriNo(),
                    musteriBilgisi, c.getFiyat(), c.getGarantiBitisTarihi()
            };
            tableModels.get("Tümü").addRow(rowData); // 'Tümü' sekmesine ekle
            String tur = c.getCihazTuru();
            if (tableModels.containsKey(tur)) tableModels.get(tur).addRow(rowData); // İlgili türe (Telefon vb.) ekle
        }
    }

    private Cihaz getSeciliCihaz() {
        String activeTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()); // Hangi sekmedeyiz?
        JTable activeTable = tables.get(activeTitle); // O sekmenin tablosunu al
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow < 0) return null; // Seçim yoksa null dön
        String seriNo = (String) activeTable.getValueAt(selectedRow, 3); // Seri numarasını al
        for (Cihaz c : cihazListesi) {
            if (c.getSeriNo().equals(seriNo)) return c; // Listeden cihazı bul ve döndür
        }
        return null;
    }

    private void servisKaydiOlusturIslemi() {
        Cihaz selectedCihaz = getSeciliCihaz(); // Seçili cihazı alıyoruz
        if (selectedCihaz == null) {
            JOptionPane.showMessageDialog(this, "Lütfen işlem yapılacak cihazı seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Kullanıcıya bilgi göstermek için metinleri hazırlıyoruz
        String garantiDurumu = selectedCihaz.garantiAktifMi() ? "Aktif" : "BİTMİŞ";
        String vipBilgi = selectedCihaz.getSahip().vipMi() ? " [VIP Müşteri]" : "";
        JComboBox<String> sorunComboBox = new JComboBox<>(FiyatlandirmaHizmeti.getSorunListesi());
        String mesaj = String.format("Cihaz: %s\nSahibi: %s%s\nGaranti: %s (%s)\n\nSorunu Seçin: ",
                selectedCihaz.getModel(), selectedCihaz.getSahip().getAd() + " " + selectedCihaz.getSahip().getSoyad(),
                vipBilgi, garantiDurumu, selectedCihaz.getGaranti().garantiTuru());

        int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION); // Kutucuğu göster
        if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
            String secilenSorun = (String) sorunComboBox.getSelectedItem();
            ServisKaydi yeniKayit = servisYonetimi.yeniServisKaydiOlustur(selectedCihaz, secilenSorun); // Kaydı oluştur
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(LocalDate.now(), 20); // 20 iş günü sonrasını hesapla
            JOptionPane.showMessageDialog(this,
                    String.format("Kayıt Başarılı!\nSorun: %s\nÖdenecek Tutar: %.2f TL\nTeknisyen: %s\nTahmini Teslim: %s",
                            yeniKayit.getSorunAciklamasi(), yeniKayit.getOdenecekTamirUcreti(), yeniKayit.getAtananTeknisyen().getAd(), tahminiTeslim));
        }
    }

    private void garantiUzatmaIslemi() {
        Cihaz seciliCihaz = getSeciliCihaz();
        if (seciliCihaz == null) {
            JOptionPane.showMessageDialog(this, "Lütfen garanti işlemi yapılacak cihazı seçin.");
            return;
        }
        if (seciliCihaz.getGaranti().garantiTuru().contains("Uzatılmış")) {
            JOptionPane.showMessageDialog(this, "Bu cihazın garantisi zaten uzatılmış.");
            return;
        }
        // Fiyatları hesaplıyoruz
        double cihazFiyati = seciliCihaz.getFiyat();
        double fiyat6Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 6);
        double fiyat12Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 12);
        double fiyat24Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 24);
        double fiyat36Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 36);

        Object[] options = {
                String.format("6 Ay (%.2f TL)", fiyat6Ay),
                String.format("12 Ay (%.2f TL)", fiyat12Ay),
                String.format("24 Ay (%.2f TL)", fiyat24Ay),
                String.format("36 Ay (%.2f TL)", fiyat36Ay),
                "İptal"
        };
        // Kullanıcıya seçenek sunuyoruz
        int n = JOptionPane.showOptionDialog(this, "Garanti Paketi Seçin:", "Garanti Uzatma",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[4]);

        int uzatilacakAy = 0;
        if (n == 0) uzatilacakAy = 6; else if (n == 1) uzatilacakAy = 12;
        else if (n == 2) uzatilacakAy = 24; else if (n == 3) uzatilacakAy = 36;

        if (uzatilacakAy > 0) {
            seciliCihaz.garantiUzat(uzatilacakAy); // Garantiyi uzat
            cihazKaydet(cihazListesi); // Kaydet
            cihazListesiniTabloyaDoldur(cihazListesi); // Tabloyu yenile
            JOptionPane.showMessageDialog(this, "İşlem Başarılı! Garanti süresi uzatıldı.");
        }
    }

    private void cihazSilIslemi() {
        Cihaz silinecekCihaz = getSeciliCihaz();
        if (silinecekCihaz != null) {
            // Emin misin diye soruyoruz
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bu cihazı (" + silinecekCihaz.getModel() + ") silmek istediğinize emin misiniz?",
                    "Onay", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cihazListesi.remove(silinecekCihaz); // Listeden sil
                cihazKaydet(cihazListesi); // Dosyayı güncelle
                cihazListesiniTabloyaDoldur(cihazListesi); // Tabloyu güncelle
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silinecek cihazı seçin.");
        }
    }

    private void cihazKaydet(List<Cihaz> liste) {
        try {
            DosyaIslemleri.cihazlariKaydet(liste, CIHAZ_DOSYA_ADI); // Dosya yazma işlemi
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        }
    }

    private void konsolRaporuOlustur() {
        RaporlamaHizmeti.konsolRaporuOlustur(cihazListesi); // Arka planda konsola rapor basar
        JOptionPane.showMessageDialog(this, "Rapor konsola yazdırıldı!");
    }
}