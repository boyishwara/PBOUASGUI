package view.gui;

import controller.AppController;
import javax.swing.*;
import java.awt.*;

public class LihatLaporanDialog extends JDialog {
    private AppController controller;

    public LihatLaporanDialog(AppController controller, JFrame owner) {
        super(owner, "Laporan Kelulusan untuk Dekan", true);
        this.controller = controller;
        initComponents();
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTextArea laporanArea = new JTextArea();
        laporanArea.setEditable(false);
        laporanArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        laporanArea.setText(controller.getLaporanUntukDekan());
        laporanArea.setCaretPosition(0); // Scroll ke atas

        JScrollPane scrollPane = new JScrollPane(laporanArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton tutupButton = new JButton("Tutup");
        tutupButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(tutupButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
