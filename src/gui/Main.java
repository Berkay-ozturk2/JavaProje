// src/gui/Main.java (HATA DÜZELTME: Cihaz Fiyatı Tabloya Eklendi ve Combobox Sorun Seçimi Entegre Edildi)
package gui;

import Cihazlar.Cihaz;
import Servis.ServisKaydı;
import Servis.ServisYonetimi;
import Servis.Teknisyen;
import java.util.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// CihazEkleListener arayüzünü uygulayarak eklenen cihazı yakalar
public class Main extends JFrame implements CihazEkleListener {

    // YENİ: Garanti dışı tahmini fiyatları içeren sorun listesi
    private static final String[] SERVIS_SORUNLARI = new String[] {
            "Ekran kırık, çatlak veya görüntü yok (500-1500 TL)",
            "Dokunmatik çalışmıyor veya hatalı çalışıyor (400-1200 TL)",
            "Pil/Batarya şarj tutmuyor veya şişme var (300-700 TL)",
            "Şarj soketi temassızlık yapıyor veya bozuk (250-550 TL)",
            "Kasa/Kapak kırık/deforme (200-600 TL)",
            "Hoparlörden ses gelmiyor veya cızırtılı (150-400 TL)",
            "Mikrofon arızası (150-400 TL)",
            "Ön/Arka kamera çalışmıyor (450-900 TL)",
            "Cihaz açılmıyor, sürekli yeniden başlıyor (800-2500 TL)",
            "Cihaz sıvı teması aldı (300-3000 TL)",
            "Diğer/Spesifik bir arıza (100-1000 TL)"
    };

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

    // Combobox seçeneğinden maksimum fiyatı çeker
    // Örneğin: "Ekran kırık... (500-1500 TL)" -> 1500.0
    private static double getMaxFiyatFromSorun(String sorunAciklamasi) {
        // Parantez içindeki sayı aralığını bulmak için regex: (\d+) ilk sayı, (\d+) ikinci sayı
        Pattern pattern = Pattern.compile("\\((\\d+)-(\\d+)\\s+TL\\)");
        Matcher matcher = pattern.matcher(sorunAciklamasi);

        if (matcher.find()) {
            try {
                // İkinci yakalanan grup (maksimum fiyat)
                return Double.parseDouble(matcher.group(2));
            } catch (NumberFormatException e) {
                // Sayısal dönüşüm hatası olursa 0.0 döndür
                return 0.0;
            }
        }
        return 0.0; // Fiyat aralığı bulunamazsa 0.0 döndür
    }

    // ===================================
    // GUI Oluşturma Metodu
    // ===================================
    private void initUI() {

        // ---------------- TABLE MODEL ----------------
        tableModel = new DefaultTableModel(
                // Tablo sütun başlıkları
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

        // 2. Servis Kaydı Oluşturma (GÜNCELLENDİ: Combobox Kullanımı ve Fiyat Ayarlama)
        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);

                // Garanti Durumunu Kontrol Et
                boolean garantiAktifMi = selectedCihaz.isGarantiAktif();
                String garantiDurumu = garantiAktifMi ? "Aktif (Servis Ücretsiz)" : "Sona Ermiş (Tahmini Ücretli)";

                // Sorun Seçimi için Combobox Oluştur
                JComboBox<String> sorunComboBox = new JComboBox<>(SERVIS_SORUNLARI);
                sorunComboBox.setSelectedIndex(-1); // Varsayılan boş seçim

                // Görüntülenecek mesajı hazırla
                String mesaj = String.format("%s için sorun seçin:\nGaranti Durumu: %s",
                        selectedCihaz.toString(), garantiDurumu);

                // JOptionPane'ı Combobox ile göster
                int option = JOptionPane.showConfirmDialog(this, sorunComboBox, mesaj, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                String secilenSorun = null;
                double tahminiUcret = 0.0;

                if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() != null) {
                    secilenSorun = (String) sorunComboBox.getSelectedItem();

                    if (garantiAktifMi) {
                        // Garanti aktifse ücret 0.0 TL
                        tahminiUcret = 0.0;
                    } else {
                        // Garanti aktif değilse Combobox'tan maksimum fiyatı çek
                        tahminiUcret = getMaxFiyatFromSorun(secilenSorun);

                        // Combobox metnini sadece sorun açıklaması olacak şekilde temizle (Örn: "Ekran kırık, çatlak veya görüntü yok")
                        secilenSorun = secilenSorun.replaceAll("\\s*\\([^)]+\\)", "").trim();
                    }


                    ServisKaydı yeniKayit = new ServisKaydı(selectedCihaz, secilenSorun); // Seçilen sorunu kullan

                    // Yeni hesaplanan tahmini ücreti kayda ata
                    yeniKayit.setTahminiTamirUcreti(tahminiUcret);

                    // YENİ: Rastgele teknisyen ata
                    Teknisyen atananTeknisyen = rastgeleTeknisyenSec();
                    yeniKayit.setAtananTeknisyen(atananTeknisyen);

                    servisYonetimi.servisKaydiEkle(yeniKayit);
                    JOptionPane.showMessageDialog(this, String.format("Servis kaydı başarıyla oluşturuldu.\nAtanan Teknisyen: %s\nÖdenecek Tahmini Ücret: %.2f TL", atananTeknisyen.getAd(), yeniKayit.getOdenecekTamirUcreti()));
                } else if (option == JOptionPane.OK_OPTION && sorunComboBox.getSelectedItem() == null) {
                    // Kullanıcı Combobox'tan hiçbir şey seçmeden OK'e basarsa
                    JOptionPane.showMessageDialog(this, "Lütfen bir sorun seçimi yapın.", "Hata", JOptionPane.ERROR_MESSAGE);
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
                    c.getFiyat(), // HATA DÜZELTME: getFiyat() artık mevcut ve kullanılıyor.
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