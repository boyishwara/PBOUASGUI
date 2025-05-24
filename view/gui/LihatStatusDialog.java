package view.gui;

import controller.AppController;
import model.Kelulusan;
import javax.swing.*;
import java.awt.*;

public class LihatStatusDialog extends JDialog {
    private AppController controller;

    public LihatStatusDialog(AppController controller, JFrame owner) {
        super(owner, "Status Kelulusan", true);
        this.controller = controller;
        initComponents();
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 baris, 2 kolom
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        Kelulusan status = controller.getStatusKelulusanMahasiswa();
        if (status == null) {
            DialogHelper.showErrorMessage(this, "Tidak dapat memuat status kelulusan.", "Error");
            SwingUtilities.invokeLater(() -> dispose());
            return;
        }

        panel.add(new JLabel("NIM:"));
        panel.add(new JLabel(status.getNimMahasiswa()));

        panel.add(new JLabel("Nama Mahasiswa:"));
        panel.add(new JLabel(controller.getLoggedInMahasiswa().getNamaMahasiswa()));

        panel.add(new JLabel("Status Tahap 1:"));
        JLabel status1Label = new JLabel(status.getStatusTahap1());
        status1Label.setFont(new Font(status1Label.getFont().getName(), Font.BOLD, status1Label.getFont().getSize()));
        panel.add(status1Label);


        panel.add(new JLabel("Status Tahap 2:"));
        JLabel status2Label = new JLabel(status.getStatusTahap2());
        status2Label.setFont(new Font(status2Label.getFont().getName(), Font.BOLD, status2Label.getFont().getSize()));
        panel.add(status2Label);

        panel.add(new JLabel("Hasil Akhir:"));
        JLabel statusAkhirLabel = new JLabel(status.getStatusAkhir());
        statusAkhirLabel.setFont(new Font(statusAkhirLabel.getFont().getName(), Font.BOLD, statusAkhirLabel.getFont().getSize()));
        if ("Diterima".equals(status.getStatusAkhir())) {
            statusAkhirLabel.setForeground(new Color(0, 128, 0)); // Hijau tua
        } else if ("Tidak Diterima".equals(status.getStatusAkhir())) {
            statusAkhirLabel.setForeground(Color.RED);
        }
        panel.add(statusAkhirLabel);
        
        add(panel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
