package gui;

import Cihazlar.Cihaz;
import Garantiler.UzatilmisGaranti;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Servis.Teknisyen;
import Servis.TeknisyenDeposu;

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
    private static final String CİHAZ_DOSYA_ADI = "cihazlar.txt";
    private ServisYonetimi servisYonetimi;

    private final List<Teknisyen> teknisyenler = TeknisyenDeposu.getTumTeknisyenler();

    public Main() {
        setTitle("Teknolojik Cihaz Garanti & Servis Takip Sistemi");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Cihazları merkezi metotla yükle
        cihazListesi = Cihaz.verileriYukle(CİHAZ_DOSYA_ADI);

        // --- DÜZELTME: ServisYonetimi artık cihaz listesini constructor'da alıyor ---
        servisYonetimi = new ServisYonetimi(cihazListesi);
        // servisYonetimi.cihazBilgileriniEslestir(cihazListesi); // BU SATIR SİLİNDİ

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);
    }

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi);
    }

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

    private Teknisyen teknisyenSec(String cihazTuru) {
        if (cihazTuru.equalsIgnoreCase("Laptop")) {
            return teknisyenler.get(0);
        } else if (cihazTuru.equalsIgnoreCase("Telefon")) {
            return teknisyenler.get(1);
        } else if (cihazTuru.equalsIgnoreCase("Tablet")) {
            return teknisyenler.get(2);
        } else {
            return teknisyenler.get(0);
        }
    }

    private void initUI() {
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Müşteri", "Fiyat (TL)", "Garanti Bitiş"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        JButton btnCihazEkle = createStyledButton("Yeni Cihaz Ekle", new Color(52, 152, 219), Color.WHITE, btnFont);
        JButton btnServisKaydi = createStyledButton("Servis Kaydı Oluştur", new Color(52, 152, 219), Color.WHITE, btnFont);
        JButton btnServisListele = createStyledButton("Servis Takip Ekranı", new Color(74, 101, 114), Color.WHITE, btnFont);
        JButton btnGarantiUzat = createStyledButton("Garanti Paketleri (Uzat)", new Color(93, 138, 103), Color.WHITE, btnFont);
        JButton btnSil = createStyledButton("Seçili Cihazı Sil", new Color(169, 50, 38), Color.WHITE, btnFont);
        JButton btnGeriDon = createStyledButton("Geri Dön", new Color(146, 43, 33), Color.WHITE, btnFont);

        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });

        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);

                boolean garantiAktifMi = selectedCihaz.isGarantiAktif();
                String garantiDurumu = garantiAktifMi ? "Aktif" : "BİTMİŞ";

                JComboBox<String> sorunComboBox = new JComboBox<>(SORUN_MALIYET_ORANLARI.keySet().toArray(new String[0]));
                String mesaj = String.format("Cihaz: %s\nGaranti: %s (%s)\n\nSorunu Seçin: ",
                        selectedCihaz.getModel(), garantiDurumu, selectedCihaz.getGaranti().garantiTuru());

                int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
                    String secilenSorun = (String) sorunComboBox.getSelectedItem();

                    double hamUcret = 0.0;
                    double oran = SORUN_MALIYET_ORANLARI.get(secilenSorun);

                    if (oran == 0.0 && secilenSorun.contains("Yazılım")) {
                        hamUcret = 250.0;
                    } else {
                        hamUcret = selectedCihaz.getFiyat() * oran;
                    }

                    double musteriOdeyecek = selectedCihaz.getGaranti().sonMaliyetHesapla(hamUcret);
                    String temizSorunAdi = secilenSorun.split("\\(")[0].trim();

                    // --- DÜZELTME: Bu noktada artık cihaz nesnesine doğrudan referans veriyoruz ---
                    ServisKaydi yeniKayit = new ServisKaydi(selectedCihaz, temizSorunAdi);

                    yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);

                    Teknisyen atananTeknisyen = teknisyenSec(selectedCihaz.getCihazTuru());
                    yeniKayit.setAtananTeknisyen(atananTeknisyen);
                    servisYonetimi.servisKaydiEkle(yeniKayit);

                    JOptionPane.showMessageDialog(this,
                            String.format("Kayıt Başarılı!\nSorun: %s\nListe Fiyatı: %.2f TL\nÖdenecek Tutar: %.2f TL\nTeknisyen: %s",
                                    temizSorunAdi, hamUcret, musteriOdeyecek, atananTeknisyen.getAd()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen cihaz seçin.");
            }
        });

        btnGarantiUzat.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz seciliCihaz = cihazListesi.get(selectedRow);

                if (!seciliCihaz.isGarantiAktif() || seciliCihaz.getGaranti().garantiTuru().contains("Standart")) {
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
                            "Garanti Paketi Seçin (Süre bitse bile %10 servis indirimi sağlar):",
                            "Garanti Uzatma Teklifi",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[3]);

                    int uzatilacakAy = 0;
                    if (n == 0) { uzatilacakAy = 6; }
                    else if (n == 1) { uzatilacakAy = 12; }
                    else if (n == 2) { uzatilacakAy = 24; }

                    if (uzatilacakAy > 0) {
                        seciliCihaz.garantiUzat(uzatilacakAy);
                        cihazKaydet(cihazListesi);
                        cihazListesiniTabloyaDoldur(cihazListesi);
                        JOptionPane.showMessageDialog(this, "İşlem Başarılı! Garanti süresi uzatıldı ve paketi güncellendi.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Bu cihazın garantisi zaten uzatılmış.");
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

        btnGeriDon.addActionListener(e -> {
            new GirisEkrani().setVisible(true);
            this.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonPanel.add(btnCihazEkle);
        buttonPanel.add(btnServisKaydi);
        buttonPanel.add(btnGarantiUzat);
        buttonPanel.add(btnServisListele);
        buttonPanel.add(btnSil);
        buttonPanel.add(btnGeriDon);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor, Font font) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(font);
        btn.setFocusPainted(false);
        return btn;
    }

    private void cihazListesiniTabloyaDoldur(List<Cihaz> liste) {
        tableModel.setRowCount(0);
        for (Cihaz c : liste) {
            tableModel.addRow(new Object[]{
                    c.getCihazTuru(),
                    c.getMarka(),
                    c.getModel(),
                    c.getSeriNo(),
                    c.getSahip().toString().toUpperCase(),
                    c.getFiyat(),
                    c.getGarantiBitisTarihi()
            });
        }
    }
}