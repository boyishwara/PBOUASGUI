package view.gui;

import controller.AppController;
import model.Soal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuatSoalDialog extends JDialog {
    private AppController controller;

    private JTextArea pertanyaanArea;
    private JTextField opsiAField, opsiBField, opsiCField, opsiDField;
    private JComboBox<String> kunciJawabanComboBox;
    private JButton simpanButton, batalButton;

    public BuatSoalDialog(AppController controller, JFrame owner) {
        super(owner, "Buat Soal Ujian Tahap 1", true);
        this.controller = controller;
        initComponents();
        setSize(500, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pertanyaan
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Pertanyaan:"), gbc);
        pertanyaanArea = new JTextArea(5, 30);
        pertanyaanArea.setLineWrap(true);
        pertanyaanArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(pertanyaanArea);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.weighty = 1.0; // Allow resize
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        gbc.weightx = 0; gbc.weighty = 0; // Reset resize
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        // Opsi Jawaban
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Opsi A:"), gbc);
        opsiAField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 1; add(opsiAField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Opsi B:"), gbc);
        opsiBField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 2; add(opsiBField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Opsi C:"), gbc);
        opsiCField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 3; add(opsiCField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Opsi D:"), gbc);
        opsiDField = new JTextField(25);
        gbc.gridx = 1; gbc.gridy = 4; add(opsiDField, gbc);

        // Kunci Jawaban
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Kunci Jawaban:"), gbc);
        kunciJawabanComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        gbc.gridx = 1; gbc.gridy = 5; add(kunciJawabanComboBox, gbc);

        // Tombol
        simpanButton = new JButton("Simpan Soal");
        batalButton = new JButton("Batal");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(simpanButton);
        buttonPanel.add(batalButton);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Action Listeners
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pertanyaan = pertanyaanArea.getText();
                String[] opsi = {
                    opsiAField.getText(),
                    opsiBField.getText(),
                    opsiCField.getText(),
                    opsiDField.getText()
                };
                int kunciIndex = kunciJawabanComboBox.getSelectedIndex(); // 0=A, 1=B, dst.

                if (pertanyaan.trim().isEmpty() || opsi[0].trim().isEmpty() || opsi[1].trim().isEmpty() || opsi[2].trim().isEmpty() || opsi[3].trim().isEmpty()) {
                    DialogHelper.showWarningMessage(BuatSoalDialog.this, "Semua field pertanyaan dan opsi harus diisi!", "Input Error");
                    return;
                }

                if (controller.tambahSoal(pertanyaan, opsi, kunciIndex)) {
                    DialogHelper.showInfoMessage(BuatSoalDialog.this, "Soal berhasil disimpan!", "Sukses");
                    pertanyaanArea.setText("");
                    opsiAField.setText("");
                    opsiBField.setText("");
                    opsiCField.setText("");
                    opsiDField.setText("");
                    kunciJawabanComboBox.setSelectedIndex(0);
                    // dispose(); // Atau biarkan terbuka untuk menambah soal lain
                } else {
                    DialogHelper.showErrorMessage(BuatSoalDialog.this, "Gagal menyimpan soal.", "Error");
                }
            }
        });

        batalButton.addActionListener(e -> dispose());
    }
}
