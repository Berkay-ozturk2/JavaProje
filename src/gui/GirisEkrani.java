package gui;

import Guvenlik.Kullanici;
import Guvenlik.KullaniciYonetimi;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GirisEkrani extends JFrame {

    private static final Color PERSONEL_RENGI = new Color(44, 62, 80);
    private static final Color MUSTERI_RENGI = new Color(52, 152, 219);

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
        lblTitle.setForeground(PERSONEL_RENGI);

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
        JButton btnMusteri = createModernButton("Müşteri Girişi", "👤", MUSTERI_RENGI);
        btnMusteri.addActionListener(e -> {
            new MusteriTakipEkrani().setVisible(true);
        });

        // Personel Butonu (GÜNCELLENDİ)
        JButton btnPersonel = createModernButton("Personel Girişi", "🛡️", PERSONEL_RENGI);
        btnPersonel.addActionListener(e -> {
            personelGirisPenceresi(); // Yeni metodumuzu çağırıyoruz
        });

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(btnMusteri, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(btnPersonel, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // --- 3. ALTBİLGİ ---
        JLabel lblFooter = new JLabel("İsmail Onur Koru - Berkay Öztürk", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.LIGHT_GRAY);
        lblFooter.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Kullanıcı Adı ve Şifre Soran Gelişmiş Giriş Penceresi
     */
    private void personelGirisPenceresi() {
        JDialog dialog = new JDialog(this, "Yetkili Girişi", true);
        dialog.setSize(400, 350); // Biraz uzattık
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // --- Başlık ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        titlePanel.setBackground(PERSONEL_RENGI);
        JLabel lblDialogTitle = new JLabel("Personel Kimlik Doğrulama");
        lblDialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDialogTitle.setForeground(Color.WHITE);
        titlePanel.add(lblDialogTitle);

        // --- Form İçeriği ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        // Kullanıcı Adı Alanı
        JLabel lblUser = new JLabel("Kullanıcı Adı:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(lblUser, gbc);

        gbc.gridy++;
        JTextField txtUser = new JTextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setPreferredSize(new Dimension(200, 30));
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Kullanıcı adınızı giriniz");
        contentPanel.add(txtUser, gbc);

        // Şifre Alanı
        gbc.gridy++;
        JLabel lblPass = new JLabel("Şifre:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(lblPass, gbc);

        gbc.gridy++;
        JPasswordField txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtPass.setPreferredSize(new Dimension(200, 30));
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "******");
        contentPanel.add(txtPass, gbc);

        // Hata Mesajı
        gbc.gridy++;
        JLabel lblError = new JLabel(" ");
        lblError.setForeground(new Color(192, 57, 43));
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(lblError, gbc);

        // --- Butonlar ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        btnPanel.setBackground(Color.WHITE);

        JButton btnGiris = new JButton("Giriş Yap");
        btnGiris.setBackground(PERSONEL_RENGI);
        btnGiris.setForeground(Color.WHITE);
        btnGiris.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGiris.setPreferredSize(new Dimension(120, 35));

        JButton btnIptal = new JButton("Vazgeç");
        btnIptal.setBackground(new Color(236, 240, 241));
        btnIptal.setPreferredSize(new Dimension(100, 35));

        // Aksiyonlar
        btnGiris.addActionListener(ev -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            // YENİ: Kullanıcı Yönetimi üzerinden doğrulama
            Kullanici girisYapan = KullaniciYonetimi.dogrula(user, pass);

            if (girisYapan != null) {
                dialog.dispose();
                // Giriş başarılıysa kullanıcı bilgisini Ana Menü'ye iletiyoruz
                new AnaMenu(girisYapan).setVisible(true);
                this.dispose();
            } else {
                lblError.setText("Kullanıcı adı veya şifre hatalı!");
                txtPass.setText("");
            }
        });

        btnIptal.addActionListener(ev -> dialog.dispose());
        dialog.getRootPane().setDefaultButton(btnGiris);

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