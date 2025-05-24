package view.gui;

import controller.AppController;
import model.Mahasiswa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LihatMahasiswaDialog extends JDialog {
    private AppController controller;
    private JTable mahasiswaTable;
    private DefaultTableModel tableModel;

    public LihatMahasiswaDialog(AppController controller, JFrame owner) {
        super(owner, "Daftar Mahasiswa Terdaftar", true);
        this.controller = controller;
        initComponents();
        loadDataMahasiswa();
        setSize(800, 400);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        String[] columnNames = {"NIM", "Nama", "Prodi", "Email", "Status Tahap 1", "Status Tahap 2", "Status Akhir"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };
        mahasiswaTable = new JTable(tableModel);
        mahasiswaTable.setFillsViewportHeight(true);
        mahasiswaTable.setAutoCreateRowSorter(true); // Mengaktifkan sorting

        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton tutupButton = new JButton("Tutup");
        tutupButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(tutupButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDataMahasiswa() {
        List<Mahasiswa> daftar = controller.getDaftarMahasiswa();
        tableModel.setRowCount(0); // Kosongkan tabel dulu
        if (daftar.isEmpty()) {
            DialogHelper.showInfoMessage(this, "Belum ada mahasiswa terdaftar.", "Info");
        } else {
            for (Mahasiswa m : daftar) {
                Object[] row = {
                    m.getNim(),
                    m.getNamaMahasiswa(),
                    m.getProdi(),
                    m.getEmail(),
                    m.getStatusKelulusan().getStatusTahap1(),
                    m.getStatusKelulusan().getStatusTahap2(),
                    m.getStatusKelulusan().getStatusAkhir()
                };
                tableModel.addRow(row);
            }
        }
    }
}
