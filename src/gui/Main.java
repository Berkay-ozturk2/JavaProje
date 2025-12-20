package gui;

import Araclar.TarihYardimcisi;
import Cihazlar.Cihaz;
import Servis.FiyatlandirmaHizmeti;
import Servis.RaporlamaHizmeti; // YENİ EKLENDİ
import Servis.ServisKaydi;
import Servis.ServisYonetimi;
import Servis.Teknisyen;
import Servis.TeknisyenDeposu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame implements CihazEkleListener {

    private JTabbedPane tabbedPane;
    private Map<String, DefaultTableModel> tableModels = new HashMap<>();
    private Map<String, JTable> tables = new HashMap<>();

    private List<Cihaz> cihazListesi = new ArrayList<>();

    private static final String CIHAZ_DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "cihazlar.txt";

    private ServisYonetimi servisYonetimi;

    public Main() {
        setTitle("Teknolojik Cihaz Garanti & Servis Takip Sistemi");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Verileri yükle
        cihazListesi = Cihaz.verileriYukle(CIHAZ_DOSYA_ADI);
        servisYonetimi = new ServisYonetimi(cihazListesi);

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);

        System.out.println("Sistem başlatıldı. Cihaz sayısı: " + cihazListesi.size());
    }

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi);
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        createTab("Tümü");
        createTab("Telefon");
        createTab("Tablet");
        createTab("Laptop");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        JPanel panelAltYonetim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JButton btnCihazEkle = createStyledButton("Yeni Cihaz Ekle", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });
        panelAltYonetim.add(btnCihazEkle);

        JButton btnServisKaydi = createStyledButton("Servis Kaydı Oluştur", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnServisKaydi.addActionListener(e -> servisKaydiOlusturIslemi());
        panelAltYonetim.add(btnServisKaydi);

        JButton btnGarantiUzat = createStyledButton("Garanti Paketleri (Uzat)", new Color(93, 138, 103), Color.WHITE, btnFont);
        btnGarantiUzat.addActionListener(e -> garantiUzatmaIslemi());
        panelAltYonetim.add(btnGarantiUzat);

        JButton btnServisListele = createStyledButton("Servis Takip Ekranı", new Color(74, 101, 114), Color.WHITE, btnFont);
        btnServisListele.addActionListener(e -> new ServisTakipFrame(servisYonetimi).setVisible(true));
        panelAltYonetim.add(btnServisListele);

        JButton btnSil = createStyledButton("Seçili Cihazı Sil", new Color(169, 50, 38), Color.WHITE, btnFont);
        btnSil.addActionListener(e -> cihazSilIslemi());
        panelAltYonetim.add(btnSil);

        JButton btnRaporla = createStyledButton("Konsol Raporu Al", new Color(36, 89, 69), Color.WHITE, btnFont);
        btnRaporla.addActionListener(e -> konsolRaporuOlustur());
        panelAltYonetim.add(btnRaporla);

        JButton btnGeriDon = createStyledButton("Geri Dön", new Color(146, 43, 33), Color.WHITE, btnFont);
        btnGeriDon.addActionListener(e -> {
            new GirisEkrani().setVisible(true);
            this.dispose();
        });
        panelAltYonetim.add(btnGeriDon);

        add(tabbedPane, BorderLayout.CENTER);
        add(panelAltYonetim, BorderLayout.SOUTH);
    }

    private void createTab(String title) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Tür", "Marka", "Model", "Seri No", "Müşteri", "Fiyat (TL)", "Garanti Bitiş"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        tableModels.put(title, model);
        tables.put(title, table);

        JScrollPane scrollPane = new JScrollPane(table);
        tabbedPane.addTab(title, scrollPane);
    }

    private Cihaz getSeciliCihaz() {
        String activeTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        JTable activeTable = tables.get(activeTitle);

        int selectedRow = activeTable.getSelectedRow();
        if (selectedRow < 0) return null;

        String seriNo = (String) activeTable.getValueAt(selectedRow, 3);

        for (Cihaz c : cihazListesi) {
            if (c.getSeriNo().equals(seriNo)) {
                return c;
            }
        }
        return null;
    }

    private void servisKaydiOlusturIslemi() {
        Cihaz selectedCihaz = getSeciliCihaz();
        if (selectedCihaz == null) {
            JOptionPane.showMessageDialog(this, "Lütfen işlem yapılacak cihazı seçin.");
            return;
        }

        String garantiDurumu = selectedCihaz.isGarantiAktif() ? "Aktif" : "BİTMİŞ";
        String vipBilgi = selectedCihaz.getSahip().isVip() ? " [VIP Müşteri]" : "";

        JComboBox<String> sorunComboBox = new JComboBox<>(FiyatlandirmaHizmeti.getSorunListesi());
        String mesaj = String.format("Cihaz: %s\nSahibi: %s%s\nGaranti: %s (%s)\n\nSorunu Seçin: ",
                selectedCihaz.getModel(),
                selectedCihaz.getSahip().getAd() + " " + selectedCihaz.getSahip().getSoyad(),
                vipBilgi,
                garantiDurumu, selectedCihaz.getGaranti().garantiTuru());

        int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
            String secilenSorun = (String) sorunComboBox.getSelectedItem();

            double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(
                    secilenSorun,
                    selectedCihaz.getFiyat(),
                    selectedCihaz.getSahip().isVip()
            );

            double musteriOdeyecek = selectedCihaz.getGaranti().sonMaliyetHesapla(hamUcret);
            String temizSorunAdi = secilenSorun.split("\\(")[0].trim();

            int tahminiIsGunu = 20;
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(LocalDate.now(), tahminiIsGunu);

            ServisKaydi yeniKayit = new ServisKaydi(selectedCihaz, temizSorunAdi);
            yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);

            Teknisyen atananTeknisyen = TeknisyenDeposu.uzmanligaGoreGetir(selectedCihaz.getCihazTuru());
            yeniKayit.setAtananTeknisyen(atananTeknisyen);

            servisYonetimi.servisKaydiEkle(yeniKayit);

            JOptionPane.showMessageDialog(this,
                    String.format("Kayıt Başarılı!\nSorun: %s\nListe Fiyatı: %.2f TL\nÖdenecek Tutar: %.2f TL\n" +
                                    "Teknisyen: %s\nTahmini Teslim: %s",
                            temizSorunAdi, hamUcret, musteriOdeyecek, atananTeknisyen.getAd(), tahminiTeslim));
        }
    }

    private void garantiUzatmaIslemi() {
        Cihaz seciliCihaz = getSeciliCihaz();
        if (seciliCihaz == null) {
            JOptionPane.showMessageDialog(this, "Lütfen garanti işlemi yapılacak cihazı seçin.");
            return;
        }

        if (seciliCihaz.getGaranti().garantiTuru().contains("Uzatılmış")) {
            JOptionPane.showMessageDialog(this, "Bu cihazın garantisi zaten uzatılmış.");
            return;
        }

        double cihazFiyati = seciliCihaz.getFiyat();

        double fiyat6Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 6);
        double fiyat12Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 12);
        double fiyat24Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 24);
        double fiyat36Ay = FiyatlandirmaHizmeti.paketFiyatiHesapla(cihazFiyati, 36);

        Object[] options = {
                String.format("6 Ay (%.2f TL)", fiyat6Ay),
                String.format("12 Ay (%.2f TL)", fiyat12Ay),
                String.format("24 Ay (%.2f TL)", fiyat24Ay),
                String.format("36 Ay (%.2f TL)", fiyat36Ay),
                "İptal"
        };

        int n = JOptionPane.showOptionDialog(this,
                "Garanti Paketi Seçin (Süre bitse bile %10 indirim):",
                "Garanti Uzatma Teklifi",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[4]);

        int uzatilacakAy = 0;
        if (n == 0) uzatilacakAy = 6;
        else if (n == 1) uzatilacakAy = 12;
        else if (n == 2) uzatilacakAy = 24;
        else if (n == 3) uzatilacakAy = 36;

        if (uzatilacakAy > 0) {
            seciliCihaz.garantiUzat(uzatilacakAy);
            cihazKaydet(cihazListesi);
            cihazListesiniTabloyaDoldur(cihazListesi);
            JOptionPane.showMessageDialog(this, "İşlem Başarılı! Garanti süresi uzatıldı.");
        }
    }

    private void cihazSilIslemi() {
        Cihaz silinecekCihaz = getSeciliCihaz();

        if (silinecekCihaz != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bu cihazı (" + silinecekCihaz.getModel() + ") silmek istediğinize emin misiniz?",
                    "Onay", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                cihazListesi.remove(silinecekCihaz);
                cihazKaydet(cihazListesi);
                cihazListesiniTabloyaDoldur(cihazListesi);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silinecek cihazı seçin.");
        }
    }

    private void cihazKaydet(List<Cihaz> liste) {
        try {
            Cihaz.verileriKaydet(liste, CIHAZ_DOSYA_ADI);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        }
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
        for (DefaultTableModel model : tableModels.values()) {
            model.setRowCount(0);
        }

        for (Cihaz c : liste) {
            String musteriBilgisi = c.getSahip().toString().toUpperCase();
            if (c.getSahip().isVip()) {
                musteriBilgisi += " [VIP]";
            }

            Object[] rowData = new Object[]{
                    c.getCihazTuru(),
                    c.getMarka(),
                    c.getModel(),
                    c.getSeriNo(),
                    musteriBilgisi,
                    c.getFiyat(),
                    c.getGarantiBitisTarihi()
            };

            tableModels.get("Tümü").addRow(rowData);

            String tur = c.getCihazTuru();
            if (tableModels.containsKey(tur)) {
                tableModels.get(tur).addRow(rowData);
            }
        }
    }

    // --- DEĞİŞTİRİLEN METOT ---
    // Artık mantık RaporlamaHizmeti'nde.
    private void konsolRaporuOlustur() {
        RaporlamaHizmeti.konsolRaporuOlustur(cihazListesi);
        JOptionPane.showMessageDialog(this, "Rapor konsola yazdırıldı!\n(IDE çıktısını kontrol ediniz.)");
    }
}
//yotummmmm
