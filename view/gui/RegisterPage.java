package view.gui;

import controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JDialog { // Menggunakan JDialog agar modal
    private AppController controller;
    private JFrame ownerFrame; // Untuk referensi parent

    private JTextField nimField, namaField, emailField, nomorTelpField, alamatField;
    private JPasswordField passwordField;
    private JComboBox<String> jenisKelaminComboBox, prodiComboBox;
    private JButton registerButton, cancelButton;

    public RegisterPage(AppController controller, JFrame owner) {
        super(owner, "Registrasi Mahasiswa Baru", true); // true untuk modal
        this.controller = controller;
        this.ownerFrame = owner;
        initComponents();
        setSize(450, 400);
        setLocationRelativeTo(owner); // Muncul relatif terhadap owner
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // NIM
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("NIM:"), gbc);
        nimField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; add(nimField, gbc);

        // Nama
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nama Lengkap:"), gbc);
        namaField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; add(namaField, gbc);

        // Jenis Kelamin
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Jenis Kelamin:"), gbc);
        jenisKelaminComboBox = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        gbc.gridx = 1; gbc.gridy = 2; add(jenisKelaminComboBox, gbc);

        // Prodi
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Program Studi:"), gbc);
        prodiComboBox = new JComboBox<>(new String[]{"SI", "Inf", "TI", "Lainnya"}); // Sesuaikan
        gbc.gridx = 1; gbc.gridy = 3; add(prodiComboBox, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4; add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 5; add(passwordField, gbc);

        // Nomor Telepon
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Nomor Telepon:"), gbc);
        nomorTelpField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6; add(nomorTelpField, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 7; add(new JLabel("Alamat:"), gbc);
        alamatField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 7; add(alamatField, gbc);

        // Tombol
        registerButton = new JButton("Register");
        cancelButton = new JButton("Batal");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Action Listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = nimField.getText();
                String nama = namaField.getText();
                String jenisKelamin = (String) jenisKelaminComboBox.getSelectedItem();
                String prodi = (String) prodiComboBox.getSelectedItem();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String nomorTelp = nomorTelpField.getText();
                String alamat = alamatField.getText();

                if (nim.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty() || nomorTelp.isEmpty() || alamat.isEmpty()) {
                    DialogHelper.showWarningMessage(RegisterPage.this, "Semua field harus diisi!", "Input Error");
                    return;
                }

                if (controller.registerMahasiswa(nim, nama, jenisKelamin, prodi, email, password, nomorTelp, alamat)) {
                    // DialogHelper.showInfoMessage(RegisterPage.this, "Registrasi berhasil!", "Sukses"); // Sudah di controller
                    dispose(); // Tutup dialog registrasi
                }
                // Pesan error juga sudah di controller
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Tutup dialog
            }
        });
    }
}
