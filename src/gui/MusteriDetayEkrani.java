package gui;

import Cihazlar.Cihaz;
import Musteri.Musteri;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class MusteriDetayEkrani extends JDialog {

    public MusteriDetayEkrani(Window parent, Musteri musteri, List<Cihaz> tumCihazlar) {
        super(parent, "Müşteri Detay Kartı", ModalityType.APPLICATION_MODAL);
        initUI(musteri, tumCihazlar);
    }

    private void initUI(Musteri musteri, List<Cihaz> tumCihazlar) {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // --- 1. ÜST BAŞLIK (HEADER) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblBaslik = new JLabel("Müşteri Bilgileri");
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBaslik.setForeground(Color.WHITE);

        JLabel lblId = new JLabel("Müşteri ID: " + musteri.getId());
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(new Color(200, 200, 200));

        headerPanel.add(lblBaslik, BorderLayout.WEST);
        headerPanel.add(lblId, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. ORTA BÖLÜM (BİLGİLER VE CİHAZ LİSTESİ) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Kişisel Bilgiler Alanı
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), "Kişisel Bilgiler"));

        addInfoRow(infoPanel, "Ad:", musteri.getAd());
        addInfoRow(infoPanel, "Soyad:", musteri.getSoyad());
        addInfoRow(infoPanel, "Telefon:", musteri.getTelefon());

        JLabel lblVip = new JLabel(musteri.vipMi() ? "EVET (%20 İndirim)" : "HAYIR");
        lblVip.setForeground(musteri.vipMi() ? new Color(39, 174, 96) : Color.DARK_GRAY);
        lblVip.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblEtiket = new JLabel("VIP Durumu:");
        lblEtiket.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEtiket.setForeground(Color.GRAY);

        infoPanel.add(lblEtiket);
        infoPanel.add(lblVip);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(20)); // Boşluk

        // Cihaz Listesi Alanı
        JLabel lblCihazlar = new JLabel("Müşteriye Ait Kayıtlı Cihazlar");
        lblCihazlar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCihazlar.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblCihazlar);
        contentPanel.add(Box.createVerticalStrut(10));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        int cihazSayaci = 0;

        // Bu müşteriye ait tüm cihazları bulup listeye ekliyoruz
        for (Cihaz c : tumCihazlar) {
            // ID üzerinden eşleştirme yapıyoruz (Daha güvenli)
            if (c.getSahip().getId() == musteri.getId()) {
                String satir = String.format("%s %s %s (Seri: %s)",
                        c.getCihazTuru(), c.getMarka(), c.getModel(), c.getSeriNo());
                listModel.addElement(satir);
                cihazSayaci++;
            }
        }

        JList<String> lstCihazlar = new JList<>(listModel);
        lstCihazlar.setFont(new Font("Monospaced", Font.PLAIN, 13));
        lstCihazlar.setFixedCellHeight(30);
        lstCihazlar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(lstCihazlar);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        scrollPane.setPreferredSize(new Dimension(400, 200));

        contentPanel.add(scrollPane);

        // Alt bilgi (Toplam cihaz sayısı)
        JLabel lblOzet = new JLabel("Toplam Cihaz Sayısı: " + cihazSayaci);
        lblOzet.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblOzet.setBorder(new EmptyBorder(5,0,0,0));
        contentPanel.add(lblOzet);

        add(contentPanel, BorderLayout.CENTER);

        // --- 3. ALT BUTON (KAPAT) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(245, 248, 250));

        JButton btnKapat = new JButton("Kapat");
        btnKapat.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        btnKapat.addActionListener(e -> dispose());

        footerPanel.add(btnKapat);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.GRAY);

        JTextField txt = new JTextField(value);
        txt.setEditable(false); // Sadece okunabilir
        txt.setBackground(Color.WHITE);
        txt.setBorder(null);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(new Color(44, 62, 80));

        panel.add(lbl);
        panel.add(txt);
    }
}