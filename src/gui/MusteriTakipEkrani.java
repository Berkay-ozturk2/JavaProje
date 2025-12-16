package gui;

import Cihazlar.Cihaz;
import Servis.ServisKaydi;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    public MusteriTakipEkrani() {
        setTitle("Cihaz Durum Sorgulama");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelArama = new JPanel(new FlowLayout());
        panelArama.add(new JLabel("Cihaz Seri No:"));
        txtSeriNo = new JTextField(15);
        JButton btnSorgula = new JButton("Sorgula");

        panelArama.add(txtSeriNo);
        panelArama.add(btnSorgula);

        txtBilgiEkrani = new JTextArea();
        txtBilgiEkrani.setEditable(false);
        txtBilgiEkrani.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBilgiEkrani.setMargin(new Insets(10, 10, 10, 10));

        btnSorgula.addActionListener(e -> sorgula());

        add(panelArama, BorderLayout.NORTH);
        add(new JScrollPane(txtBilgiEkrani), BorderLayout.CENTER);
    }

    // --- TXT VERİ OKUMA METOTLARI ---
    private List<Cihaz> cihazlariYukleTxt() {
        List<Cihaz> liste = new ArrayList<>();
        File dosya = new File("cihazlar.txt");
        if(dosya.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(dosya))){
                String line;
                while((line = br.readLine()) != null){
                    if(!line.trim().isEmpty()) {
                        Cihaz c = Cihaz.fromTxtFormat(line);
                        if(c != null) liste.add(c);
                    }
                }
            } catch(Exception e){}
        }
        return liste;
    }

    private List<ServisKaydi> servisleriYukleTxt() {
        List<ServisKaydi> liste = new ArrayList<>();
        File dosya = new File("servisler.txt");
        if(dosya.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(dosya))){
                String line;
                while((line = br.readLine()) != null){
                    if(!line.trim().isEmpty()) {
                        ServisKaydi k = ServisKaydi.fromTxtFormat(line);
                        if(k != null) liste.add(k);
                    }
                }
            } catch(Exception e){}
        }
        return liste;
    }

    private void sorgula() {
        String arananSeriNo = txtSeriNo.getText().trim();
        if (arananSeriNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası Giriniz.");
            return;
        }

        txtBilgiEkrani.setText("Sorgulanıyor...\n");
        StringBuilder rapor = new StringBuilder();
        boolean cihazBulundu = false;

        // 1. ADIM: TXT'den Cihazları Yükle ve Ara
        List<Cihaz> cihazlar = cihazlariYukleTxt();
        Cihaz bulunanCihaz = null;

        for (Cihaz c : cihazlar) {
            if (c.getSeriNo().equalsIgnoreCase(arananSeriNo)) {
                bulunanCihaz = c;
                cihazBulundu = true;
                break;
            }
        }

        if (bulunanCihaz != null) {
            rapor.append("=== CİHAZ BİLGİLERİ ===\n");
            rapor.append("Marka/Model: ").append(bulunanCihaz.getMarka()).append(" ").append(bulunanCihaz.getModel()).append("\n");
            rapor.append("Tür: ").append(bulunanCihaz.getCihazTuru()).append("\n");
            boolean garantiVarMi = bulunanCihaz.isGarantiAktif();
            rapor.append("Garanti Durumu: ").append(garantiVarMi ? "DEVAM EDİYOR" : "BİTMİŞ").append("\n");
            rapor.append("Garanti Bitiş: ").append(bulunanCihaz.getGarantiBitisTarihi()).append("\n\n");
        }

        // 2. ADIM: TXT'den Servis Kayıtlarını Yükle ve Ara
        List<ServisKaydi> servisKayitlari = servisleriYukleTxt();
        ServisKaydi bulunanKayit = null;

        for (ServisKaydi k : servisKayitlari) {
            if (k.getCihaz().getSeriNo().equalsIgnoreCase(arananSeriNo)) {
                bulunanKayit = k;
                break;
            }
        }

        if (bulunanKayit != null) {
            rapor.append("=== SERVİS DURUMU ===\n");
            rapor.append("Durum: ").append(bulunanKayit.getDurum()).append("\n");
            rapor.append("Şikayet: ").append(bulunanKayit.getSorunAciklamasi()).append("\n");
            rapor.append("Giriş Tarihi: ").append(bulunanKayit.getGirisTarihi()).append("\n");

            String teknisyen = (bulunanKayit.getAtananTeknisyen() != null) ? bulunanKayit.getAtananTeknisyen().getAd() : "Henüz Atanmadı";
            rapor.append("İlgilenen Uzman: ").append(teknisyen).append("\n");

            if (bulunanKayit.getOdenecekTamirUcreti() > 0) {
                rapor.append("Tahmini Ücret: ").append(bulunanKayit.getOdenecekTamirUcreti()).append(" TL\n");
            } else {
                rapor.append("Ücret: Garanti Kapsamında / Ücretsiz\n");
            }
        } else if (cihazBulundu) {
            rapor.append("=== SERVİS DURUMU ===\n");
            rapor.append("Bu cihaz için aktif bir servis kaydı bulunmamaktadır.\n");
        }

        if (!cihazBulundu) {
            txtBilgiEkrani.setText("HATA: Bu seri numarasına ait bir cihaz bulunamadı.\nLütfen numarayı kontrol ediniz.");
        } else {
            txtBilgiEkrani.setText(rapor.toString());
        }
    }
}