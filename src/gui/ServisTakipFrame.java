// src/gui/ServisTakipFrame.java
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
        setSize(1000, 450); // Genişliği artırdık sütunlar sığsın diye
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    // src/gui/ServisTakipFrame.java -> initUI metodu içi

    private void initUI() {
        // ... (Tablo tanımlamaları aynı kalacak) ...
        servisTableModel = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz", "Sorun", "Giriş Tarihi", "Durum", "Atanan Teknisyen", "Ücret (TL)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servisTable = new JTable(servisTableModel);
        JScrollPane scrollPane = new JScrollPane(servisTable);

        // --- MEVCUT BUTON ---
        JButton btnDurumGuncelle = new JButton("Durumu Güncelle (Tamamlandı)");
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

        // --- YENİ EKLENECEK SİLME BUTONU ---
        JButton btnSil = new JButton("Seçili Servis Kaydını Sil");
        btnSil.setBackground(new Color(255, 200, 200)); // Hafif kırmızı renk

        btnSil.addActionListener(e -> {
            int selectedRow = servisTable.getSelectedRow();
            if (selectedRow >= 0) {
                ServisKaydi silinecekKayit = servisYonetimi.getKayitlar().get(selectedRow);

                // Güvenlik Sorusu
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
                    // Listeden sil
                    servisYonetimi.getKayitlar().remove(selectedRow);
                    // Dosyaya kaydet (servisler.txt güncellenir)
                    servisYonetimi.kayitGuncelle(); // Bu metod zaten kaydet() çağırıyor
                    // Tabloyu yenile
                    kayitlariTabloyaDoldur();
                    JOptionPane.showMessageDialog(this, "Servis kaydı başarıyla silindi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek kaydı tablodan seçin.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnDurumGuncelle);
        buttonPanel.add(btnSil); // Yeni butonu panele ekledik

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
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum(),
                    teknisyenAdi,          // Yeni Sütun Verisi
                    sk.getOdenecekTamirUcreti() // Yeni Sütun Verisi
            });
        }
    }
}