// ===== B KRİTERLERİNE UYGUN, SWING GUI MAIN SINIFI =====
// Cihaz, Telefon, Tablet, Laptop sınıflarını kullanan ana arayüz

package gui;

import Cihazlar.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final List<Cihaz> cihazListesi = new ArrayList<>();

    public Main() {
        setTitle("Dijital Cihaz Garanti & Servis Sistemi");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        // ---------------- TABLE MODEL ----------------
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Fiyat"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tablo düzenlenemez
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // ---------------- BUTTONS ----------------
        JButton btnTelefon = new JButton("Telefon Ekle");
        JButton btnTablet = new JButton("Tablet Ekle");
        JButton btnLaptop = new JButton("Laptop Ekle");
        JButton btnSil = new JButton("Seçili Cihazı Sil");

        btnTelefon.addActionListener(e -> {
            Telefon t = new Telefon(
                    "T" + (cihazListesi.size() + 1),
                    "Samsung",
                    "Galaxy S",
                    25000,
                    48,
                    true
            );
            cihazEkle(t);
        });

        btnTablet.addActionListener(e -> {
            Tablet tb = new Tablet(
                    "TB" + (cihazListesi.size() + 1),
                    "Apple",
                    "iPad",
                    32000,
                    11.0,
                    true
            );
            cihazEkle(tb);
        });

        btnLaptop.addActionListener(e -> {
            Laptop l = new Laptop(
                    "L" + (cihazListesi.size() + 1),
                    "Dell",
                    "XPS",
                    45000,
                    "i7",
                    16,
                    true
            );
            cihazEkle(l);
        });

        btnSil.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                cihazListesi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek cihazı seçin");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnTelefon);
        buttonPanel.add(btnTablet);
        buttonPanel.add(btnLaptop);
        buttonPanel.add(btnSil);

        // ---------------- LAYOUT ----------------
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cihazEkle(Cihaz c) {
        cihazListesi.add(c);
        tableModel.addRow(new Object[]{
                c.getCihazTuru(),
                c.getMarka(),
                c.getModel(),
                c.getSeriNo(),
                c.getFiyat()
        });
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
