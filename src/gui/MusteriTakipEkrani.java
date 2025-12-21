package gui;

import Servis.RaporlamaHizmeti;
import Istisnalar.KayitBulunamadiException;
// FlatLaf özellikleri için gerekli import (Eğer proje kütüphanesinde yüklü değilse hata vermez, string olarak çalışır)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    public MusteriTakipEkrani() {
        initUI();
    }

    private void initUI() {
        setTitle("Müşteri Cihaz Sorgulama Sistemi");
        setSize(600, 550); // Ekran biraz daha büyütüldü
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Ana Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 248, 250)); // Çok açık gri-mavi arka plan
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Kenar boşlukları
        setContentPane(mainPanel);

        // --- 1. ÜST BAŞLIK (HEADER) ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 0));
        headerPanel.setOpaque(false); // Arka planı transparent yap

        JLabel lblTitle = new JLabel("Cihaz Durum Sorgulama", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblDesc = new JLabel("Cihazınızın son durumunu öğrenmek için Seri Numarasını giriniz.", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(Color.GRAY);

        headerPanel.add(lblTitle);
        headerPanel.add(lblDesc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. ORTA BÖLÜM (ARAMA VE SONUÇ) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);

        // Arama Paneli
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setOpaque(false);

        txtSeriNo = new JTextField(15);
        txtSeriNo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSeriNo.setPreferredSize(new Dimension(200, 35));
        // Placeholder metni (FlatLaf destekliyorsa görünür)
        txtSeriNo.putClientProperty("JTextField.placeholderText", "Örn: TEL-1234");

        JButton btnSorgula = new JButton("Sorgula");
        btnSorgula.setBackground(new Color(52, 152, 219)); // Mavi Buton
        btnSorgula.setForeground(Color.WHITE);
        btnSorgula.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSorgula.setFocusPainted(false);
        btnSorgula.setPreferredSize(new Dimension(120, 35));
        btnSorgula.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnTemizle = new JButton("Temizle");
        btnTemizle.setBackground(new Color(149, 165, 166)); // Gri Buton
        btnTemizle.setForeground(Color.WHITE);
        btnTemizle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTemizle.setFocusPainted(false);
        btnTemizle.setPreferredSize(new Dimension(100, 35));

        searchPanel.add(txtSeriNo);
        searchPanel.add(btnSorgula);
        searchPanel.add(btnTemizle);

        // Sonuç Ekranı (Rapor Kartı Görünümü)
        txtBilgiEkrani = new JTextArea();
        txtBilgiEkrani.setEditable(false);
        // "Consolas" veya "Monospaced" kullanarak raporun düzgün hizalanmasını sağlıyoruz
        txtBilgiEkrani.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBilgiEkrani.setBackground(Color.WHITE);
        txtBilgiEkrani.setForeground(new Color(40, 40, 40));
        txtBilgiEkrani.setMargin(new Insets(15, 15, 15, 15)); // İç boşluk
        txtBilgiEkrani.setText("\n\n      Henüz bir sorgulama yapılmadı.\n      Lütfen yukarıdan Seri No giriniz.");

        JScrollPane scrollPane = new JScrollPane(txtBilgiEkrani);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), // İnce gri çerçeve
                new EmptyBorder(5, 5, 5, 5)
        ));

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- AKSİYONLAR ---
        btnSorgula.addActionListener(e -> sorgulaIslemi());
        txtSeriNo.addActionListener(e -> sorgulaIslemi()); // Enter tuşuyla çalışması için

        btnTemizle.addActionListener(e -> {
            txtSeriNo.setText("");
            txtBilgiEkrani.setText("\n\n      Ekran temizlendi.\n      Yeni bir sorgulama yapabilirsiniz.");
            txtBilgiEkrani.setForeground(Color.GRAY);
        });
    }

    private void sorgulaIslemi() {
        String arananSeriNo = txtSeriNo.getText().trim();

        if (arananSeriNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası alanını boş bırakmayınız.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtBilgiEkrani.setForeground(new Color(40, 40, 40)); // Yazı rengini normale döndür
        txtBilgiEkrani.setText("Sorgulanıyor...\nLütfen bekleyiniz.");

        // İşlemi küçük bir gecikmeyle hissettirmek veya UI donmasını önlemek için SwingWorker kullanılabilir
        // Ancak basitlik adına doğrudan çağırıyoruz:
        try {
            String rapor = RaporlamaHizmeti.musteriCihazDurumRaporuOlustur(arananSeriNo);

            // Raporu biraz süsleyelim
            StringBuilder susluRapor = new StringBuilder();
            susluRapor.append("=========================================\n");
            susluRapor.append("          SERVİS DURUM RAPORU            \n");
            susluRapor.append("=========================================\n\n");
            susluRapor.append(rapor);
            susluRapor.append("\n\n=========================================\n");
            susluRapor.append("   Bizi tercih ettiğiniz için teşekkürler  \n");

            txtBilgiEkrani.setText(susluRapor.toString());

        } catch (KayitBulunamadiException ex) {
            txtBilgiEkrani.setForeground(new Color(192, 57, 43)); // Hata durumunda kırmızı yazı
            txtBilgiEkrani.setText("\n!!! KAYIT BULUNAMADI !!!\n\n" +
                    "Girilen Seri No: " + arananSeriNo + "\n\n" +
                    "Hata Mesajı: " + ex.getMessage() + "\n\n" +
                    "Lütfen seri numaranızı kontrol edip tekrar deneyiniz.");
        } catch (Exception ex) {
            txtBilgiEkrani.setText("Beklenmeyen bir hata oluştu: " + ex.getMessage());
        }
    }
}