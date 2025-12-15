package gui;

import Servis.ServisKaydı;
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
        setSize(900, 450);
        setLocationRelativeTo(null);

        initUI();
        kayitlariTabloyaDoldur();
    }

    private void initUI() {
        // --- Table Model ---
        servisTableModel = new DefaultTableModel(
                new Object[]{"Seri No", "Cihaz Adı", "Sorun Açıklaması", "Giriş Tarihi", "Durum"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servisTable = new JTable(servisTableModel);
        JScrollPane scrollPane = new JScrollPane(servisTable);

        // --- Buttons ---
        JButton btnDurumGuncelle = new JButton("Durumu Güncelle (Tamamlandı)");

        btnDurumGuncelle.addActionListener(e -> {
            int selectedRow = servisTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Kayıt listesinden ilgili nesneyi bul
                ServisKaydı kayit = servisYonetimi.getKayitlar().get(selectedRow);

                if (!kayit.getDurum().equals("Tamamlandı")) {
                    kayit.setDurum("Tamamlandı");
                    servisYonetimi.kayitGuncelle(); // Kaydet
                    kayitlariTabloyaDoldur(); // Tabloyu yenile
                    JOptionPane.showMessageDialog(this, kayit.getCihaz().getSeriNo() + " cihazının durumu 'Tamamlandı' olarak güncellendi.");
                } else {
                    JOptionPane.showMessageDialog(this, "Bu kayıt zaten tamamlanmış.");
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
            servisTableModel.addRow(new Object[]{
                    sk.getCihaz().getSeriNo(),
                    sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                    sk.getSorunAciklamasi(),
                    sk.getGirisTarihi(),
                    sk.getDurum()
            });
        }
    }
}