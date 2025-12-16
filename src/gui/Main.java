package gui;

import Cihazlar.Cihaz;
import Garantiler.UzatilmisGaranti;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Servis.Teknisyen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main extends JFrame implements CihazEkleListener {

    private static final Map<String, Double> SORUN_MALIYET_ORANLARI = new LinkedHashMap<>();

    static {
        SORUN_MALIYET_ORANLARI.put("Ekran Kırık (Cihaz Fiyatı %15)", 0.15);
        SORUN_MALIYET_ORANLARI.put("Dokunmatik Arızası (Cihaz Fiyatı %10)", 0.10);
        SORUN_MALIYET_ORANLARI.put("Batarya/Pil Değişimi (Cihaz Fiyatı %8)", 0.08);
        SORUN_MALIYET_ORANLARI.put("Şarj Soketi Arızası (Cihaz Fiyatı %5)", 0.05);
        SORUN_MALIYET_ORANLARI.put("Kasa/Kapak Değişimi (Cihaz Fiyatı %7)", 0.07);
        SORUN_MALIYET_ORANLARI.put("Kamera Arızası (Cihaz Fiyatı %12)", 0.12);
        SORUN_MALIYET_ORANLARI.put("Anakarta Sıvı Teması (Cihaz Fiyatı %30)", 0.30);
        SORUN_MALIYET_ORANLARI.put("Yazılım/Sıfırlama (Sabit 250 TL)", 0.0);
    }

    private JTable table;
    private DefaultTableModel tableModel;
    private List<Cihaz> cihazListesi = new ArrayList<>();

    // DOSYA ADI DEĞİŞTİ
    private static final String CİHAZ_DOSYA_ADI = "cihazlar.txt";

    private ServisYonetimi servisYonetimi;

    // İsimleri ve uzmanlık alanlarını isteğinize göre sabitledik
    private final List<Teknisyen> teknisyenler = Arrays.asList(
            new Teknisyen("Osman Can Küçdemir", "Laptop Onarım"), // 0. İndeks: Laptop
            new Teknisyen("Çağatay Oğuz", "Telefon Onarım"),      // 1. İndeks: Telefon
            new Teknisyen("İsmail Onur Koru", "Tablet Onarım"));  // 2. İndeks: Tablet
    private final Random random = new Random();

    public Main() {
        setTitle("Teknolojik Cihaz Garanti & Servis Takip Sistemi");
        setSize(950, 500);
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

    // --- TXT YÜKLEME METODU ---
    // --- TXT YÜKLEME METODU ---
    private List<Cihaz> cihazYukle() {
        List<Cihaz> liste = new ArrayList<>();
        File dosya = new File(CİHAZ_DOSYA_ADI);
        if (!dosya.exists()) return liste;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(dosya), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Cihaz c = Cihaz.fromTxtFormat(line);
                    if (c != null) liste.add(c);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Dosya okuma hatası: " + e.getMessage());
        }
        return liste;
    }

    // --- TXT KAYDETME METODU ---
    private void cihazKaydet(List<Cihaz> liste) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(CİHAZ_DOSYA_ADI), "UTF-8"))) {
            for (Cihaz c : liste) {
                bw.write(c.toTxtFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        }
    }

    // Cihaz türüne göre ilgili teknisyeni döndüren metot
    private Teknisyen teknisyenSec(String cihazTuru) {
        if (cihazTuru.equalsIgnoreCase("Laptop")) {
            return teknisyenler.get(0); // Osman Can Küçdemir
        } else if (cihazTuru.equalsIgnoreCase("Telefon")) {
            return teknisyenler.get(1); // Çağatay Oğuz
        } else if (cihazTuru.equalsIgnoreCase("Tablet")) {
            return teknisyenler.get(2); // İsmail Onur Koru
        } else {
            return teknisyenler.get(0); // Varsayılan olarak ilk kişi
        }
    }

    private void initUI() {
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Fiyat (TL)", "Garanti Bitiş"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCihazEkle = new JButton("Yeni Cihaz Ekle");
        JButton btnServisKaydi = new JButton("Servis Kaydı Oluştur");
        JButton btnServisListele = new JButton("Servis Takip Ekranı");
        JButton btnGarantiUzat = new JButton("Garanti Paketleri (Uzat)");
        btnGarantiUzat.setBackground(new Color(220, 255, 220));
        JButton btnSil = new JButton("Seçili Cihazı Sil");

        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });

        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);
                boolean garantiAktifMi = selectedCihaz.isGarantiAktif();
                String garantiDurumu = garantiAktifMi ? "Aktif (Ücretsiz)" : "BİTMİŞ (Ücretli)";

                JComboBox<String> sorunComboBox = new JComboBox<>(SORUN_MALIYET_ORANLARI.keySet().toArray(new String[0]));
                String mesaj = String.format("Cihaz: " + selectedCihaz.getModel() + "\nFiyat: " + selectedCihaz.getFiyat() + " TL" + "\nGaranti: " + garantiDurumu + "\n\nSorunu Seçin: ");

                int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
                    String secilenSorun = (String) sorunComboBox.getSelectedItem();
                    double hesaplananUcret = 0.0;

                    if (garantiAktifMi) {
                        hesaplananUcret = 0.0;
                    } else {
                        double oran = SORUN_MALIYET_ORANLARI.get(secilenSorun);
                        if (oran == 0.0 && secilenSorun.contains("Yazılım")) {
                            hesaplananUcret = 250.0;
                        } else {
                            hesaplananUcret = selectedCihaz.getFiyat() * oran;
                        }
                    }

                    String temizSorunAdi = secilenSorun.split("\\(")[0].trim();
                    ServisKaydi yeniKayit = new ServisKaydi(selectedCihaz, temizSorunAdi);
                    yeniKayit.setTahminiTamirUcreti(hesaplananUcret);

                    // Seçilen cihazın türünü metoda gönderiyoruz
                    Teknisyen atananTeknisyen = teknisyenSec(selectedCihaz.getCihazTuru());
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

        btnGarantiUzat.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz seciliCihaz = cihazListesi.get(selectedRow);
                if (!seciliCihaz.isGarantiAktif()) {
                    double cihazFiyati = seciliCihaz.getFiyat();
                    double fiyat6Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 6);
                    double fiyat12Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 12);
                    double fiyat24Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 24);

                    Object[] options = {
                            String.format("6 Ay (%.2f TL)", fiyat6Ay),
                            String.format("12 Ay (%.2f TL)", fiyat12Ay),
                            String.format("24 Ay (%.2f TL)", fiyat24Ay),
                            "İptal"
                    };

                    int n = JOptionPane.showOptionDialog(this,
                            "Bu cihazın garantisi bitmiş.\nGarantiyi uzatmak için paket seçin:",
                            "Garanti Uzatma Teklifi",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[3]);

                    int uzatilacakAy = 0;
                    double odenecekTutar = 0;
                    if (n == 0) { uzatilacakAy = 6; odenecekTutar = fiyat6Ay; }
                    else if (n == 1) { uzatilacakAy = 12; odenecekTutar = fiyat12Ay; }
                    else if (n == 2) { uzatilacakAy = 24; odenecekTutar = fiyat24Ay; }

                    if (uzatilacakAy > 0) {
                        seciliCihaz.garantiUzat(uzatilacakAy);
                        cihazKaydet(cihazListesi);
                        cihazListesiniTabloyaDoldur(cihazListesi);
                        JOptionPane.showMessageDialog(this, "İşlem Başarılı! Garanti uzatıldı.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Bu cihazın garantisi zaten devam ediyor.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen garanti işlemi yapılacak cihazı seçin.");
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

        JButton btnGeriDon = new JButton("Geri Dön");
        btnGeriDon.setBackground(new Color(150, 40, 100)); // Dikkat çekmesi için kırmızı tonu
        btnGeriDon.setForeground(Color.WHITE); // Yazı rengi beyaz

        btnGeriDon.addActionListener(e -> {
            // 1. Giriş ekranını yeniden oluştur ve göster
            new GirisEkrani().setVisible(true);

            // 2. Mevcut Ana Menü penceresini kapat (Program tamamen kapanmaz, sadece bu pencere gider)
            this.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnCihazEkle);
        buttonPanel.add(btnServisKaydi);
        buttonPanel.add(btnGarantiUzat);
        buttonPanel.add(btnServisListele);
        buttonPanel.add(btnSil);
        buttonPanel.add(btnGeriDon);

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
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}