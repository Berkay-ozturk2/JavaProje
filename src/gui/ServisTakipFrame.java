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
        setSize(1000, 500);
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    private void initUI() {
        servisTableModel = new DefaultTableModel(
                // "Müşteri İletişim" sütunu eklendi
                new Object[]{"Seri No", "Cihaz", "Müşteri İletişim", "Sorun", "Giriş Tarihi", "Durum", "Atanan Teknisyen", "Ücret (TL)"}, 0
        ) {
            // ...
        };

        servisTable = new JTable(servisTableModel);
        servisTable.setRowHeight(25);
        servisTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        servisTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(servisTable);

        // --- BUTONLAR ---
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        // DEĞİŞİKLİK 1: Parlak turkuaz yerine daha sakin bir gri-mavi tonu.
        // Bu ton, ana ekrandaki mavi butonlarla uyumlu ama onlardan farklıdır.
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

        // DEĞİŞİKLİK 2: Parlak kırmızı yerine, ana ekranda kullandığımız tok kiremit kırmızısı.
        JButton btnSil = new JButton("Seçili Servis Kaydını Sil");
        btnSil.setBackground(new Color(169, 50, 38));
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnDurumGuncelle);
        buttonPanel.add(btnSil);


        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void kayitlariTabloyaDoldur() {
        servisTableModel.setRowCount(0);
        List<ServisKaydi> kayitlar = servisYonetimi.getKayitlar();

        for (ServisKaydi sk : kayitlar) {
            String teknisyenAdi = (sk.getAtananTeknisyen() != null) ? sk.getAtananTeknisyen().getAd() : "Atanmadı";


            servisTableModel.addRow(new Object[]{
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getCihaz().getSahip().toString().toUpperCase(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum(),
                    teknisyenAdi,
                    sk.getOdenecekTamirUcreti()
            });
        }
    }
}
//DJSBVJHADSBJVBAU