package view.gui;

import controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UploadDokumenDialog extends JDialog {
    private AppController controller;
    private JTextField namaDokumenField;
    private JTextField pathField;
    private JButton browseButton;
    private JButton uploadButton;

    public UploadDokumenDialog(AppController controller, JFrame owner) {
        super(owner, "Upload Dokumen", true);
        this.controller = controller;
        initComponents();
        setSize(450, 200);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nama Dokumen:"), gbc);
        namaDokumenField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; add(namaDokumenField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Path File:"), gbc);
        pathField = new JTextField(20);
        pathField.setEditable(false); // Path diisi via JFileChooser
        gbc.gridx = 1; gbc.gridy = 1; add(pathField, gbc);

        browseButton = new JButton("Browse...");
        gbc.gridx = 2; gbc.gridy = 1; add(browseButton, gbc);
        
        uploadButton = new JButton("Upload");
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.CENTER;
        add(uploadButton, gbc);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(UploadDokumenDialog.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    pathField.setText(selectedFile.getAbsolutePath());
                    if (namaDokumenField.getText().trim().isEmpty()) {
                        namaDokumenField.setText(selectedFile.getName());
                    }
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namaDokumen = namaDokumenField.getText();
                String pathFile = pathField.getText();
                if (namaDokumen.trim().isEmpty() || pathFile.trim().isEmpty()) {
                    DialogHelper.showWarningMessage(UploadDokumenDialog.this, "Nama dokumen dan path file harus diisi!", "Input Error");
                    return;
                }
                if (controller.uploadDokumen(namaDokumen, pathFile)) {
                    DialogHelper.showInfoMessage(UploadDokumenDialog.this, "Dokumen '" + namaDokumen + "' berhasil diupload (simulasi).\nSilakan menunggu jadwal ujian tahap I.", "Upload Sukses");
                    dispose();
                } else {
                    // Pesan error sudah dari controller
                }
            }
        });
    }
}
