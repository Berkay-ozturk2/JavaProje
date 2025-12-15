// src/gui/ServisTakipFrame.java (GÜNCELLENDİ)
package gui;

import Servis.ServisKaydı;
import Servis.ServisYonetimi;
import Servis.ServisDurumu; // YENİ IMPORT

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
        setSize(1100, 450); // Genişlik artırıldı
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    private void initUI() {
        // --- Table Model ---
        // Atanan Teknisyen sütunu eklendi
        servisTableModel = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz Adı", "Sorun Açıklaması", "Giriş Tarihi", "Atanan Teknisyen", "Durum", "Ücret (TL)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servisTable = new JTable(servisTableModel);
        JScrollPane scrollPane = new JScrollPane(servisTable);

        // --- Buttons ---
        JButton btnDurumGuncelle = new JButton("Durumu Seç & Güncelle"); // İsim güncellendi

        // Durum Seç & Güncelle Aksiyonu (GÜNCELLENDİ: Enum Kullanımı)
        btnDurumGuncelle.addActionListener(e -> {
            int selectedRow = servisTable.getSelectedRow();
            if (selectedRow >= 0) {
                ServisKaydı kayit = servisYonetimi.getKayitlar().get(selectedRow);

                ServisDurumu[] durumlar = ServisDurumu.values();
                ServisDurumu yeniDurum = (ServisDurumu) JOptionPane.showInputDialog(this,
                        kayit.getCihaz().getSeriNo() + " cihazının yeni durumunu seçin:",
                        "Durum Güncelleme",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        durumlar,
                        kayit.getDurum() // Varsayılan olarak mevcut durumu göster
                );

                if (yeniDurum != null) {
                    kayit.setDurum(yeniDurum); // Enum ile durum güncelle
                    servisYonetimi.kayitGuncelle(); // Kaydet
                    kayitlariTabloyaDoldur(); // Tabloyu yenile
                    JOptionPane.showMessageDialog(this, kayit.getCihaz().getSeriNo() + " cihazının durumu '" + yeniDurum.toString() + "' olarak güncellendi.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Lütfen güncellenecek bir servis kaydı seçin.");
            }
        });

        // --- Layout ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnDurumGuncelle);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void kayitlariTabloyaDoldur() {
        servisTableModel.setRowCount(0); // Tabloyu temizle
        List<ServisKaydı> kayitlar = servisYonetimi.getKayitlar();

        for (ServisKaydı sk : kayitlar) {
            String teknisyenAd = sk.getAtananTeknisyen() != null ? sk.getAtananTeknisyen().getAd() : "Atanmadı";

            servisTableModel.addRow(new Object[]{
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    teknisyenAd,
                    sk.getDurum().toString(), // Enum'un toString() metodu kullanılır
                    sk.getOdenecekTamirUcreti()
            });
        }
    }
}