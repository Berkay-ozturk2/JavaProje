package gui;

import javax.swing.*;
import java.awt.*;

public class GirisEkrani extends JFrame {

    public GirisEkrani() {
        setTitle("Teknik Servis Sistemi - Giriş");
        setSize(450, 280); // Boyut biraz artırıldı
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2, 15, 15)); // Boşluklar artırıldı

        // Arka plan rengini hafif gri yaparak modernleştirme
        getContentPane().setBackground(new Color(245, 245, 245));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Müşteri Butonu ---
        JButton btnMusteri = new JButton("MÜŞTERİ GİRİŞİ");
        // Profesyonel Mavi (Belize Hole / Peter River tonları)
        btnMusteri.setBackground(new Color(41, 128, 185));
        btnMusteri.setForeground(Color.WHITE);
        btnMusteri.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnMusteri.setFocusPainted(false); // Tıklanınca oluşan çerçeveyi kaldırır

        btnMusteri.addActionListener(e -> {
            new MusteriTakipEkrani().setVisible(true);
        });

        // --- Personel Butonu ---
        JButton btnPersonel = new JButton("PERSONEL GİRİŞİ");
        // Kurumsal Koyu Gri/Lacivert (Midnight Blue)
        btnPersonel.setBackground(new Color(44, 62, 80));
        btnPersonel.setForeground(Color.WHITE);
        btnPersonel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPersonel.setFocusPainted(false);

        btnPersonel.addActionListener(e -> {
            // Basit bir güvenlik kontrolü
            String sifre = JOptionPane.showInputDialog(this, "Personel Şifresi Giriniz:", "Güvenlik", JOptionPane.QUESTION_MESSAGE);

            // Şifre: "a"
            if ("a".equalsIgnoreCase(sifre)) {
                new Main().setVisible(true); // Mevcut Main ekranını aç
                this.dispose(); // Giriş ekranını kapat
            } else if (sifre != null) {
                JOptionPane.showMessageDialog(this, "Hatalı Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(btnMusteri);
        add(btnPersonel);
    }

    public static void main(String[] args) {
        try {
            // Modern "FlatLaf Light" temasını uygular
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("FlatLaf başlatılamadı!");
        }
        // Programın yeni başlangıç noktası
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}