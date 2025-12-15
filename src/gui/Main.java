// ===== B KRİTERLERİNE UYGUN, SWING GUI MAIN SINIFI =====
// Cihaz, Telefon, Tablet, Laptop sınıflarını kullanan ana arayüz

package gui;

import Cihazlar.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate; // Gerekli import eklendi

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
            // DÜZELTME: Cihaz.java'daki yapıcı metot (constructor) gereksinimi olan LocalDate yerine
            // yanlışlıkla int tipinde bir değer (48) gönderilmişti.
            // Bu kodun çalışması için Telefon.java sınıfındaki constructor'ın da int yerine LocalDate alacak şekilde düzeltilmesi GEREKİR.
            Telefon t = new Telefon(
                    "T" + (cihazListesi.size() + 1),
                    "Samsung",
                    "Galaxy S",
                    25000,
                    LocalDate.now(), // LocalDate.now() ile değiştirildi
                    true
            );
            cihazEkle(t);
        });

        btnTablet.addActionListener(e -> {
            // DÜZELTME: Cihaz.java'daki yapıcı metot (constructor) gereksinimi olan LocalDate yerine
            // yanlışlıkla double tipinde bir değer (11.0) gönderilmişti.
            // Bu kodun çalışması için Tablet.java sınıfındaki constructor'ın da double yerine LocalDate alacak şekilde düzeltilmesi GEREKİR.
            Tablet tb = new Tablet(
                    "TB" + (cihazListesi.size() + 1),
                    "Apple",
                    "iPad",
                    32000,
                    LocalDate.now(), // LocalDate.now() ile değiştirildi
                    true
            );
            cihazEkle(tb);
        });

        btnLaptop.addActionListener(e -> {
            // DÜZELTME: Laptop constructor'ı 6 parametre beklerken 7 parametre ve yanlış tiplerle çağrılmıştı.
            // (String, String, String, double, LocalDate, boolean) yapısına uygun hale getirildi.
            Laptop l = new Laptop(
                    "L" + (cihazListesi.size() + 1),
                    "Dell",
                    "XPS",
                    45000,
                    LocalDate.now(), // "i7" yerine LocalDate.now() (garantiBaslangic)
                    true             // 16 ve fazladan true parametresi kaldırıldı, sadece hariciEkranKarti kaldı.
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