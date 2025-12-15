// src/gui/Main.java (GÜNCELLENDİ)
package gui;
//0000000

import Cihazlar.Cihaz;
import Garantiler.Garanti;
import Garantiler.StandartGaranti;
import Servis.ServisKaydı;
import Servis.ServisYonetimi;
import Servis.Teknisyen; // YENİ IMPORT
import java.util.Random; // YENİ IMPORT

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// CihazEkleListener arayüzünü uygulayarak eklenen cihazı yakalar
public class Main extends JFrame implements CihazEkleListener {

    private JTable table;
    private DefaultTableModel tableModel;

    // Alanlar: Cihaz listesi, dosya adı ve servis yöneticisi
    private List<Cihaz> cihazListesi = new ArrayList<>();
    private static final String CİHAZ_DOSYA_ADI = "cihaz_listesi.ser";

    private ServisYonetimi servisYonetimi;

    // YENİ: Teknisyen listesi ve Random nesnesi
    private final List<Teknisyen> teknisyenler = Arrays.asList(
            new Teknisyen("Osman Can Küçdemir", "Genel Onarım"),
            new Teknisyen("Çağatay Oğuz", "Telefon/Tablet"),
            new Teknisyen("İsmail Onur Koru", "Laptop")
    );
    private final Random random = new Random();

    // CONSTRUCTOR
    public Main() {
        setTitle("Dijital Cihaz Garanti & Servis Sistemi (Ana Ekran)");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 1. Verileri yükle
        cihazListesi = cihazYukle();
        servisYonetimi = new ServisYonetimi();

        // 2. Arayüzü başlat ve tabloyu doldur
        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);
    }

    // ===================================
    // CihazEkleListener Metodu (Callback)
    // ===================================
    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi); // Ekleme sonrası dosyaya kaydet
    }

    // ===================================
    // Dosya İşlemleri Metotları
    // ===================================

    private List<Cihaz> cihazYukle() {
        File dosya = new File(CİHAZ_DOSYA_ADI);
        if (dosya.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dosya))) {
                // List<Cihaz> tipine güvenli dönüşüm
                @SuppressWarnings("unchecked")
                List<Cihaz> yuklenenListe = (List<Cihaz>) ois.readObject();
                return yuklenenListe;
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Cihaz listesi yüklenirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
        return new ArrayList<>();
    }

    private void cihazKaydet(List<Cihaz> liste) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CİHAZ_DOSYA_ADI))) {
            oos.writeObject(liste);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cihaz listesi kaydedilemedi: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Rastgele bir teknisyen seçer.
     */
    private Teknisyen rastgeleTeknisyenSec() {
        int index = random.nextInt(teknisyenler.size());
        return teknisyenler.get(index);
    }

    // ===================================
    // GUI Oluşturma Metodu
    // ===================================
    private void initUI() {

        // ---------------- TABLE MODEL ----------------
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Fiyat", "Garanti Bitiş"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // ---------------- BUTTONS ----------------
        JButton btnCihazEkle = new JButton("Yeni Cihaz Ekle (Manuel Giriş)...");
        JButton btnServisKaydi = new JButton("Servis Kaydı Oluştur");
        JButton btnServisListele = new JButton("Servis Takip Ekranı");
        JButton btnSil = new JButton("Seçili Cihazı Sil");

        // --- Buton Aksiyonları ---

        // 1. Yeni Cihaz Kayıt Dialogunu Aç
        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });

        // 2. Servis Kaydı Oluşturma (GÜNCELLENDİ: Random Teknisyen Atama Eklendi)
        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);

                // Garanti Durumuna göre servis ücretini hesapla (tahmini tamir ücreti için kullanılır)
                Garanti standartGaranti = new StandartGaranti(selectedCihaz.getGarantiSuresiYil());
                double servisUcreti = standartGaranti.servisUcretiHesapla(selectedCihaz.isGarantiAktif());

                String garantiDurumu = selectedCihaz.isGarantiAktif() ? "Aktif" : "Sona Ermiş";

                String sorun = JOptionPane.showInputDialog(this,
                        String.format("%s için sorun açıklamasını girin:\nGaranti Durumu: %s\nTahmini Servis Ücreti: %.2f TL",
                                selectedCihaz.toString(), garantiDurumu, servisUcreti),
                        "Servis Kaydı Oluştur", JOptionPane.PLAIN_MESSAGE);


                if (sorun != null && !sorun.trim().isEmpty()) {
                    ServisKaydı yeniKayit = new ServisKaydı(selectedCihaz, sorun.trim());

                    // Hesaplanan tahmini ücreti kayda ata
                    yeniKayit.setTahminiTamirUcreti(servisUcreti);

                    // YENİ: Rastgele teknisyen ata
                    Teknisyen atananTeknisyen = rastgeleTeknisyenSec();
                    yeniKayit.setAtananTeknisyen(atananTeknisyen);

                    servisYonetimi.servisKaydiEkle(yeniKayit);
                    JOptionPane.showMessageDialog(this, "Servis kaydı başarıyla oluşturuldu. Atanan Teknisyen: " + atananTeknisyen.getAd());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen servis kaydı oluşturulacak cihazı seçin");
            }
        });

        // 3. Servis Takip Ekranını Aç
        btnServisListele.addActionListener(e -> {
            ServisTakipFrame servisFrame = new ServisTakipFrame(servisYonetimi);
            servisFrame.setVisible(true);
        });

        // 4. Cihaz Silme
        btnSil.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                cihazListesi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                cihazKaydet(cihazListesi);
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek cihazı seçin");
            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnCihazEkle);
        buttonPanel.add(btnServisKaydi);
        buttonPanel.add(btnServisListele);
        buttonPanel.add(btnSil);

        // ---------------- LAYOUT ----------------
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ===================================
    // Tablo Doldurma Metodu
    // ===================================
    private void cihazListesiniTabloyaDoldur(List<Cihaz> liste) {
        tableModel.setRowCount(0); // Tabloyu temizle
        for (Cihaz c : liste) {
            tableModel.addRow(new Object[]{
                    c.getCihazTuru(),
                    c.getMarka(),
                    c.getModel(),
                    c.getSeriNo(),
                    c.getFiyat(),
                    c.getGarantiBitisTarihi()
            });
        }
    }

    // ===================================
    // MAIN Metodu
    // ===================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}