package gui;

import Cihazlar.Cihaz;
import Garantiler.UzatilmisGaranti;
import Servis.FiyatlandirmaHizmeti;
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Servis.Teknisyen;
import Servis.TeknisyenDeposu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame implements CihazEkleListener {

    private JTable table;
    private DefaultTableModel tableModel;
    private List<Cihaz> cihazListesi = new ArrayList<>();

    // --- GÜNCELLENEN DOSYA YOLU ---
    private static final String CIHAZ_DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "cihazlar.txt";

    private ServisYonetimi servisYonetimi;
    private final List<Teknisyen> teknisyenler = TeknisyenDeposu.getTumTeknisyenler();

    public Main() {
        setTitle("Teknolojik Cihaz Garanti & Servis Takip Sistemi");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Verileri yükle
        cihazListesi = Cihaz.verileriYukle(CIHAZ_DOSYA_ADI);
        servisYonetimi = new ServisYonetimi(cihazListesi);

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);
    }

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi);
    }

    private void initUI() {
        setupTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // --- BUTON GRUPLANDIRMA ---
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        // 1. ÜST PANEL (Temel İşlemler: Ekle, Servis, Garanti, Takip)
        JPanel panelUstIslemler = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelUstIslemler.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton btnCihazEkle = createStyledButton("Yeni Cihaz Ekle", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });
        panelUstIslemler.add(btnCihazEkle);

        JButton btnServisKaydi = createStyledButton("Servis Kaydı Oluştur", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnServisKaydi.addActionListener(e -> servisKaydiOlusturIslemi());
        panelUstIslemler.add(btnServisKaydi);

        JButton btnGarantiUzat = createStyledButton("Garanti Paketleri (Uzat)", new Color(93, 138, 103), Color.WHITE, btnFont);
        btnGarantiUzat.addActionListener(e -> garantiUzatmaIslemi());
        panelUstIslemler.add(btnGarantiUzat);

        JButton btnServisListele = createStyledButton("Servis Takip Ekranı", new Color(74, 101, 114), Color.WHITE, btnFont);
        btnServisListele.addActionListener(e -> new ServisTakipFrame(servisYonetimi).setVisible(true));
        panelUstIslemler.add(btnServisListele);


        // 2. ALT PANEL (Yönetim ve Çıkış: Sil, Temizle, Geri Dön)
        JPanel panelAltYonetim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15)); // Boşluk biraz artırıldı

        JButton btnSil = createStyledButton("Seçili Cihazı Sil", new Color(169, 50, 38), Color.WHITE, btnFont);
        btnSil.addActionListener(e -> cihazSilIslemi());
        panelAltYonetim.add(btnSil);

        JButton btnTemizle = createStyledButton("Tüm Servis Verilerini Sil", new Color(17, 4, 8), Color.WHITE, btnFont);
        btnTemizle.addActionListener(e -> veriTemizlemeIslemi());
        panelAltYonetim.add(btnTemizle);

        // Geri Dön butonunu belirginleştirmek için gri tonlarında yapabiliriz veya mevcut rengi koruyabiliriz
        JButton btnGeriDon = createStyledButton("Geri Dön", new Color(146, 43, 33), Color.WHITE, btnFont);
        btnGeriDon.addActionListener(e -> {
            new GirisEkrani().setVisible(true);
            this.dispose();
        });
        panelAltYonetim.add(btnGeriDon);

        // --- YERLEŞİM (LAYOUT) ---
        // Üst paneli pencerenin üstüne (NORTH), Tabloyu ortaya (CENTER), Alt paneli alta (SOUTH) koyuyoruz.
        add(panelUstIslemler, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelAltYonetim, BorderLayout.SOUTH);
    }
    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Müşteri", "Fiyat (TL)", "Garanti Bitiş"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void servisKaydiOlusturIslemi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Lütfen cihaz seçin.");
            return;
        }

        Cihaz selectedCihaz = cihazListesi.get(selectedRow);
        String garantiDurumu = selectedCihaz.isGarantiAktif() ? "Aktif" : "BİTMİŞ";

        JComboBox<String> sorunComboBox = new JComboBox<>(FiyatlandirmaHizmeti.getSorunListesi());
        String mesaj = String.format("Cihaz: %s\nGaranti: %s (%s)\n\nSorunu Seçin: ",
                selectedCihaz.getModel(), garantiDurumu, selectedCihaz.getGaranti().garantiTuru());

        int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
            String secilenSorun = (String) sorunComboBox.getSelectedItem();

            double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(secilenSorun, selectedCihaz.getFiyat());
            double musteriOdeyecek = selectedCihaz.getGaranti().sonMaliyetHesapla(hamUcret);
            String temizSorunAdi = secilenSorun.split("\\(")[0].trim();

            ServisKaydi yeniKayit = new ServisKaydi(selectedCihaz, temizSorunAdi);
            yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);

            Teknisyen atananTeknisyen = teknisyenSec(selectedCihaz.getCihazTuru());
            yeniKayit.setAtananTeknisyen(atananTeknisyen);

            servisYonetimi.servisKaydiEkle(yeniKayit);

            JOptionPane.showMessageDialog(this,
                    String.format("Kayıt Başarılı!\nSorun: %s\nListe Fiyatı: %.2f TL\nÖdenecek Tutar: %.2f TL\nTeknisyen: %s",
                            temizSorunAdi, hamUcret, musteriOdeyecek, atananTeknisyen.getAd()));
        }
    }

    private void garantiUzatmaIslemi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Lütfen garanti işlemi yapılacak cihazı seçin.");
            return;
        }

        Cihaz seciliCihaz = cihazListesi.get(selectedRow);

        if (seciliCihaz.getGaranti().garantiTuru().contains("Uzatılmış")) {
            JOptionPane.showMessageDialog(this, "Bu cihazın garantisi zaten uzatılmış.");
            return;
        }

        double cihazFiyati = seciliCihaz.getFiyat();
        double fiyat6Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 6);
        double fiyat12Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 12);
        double fiyat24Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 24);
        // EKLENDİ: 36 Ay fiyatı hesaplama
        double fiyat36Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 36);

        Object[] options = {
                String.format("6 Ay (%.2f TL)", fiyat6Ay),
                String.format("12 Ay (%.2f TL)", fiyat12Ay),
                String.format("24 Ay (%.2f TL)", fiyat24Ay),
                String.format("36 Ay (%.2f TL)", fiyat36Ay), // EKLENDİ: Seçeneklere eklendi
                "İptal"
        };

        int n = JOptionPane.showOptionDialog(this,
                "Garanti Paketi Seçin (Süre bitse bile %10 indirim):",
                "Garanti Uzatma Teklifi",
                JOptionPane.YES_NO_CANCEL_OPTION, // Buton tipi
                JOptionPane.QUESTION_MESSAGE,     // İkon tipi
                null,
                options,
                options[4]); // Varsayılan buton (İptal) indeksi güncellendi

        int uzatilacakAy = 0;
        if (n == 0) uzatilacakAy = 6;
        else if (n == 1) uzatilacakAy = 12;
        else if (n == 2) uzatilacakAy = 24;
        else if (n == 3) uzatilacakAy = 36; // EKLENDİ: 36 ay seçimi kontrolü

        if (uzatilacakAy > 0) {
            seciliCihaz.garantiUzat(uzatilacakAy);
            cihazKaydet(cihazListesi);
            cihazListesiniTabloyaDoldur(cihazListesi);
            JOptionPane.showMessageDialog(this, "İşlem Başarılı! Garanti süresi uzatıldı.");
        }
    }

    private void cihazSilIslemi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bu cihazı silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                cihazListesi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                cihazKaydet(cihazListesi);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silinecek cihazı seçin.");
        }
    }

    private void cihazKaydet(List<Cihaz> liste) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(CIHAZ_DOSYA_ADI), "UTF-8"))) {
            for (Cihaz c : liste) {
                bw.write(c.toTxtFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        }
    }

    private Teknisyen teknisyenSec(String cihazTuru) {
        if (cihazTuru.equalsIgnoreCase("Laptop")) return teknisyenler.get(0);
        else if (cihazTuru.equalsIgnoreCase("Telefon")) return teknisyenler.get(1);
        else if (cihazTuru.equalsIgnoreCase("Tablet")) return teknisyenler.get(2);
        return teknisyenler.get(0);
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
        // --- EKLENEN SIRALAMA İŞLEMİ (Gereksinim: sort) ---
        // Cihazları fiyatlarına göre pahalıdan ucuza doğru sıralar
        liste.sort((c1, c2) -> Double.compare(c2.getFiyat(), c1.getFiyat()));

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
    private void veriTemizlemeIslemi() {
        // Kullanıcıdan güvenlik onayı al
        int secim = JOptionPane.showConfirmDialog(this,
                "DİKKAT: Tüm GEÇMİŞ SERVİS KAYITLARI kalıcı olarak silinecektir!\n" +
                        "Cihaz kayıtları silinmez, sadece servis geçmişi temizlenir.\n\n" +
                        "Bu işlem geri alınamaz. Devam etmek istiyor musunuz?",
                "Veri Temizleme Onayı",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (secim == JOptionPane.YES_OPTION) {
            // ServisYonetimi içindeki metodumuzu çağırıyoruz
            servisYonetimi.verileriTemizle();

            JOptionPane.showMessageDialog(this,
                    "İşlem Başarılı.\nTüm servis geçmişi temizlendi.",
                    "Bilgi",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}