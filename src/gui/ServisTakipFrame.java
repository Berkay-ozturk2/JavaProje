package gui;

import Cihazlar.Cihaz;
import Servis.ServisDurumu;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServisTakipFrame extends JFrame {

    private final ServisYonetimi servisYonetimi;

    // Çoklu tablo yönetimi için yapılar
    private JTabbedPane tabbedPane;
    private Map<String, DefaultTableModel> tableModels = new HashMap<>();
    private Map<String, JTable> tables = new HashMap<>();

    public ServisTakipFrame(ServisYonetimi yonetim) {
        this.servisYonetimi = yonetim;
        setTitle("Servis Kayıtları ve Takibi");
        setSize(1100, 600);
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    private void initUI() {
        // --- SEKME YAPISI (TABBED PANE) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Kategoriler için sekmeleri oluştur
        createTab("Tümü");
        createTab("Telefon");
        createTab("Tablet");
        createTab("Laptop");

        // --- BUTONLAR ---
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        // 1. Durum Güncelle Butonu
        JButton btnDurumGuncelle = new JButton("Durumu Güncelle (Tamamlandı)");
        btnDurumGuncelle.setBackground(new Color(84, 110, 122));
        btnDurumGuncelle.setForeground(Color.WHITE);
        btnDurumGuncelle.setFont(btnFont);
        btnDurumGuncelle.setFocusPainted(false);

        btnDurumGuncelle.addActionListener(e -> {
            ServisKaydi kayit = getSeciliKayit();
            if (kayit != null) {
                if (kayit.getDurum() != ServisDurumu.TAMAMLANDI) {
                    kayit.setDurum(ServisDurumu.TAMAMLANDI);
                    servisYonetimi.kayitGuncelle();
                    kayitlariTabloyaDoldur(); // Tüm sekmeleri yenile
                    JOptionPane.showMessageDialog(this, "Durum güncellendi: Tamamlandı.");
                } else {
                    JOptionPane.showMessageDialog(this, "Zaten tamamlanmış.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen bir kayıt seçin.");
            }
        });

        // 2. Seçili Kaydı Sil Butonu
        JButton btnSil = new JButton("Seçili Servis Kaydını Sil");
        btnSil.setBackground(new Color(169, 50, 38)); // Kırmızı ton
        btnSil.setForeground(Color.WHITE);
        btnSil.setFont(btnFont);
        btnSil.setFocusPainted(false);

        btnSil.addActionListener(e -> {
            ServisKaydi silinecekKayit = getSeciliKayit();
            if (silinecekKayit != null) {
                int secim = JOptionPane.showConfirmDialog(
                        this,
                        "Seçilen servis kaydını silmek üzeresiniz:\n" +
                                "Cihaz: " + silinecekKayit.getCihaz().getModel() + "\n" +
                                "Sorun: " + silinecekKayit.getSorunAciklamasi() + "\n\n" +
                                "Bu işlem geri alınamaz. Emin misiniz?",
                        "Servis Kaydı Silme Onayı",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (secim == JOptionPane.YES_OPTION) {
                    servisYonetimi.getKayitlar().remove(silinecekKayit);
                    servisYonetimi.kayitGuncelle();
                    kayitlariTabloyaDoldur(); // Tüm sekmeleri yenile
                    JOptionPane.showMessageDialog(this, "Servis kaydı başarıyla silindi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek kaydı tablodan seçin.");
            }
        });

        // 3. Tüm Verileri Sil Butonu
        JButton btnTumunuSil = new JButton("Tüm Servis Verilerini Sil");
        btnTumunuSil.setBackground(new Color(17, 4, 8)); // Çok koyu
        btnTumunuSil.setForeground(Color.WHITE);
        btnTumunuSil.setFont(btnFont);
        btnTumunuSil.setFocusPainted(false);

        btnTumunuSil.addActionListener(e -> {
            int secim = JOptionPane.showConfirmDialog(this,
                    "DİKKAT: Tüm GEÇMİŞ SERVİS KAYITLARI kalıcı olarak silinecektir!\n" +
                            "Cihaz kayıtları silinmez, sadece servis geçmişi temizlenir.\n\n" +
                            "Bu işlem geri alınamaz. Devam etmek istiyor musunuz?",
                    "Veri Temizleme Onayı",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (secim == JOptionPane.YES_OPTION) {
                servisYonetimi.verileriTemizle();
                kayitlariTabloyaDoldur();
                JOptionPane.showMessageDialog(this,
                        "İşlem Başarılı.\nTüm servis geçmişi temizlendi.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnDurumGuncelle);
        buttonPanel.add(btnSil);
        buttonPanel.add(btnTumunuSil);

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Her kategori için ayrı bir tablo oluşturan yardımcı metot
    private void createTab(String title) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz", "Müşteri İletişim", "Sorun", "Giriş Tarihi", "Durum", "Atanan Teknisyen", "Ücret (TL)", "Bitiş Tarihi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Map'lere kaydet
        tableModels.put(title, model);
        tables.put(title, table);

        JScrollPane scrollPane = new JScrollPane(table);
        tabbedPane.addTab(title, scrollPane);
    }

    // Aktif sekmedeki seçili satıra karşılık gelen ServisKaydi nesnesini bulur
    private ServisKaydi getSeciliKayit() {
        String activeTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        JTable activeTable = tables.get(activeTitle);

        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow < 0) return null;

        // Tablodaki ayırt edici verileri alıyoruz (Seri No ve Sorun Açıklaması)
        String seriNo = (String) activeTable.getValueAt(selectedRow, 0);
        String sorun = (String) activeTable.getValueAt(selectedRow, 3);

        // Gerçek listeyi tara ve eşleşeni bul
        for (ServisKaydi k : servisYonetimi.getKayitlar()) {
            if (k.getCihaz().getSeriNo().equals(seriNo) && k.getSorunAciklamasi().equals(sorun)) {
                return k;
            }
        }
        return null;
    }

    private void kayitlariTabloyaDoldur() {
        // Tüm modelleri temizle
        for (DefaultTableModel model : tableModels.values()) {
            model.setRowCount(0);
        }

        List<ServisKaydi> kayitlar = servisYonetimi.getKayitlar();

        for (ServisKaydi sk : kayitlar) {
            String teknisyenAdi = (sk.getAtananTeknisyen() != null) ? sk.getAtananTeknisyen().getAd() : "Atanmadı";
            Object bitisTarihi = (sk.getTamamlamaTarihi() != null) ? sk.getTamamlamaTarihi() : "-";

            Object[] rowData = new Object[]{
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getCihaz().getSahip().toString().toUpperCase(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum(),
                    teknisyenAdi,
                    sk.getOdenecekTamirUcreti(),
                    bitisTarihi
            };

            // 1. "Tümü" sekmesine ekle
            tableModels.get("Tümü").addRow(rowData);

            // 2. Kategori sekmesine ekle (Telefon, Tablet, Laptop)
            String tur = sk.getCihaz().getCihazTuru();
            if (tableModels.containsKey(tur)) {
                tableModels.get(tur).addRow(rowData);
            }
        }
    }
    private void veriTemizlemeIslemi() {
        // Kullanıcıdan güvenlik onayı al
        int secim = JOptionPane.showConfirmDialog(this,
                "DİKKAT: Tüm GEÇMİŞ SERVİS KAYITLARI kalıcı olarak silinecektir!\n" +
                        "Cihaz kayıtları silinmez, sadece servis geçmişi temizlenir.\n\n" +
                        "Bu işlem geri alınamaz. Devam etmek istiyor musunuz?",
                "Veri Temizleme Onayı",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (secim == JOptionPane.YES_OPTION) {
            // ServisYonetimi içindeki metodumuzu çağırıyoruz
            servisYonetimi.verileriTemizle();

            JOptionPane.showMessageDialog(this,
                    "İşlem Başarılı.\nTüm servis geçmişi temizlendi.",
                    "Bilgi",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}