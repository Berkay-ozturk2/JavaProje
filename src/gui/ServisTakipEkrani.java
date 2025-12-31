package gui;

import Guvenlik.Kullanici;
import Guvenlik.KullaniciRol;
import Servis.ServisDurumu;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// Servis kayıtlarının listelendiği ve durumlarının yönetildiği ana pencere sınıfıdır.
public class ServisTakipEkrani extends JFrame {

    // Servis mantığı işlemlerini yürüten yönetici nesne.
    private final ServisYonetimi servisYonetimi;
    private final Kullanici aktifKullanici; // Yetki kontrolü için eklendi

    // Kategorilere ayrılmış sekmeleri tutan panel.
    private JTabbedPane tabbedPane;
    // Her sekme için tablo modelini ve tabloyu saklayan haritalar.
    private Map<String, DefaultTableModel> tableModels = new HashMap<>();
    private Map<String, JTable> tables = new HashMap<>();

    // GÜNCELLENEN CONSTRUCTOR: Kullanıcı bilgisini alır
    public ServisTakipEkrani(ServisYonetimi yonetim, Kullanici kullanici) {
        this.servisYonetimi = yonetim;
        this.aktifKullanici = kullanici;
        initUI();
        kayitlariTabloyaDoldur();
    }

    // Eski kodlarla uyumluluk için (Test amaçlı - Varsayılan Teknisyen olarak açar)
    public ServisTakipEkrani(ServisYonetimi yonetim) {
        this(yonetim, new Kullanici("Test", "", KullaniciRol.TEKNISYEN));
    }

    // Pencere boyutunu, başlıkları ve yerleşimi ayarlayan ana metottur.
    private void initUI() {
        setTitle("Servis Takip Ekranı - " + aktifKullanici.getKullaniciAdi()); // Başlığa kullanıcı adı eklendi
        setSize(1100, 650);
        setLocationRelativeTo(null);

        // Ana paneli oluşturur ve kenar boşluklarını ayarlar.
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- 1. HEADER ---
        // Sayfanın üst kısmındaki başlık etiketini oluşturur.
        JLabel lblHeader = new JLabel("Servis Kayıtları ve Takibi");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(new Color(44, 62, 80));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- 2. SEKMELER VE TABLO ---
        // Sekmeli paneli oluşturur ve kategorileri ekler.
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.putClientProperty(FlatClientProperties.STYLE, "tabSeparators: true");

        createTab("Tümü");
        createTab("Telefon");
        createTab("Tablet");
        createTab("Laptop");

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // --- 3. AKSİYON BUTONLARI ---
        // Alt kısımdaki işlem butonlarını içeren paneli hazırlar.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Durum güncelleme butonunu oluşturur ve işlevini atar.
        JButton btnDurumGuncelle = createStyledButton("Durum Güncelle (Tamamla)", new Color(52, 152, 219));
        btnDurumGuncelle.addActionListener(e -> durumGuncelleIslemi());

        // YENİ BUTON: GEÇMİŞİ GÖR
        JButton btnGecmis = createStyledButton("Geçmiş İşlemler", new Color(155, 89, 182)); // Mor renk
        btnGecmis.addActionListener(e -> gecmisGoruntuleIslemi());

        // Kayıt silme butonunu oluşturur ve işlevini atar.
        JButton btnSil = createStyledButton("Kaydı Sil", new Color(231, 76, 60));
        btnSil.addActionListener(e -> kayitSilIslemi());

        // Tüm geçmişi temizleme butonunu oluşturur ve işlevini atar.
        JButton btnTumunuSil = createStyledButton("Tüm Geçmişi Temizle", new Color(44, 62, 80));
        btnTumunuSil.addActionListener(e -> tumunuTemizleIslemi());

        // --- YETKİ KONTROLÜ ---
        // Eğer giriş yapan kullanıcı TEKNISYEN ise, silme butonlarını gizle/devre dışı bırak
        if (aktifKullanici.getRol() == KullaniciRol.TEKNISYEN) {
            btnSil.setEnabled(false);
            btnSil.setVisible(false); // Görünmez yap

            btnTumunuSil.setEnabled(false);
            btnTumunuSil.setVisible(false); // Görünmez yap
        }

        buttonPanel.add(btnDurumGuncelle);
        buttonPanel.add(btnGecmis); // Araya ekledik
        buttonPanel.add(btnSil);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Boşluk
        buttonPanel.add(btnTumunuSil);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Belirtilen başlık altında yeni bir sekme ve tablo oluşturur.
    private void createTab(String title) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz", "Müşteri İletişim", "Sorun", "Giriş Tarihi", "Durum", "Teknisyen", "Ücret (TL)", "Bitiş Tarihi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);

        // Tablo görünümü için modern stil ayarlarını yapar.
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(220, 230, 240));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(100, 100, 100));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        tableModels.put(title, model);
        tables.put(title, table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tabbedPane.addTab(title, scrollPane);
    }

    // Belirtilen renk ve metinle modern görünümlü bir buton oluşturur.
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        return btn;
    }

    // --- İŞ MANTIĞI ---

    // Aktif sekmedeki seçili satıra ait servis kaydını bulup döndürür.
    private ServisKaydi getSeciliKayit() {
        String activeTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        JTable activeTable = tables.get(activeTitle);
        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow < 0) return null;
        String seriNo = (String) activeTable.getValueAt(selectedRow, 0);
        String sorun = (String) activeTable.getValueAt(selectedRow, 3);

        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equals(seriNo) && k.getSorunAciklamasi().equals(sorun)) {
                return k;
            }
        }
        return null;
    }

    // YENİ: Geçmişi görüntüleyen metot
    private void gecmisGoruntuleIslemi() {
        ServisKaydi kayit = getSeciliKayit();
        if (kayit != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("CİHAZ: ").append(kayit.getCihaz().getModel()).append("\n");
            sb.append("SERİ NO: ").append(kayit.getCihaz().getSeriNo()).append("\n\n");
            sb.append("=== İŞLEM TARİHÇESİ ===\n");

            for (String islem : kayit.getIslemGecmisi()) {
                sb.append(islem).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Servis Geçmişi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen geçmişini görmek istediğiniz kaydı seçin.");
        }
    }

    // Seçili kaydın durumunu 'Tamamlandı' olarak günceller ve listeyi yeniler.
    private void durumGuncelleIslemi() {
        ServisKaydi kayit = getSeciliKayit();
        if (kayit != null) {
            if (kayit.getDurum() != ServisDurumu.TAMAMLANDI) {

                // YENİ: Kullanıcıdan işlem notu alalım
                String aciklama = JOptionPane.showInputDialog(this,
                        "Yapılan işlem hakkında kısa bilgi giriniz:",
                        "İşlem Tamamlama", JOptionPane.QUESTION_MESSAGE);

                if (aciklama != null && !aciklama.trim().isEmpty()) {
                    kayit.setDurum(ServisDurumu.TAMAMLANDI);

                    // YENİ: LOGLAMA İŞLEMİ
                    kayit.islemEkle("Durum 'Tamamlandı' olarak güncellendi. Not: " + aciklama);

                    servisYonetimi.kayitGuncelle();
                    kayitlariTabloyaDoldur();
                    JOptionPane.showMessageDialog(this, "Kayıt güncellendi ve işlem geçmişine eklendi.");
                } else {
                    JOptionPane.showMessageDialog(this, "Açıklama girilmediği için işlem iptal edildi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bu kayıt zaten tamamlanmış.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lütfen bir kayıt seçin.");
        }
    }

    // Kullanıcı onayı ile seçili servis kaydını listeden siler.
    private void kayitSilIslemi() {
        ServisKaydi silinecekKayit = getSeciliKayit();
        if (silinecekKayit != null) {
            int secim = JOptionPane.showConfirmDialog(this,
                    "Seçili kaydı silmek istediğinize emin misiniz?",
                    "Silme Onayı", JOptionPane.YES_NO_OPTION);
            if (secim == JOptionPane.YES_OPTION) {
                servisYonetimi.getKayitlar().remove(silinecekKayit);
                servisYonetimi.kayitGuncelle();
                kayitlariTabloyaDoldur();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silinecek kaydı seçin.");
        }
    }

    // Kullanıcı onayı ile tüm servis geçmişini kalıcı olarak temizler.
    private void tumunuTemizleIslemi() {
        int secim = JOptionPane.showConfirmDialog(this,
                "DİKKAT: Tüm servis geçmişi silinecek!\nDevam etmek istiyor musunuz?",
                "Kritik İşlem", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (secim == JOptionPane.YES_OPTION) {
            servisYonetimi.verileriTemizle();
            kayitlariTabloyaDoldur();
            JOptionPane.showMessageDialog(this, "Tüm veriler temizlendi.");
        }
    }

    // Güncel servis kayıtlarını ilgili tablolara doldurur ve görünümleri yeniler.
    private void kayitlariTabloyaDoldur() {
        for (DefaultTableModel model : tableModels.values()) model.setRowCount(0);

        for (ServisKaydi sk : servisYonetimi.getKayitlar()) {
            String teknisyen = (sk.getAtananTeknisyen() != null) ? sk.getAtananTeknisyen().getAd() : "-";
            Object bitis = (sk.getTamamlamaTarihi() != null) ? sk.getTamamlamaTarihi() : "-";

            Object[] row = {
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getCihaz().getSahip().toString().toUpperCase(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum(),
                    teknisyen,
                    sk.getOdenecekTamirUcreti(),
                    bitis
            };
            tableModels.get("Tümü").addRow(row);
            String tur = sk.getCihaz().getCihazTuru();
            if (tableModels.containsKey(tur)) tableModels.get(tur).addRow(row);
        }
    }
}