// src/gui/Main.java
package gui;

import Cihazlar.Cihaz;
import Servis.ServisKaydı;
import Servis.ServisYonetimi;
import Servis.Teknisyen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main extends JFrame implements CihazEkleListener {

    // YENİ: Sorunlar ve Cihaz Fiyatına Göre Maliyet Oranları (Örn: 0.15 = %15)
    private static final Map<String, Double> SORUN_MALIYET_ORANLARI = new LinkedHashMap<>();

    static {
        SORUN_MALIYET_ORANLARI.put("Ekran Kırık (Cihaz Fiyatı %15)", 0.15);
        SORUN_MALIYET_ORANLARI.put("Dokunmatik Arızası (Cihaz Fiyatı %10)", 0.10);
        SORUN_MALIYET_ORANLARI.put("Batarya/Pil Değişimi (Cihaz Fiyatı %8)", 0.08);
        SORUN_MALIYET_ORANLARI.put("Şarj Soketi Arızası (Cihaz Fiyatı %5)", 0.05);
        SORUN_MALIYET_ORANLARI.put("Kasa/Kapak Değişimi (Cihaz Fiyatı %7)", 0.07);
        SORUN_MALIYET_ORANLARI.put("Kamera Arızası (Cihaz Fiyatı %12)", 0.12);
        SORUN_MALIYET_ORANLARI.put("Anakarta Sıvı Teması (Cihaz Fiyatı %30)", 0.30);
        SORUN_MALIYET_ORANLARI.put("Yazılım/Sıfırlama (Sabit 250 TL)", 0.0); // Özel durum
    }

    private JTable table;
    private DefaultTableModel tableModel;
    private List<Cihaz> cihazListesi = new ArrayList<>();
    private static final String CİHAZ_DOSYA_ADI = "cihaz_listesi.ser";
    private ServisYonetimi servisYonetimi;

    private final List<Teknisyen> teknisyenler = Arrays.asList(
            new Teknisyen("Osman Can Küçdemir", "Genel Onarım"),
            new Teknisyen("Çağatay Oğuz", "Telefon/Tablet"),
            new Teknisyen("İsmail Onur Koru", "Laptop")
    );
    private final Random random = new Random();

    public Main() {
        setTitle("Dijital Cihaz Garanti & Servis Sistemi");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cihazListesi = cihazYukle();
        servisYonetimi = new ServisYonetimi();

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);
    }

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi);
    }

    private List<Cihaz> cihazYukle() {
        File dosya = new File(CİHAZ_DOSYA_ADI);
        if (dosya.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dosya))) {
                return (List<Cihaz>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Yükleme Hatası: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private void cihazKaydet(List<Cihaz> liste) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CİHAZ_DOSYA_ADI))) {
            oos.writeObject(liste);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        }
    }

    private Teknisyen rastgeleTeknisyenSec() {
        return teknisyenler.get(random.nextInt(teknisyenler.size()));
    }

    private void initUI() {
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Fiyat (TL)", "Garanti Bitiş"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCihazEkle = new JButton("Yeni Cihaz Ekle...");
        JButton btnServisKaydi = new JButton("Servis Kaydı Oluştur");
        JButton btnServisListele = new JButton("Servis Takip Ekranı");
        JButton btnSil = new JButton("Seçili Cihazı Sil");

        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });

        // --- SERVİS KAYDI ve DİNAMİK FİYAT HESAPLAMA ---
        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);
                boolean garantiAktifMi = selectedCihaz.isGarantiAktif();
                String garantiDurumu = garantiAktifMi ? "Aktif (Ücretsiz)" : "BİTMİŞ (Ücretli)";

                // Sorunları ComboBox'a yükle
                JComboBox<String> sorunComboBox = new JComboBox<>(SORUN_MALIYET_ORANLARI.keySet().toArray(new String[0]));
                String mesaj = String.format("Cihaz: %s\nFiyat: %.2f TL\nGaranti: %s\n\nSorunu Seçin:",
                        selectedCihaz.getModel(), selectedCihaz.getFiyat(), garantiDurumu);

                int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
                    String secilenSorun = (String) sorunComboBox.getSelectedItem();
                    double hesaplananUcret = 0.0;

                    if (garantiAktifMi) {
                        hesaplananUcret = 0.0;
                    } else {
                        // YENİ HESAPLAMA MANTIĞI: Cihaz Fiyatı * Oran
                        double oran = SORUN_MALIYET_ORANLARI.get(secilenSorun);
                        if (oran == 0.0 && secilenSorun.contains("Yazılım")) {
                            hesaplananUcret = 250.0; // Sabit ücret örneği
                        } else {
                            hesaplananUcret = selectedCihaz.getFiyat() * oran;
                        }
                    }

                    // Sorun ismini temizle (Parantez içini kaldır)
                    String temizSorunAdi = secilenSorun.split("\\(")[0].trim();

                    ServisKaydı yeniKayit = new ServisKaydı(selectedCihaz, temizSorunAdi);
                    yeniKayit.setTahminiTamirUcreti(hesaplananUcret);

                    Teknisyen atananTeknisyen = rastgeleTeknisyenSec();
                    yeniKayit.setAtananTeknisyen(atananTeknisyen);

                    servisYonetimi.servisKaydiEkle(yeniKayit);

                    JOptionPane.showMessageDialog(this,
                            String.format("Kayıt Başarılı!\nCihaz Değeri: %.2f TL\nSorun: %s\nHesaplanan Tamir Ücreti: %.2f TL\nTeknisyen: %s",
                                    selectedCihaz.getFiyat(), temizSorunAdi, hesaplananUcret, atananTeknisyen.getAd()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen cihaz seçin.");
            }
        });

        btnServisListele.addActionListener(e -> {
            ServisTakipFrame servisFrame = new ServisTakipFrame(servisYonetimi);
            servisFrame.setVisible(true);
        });

        btnSil.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                cihazListesi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                cihazKaydet(cihazListesi);
            } else {
                JOptionPane.showMessageDialog(this, "Silinecek cihazı seçin.");
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnCihazEkle);
        buttonPanel.add(btnServisKaydi);
        buttonPanel.add(btnServisListele);
        buttonPanel.add(btnSil);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cihazListesiniTabloyaDoldur(List<Cihaz> liste) {
        tableModel.setRowCount(0);
        for (Cihaz c : liste) {
            tableModel.addRow(new Object[]{
                    c.getCihazTuru(), c.getMarka(), c.getModel(), c.getSeriNo(), c.getFiyat(), c.getGarantiBitisTarihi()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}