package gui;

import com.formdev.flatlaf.FlatClientProperties; // FlatLaf özellikleri için

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GirisEkrani extends JFrame {

    // Personel butonu rengi (Kurumsal Lacivert)
    private static final Color COLOR_PERSONEL = new Color(44, 62, 80);
    // Müşteri butonu rengi (Açık Mavi)
    private static final Color COLOR_MUSTERI = new Color(52, 152, 219);

    public GirisEkrani() {
        initUI();
    }

    private void initUI() {
        setTitle("Teknik Servis Sistemi");
        setSize(550, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        // --- 1. BAŞLIK BÖLÜMÜ ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20));

        JLabel lblTitle = new JLabel("Teknik Servis Yönetimi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PERSONEL);

        JLabel lblSubtitle = new JLabel("Lütfen işlem yapmak istediğiniz paneli seçiniz", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. BUTONLAR BÖLÜMÜ ---
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Müşteri Butonu
        JButton btnMusteri = createModernButton("Müşteri Girişi", "👤", COLOR_MUSTERI);
        btnMusteri.addActionListener(e -> {
            new MusteriTakipEkrani().setVisible(true);
        });

        // Personel Butonu
        JButton btnPersonel = createModernButton("Personel Girişi", "🛡️", COLOR_PERSONEL);
        btnPersonel.addActionListener(e -> {
            // Standart JOptionPane yerine özel dialog çağırıyoruz
            showCustomSecurityDialog();
        });

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(btnMusteri, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(btnPersonel, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // --- 3. ALTBİLGİ ---
        JLabel lblFooter = new JLabel("v1.0.3 - 2025 © Tüm Hakları Saklıdır", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.LIGHT_GRAY);
        lblFooter.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Özel Güvenlik Penceresini (Dialog) Oluşturan ve Gösteren Metot
     */
    private void showCustomSecurityDialog() {
        JDialog dialog = new JDialog(this, "Güvenlik Kontrolü", true); // Modal true
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // --- Dialog Başlık (Koyu Lacivert Alan) ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        titlePanel.setBackground(COLOR_PERSONEL);
        JLabel lblDialogTitle = new JLabel("Yetkili Girişi 🔒");
        lblDialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDialogTitle.setForeground(Color.WHITE);
        titlePanel.add(lblDialogTitle);

        // --- Dialog İçerik (Şifre Alanı) ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel lblPass = new JLabel("Personel Şifresi:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPass.setForeground(Color.GRAY);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "******");
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)));

        JLabel lblError = new JLabel(" "); // Başlangıçta boş hata mesajı
        lblError.setForeground(new Color(192, 57, 43));
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerContainer = new JPanel(new GridLayout(3, 1, 5, 10));
        centerContainer.setBackground(Color.WHITE);
        centerContainer.add(lblPass);
        centerContainer.add(txtPass);
        centerContainer.add(lblError);

        contentPanel.add(centerContainer, BorderLayout.NORTH);

        // --- Dialog Butonlar ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        btnPanel.setBackground(Color.WHITE);

        JButton btnGiris = new JButton("Giriş Yap");
        btnGiris.setBackground(COLOR_PERSONEL);
        btnGiris.setForeground(Color.WHITE);
        btnGiris.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGiris.setFocusPainted(false);
        btnGiris.setPreferredSize(new Dimension(120, 35));

        JButton btnIptal = new JButton("Vazgeç");
        btnIptal.setBackground(new Color(236, 240, 241));
        btnIptal.setForeground(new Color(127, 140, 141));
        btnIptal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIptal.setFocusPainted(false);
        btnIptal.setPreferredSize(new Dimension(100, 35));

        // Buton Aksiyonları
        btnGiris.addActionListener(ev -> {
            String password = new String(txtPass.getPassword());
            if ("a".equals(password)) { // Şifre kontrolü
                dialog.dispose();
                new Main().setVisible(true);
                this.dispose();
            } else {
                lblError.setText("Hatalı şifre! Tekrar deneyiniz.");
                txtPass.setText("");
                txtPass.requestFocus();
                // Hata efektleri (titreme vs.) buraya eklenebilir
            }
        });

        btnIptal.addActionListener(ev -> dialog.dispose());

        // Enter tuşuna basınca giriş yapması için
        dialog.getRootPane().setDefaultButton(btnGiris);

        btnPanel.add(btnGiris);
        btnPanel.add(btnIptal);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JButton createModernButton(String text, String icon, Color bgColor) {
        JButton btn = new JButton("<html><center><span style='font-size:24px'>" + icon + "</span><br><br>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 100));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });

        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) { }
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}