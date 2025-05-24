package view.gui;

import controller.AppController;
import java.awt.*;
import javax.swing.*;
import model.Mahasiswa;

public class KoordinatorDashboardPage extends JFrame {
    private AppController controller;

    private JButton tambahSoalButton;
    private JButton lihatMahasiswaButton;
    private JButton prosesHasilTahap1Button;
    private JButton inputHasilTahap2Button;
    private JButton umumkanHasilAkhirButton;
    private JButton lihatLaporanButton;
    private JButton logoutButton;

    public KoordinatorDashboardPage(AppController controller) {
        this.controller = controller;

        if (!controller.isKoordinatorLoggedIn()) {
             DialogHelper.showErrorMessage(null, "Error: Tidak ada koordinator yang login.", "Fatal Error");
            // Kembali ke login atau tutup aplikasi
            new LoginPage(controller).setVisible(true);
            dispose();
            return;
        }

        setTitle("Dashboard Koordinator");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Selamat Datang, Koordinator!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Grid dinamis, 2 kolom
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tambahSoalButton = new JButton("Tambah Soal Ujian Tahap 1");
        lihatMahasiswaButton = new JButton("Lihat Daftar Mahasiswa");
        prosesHasilTahap1Button = new JButton("Proses Hasil Ujian Tahap 1");
        inputHasilTahap2Button = new JButton("Input Hasil Ujian Tahap 2");
        umumkanHasilAkhirButton = new JButton("Umumkan Hasil Akhir");
        lihatLaporanButton = new JButton("Lihat Laporan untuk Dekan");
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 100, 100));
        logoutButton.setForeground(Color.RED); // Change from Color.WHITE to Color.RED
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        tambahSoalButton.setFont(buttonFont);
        lihatMahasiswaButton.setFont(buttonFont);
        prosesHasilTahap1Button.setFont(buttonFont);
        inputHasilTahap2Button.setFont(buttonFont);
        umumkanHasilAkhirButton.setFont(buttonFont);
        lihatLaporanButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);
        logoutButton.setBackground(new Color(255, 100, 100));
        logoutButton.setForeground(Color.RED); // Change from Color.WHITE to Color.RED

        buttonPanel.add(tambahSoalButton);
        buttonPanel.add(lihatMahasiswaButton);
        buttonPanel.add(prosesHasilTahap1Button);
        buttonPanel.add(inputHasilTahap2Button);
        buttonPanel.add(umumkanHasilAkhirButton);
        buttonPanel.add(lihatLaporanButton);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        tambahSoalButton.addActionListener(e -> {
            BuatSoalDialog bsd = new BuatSoalDialog(controller, this);
            bsd.setVisible(true);
        });

        lihatMahasiswaButton.addActionListener(e -> {
            LihatMahasiswaDialog lmd = new LihatMahasiswaDialog(controller, this);
            lmd.setVisible(true);
        });

        prosesHasilTahap1Button.addActionListener(e -> {
            boolean confirm = DialogHelper.showConfirmDialog(this, "Anda yakin ingin memproses hasil ujian tahap 1 semua mahasiswa yang menunggu?", "Konfirmasi");
            if (confirm) {
                String log = controller.prosesHasilUjianTahap1();
                JTextArea textArea = new JTextArea(log, 15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                DialogHelper.showInfoMessage(this, scrollPane, "Log Proses Hasil Tahap 1");
            }
        });

        inputHasilTahap2Button.addActionListener(e -> {
            String nim = DialogHelper.showInputDialog(this, "Masukkan NIM Mahasiswa:", "Input Hasil Tahap 2");
            if (nim != null && !nim.trim().isEmpty()) {
                Mahasiswa mhs = controller.findMahasiswaByNim(nim.trim());
                if (mhs == null) {
                    DialogHelper.showErrorMessage(this, "Mahasiswa dengan NIM " + nim + " tidak ditemukan.", "Error");
                    return;
                }
                if (!"Lulus".equals(mhs.getStatusKelulusan().getStatusTahap1())) {
                     DialogHelper.showWarningMessage(this, "Mahasiswa NIM " + nim + " belum Lulus Tahap 1.", "Info");
                    return;
                }
                
                String[] options = {"Lulus", "Tidak Lulus"};
                String hasil = (String) JOptionPane.showInputDialog(this, 
                    "Pilih hasil wawancara untuk NIM " + nim + " (" + mhs.getNamaMahasiswa() + "):",
                    "Input Hasil Wawancara", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (hasil != null) {
                    String message = controller.inputHasilUjianTahap2(nim.trim(), hasil);
                    DialogHelper.showInfoMessage(this, message, "Info Hasil Tahap 2");
                }
            } else if (nim != null) { // nim is not null but empty after trim
                 DialogHelper.showWarningMessage(this, "NIM tidak boleh kosong.", "Input Error");
            }
        });

        umumkanHasilAkhirButton.addActionListener(e -> {
             boolean confirm = DialogHelper.showConfirmDialog(this, "Anda yakin ingin mengumumkan hasil akhir kelulusan?", "Konfirmasi");
            if (confirm) {
                String log = controller.umumkanHasilAkhir();
                JTextArea textArea = new JTextArea(log, 15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                DialogHelper.showInfoMessage(this, scrollPane, "Log Pengumuman Hasil Akhir");
            }
        });

        lihatLaporanButton.addActionListener(e -> {
            LihatLaporanDialog lld = new LihatLaporanDialog(controller, this);
            lld.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            controller.logout();
            DialogHelper.showInfoMessage(this, "Logout berhasil.", "Logout");
            new LoginPage(controller).setVisible(true);
            dispose();
        });
    }
}
