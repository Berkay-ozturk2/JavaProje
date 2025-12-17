package gui;

import javax.swing.*;
import java.awt.*;

public class GirisEkrani extends JFrame {

    public GirisEkrani() {
        setTitle("Teknik Servis Sistemi - Giriş");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2, 10, 10));

        // --- Müşteri Butonu ---
        JButton btnMusteri = new JButton("MÜŞTERİ GİRİŞİ");
        btnMusteri.setBackground(new Color(200, 230, 255)); // Açık Mavi
        btnMusteri.setFont(new Font("Arial", Font.BOLD, 14));

        btnMusteri.addActionListener(e -> {
            new MusteriTakipEkrani().setVisible(true);
            // Müşteri ekranı açılınca bu pencereyi gizlemiyoruz, belki geri dönerler.
        });

        // --- Personel Butonu ---
        JButton btnPersonel = new JButton("PERSONEL GİRİŞİ");
        btnPersonel.setBackground(new Color(255, 200, 200)); // Açık Kırmızı
        btnPersonel.setFont(new Font("Arial", Font.BOLD, 14));

        btnPersonel.addActionListener(e -> {
            // Basit bir güvenlik kontrolü
            String sifre = JOptionPane.showInputDialog(this, "Personel Şifresi Giriniz:", "Güvenlik", JOptionPane.QUESTION_MESSAGE);

            // Şifre: "admin" (Gerçek projede veritabanından kontrol edilir)
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

    public static void main(String[] args) {try {
        // Modern "FlatLaf Light" temasını uygular
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception ex) {
        System.err.println("FlatLaf başlatılamadı!");
    }
        // Programın yeni başlangıç noktası
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}