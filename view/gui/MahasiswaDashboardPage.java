package view.gui;

import controller.AppController;
import java.awt.*;
import javax.swing.*;
import model.Mahasiswa;

public class MahasiswaDashboardPage extends JFrame {
    private AppController controller;
    private Mahasiswa mahasiswa;

    private JButton uploadDokumenButton;
    private JButton ikutiUjianButton;
    private JButton lihatStatusButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;

    public MahasiswaDashboardPage(AppController controller) {
        this.controller = controller;
        this.mahasiswa = controller.getLoggedInMahasiswa();

        if (this.mahasiswa == null) {
            DialogHelper.showErrorMessage(null, "Error: Tidak ada mahasiswa yang login.", "Fatal Error");
            // Kembali ke login atau tutup aplikasi
            new LoginPage(controller).setVisible(true);
            dispose();
            return;
        }

        setTitle("Dashboard Mahasiswa - " + mahasiswa.getNamaMahasiswa());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Gap antar komponen

        welcomeLabel = new JLabel("Selamat Datang, " + mahasiswa.getNamaMahasiswa() + " (NIM: " + mahasiswa.getNim() + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 baris, 1 kolom, gap
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Padding panel

        uploadDokumenButton = new JButton("Upload Dokumen");
        ikutiUjianButton = new JButton("Ikuti Ujian Tahap 1");
        lihatStatusButton = new JButton("Lihat Status Kelulusan");
        logoutButton = new JButton("Logout");

        // Styling tombol (opsional)
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        uploadDokumenButton.setFont(buttonFont);
        ikutiUjianButton.setFont(buttonFont);
        lihatStatusButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);
        logoutButton.setBackground(new Color(255, 100, 100));
        logoutButton.setForeground(Color.RED); // Change from Color.WHITE to Color.RED


        buttonPanel.add(uploadDokumenButton);
        buttonPanel.add(ikutiUjianButton);
        buttonPanel.add(lihatStatusButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        uploadDokumenButton.addActionListener(e -> {
            UploadDokumenDialog udd = new UploadDokumenDialog(controller, this);
            udd.setVisible(true);
        });

        ikutiUjianButton.addActionListener(e -> {
            // Cek dulu apakah bisa ikut ujian
            String statusTahap1 = mahasiswa.getStatusKelulusan().getStatusTahap1();
            if (!"Belum Ujian".equals(statusTahap1)) {
                DialogHelper.showWarningMessage(this, "Anda tidak dapat mengikuti ujian tahap 1 saat ini.\nStatus Anda: " + statusTahap1, "Info Ujian");
                return;
            }
            
            if (controller.getBankSoalTahap1().isEmpty()){
                DialogHelper.showInfoMessage(this, "Belum ada soal ujian yang tersedia. Hubungi koordinator.", "Info Ujian");
                return;
            }

            boolean confirm = DialogHelper.showConfirmDialog(this, 
                "Anda akan memulai Ujian Tahap 1. Pastikan koneksi stabil.\nJumlah soal: " + controller.getBankSoalTahap1().size() + "\nLanjutkan?", 
                "Konfirmasi Ujian Tahap 1");
            if (confirm) {
                KerjakanUjianDialog kud = new KerjakanUjianDialog(controller, this);
                kud.setVisible(true);
            }
        });

        lihatStatusButton.addActionListener(e -> {
            LihatStatusDialog lsd = new LihatStatusDialog(controller, this);
            lsd.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            controller.logout();
            DialogHelper.showInfoMessage(this, "Logout berhasil.", "Logout");
            new LoginPage(controller).setVisible(true);
            dispose();
        });
    }
}
