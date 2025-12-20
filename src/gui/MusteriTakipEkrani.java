package gui;

import Servis.RaporlamaHizmeti;
import Istisnalar.KayitBulunamadiException;

import javax.swing.*;
import java.awt.*;

public class MusteriTakipEkrani extends JFrame {

    private JTextField txtSeriNo;
    private JTextArea txtBilgiEkrani;

    public MusteriTakipEkrani() {
        setTitle("Cihaz Durum Sorgulama");
        setSize(500, 450);
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

        // ActionListener sadece UI işlemini tetikler, mantığı bilmez
        btnSorgula.addActionListener(e -> sorgulaIslemi());
        txtSeriNo.addActionListener(e -> sorgulaIslemi());

        add(panelArama, BorderLayout.NORTH);
        add(new JScrollPane(txtBilgiEkrani), BorderLayout.CENTER);
    }

    private void sorgulaIslemi() {
        String arananSeriNo = txtSeriNo.getText().trim();
        if (arananSeriNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen Seri Numarası Giriniz.");
            return;
        }

        txtBilgiEkrani.setText("Sorgulanıyor...");

        try {
            // ARTIK İŞ MANTIĞI GUI İÇİNDE DEĞİL, SERVİS SINIFINDA:
            String rapor = RaporlamaHizmeti.musteriCihazDurumRaporuOlustur(arananSeriNo);
            txtBilgiEkrani.setText(rapor);
        } catch (KayitBulunamadiException ex) {
            txtBilgiEkrani.setText(ex.getMessage());
        } catch (Exception ex) {
            txtBilgiEkrani.setText("Beklenmeyen bir hata oluştu: " + ex.getMessage());
        }
    }
}