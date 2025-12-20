package gui;

import Servis.ServisDurumu;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServisTakipFrame extends JFrame {

    private final ServisYonetimi servisYonetimi;
    private JTable servisTable;
    private DefaultTableModel servisTableModel;

    public ServisTakipFrame(ServisYonetimi yonetim) {
        this.servisYonetimi = yonetim;
        setTitle("Servis Kayıtları ve Takibi");
        setSize(1100, 500); // Tablo genişlediği için frame boyutunu biraz artırdık
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    private void initUI() {
        // --- Tablo Yapılandırması ---
        servisTableModel = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz", "Müşteri İletişim", "Sorun", "Giriş Tarihi", "Durum", "Atanan Teknisyen", "Ücret (TL)", "Bitiş Tarihi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servisTable = new JTable(servisTableModel);
        servisTable.setRowHeight(25);
        servisTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        servisTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(servisTable);

        // --- BUTONLAR ---
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        // 1. Durum Güncelle Butonu
        JButton btnDurumGuncelle = new JButton("Durumu Güncelle (Tamamlandı)");
        btnDurumGuncelle.setBackground(new Color(84, 110, 122));
        btnDurumGuncelle.setForeground(Color.WHITE);
        btnDurumGuncelle.setFont(btnFont);
        btnDurumGuncelle.setFocusPainted(false);

        btnDurumGuncelle.addActionListener(e -> {
            int selectedRow = servisTable.getSelectedRow();
            if (selectedRow >= 0) {
                ServisKaydi kayit = servisYonetimi.getKayitlar().get(selectedRow);
                if (kayit.getDurum() != ServisDurumu.TAMAMLANDI) {
                    kayit.setDurum(ServisDurumu.TAMAMLANDI);
                    servisYonetimi.kayitGuncelle();
                    kayitlariTabloyaDoldur();
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
            int selectedRow = servisTable.getSelectedRow();
            if (selectedRow >= 0) {
                ServisKaydi silinecekKayit = servisYonetimi.getKayitlar().get(selectedRow);

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
                    servisYonetimi.getKayitlar().remove(selectedRow);
                    servisYonetimi.kayitGuncelle();
                    kayitlariTabloyaDoldur();
                    JOptionPane.showMessageDialog(this, "Servis kaydı başarıyla silindi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek kaydı tablodan seçin.");
            }
        });

        // 3. YENİ EKLENEN BUTON: Tüm Verileri Sil
        JButton btnTumunuSil = new JButton("Tüm Servis Verilerini Sil");
        btnTumunuSil.setBackground(new Color(17, 4, 8)); // Çok koyu (Siyahımsı)
        btnTumunuSil.setForeground(Color.WHITE);
        btnTumunuSil.setFont(btnFont);
        btnTumunuSil.setFocusPainted(false);

        btnTumunuSil.addActionListener(e -> {
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

                // Tabloyu güncelle (Boşalt)
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
        buttonPanel.add(btnTumunuSil); // Panele eklendi

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void kayitlariTabloyaDoldur() {
        servisTableModel.setRowCount(0);
        List<ServisKaydi> kayitlar = servisYonetimi.getKayitlar();

        for (ServisKaydi sk : kayitlar) {
            String teknisyenAdi = (sk.getAtananTeknisyen() != null) ? sk.getAtananTeknisyen().getAd() : "Atanmadı";
            Object bitisTarihi = (sk.getTamamlamaTarihi() != null) ? sk.getTamamlamaTarihi() : "-";

            servisTableModel.addRow(new Object[]{
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getCihaz().getSahip().toString().toUpperCase(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum(),
                    teknisyenAdi,
                    sk.getOdenecekTamirUcreti(),
                    bitisTarihi
            });
        }
    }
}