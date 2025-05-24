package view.gui;

import controller.AppController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class LoginPage extends JFrame {
    private AppController controller;

    // Hapus field NIM dan Password jika tidak lagi relevan untuk desain baru ini,
    // atau biarkan jika masih digunakan untuk login mahasiswa.
    // Untuk contoh ini, kita akan asumsikan field password masih ada untuk mahasiswa,
    // tapi koordinator akan pakai dialog box.
    private JTextField nimField; // Untuk NIM Mahasiswa
    private JPasswordField passwordMahasiswaField; // Password khusus untuk mahasiswa

    private JButton loginMahasiswaButton;
    private JButton loginKoordinatorButton;
    private JButton registerButton;

    private javax.swing.JTextField idTextField;
    private javax.swing.JPasswordField passwordField;

    public LoginPage(AppController controller) {
        this.controller = controller;
        initComponents();
        setTitle("Login Sistem Penerimaan Anggota");
        // Sesuaikan ukuran jika field dikurangi
        setSize(400, 200); // Ukuran mungkin bisa lebih kecil
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label dan Field NIM (Untuk Mahasiswa)
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("NIM Mahasiswa:"), gbc);

        nimField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(nimField, gbc);

        // Label dan Field Password (Untuk Mahasiswa)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Password Mahasiswa:"), gbc);

        passwordMahasiswaField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(passwordMahasiswaField, gbc);

        // Tombol Login Mahasiswa
        loginMahasiswaButton = new JButton("Login Mahasiswa");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Kembali ke 1 kolom
        add(loginMahasiswaButton, gbc);

        // Tombol Login Koordinator
        loginKoordinatorButton = new JButton("Login Koordinator");
        gbc.gridx = 1; // Pindah ke sebelah tombol login mahasiswa
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(loginKoordinatorButton, gbc);
        
        // Tombol Register
        registerButton = new JButton("Register Mahasiswa");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span 2 kolom
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);


        // Action Listeners
        loginMahasiswaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = nimField.getText();
                String password = new String(passwordMahasiswaField.getPassword());
                if (nim.isEmpty() || password.isEmpty()) {
                    DialogHelper.showWarningMessage(LoginPage.this, "NIM dan Password mahasiswa tidak boleh kosong!", "Input Error");
                    return;
                }
                if (controller.loginMahasiswa(nim, password)) {
                    DialogHelper.showInfoMessage(LoginPage.this, "Login Mahasiswa berhasil!", "Login Sukses");
                    new MahasiswaDashboardPage(controller).setVisible(true);
                    dispose();
                } 
            }
        });

        // Create an Action for the mahasiswa login
        Action loginMahasiswaAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginMahasiswaButton.doClick();
            }
        };

        // Register the action with the NIM field
        nimField.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "loginMahasiswa");
        nimField.getActionMap().put("loginMahasiswa", loginMahasiswaAction);

        // Register the action with the password field
        passwordMahasiswaField.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "loginMahasiswa");
        passwordMahasiswaField.getActionMap().put("loginMahasiswa", loginMahasiswaAction);

        // Modify your koordinator login action listener to include Enter key handling in the dialog
        loginKoordinatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Panel untuk input ID dan Password Koordinator
                JPanel panel = new JPanel(new GridLayout(2, 2));
                JTextField idField = new JTextField();
                JPasswordField passField = new JPasswordField();
                panel.add(new JLabel("ID Koordinator:"));
                panel.add(idField);
                panel.add(new JLabel("Password:"));
                panel.add(passField);

                // Add action listener for Enter key in the password field
                passField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Simulate clicking OK on the dialog
                        JOptionPane optionPane = (JOptionPane) SwingUtilities.getAncestorOfClass(
                            JOptionPane.class, passField);
                        if (optionPane != null) {
                            optionPane.setValue(JOptionPane.OK_OPTION);
                        }
                    }
                });

                int result = JOptionPane.showConfirmDialog(
                    LoginPage.this,
                    panel,
                    "Login Koordinator",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String id = idField.getText();
                    String password = new String(passField.getPassword());
                    if (id.isEmpty() || password.isEmpty()) {
                        DialogHelper.showWarningMessage(LoginPage.this, "ID dan Password Koordinator tidak boleh kosong!", "Input Error");
                        return;
                    }
                    if (controller.loginKoordinator(id, password)) {
                        DialogHelper.showInfoMessage(LoginPage.this, "Login Koordinator berhasil.", "Login Sukses");
                        new KoordinatorDashboardPage(controller).setVisible(true);
                        dispose();
                    }
                    // Pesan error jika login gagal sudah ditangani di AppController
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterPage registerPage = new RegisterPage(controller, LoginPage.this);
                registerPage.setVisible(true);
            }
        });
    }
}