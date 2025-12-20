package gui;

import Araclar.RaporKutusu;      // YENİ EKLENDİ
import Araclar.TarihYardimcisi;  // YENİ EKLENDİ
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
import java.time.LocalDate;
import java.time.LocalDateTime; // YENİ EKLENDİ
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame implements CihazEkleListener {

    // Tek bir tablo yerine artık sekmelerimiz ve çoklu tablolarımız var
    private JTabbedPane tabbedPane;
    private Map<String, DefaultTableModel> tableModels = new HashMap<>();
    private Map<String, JTable> tables = new HashMap<>();

    private List<Cihaz> cihazListesi = new ArrayList<>();

    private static final String CIHAZ_DOSYA_ADI = System.getProperty("user.dir") +
            System.getProperty("file.separator") +
            "cihazlar.txt";

    private ServisYonetimi servisYonetimi;
    private final List<Teknisyen> teknisyenler = TeknisyenDeposu.getTumTeknisyenler();

    public Main() {
        setTitle("Teknolojik Cihaz Garanti & Servis Takip Sistemi");
        setSize(1250, 700); // Biraz genişletildi
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Verileri yükle
        cihazListesi = Cihaz.verileriYukle(CIHAZ_DOSYA_ADI);
        servisYonetimi = new ServisYonetimi(cihazListesi);

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);

        // Başlangıçta konsola bir generic test mesajı atalım (Opsiyonel)
        System.out.println("Sistem başlatıldı. Cihaz sayısı: " + cihazListesi.size());
    }

    @Override
    public void cihazEklendi(Cihaz cihaz) {
        cihazListesi.add(cihaz);
        cihazListesiniTabloyaDoldur(cihazListesi);
        cihazKaydet(cihazListesi);
    }

    private void initUI() {
        // --- SEKME YAPISI (TABBED PANE) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Kategoriler için sekmeleri oluştur
        createTab("Tümü");
        createTab("Telefon");
        createTab("Tablet");
        createTab("Laptop");

        // --- BUTON YAPILANDIRMASI ---
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        JPanel panelAltYonetim = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

        // 1. Yeni Cihaz Ekle
        JButton btnCihazEkle = createStyledButton("Yeni Cihaz Ekle", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnCihazEkle.addActionListener(e -> {
            CihazKayitDialog dialog = new CihazKayitDialog(this, this);
            dialog.setVisible(true);
        });
        panelAltYonetim.add(btnCihazEkle);

        // 2. Servis Kaydı Oluştur
        JButton btnServisKaydi = createStyledButton("Servis Kaydı Oluştur", new Color(52, 152, 219), Color.WHITE, btnFont);
        btnServisKaydi.addActionListener(e -> servisKaydiOlusturIslemi());
        panelAltYonetim.add(btnServisKaydi);

        // 3. Garanti Paketleri
        JButton btnGarantiUzat = createStyledButton("Garanti Paketleri (Uzat)", new Color(93, 138, 103), Color.WHITE, btnFont);
        btnGarantiUzat.addActionListener(e -> garantiUzatmaIslemi());
        panelAltYonetim.add(btnGarantiUzat);

        // 4. Servis Takip Ekranı
        JButton btnServisListele = createStyledButton("Servis Takip Ekranı", new Color(74, 101, 114), Color.WHITE, btnFont);
        btnServisListele.addActionListener(e -> new ServisTakipFrame(servisYonetimi).setVisible(true));
        panelAltYonetim.add(btnServisListele);

        // 5. Seçili Cihazı Sil
        JButton btnSil = createStyledButton("Seçili Cihazı Sil", new Color(169, 50, 38), Color.WHITE, btnFont);
        btnSil.addActionListener(e -> cihazSilIslemi());
        panelAltYonetim.add(btnSil);

        // 6. Konsol Raporu Al (YENİ EKLENEN - Generic/Wildcard Gösterimi İçin)
        JButton btnRaporla = createStyledButton("Konsol Raporu Al", new Color(36, 89, 69), Color.WHITE, btnFont);
        btnRaporla.addActionListener(e -> konsolRaporuOlustur());
        panelAltYonetim.add(btnRaporla);

        // 7. Geri Dön
        JButton btnGeriDon = createStyledButton("Geri Dön", new Color(146, 43, 33), Color.WHITE, btnFont);
        btnGeriDon.addActionListener(e -> {
            new GirisEkrani().setVisible(true);
            this.dispose();
        });
        panelAltYonetim.add(btnGeriDon);

        // --- YERLEŞİM ---
        add(tabbedPane, BorderLayout.CENTER);
        add(panelAltYonetim, BorderLayout.SOUTH);
    }

    // Her kategori için ayrı bir tablo ve model oluşturan yardımcı metot
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

        // Listelere kaydet
        tableModels.put(title, model);
        tables.put(title, table);

        JScrollPane scrollPane = new JScrollPane(table);
        tabbedPane.addTab(title, scrollPane);
    }

    // O an hangi sekme açıksa, oradaki seçili cihazı bulan yardımcı metot
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

        JComboBox<String> sorunComboBox = new JComboBox<>(FiyatlandirmaHizmeti.getSorunListesi());
        String mesaj = String.format("Cihaz: %s\nGaranti: %s (%s)\n\nSorunu Seçin: ",
                selectedCihaz.getModel(), garantiDurumu, selectedCihaz.getGaranti().garantiTuru());

        int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
            String secilenSorun = (String) sorunComboBox.getSelectedItem();

            double hamUcret = FiyatlandirmaHizmeti.tamirUcretiHesapla(secilenSorun, selectedCihaz.getFiyat());
            double musteriOdeyecek = selectedCihaz.getGaranti().sonMaliyetHesapla(hamUcret);
            String temizSorunAdi = secilenSorun.split("\\(")[0].trim();

            // --- YENİ EKLENEN: TARİH YARDIMCISI KULLANIMI (Gereksinim: Madde 6) ---
            // Tüm arızalar için standart 20 iş günü (Yaklaşık 1 takvim ayı sürer)
            // Bu sayede araya giren hafta sonlarının atlandığı net şekilde görülebilir.
            int tahminiIsGunu = 20;
            LocalDate tahminiTeslim = TarihYardimcisi.isGunuEkle(LocalDate.now(), tahminiIsGunu);

            // Konsola bilgi vererek hocaya bu sınıfın çalıştığını kanıtlıyoruz
            System.out.println("[TarihYardimcisi] 20 İş günü sonrası hesaplanıyor...");
            System.out.println("Tahmini teslim tarihi: " + tahminiTeslim);
            // ---------------------------------------------------------------------

            ServisKaydi yeniKayit = new ServisKaydi(selectedCihaz, temizSorunAdi);
            yeniKayit.setTahminiTamirUcreti(musteriOdeyecek);

            Teknisyen atananTeknisyen = teknisyenSec(selectedCihaz.getCihazTuru());
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
        double fiyat6Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 6);
        double fiyat12Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 12);
        double fiyat24Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 24);
        double fiyat36Ay = UzatilmisGaranti.paketFiyatiHesapla(cihazFiyati, 36);

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
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(CIHAZ_DOSYA_ADI), "UTF-8"))) {
            for (Cihaz c : liste) {
                bw.write(c.toTxtFormat());
                bw.newLine();
            }
        }
        // --- YENİ EKLENEN: ÇOKLU CATCH YAPISI (Gereksinim: Madde 7 - En az 2 yerde gerekli) ---
        // Birincisi CihazKayitDialog'da idi, ikincisi burada.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Dosya bulunamadı: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Kaydetme Hatası: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Beklenmeyen hata: " + e.getMessage());
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
        // Önce listeyi fiyata göre sırala
        liste.sort((c1, c2) -> Double.compare(c2.getFiyat(), c1.getFiyat()));

        // Tüm sekmelerin modellerini temizle
        for (DefaultTableModel model : tableModels.values()) {
            model.setRowCount(0);
        }

        // Listeyi dön ve uygun sekmelere ekle
        for (Cihaz c : liste) {
            Object[] rowData = new Object[]{
                    c.getCihazTuru(),
                    c.getMarka(),
                    c.getModel(),
                    c.getSeriNo(),
                    c.getSahip().toString().toUpperCase(),
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

    // --- YENİ EKLENEN: KONSOL RAPOR METODU (Gereksinim: Madde 5 - Generic/Wildcard) ---
    private void konsolRaporuOlustur() {
        System.out.println("\n========== SİSTEM RAPORU BAŞLATILIYOR ==========");

        // 1. GENERIC SINIF KULLANIMI (RaporKutusu<Cihaz>)
        // Cihaz listesini generic sınıfa veriyoruz.
        RaporKutusu<Cihaz> cihazRaporKutusu = new RaporKutusu<>(cihazListesi);

        System.out.println("\n[1] CİHAZ LİSTESİ DÖKÜMÜ:");
        cihazRaporKutusu.listeyiKonsolaYazdir();

        // 2. GENERIC METOT KULLANIMI (<E>)
        // Herhangi bir nesneyi (String, Integer vs.) yazdırabilir.
        System.out.println("\n[2] SİSTEM MESAJI:");
        cihazRaporKutusu.tekElemanYazdir("Raporlama işlemi başarıyla başlatıldı.");
        cihazRaporKutusu.tekElemanYazdir(LocalDateTime.now()); // Tarih nesnesi de gönderebiliriz

        // 3. WILDCARD KULLANIMI (List<? extends Number>)
        // Cihaz fiyatlarını sayısal bir liste olarak toplayıp wildcard metoda gönderiyoruz.
        List<Double> fiyatListesi = new ArrayList<>();
        for (Cihaz c : cihazListesi) {
            fiyatListesi.add(c.getFiyat());
        }

        // Servis ücretleri veya süreleri (Farklı sayısal türde örnek)
        List<Integer> servisSureleri = new ArrayList<>();
        servisSureleri.add(3);
        servisSureleri.add(5);
        servisSureleri.add(14);

        System.out.println("\n[3] FİYAT VE İSTATİSTİK ANALİZİ (Wildcard):");
        cihazRaporKutusu.wildcardTest(fiyatListesi);   // Double listesi kabul eder
        cihazRaporKutusu.wildcardTest(servisSureleri); // Integer listesi kabul eder (? extends Number sayesinde)

        JOptionPane.showMessageDialog(this, "Rapor konsola yazdırıldı!\n(IDE çıktısını kontrol ediniz.)");
    }
}