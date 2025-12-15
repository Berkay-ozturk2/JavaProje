// ===== B KRİTERLERİNE UYGUN, SWING GUI MAIN SINIFI =====

package gui;

import Cihazlar.*;
import Servis.ServisKaydı;
import Servis.ServisYonetimi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*; // Dosya işlemleri için
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    // Cihaz listesi ve yönetimi için gerekli değişkenler
    private List<Cihaz> cihazListesi = new ArrayList<>();
    private static final String CİHAZ_DOSYA_ADI = "cihaz_listesi.ser";

    // Servis yönetimi nesnesi
    private ServisYonetimi servisYonetimi;

    public Main() {
        setTitle("Dijital Cihaz Garanti & Servis Sistemi");
        setSize(1000, 500); // Pencere boyutunu büyütelim
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 1. Başlangıçta cihaz ve servis kayıtlarını yükle
        cihazListesi = cihazYukle();
        servisYonetimi = new ServisYonetimi(); // Bu constructor içinde otomatik yükleme yapılır

        initUI();
        cihazListesiniTabloyaDoldur(cihazListesi);
    }

    // Cihaz listesini dosyadan yükleme metodu
    private List<Cihaz> cihazYukle() {
        File dosya = new File(CİHAZ_DOSYA_ADI);
        if (dosya.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dosya))) {
                return (List<Cihaz>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Cihaz listesi yüklenirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
        return new ArrayList<>();
    }

    // Cihaz listesini dosyaya kaydetme metodu
    private void cihazKaydet(List<Cihaz> liste) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CİHAZ_DOSYA_ADI))) {
            oos.writeObject(liste);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cihaz listesi kaydedilemedi: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

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
        JButton btnTelefon = new JButton("Telefon Ekle");
        JButton btnTablet = new JButton("Tablet Ekle");
        JButton btnLaptop = new JButton("Laptop Ekle");
        JButton btnServisKaydi = new JButton("Servis Kaydı Oluştur"); // YENİ: Servis Kaydı butonu
        JButton btnServisListele = new JButton("Servis Kayıtlarını Göster"); // YENİ: Servis Listeleme butonu
        JButton btnSil = new JButton("Seçili Cihazı Sil");

        // Cihaz Ekleme Butonları (Kaydetme çağrısı eklendi)
        btnTelefon.addActionListener(e -> {
            Telefon t = new Telefon(
                    "T" + (cihazListesi.size() + 1), "Samsung", "Galaxy S", 25000, LocalDate.now(), true
            );
            cihazEkle(t);
        });

        btnTablet.addActionListener(e -> {
            Tablet tb = new Tablet(
                    "TB" + (cihazListesi.size() + 1), "Apple", "iPad", 32000, LocalDate.now(), true
            );
            cihazEkle(tb);
        });

        btnLaptop.addActionListener(e -> {
            Laptop l = new Laptop(
                    "L" + (cihazListesi.size() + 1), "Dell", "XPS", 45000, LocalDate.now(), true
            );
            cihazEkle(l);
        });

        // Seçili Cihazı Silme (Kaydetme çağrısı eklendi)
        btnSil.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                cihazListesi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                cihazKaydet(cihazListesi); // Silme sonrası kaydet
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek cihazı seçin");
            }
        });

        // YENİ: Servis Kaydı Oluşturma İşlemi
        btnServisKaydi.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Cihaz selectedCihaz = cihazListesi.get(selectedRow);

                // Kullanıcıdan sorun açıklamasını al
                String sorun = JOptionPane.showInputDialog(this,
                        selectedCihaz.toString() + " için sorun açıklamasını girin:",
                        "Servis Kaydı Oluştur", JOptionPane.PLAIN_MESSAGE);

                if (sorun != null && !sorun.trim().isEmpty()) {
                    ServisKaydı yeniKayit = new ServisKaydı(selectedCihaz, sorun.trim());
                    servisYonetimi.servisKaydiEkle(yeniKayit); // Ekle ve Kaydet
                    JOptionPane.showMessageDialog(this, "Servis kaydı başarıyla oluşturuldu ve dosyaya kaydedildi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen servis kaydı oluşturulacak cihazı seçin");
            }
        });

        // YENİ: Servis Kayıtlarını Listeleme İşlemi
        btnServisListele.addActionListener(e -> {
            // Servis kayıtlarını yeni bir pencerede listele
            ServisKayitlariniGoster(servisYonetimi.getKayitlar());
        });


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnTelefon);
        buttonPanel.add(btnTablet);
        buttonPanel.add(btnLaptop);
        buttonPanel.add(btnServisKaydi);
        buttonPanel.add(btnServisListele);
        buttonPanel.add(btnSil);

        // ---------------- LAYOUT ----------------
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cihazEkle(Cihaz c) {
        cihazListesi.add(c);
        cihazListesiniTabloyaDoldur(cihazListesi); // Tabloyu yeniden doldur
        cihazKaydet(cihazListesi); // Ekleme sonrası kaydet
    }

    // Cihaz listesini tabloya doldurma metodu
    private void cihazListesiniTabloyaDoldur(List<Cihaz> liste) {
        tableModel.setRowCount(0); // Tabloyu temizle
        for (Cihaz c : liste) {
            tableModel.addRow(new Object[]{
                    c.getCihazTuru(),
                    c.getMarka(),
                    c.getModel(),
                    c.getSeriNo(),
                    c.getFiyat(),
                    c.getGarantiBitisTarihi() // Garanti bitiş tarihini de göster
            });
        }
    }

    // Servis kayıtlarını ayrı bir pencerede gösteren basit bir metot
    private void ServisKayitlariniGoster(List<ServisKaydı> kayitlar) {
        JFrame servisFrame = new JFrame("Tüm Servis Kayıtları");
        servisFrame.setSize(800, 300);

        if (kayitlar.isEmpty()) {
            servisFrame.add(new JLabel("Henüz oluşturulmuş bir servis kaydı bulunmamaktadır."), BorderLayout.CENTER);
        } else {
            DefaultTableModel servisTableModel = new DefaultTableModel(
                    new Object[]{"Seri No", "Cihaz", "Sorun", "Giriş Tarihi", "Durum"}, 0
            );
            JTable servisTable = new JTable(servisTableModel);

            for (ServisKaydı sk : kayitlar) {
                servisTableModel.addRow(new Object[]{
                        sk.getCihaz().getSeriNo(),
                        sk.getCihaz().getMarka() + " " + sk.getCihaz().getModel(),
                        sk.getSorunAciklamasi(),
                        sk.getGirisTarihi(),
                        sk.getDurum()
                });
            }
            servisFrame.add(new JScrollPane(servisTable), BorderLayout.CENTER);
        }

        servisFrame.setLocationRelativeTo(this);
        servisFrame.setVisible(true);
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}