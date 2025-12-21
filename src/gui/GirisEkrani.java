package gui;

import com.formdev.flatlaf.FlatClientProperties; // FlatLaf özellikleri için

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GirisEkrani extends JFrame {

    // Personel ve müşteri butonları için sabit renkler tanımladık
    private static final Color PERSONEL_RENGI = new Color(44, 62, 80);
    private static final Color MUSTERI_RENGI = new Color(52, 152, 219);

    public GirisEkrani() {
        initUI(); // Ekran tasarımını başlatan metodu çağırdık
    }

    private void initUI() {
        setTitle("Teknik Servis Sistemi"); // Pencerenin başlığını yazdık
        setSize(550, 420); // Pencerenin genişlik ve yüksekliğini ayarladık
        setLocationRelativeTo(null); // Pencerenin ekranın ortasında açılmasını sağladık
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Çarpıya basınca program tamamen kapansın

        JPanel mainPanel = new JPanel(new BorderLayout()); // Ana taşıyıcı paneli oluşturduk
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel); // Bu paneli pencerenin içeriği olarak atadık

        // --- 1. BAŞLIK BÖLÜMÜ ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // Başlıkları alt alta dizmek için panel
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(40, 20, 20, 20)); // Kenarlardan boşluk bıraktık

        JLabel lblTitle = new JLabel("Teknik Servis Yönetimi", SwingConstants.CENTER); // Ana başlığı ortalayarak oluşturduk
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Yazı tipini kalın ve büyük yaptık
        lblTitle.setForeground(PERSONEL_RENGI); // Yazı rengini lacivert yaptık

        JLabel lblSubtitle = new JLabel("Lütfen işlem yapmak istediğiniz paneli seçiniz", SwingConstants.CENTER); // Alt açıklama yazısı
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY); // Rengini gri yaptık

        headerPanel.add(lblTitle); // Başlığı panele ekledik
        headerPanel.add(lblSubtitle); // Alt başlığı panele ekledik
        mainPanel.add(headerPanel, BorderLayout.NORTH); // Bu paneli sayfanın en üstüne koyduk

        // --- 2. BUTONLAR BÖLÜMÜ ---
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Butonları ortalamak için GridBagLayout kullandık
        buttonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Butonlar arasına boşluk koyduk
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Müşteri Butonu
        JButton btnMusteri = createModernButton("Müşteri Girişi", "👤", MUSTERI_RENGI); // Özel tasarım metodumuzla butonu oluşturduk
        btnMusteri.addActionListener(e -> {
            new MusteriTakipEkrani().setVisible(true); // Müşteri sorgulama ekranını açtık
        });

        // Personel Butonu
        JButton btnPersonel = createModernButton("Personel Girişi", "🛡️", PERSONEL_RENGI); // Personel butonunu oluşturduk
        btnPersonel.addActionListener(e -> {
            // Standart JOptionPane yerine özel dialog çağırıyoruz
            showCustomSecurityDialog(); // Şifre soran özel pencereyi açtık
        });

        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(btnMusteri, gbc); // Müşteri butonunu sol tarafa ekledik

        gbc.gridx = 1; gbc.gridy = 0;
        buttonPanel.add(btnPersonel, gbc); // Personel butonunu sağ tarafa ekledik

        mainPanel.add(buttonPanel, BorderLayout.CENTER); // Buton panelini sayfanın ortasına yerleştirdik

        // --- 3. ALTBİLGİ ---
        JLabel lblFooter = new JLabel("İsmail Onur Koru - Berkay Öztürk", SwingConstants.CENTER); // İsimlerimizi yazdık
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.LIGHT_GRAY);
        lblFooter.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH); // İsimleri sayfanın en altına koyduk
    }

    /**
     * Özel Güvenlik Penceresini (Dialog) Oluşturan ve Gösteren Metot
     */
    private void showCustomSecurityDialog() {
        JDialog dialog = new JDialog(this, "Güvenlik Kontrolü", true); // Arkadaki pencereye tıklanmasını engelleyen (modal) bir pencere açtık
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // --- Dialog Başlık (Koyu Lacivert Alan) ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        titlePanel.setBackground(PERSONEL_RENGI);
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

        JPasswordField txtPass = new JPasswordField(); // Şifrenin yıldızlı görünmesi için alan oluşturduk
        txtPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "******"); // İçine silik yazı ekledik
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)));

        JLabel lblError = new JLabel(" "); // Başlangıçta boş hata mesajı etiketi oluşturduk
        lblError.setForeground(new Color(192, 57, 43)); // Hata rengini kırmızı yaptık
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

        JButton btnGiris = new JButton("Giriş Yap"); // Giriş butonunu tasarladık
        btnGiris.setBackground(PERSONEL_RENGI);
        btnGiris.setForeground(Color.WHITE);
        btnGiris.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGiris.setFocusPainted(false);
        btnGiris.setPreferredSize(new Dimension(120, 35));

        JButton btnIptal = new JButton("Vazgeç"); // Vazgeç butonunu tasarladık
        btnIptal.setBackground(new Color(236, 240, 241));
        btnIptal.setForeground(new Color(127, 140, 141));
        btnIptal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIptal.setFocusPainted(false);
        btnIptal.setPreferredSize(new Dimension(100, 35));

        // Buton Aksiyonları
        btnGiris.addActionListener(ev -> {
            String password = new String(txtPass.getPassword()); // Girilen şifreyi aldık
            if ("a".equals(password)) { // Şifre doğruysa (test için 'a' yaptık)
                dialog.dispose(); // Küçük pencereyi kapat
                new Main().setVisible(true); // Ana yönetim ekranını aç
                this.dispose(); // Giriş ekranını kapat
            } else {
                lblError.setText("Hatalı şifre! Tekrar deneyiniz."); // Hata mesajını göster
                txtPass.setText(""); // Şifre kutusunu temizle
                txtPass.requestFocus(); // İmleci tekrar kutuya odakla
                // Hata efektleri (titreme vs.) buraya eklenebilir
            }
        });

        btnIptal.addActionListener(ev -> dialog.dispose()); // Vazgeç butonuna basınca sadece bu pencereyi kapat

        // Enter tuşuna basınca giriş yapması için butonu varsayılan yaptık
        dialog.getRootPane().setDefaultButton(btnGiris);

        btnPanel.add(btnGiris);
        btnPanel.add(btnIptal);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true); // Pencereyi görünür yaptık
    }

    private JButton createModernButton(String text, String icon, Color bgColor) {
        // HTML kullanarak butonun içine hem simge hem yazı ekledik
        JButton btn = new JButton("<html><center><span style='font-size:24px'>" + icon + "</span><br><br>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Çerçeve çizgisini kaldırdık
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Üzerine gelince el işareti çıksın
        btn.setPreferredSize(new Dimension(190, 100)); // Buton boyutunu sabitledik
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 20"); // Köşeleri yuvarlattık

        // Fare üzerine gelince rengi biraz açmak için Listener ekledik
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });

        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); // Modern tema (FlatLaf) yükledik
        } catch (Exception ex) { }
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true)); // Uygulamayı başlattık
    }
}