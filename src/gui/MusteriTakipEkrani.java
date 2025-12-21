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
        initUI(); // Ekran açılırken tasarımı yükleyen metodu çağır
    }

    //Kullanıcı Ara Yüzünü Başlatma metodu
    private void initUI() {
        setTitle("Müşteri Cihaz Sorgulama Sistemi"); // Pencerenin başlığı
        setSize(600, 550); // Pencerenin genişlik ve yüksekliği
        setLocationRelativeTo(null); // Pencerenin ekranın tam ortasında açılmasını sağladık
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Pencere kapanınca uygulamayı sonlandır

        // Ana Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 248, 250)); // Arka plan rengini açık gri yaptık
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Kenarlardan boşluk bırak
        setContentPane(mainPanel); // Bu paneli ana içerik olarak ayarla

        // 1. ÜST BAŞLIK
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 0)); // Başlıkları alt alta dizmek için panel oluşturduk
        headerPanel.setOpaque(false); // Arka planı şeffaf yap

        JLabel lblTitle = new JLabel("Cihaz Durum Sorgulama", SwingConstants.CENTER); // Ana başlığı oluşturup ortala
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Yazı tipini ve boyutunu ayarladık
        lblTitle.setForeground(new Color(44, 62, 80)); // Yazı rengini koyu mavi tonu yaptık

        JLabel lblDesc = new JLabel("Cihazınızın son durumunu öğrenmek için Seri Numarasını giriniz.", SwingConstants.CENTER); // Açıklama yazısı ekledik
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Yazı tipi ve boyutu
        lblDesc.setForeground(Color.GRAY); // Yazı rengini gri yaptık

        headerPanel.add(lblTitle); // Başlığı panele ekle
        headerPanel.add(lblDesc); // Açıklamayı panele ekle

        mainPanel.add(headerPanel, BorderLayout.NORTH); // Başlık panelini en üste yerleştirdik

        // 2. ORTA BÖLÜM (ARAMA VE SONUÇ)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15)); // Arama ve sonuç kısmını tutacak paneli oluşturduk
        centerPanel.setOpaque(false);

        // Arama Paneli
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Butonları yan yana dizmek için panel
        searchPanel.setOpaque(false);

        txtSeriNo = new JTextField(15); // Yazı kutusunu oluşturma
        txtSeriNo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSeriNo.setPreferredSize(new Dimension(200, 35)); // Kutunun boyutunu belirledik
        // Placeholder metni (FlatLaf destekliyorsa görünür)
        txtSeriNo.putClientProperty("JTextField.placeholderText", "Örn: TEL-1234"); // Kutunun içinde silik yazı gösterdik

        JButton btnSorgula = new JButton("Sorgula"); // Sorgula butonunu oluşturduk
        btnSorgula.setBackground(new Color(52, 152, 219)); // Buton rengi mavi
        btnSorgula.setForeground(Color.WHITE); // Yazı rengi beyaz
        btnSorgula.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSorgula.setFocusPainted(false); // Tıklanınca oluşan çerçeveyi kaldır
        btnSorgula.setPreferredSize(new Dimension(120, 35));// Butonun boyutu
        btnSorgula.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Mouse üzerine gelince el işareti çıksın

        JButton btnTemizle = new JButton("Temizle"); // Temizle butonu
        btnTemizle.setBackground(new Color(149, 165, 166)); // Buton rengi gri
        btnTemizle.setForeground(Color.WHITE);
        btnTemizle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTemizle.setFocusPainted(false);
        btnTemizle.setPreferredSize(new Dimension(100, 35));// Butonun boyutu

        // Text alanını ve butonları arama paneline ekledik
        searchPanel.add(txtSeriNo);
        searchPanel.add(btnSorgula);
        searchPanel.add(btnTemizle);

        // Sonuç Ekranı
        txtBilgiEkrani = new JTextArea(); // Sonuçların yazılacağı alanı oluşturduk
        txtBilgiEkrani.setEditable(false); // Kullanıcının buraya yazı yazmasını engelle
        //"Monospaced" kullanarak raporun düzgün hizalanmasını sağlıyoruz
        txtBilgiEkrani.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBilgiEkrani.setBackground(Color.WHITE);
        txtBilgiEkrani.setForeground(new Color(40, 40, 40));
        txtBilgiEkrani.setMargin(new Insets(15, 15, 15, 15)); // Yazının kenarlara yapışmaması için boşluk
        txtBilgiEkrani.setText("\n\n      Henüz bir sorgulama yapılmadı.\n      Lütfen yukarıdan Seri No giriniz."); // Varsayılan metin

        JScrollPane scrollPane = new JScrollPane(txtBilgiEkrani); // Yazı uzun olursa kaydırma çubuğu çıksın diye panele koyduk
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), // Etrafına ince gri çizgi çektik
                new EmptyBorder(5, 5, 5, 5)
        ));

        centerPanel.add(searchPanel, BorderLayout.NORTH); // Arama panelini üste koyduk
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Sonuç ekranını ortaya koyduk

        mainPanel.add(centerPanel, BorderLayout.CENTER); // Orta paneli ana panele ekledik

        // AKSİYONLAR
        btnSorgula.addActionListener(e -> sorgulaIslemi()); // Sorgula butonuna basınca çalışacak metot
        txtSeriNo.addActionListener(e -> sorgulaIslemi()); // Enter tuşuna basınca da çalışmasını sağla

        btnTemizle.addActionListener(e -> { // Temizle butonuna basınca yapılacakları yazdık
            txtSeriNo.setText(""); // Kutuyu temizledik
            txtBilgiEkrani.setText("\n\n      Ekran temizlendi.\n      Yeni bir sorgulama yapabilirsiniz."); // Bilgi ekranını sıfırladık
            txtBilgiEkrani.setForeground(Color.GRAY);
        });
    }

    private void sorgulaIslemi() {
        String arananSeriNo = txtSeriNo.getText().trim(); // Kutudaki yazıyı al ve boşlukları sil

        if (arananSeriNo.isEmpty()) { // Eğer kutu boşsa uyarı ver
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası alanını boş bırakmayınız.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtBilgiEkrani.setForeground(new Color(40, 40, 40)); // Yazı rengini normale döndürdük
        txtBilgiEkrani.setText("Sorgulanıyor...\nLütfen bekleyiniz."); // Kullanıcıya işlem yapıldığını belirtme

        try {
            String rapor = RaporlamaHizmeti.musteriCihazDurumRaporuOlustur(arananSeriNo); // Servisten raporu çağırdık

            StringBuilder susluRapor = new StringBuilder(); // Yazıları birleştirmek için StringBuilder kullandık
            susluRapor.append("=========================================\n");
            susluRapor.append("          SERVİS DURUM RAPORU            \n");
            susluRapor.append("=========================================\n\n");
            susluRapor.append(rapor); // Gelen raporu araya ekleme
            susluRapor.append("\n\n=========================================\n");
            susluRapor.append("   Bizi tercih ettiğiniz için teşekkürler  \n");

            txtBilgiEkrani.setText(susluRapor.toString()); // Hazırladığımız metni ekrana yazdır

            // Eğer kayıt yoksa burası çalışır
        } catch (KayitBulunamadiException ex) {
            txtBilgiEkrani.setForeground(new Color(192, 57, 43)); // Hata mesajının rengi kırmızı
            txtBilgiEkrani.setText("\n!!! KAYIT BULUNAMADI !!!\n\n" +
                    "Girilen Seri No: " + arananSeriNo + "\n\n" +
                    "Hata Mesajı: " + ex.getMessage() + "\n\n" +
                    "Lütfen seri numaranızı kontrol edip tekrar deneyiniz.");
        } catch (Exception ex) { // Başka bir hata olursa burası çalışır
            txtBilgiEkrani.setText("Beklenmeyen bir hata oluştu: " + ex.getMessage());
        }
    }
}