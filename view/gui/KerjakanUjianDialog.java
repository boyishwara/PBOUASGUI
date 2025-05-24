package view.gui;

import controller.AppController;
import model.Soal;
import model.Ujian;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class KerjakanUjianDialog extends JDialog {
    private AppController controller;
    private List<Soal> soalUjian;
    private Ujian ujian;
    private int currentSoalIndex;

    private JLabel nomorSoalLabel;
    private JTextArea pertanyaanArea;
    private JRadioButton opsiAButton, opsiBButton, opsiCButton, opsiDButton;
    private ButtonGroup opsiGroup;
    private JButton nextButton, submitButton; // Next/Previous, Submit Ujian

    public KerjakanUjianDialog(AppController controller, JFrame owner) {
        super(owner, "Ujian Tahap 1", true);
        this.controller = controller;
        
        // Ambil soal dan acak urutannya
        List<Soal> bankSoal = new ArrayList<>(controller.getBankSoalTahap1()); // Salin list agar tidak mengubah list asli
        Collections.shuffle(bankSoal); // Acak urutan soal
        this.soalUjian = bankSoal;

        this.ujian = controller.mulaiUjianTahap1();

        if (this.ujian == null || this.soalUjian.isEmpty()) {
            // Pesan error sudah ditampilkan oleh controller atau kondisi tidak terpenuhi
            // DialogHelper.showErrorMessage(owner, "Tidak dapat memulai ujian.", "Error Ujian");
            SwingUtilities.invokeLater(() -> dispose()); // Tutup dialog jika tidak bisa mulai
            return;
        }

        this.currentSoalIndex = 0;
        initComponents();
        displaySoal();
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Mencegah penutupan paksa
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (DialogHelper.showConfirmDialog(KerjakanUjianDialog.this, 
                    "Apakah Anda yakin ingin keluar dari ujian? Jawaban yang belum disubmit akan hilang.", "Konfirmasi Keluar Ujian")) {
                    dispose();
                }
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nomorSoalLabel = new JLabel("Soal Nomor: 1/" + soalUjian.size());
        nomorSoalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(nomorSoalLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel soalPanel = new JPanel();
        soalPanel.setLayout(new BoxLayout(soalPanel, BoxLayout.Y_AXIS));
        soalPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        pertanyaanArea = new JTextArea(5, 40);
        pertanyaanArea.setLineWrap(true);
        pertanyaanArea.setWrapStyleWord(true);
        pertanyaanArea.setEditable(false);
        pertanyaanArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(pertanyaanArea);
        soalPanel.add(scrollPane);
        soalPanel.add(Box.createRigidArea(new Dimension(0,10))); // Spasi

        opsiAButton = new JRadioButton("Opsi A");
        opsiBButton = new JRadioButton("Opsi B");
        opsiCButton = new JRadioButton("Opsi C");
        opsiDButton = new JRadioButton("Opsi D");
        
        Font opsiFont = new Font("Arial", Font.PLAIN, 14);
        opsiAButton.setFont(opsiFont);
        opsiBButton.setFont(opsiFont);
        opsiCButton.setFont(opsiFont);
        opsiDButton.setFont(opsiFont);

        opsiGroup = new ButtonGroup();
        opsiGroup.add(opsiAButton);
        opsiGroup.add(opsiBButton);
        opsiGroup.add(opsiCButton);
        opsiGroup.add(opsiDButton);

        soalPanel.add(opsiAButton);
        soalPanel.add(opsiBButton);
        soalPanel.add(opsiCButton);
        soalPanel.add(opsiDButton);

        add(soalPanel, BorderLayout.CENTER);

        JPanel buttonNavPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButton = new JButton("Soal Berikutnya");
        submitButton = new JButton("Submit Ujian");
        submitButton.setVisible(false); // Tampil hanya di soal terakhir

        buttonNavPanel.add(nextButton);
        buttonNavPanel.add(submitButton);
        add(buttonNavPanel, BorderLayout.SOUTH);

        // Action Listeners
        nextButton.addActionListener(e -> simpanJawabanDanLanjut());
        submitButton.addActionListener(e -> submitSeluruhUjian());
    }

    private void displaySoal() {
        if (currentSoalIndex < soalUjian.size()) {
            Soal soal = soalUjian.get(currentSoalIndex);
            nomorSoalLabel.setText("Soal Nomor: " + (currentSoalIndex + 1) + "/" + soalUjian.size());
            pertanyaanArea.setText(soal.getPertanyaan());
            String[] opsi = soal.getOpsiJawaban();
            opsiAButton.setText("A. " + opsi[0]);
            opsiBButton.setText("B. " + opsi[1]);
            opsiCButton.setText("C. " + opsi[2]);
            opsiDButton.setText("D. " + opsi[3]);
            opsiGroup.clearSelection(); // Bersihkan pilihan sebelumnya

            // Cek apakah soal ini sudah pernah dijawab
            Integer jawabanTersimpan = ujian.getJawabanMahasiswa().get(soal.getIdSoal());
            if (jawabanTersimpan != null) {
                if (jawabanTersimpan == 0) opsiAButton.setSelected(true);
                else if (jawabanTersimpan == 1) opsiBButton.setSelected(true);
                else if (jawabanTersimpan == 2) opsiCButton.setSelected(true);
                else if (jawabanTersimpan == 3) opsiDButton.setSelected(true);
            }


            if (currentSoalIndex == soalUjian.size() - 1) {
                nextButton.setVisible(false);
                submitButton.setVisible(true);
            } else {
                nextButton.setVisible(true);
                submitButton.setVisible(false);
            }
        }
    }

    private int getSelectedOpsiIndex() {
        if (opsiAButton.isSelected()) return 0;
        if (opsiBButton.isSelected()) return 1;
        if (opsiCButton.isSelected()) return 2;
        if (opsiDButton.isSelected()) return 3;
        return -1; // Tidak ada yang dipilih
    }

    private void simpanJawabanDanLanjut() {
        int jawabanIndex = getSelectedOpsiIndex();
        if (jawabanIndex == -1) {
            DialogHelper.showWarningMessage(this, "Anda harus memilih satu jawaban.", "Peringatan");
            return;
        }
        Soal soalSaatIni = soalUjian.get(currentSoalIndex);
        ujian.tambahJawaban(soalSaatIni.getIdSoal(), jawabanIndex);

        currentSoalIndex++;
        if (currentSoalIndex < soalUjian.size()) {
            displaySoal();
        } else {
            // Harusnya tidak sampai sini jika tombol next disembunyikan
            submitSeluruhUjian();
        }
    }

    private void submitSeluruhUjian() {
        // Pastikan soal terakhir juga tersimpan jawabannya jika belum
        if (currentSoalIndex == soalUjian.size() -1) { // Jika sedang di soal terakhir
             int jawabanIndex = getSelectedOpsiIndex();
             if (jawabanIndex == -1) {
                DialogHelper.showWarningMessage(this, "Anda harus memilih satu jawaban untuk soal terakhir.", "Peringatan");
                return;
            }
            Soal soalSaatIni = soalUjian.get(currentSoalIndex);
            ujian.tambahJawaban(soalSaatIni.getIdSoal(), jawabanIndex);
        }


        boolean confirm = DialogHelper.showConfirmDialog(this, "Apakah Anda yakin ingin mengirim semua jawaban dan menyelesaikan ujian?", "Konfirmasi Submit Ujian");
        if (confirm) {
            controller.submitUjianTahap1(ujian);
            DialogHelper.showInfoMessage(this, "Ujian Tahap 1 telah disubmit. Hasil akan diumumkan oleh Koordinator.", "Ujian Selesai");
            dispose();
        }
    }
}
