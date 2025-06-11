package controller;

import java.awt.GridLayout;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.*;
import util.KoneksiDatabase;

/**
 * Controller utama yang menghubungkan View dengan Model dan Database.
 * Semua operasi data (CRUD) dilakukan di sini melalui JDBC.
 */
public class AppController {

    private Mahasiswa loggedInMahasiswa;
    private Koordinator loggedInKoordinator;
    private boolean koordinatorLoggedIn;

    public AppController() {
        // Terus menampilkan dialog sampai koneksi berhasil atau pengguna membatalkan.
        while (true) {
            // Membuat panel untuk dialog input
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField userField = new JTextField("root"); // Nilai default 'root'
            JPasswordField passField = new JPasswordField();
            panel.add(new JLabel("Username Database:"));
            panel.add(userField);
            panel.add(new JLabel("Password Database:"));
            panel.add(passField);

            // Menampilkan dialog
            int result = JOptionPane.showConfirmDialog(null, panel, "Inisialisasi Koneksi Database", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String user = userField.getText();
                String pass = new String(passField.getPassword());
                
                // Mencoba inisialisasi koneksi dengan kredensial yang diberikan
                if (KoneksiDatabase.init(user, pass)) {
                    JOptionPane.showMessageDialog(null, "Koneksi database berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    break; // Keluar dari loop jika koneksi sukses
                } 
                // Jika gagal, KoneksiDatabase.init() akan menampilkan pesan error sendiri, dan loop akan berlanjut.
            } else {
                // Pengguna menekan Batal atau menutup dialog, maka aplikasi akan keluar.
                System.exit(0);
            }
        }
        
        this.loggedInMahasiswa = null;
        this.loggedInKoordinator = null;
        this.koordinatorLoggedIn = false;
    }

    // --- Metode untuk Login ---
    public boolean loginMahasiswa(String nim, String password) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ? AND password = ?";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nim);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Data ditemukan, buat objek Mahasiswa lengkap dengan status kelulusannya
                    Kelulusan status = new Kelulusan(
                        rs.getString("nim"),
                        rs.getString("status_tahap1"),
                        rs.getString("status_tahap2"),
                        rs.getString("status_akhir")
                    );
                    
                    loggedInMahasiswa = new Mahasiswa(
                        rs.getString("nim"),
                        rs.getString("nama_mahasiswa"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("prodi"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nomor_telp"),
                        rs.getString("alamat"),
                        status // Masukkan objek status kelulusan
                    );
                    
                    koordinatorLoggedIn = false;
                    loggedInKoordinator = null;
                    return true;
                } else {
                    loggedInMahasiswa = null;
                    JOptionPane.showMessageDialog(null, "Login gagal. NIM atau Password salah.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean loginKoordinator(String id, String password) {
        String sql = "SELECT * FROM koordinator WHERE id = ? AND password = ?";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    loggedInKoordinator = new Koordinator(rs.getString("id"), rs.getString("nama"), rs.getString("password"));
                    koordinatorLoggedIn = true;
                    loggedInMahasiswa = null;
                    return true;
                } else {
                    koordinatorLoggedIn = false;
                    loggedInKoordinator = null;
                    JOptionPane.showMessageDialog(null, "ID atau Password Koordinator salah.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void logout() {
        loggedInMahasiswa = null;
        koordinatorLoggedIn = false;
        loggedInKoordinator = null;
    }

    // --- Metode untuk Registrasi Mahasiswa ---
    public boolean registerMahasiswa(String nim, String nama, String jenisKelamin, String prodi, String email, String password, String nomorTelp, String alamat) {
        String checkSql = "SELECT nim FROM mahasiswa WHERE nim = ? OR email = ?";
        String insertSql = "INSERT INTO mahasiswa (nim, nama_mahasiswa, jenis_kelamin, prodi, email, password, nomor_telp, alamat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = KoneksiDatabase.getKoneksi()) {
            // Cek dulu apakah NIM atau email sudah ada
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, nim);
                checkStmt.setString(2, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Registrasi gagal. NIM atau Email sudah terdaftar.", "Registrasi Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            // Jika tidak ada, lakukan INSERT
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, nim);
                insertStmt.setString(2, nama);
                insertStmt.setString(3, jenisKelamin);
                insertStmt.setString(4, prodi);
                insertStmt.setString(5, email);
                insertStmt.setString(6, password);
                insertStmt.setString(7, nomorTelp);
                insertStmt.setString(8, alamat);

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Registrasi berhasil untuk NIM: " + nim + ". Silakan login.", "Registrasi Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error database saat registrasi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // --- Getter untuk status login dan data ---
    public Mahasiswa getLoggedInMahasiswa() { return loggedInMahasiswa; }
    public Koordinator getLoggedInKoordinator() { return loggedInKoordinator; }
    public boolean isKoordinatorLoggedIn() { return koordinatorLoggedIn; }
    
    public List<Mahasiswa> getDaftarMahasiswa() {
        List<Mahasiswa> daftarMahasiswa = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nama_mahasiswa";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while(rs.next()){
                Kelulusan status = new Kelulusan(
                    rs.getString("nim"),
                    rs.getString("status_tahap1"),
                    rs.getString("status_tahap2"),
                    rs.getString("status_akhir")
                );
                Mahasiswa mhs = new Mahasiswa(
                    rs.getString("nim"),
                    rs.getString("nama_mahasiswa"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("prodi"),
                    rs.getString("email"),
                    "******", // Jangan tampilkan password
                    rs.getString("nomor_telp"),
                    rs.getString("alamat"),
                    status
                );
                daftarMahasiswa.add(mhs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data mahasiswa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return daftarMahasiswa;
    }

    public List<Soal> getBankSoalTahap1() {
        List<Soal> bankSoal = new ArrayList<>();
        String sql = "SELECT * FROM soal WHERE tipe_ujian = 'Tahap1'";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Soal soal = new Soal(
                    rs.getInt("id_soal"),
                    rs.getString("pertanyaan"),
                    new String[]{
                        rs.getString("opsi_a"),
                        rs.getString("opsi_b"),
                        rs.getString("opsi_c"),
                        rs.getString("opsi_d")
                    },
                    rs.getInt("kunci_jawaban_index"),
                    rs.getString("tipe_ujian")
                );
                bankSoal.add(soal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil data soal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bankSoal;
    }

    // --- Metode untuk Mahasiswa ---
    public boolean uploadDokumen(String namaDokumen, String pathFile) {
        if (loggedInMahasiswa == null) {
            JOptionPane.showMessageDialog(null, "Harap login terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql = "INSERT INTO dokumen (nim_mahasiswa, nama_dokumen, path_file) VALUES (?, ?, ?)";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, loggedInMahasiswa.getNim());
            pstmt.setString(2, namaDokumen);
            pstmt.setString(3, pathFile);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengupload dokumen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Ujian mulaiUjianTahap1() {
        if (loggedInMahasiswa == null) {
            JOptionPane.showMessageDialog(null, "Harap login terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String statusTahap1 = loggedInMahasiswa.getStatusKelulusan().getStatusTahap1();
        if (!"Belum Ujian".equals(statusTahap1)) {
            JOptionPane.showMessageDialog(null, "Anda sudah mengikuti Ujian Tahap 1 atau status Anda adalah '" + statusTahap1 + "'.", "Info Ujian", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Ujian(loggedInMahasiswa.getNim(), "Tahap1");
    }

    public void submitUjianTahap1(Ujian ujianSelesai) {
        if (loggedInMahasiswa == null || ujianSelesai == null) return;

        List<Soal> bankSoal = getBankSoalTahap1(); // Ambil soal dari DB untuk perhitungan skor
        ujianSelesai.hitungSkor(bankSoal);
        int skor = ujianSelesai.getSkor();

        Connection conn = null;
        try {
            conn = KoneksiDatabase.getKoneksi();
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Simpan data ujian ke tabel 'ujian' dan dapatkan id_ujian
            String sqlUjian = "INSERT INTO ujian (nim_mahasiswa, tipe_ujian, skor) VALUES (?, ?, ?)";
            int idUjian;
            try (PreparedStatement pstmtUjian = conn.prepareStatement(sqlUjian, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUjian.setString(1, ujianSelesai.getNimMahasiswa());
                pstmtUjian.setString(2, "Tahap1");
                pstmtUjian.setInt(3, skor);
                pstmtUjian.executeUpdate();
                
                try (ResultSet generatedKeys = pstmtUjian.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idUjian = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Gagal membuat ujian, tidak ada ID yang diperoleh.");
                    }
                }
            }

            // 2. Simpan semua jawaban ke tabel 'jawaban_ujian'
            String sqlJawaban = "INSERT INTO jawaban_ujian (id_ujian, id_soal, jawaban_index) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtJawaban = conn.prepareStatement(sqlJawaban)) {
                for (Map.Entry<Integer, Integer> jawaban : ujianSelesai.getJawabanMahasiswa().entrySet()) {
                    pstmtJawaban.setInt(1, idUjian);
                    pstmtJawaban.setInt(2, jawaban.getKey());   // id_soal
                    pstmtJawaban.setInt(3, jawaban.getValue()); // jawaban_index
                    pstmtJawaban.addBatch();
                }
                pstmtJawaban.executeBatch();
            }
            
            // 3. Update status mahasiswa menjadi 'Menunggu Pengumuman'
            String sqlUpdateStatus = "UPDATE mahasiswa SET status_tahap1 = 'Menunggu Pengumuman' WHERE nim = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateStatus)) {
                pstmtUpdate.setString(1, loggedInMahasiswa.getNim());
                pstmtUpdate.executeUpdate();
            }

            conn.commit(); // Selesaikan transaksi jika semua berhasil
            loggedInMahasiswa.getStatusKelulusan().setStatusTahap1("Menunggu Pengumuman");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Batalkan transaksi jika ada error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Gagal menyimpan hasil ujian: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Kelulusan getStatusKelulusanMahasiswa() {
        if (loggedInMahasiswa != null) {
            // Refresh data dari DB jika diperlukan, atau andalkan data saat login
            return loggedInMahasiswa.getStatusKelulusan();
        }
        return null;
    }

    // --- Metode untuk Koordinator ---
    public boolean tambahSoal(String pertanyaan, String[] opsiJawaban, int kunciJawabanIndex) {
        if (!koordinatorLoggedIn) {
            JOptionPane.showMessageDialog(null, "Hanya koordinator yang dapat menambah soal.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql = "INSERT INTO soal (pertanyaan, opsi_a, opsi_b, opsi_c, opsi_d, kunci_jawaban_index) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pertanyaan);
            pstmt.setString(2, opsiJawaban[0]);
            pstmt.setString(3, opsiJawaban[1]);
            pstmt.setString(4, opsiJawaban[2]);
            pstmt.setString(5, opsiJawaban[3]);
            pstmt.setInt(6, kunciJawabanIndex);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menambah soal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String prosesHasilUjianTahap1() {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat memproses hasil.";
        
        StringBuilder prosesLog = new StringBuilder("--- Proses Hasil Ujian Tahap 1 ---\n");
        String sqlSelect = "SELECT nim FROM mahasiswa WHERE status_tahap1 = 'Menunggu Pengumuman'";
        String sqlGetUjian = "SELECT skor FROM ujian WHERE nim_mahasiswa = ? AND tipe_ujian = 'Tahap1' ORDER BY waktu_submit DESC LIMIT 1";
        String sqlUpdate = "UPDATE mahasiswa SET status_tahap1 = ? WHERE nim = ?";
        
        long totalSoal = getBankSoalTahap1().size();
        if (totalSoal == 0) {
            return "Tidak ada soal di database. Tidak dapat memproses hasil.";
        }

        try (Connection conn = KoneksiDatabase.getKoneksi();
            Statement stmtSelect = conn.createStatement();
            ResultSet rs = stmtSelect.executeQuery(sqlSelect)) {

            boolean adaYangDiproses = false;
            while(rs.next()){
                adaYangDiproses = true;
                String nim = rs.getString("nim");
                
                try (PreparedStatement pstmtGetUjian = conn.prepareStatement(sqlGetUjian)) {
                    pstmtGetUjian.setString(1, nim);
                    try (ResultSet rsUjian = pstmtGetUjian.executeQuery()) {
                        if (rsUjian.next()) {
                            int skor = rsUjian.getInt("skor");
                            boolean lulus = (double) skor / totalSoal > 0.5;
                            String statusLulus = lulus ? "Lulus" : "Tidak Lulus";

                            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                                pstmtUpdate.setString(1, statusLulus);
                                pstmtUpdate.setString(2, nim);
                                pstmtUpdate.executeUpdate();
                                prosesLog.append("NIM ").append(nim).append(": Skor ").append(skor).append(" -> ").append(statusLulus).append("\n");
                            }
                        } else {
                            prosesLog.append("NIM ").append(nim).append(": Data ujian tidak ditemukan, status tidak diubah.\n");
                        }
                    }
                }
            }
            if (!adaYangDiproses) {
                prosesLog.append("Tidak ada mahasiswa dengan status 'Menunggu Pengumuman' untuk diproses.\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            prosesLog.append("\nERROR SAAT MEMPROSES: ").append(e.getMessage());
        }
        prosesLog.append("Pemrosesan hasil Ujian Tahap 1 selesai.");
        return prosesLog.toString();
    }
    
    public Mahasiswa findMahasiswaByNim(String nim) {
        // Ini hanya mencari data dasar, bisa disesuaikan jika perlu data lebih lengkap
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Kelulusan status = new Kelulusan(
                        rs.getString("nim"), rs.getString("status_tahap1"), rs.getString("status_tahap2"), rs.getString("status_akhir")
                    );
                    return new Mahasiswa(
                        rs.getString("nim"), rs.getString("nama_mahasiswa"), rs.getString("jenis_kelamin"), 
                        rs.getString("prodi"), rs.getString("email"), "******", rs.getString("nomor_telp"),
                        rs.getString("alamat"), status
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String inputHasilUjianTahap2(String nim, String hasilWawancara) {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat input hasil.";
        
        Mahasiswa mhs = findMahasiswaByNim(nim);
        if (mhs == null) return "Mahasiswa dengan NIM " + nim + " tidak ditemukan.";
        if (!"Lulus".equals(mhs.getStatusKelulusan().getStatusTahap1())) {
            return "Mahasiswa dengan NIM " + nim + " belum lulus Tahap 1.";
        }

        String sql = "UPDATE mahasiswa SET status_tahap2 = ? WHERE nim = ?";
        try (Connection conn = KoneksiDatabase.getKoneksi();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hasilWawancara);
            pstmt.setString(2, nim);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return "Hasil Ujian Tahap 2 untuk NIM " + nim + " berhasil disimpan sebagai: " + hasilWawancara;
            } else {
                return "Gagal mengupdate hasil untuk NIM " + nim;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error database: " + e.getMessage();
        }
    }

    public String umumkanHasilAkhir() {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat mengumumkan hasil.";
        
        StringBuilder pengumumanLog = new StringBuilder("--- Pengumuman Hasil Akhir Kelulusan ---\n");
        String sqlUpdateDiterima = "UPDATE mahasiswa SET status_akhir = 'Diterima' WHERE status_tahap1 = 'Lulus' AND status_tahap2 = 'Lulus' AND status_akhir = 'Belum Ditentukan'";
        String sqlUpdateTidakDiterima = "UPDATE mahasiswa SET status_akhir = 'Tidak Diterima' WHERE (status_tahap1 != 'Lulus' OR status_tahap2 != 'Lulus') AND status_akhir = 'Belum Ditentukan'";

        try (Connection conn = KoneksiDatabase.getKoneksi();
            Statement stmt = conn.createStatement()) {
            
            int diterimaCount = stmt.executeUpdate(sqlUpdateDiterima);
            pengumumanLog.append(diterimaCount).append(" mahasiswa diubah statusnya menjadi DITERIMA.\n");

            int tidakDiterimaCount = stmt.executeUpdate(sqlUpdateTidakDiterima);
            pengumumanLog.append(tidakDiterimaCount).append(" mahasiswa diubah statusnya menjadi TIDAK DITERIMA.\n");
            
            if (diterimaCount == 0 && tidakDiterimaCount == 0) {
                pengumumanLog.append("Tidak ada mahasiswa yang statusnya perlu diumumkan saat ini.\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            pengumumanLog.append("\nERROR SAAT PENGUMUMAN: ").append(e.getMessage());
        }
        
        pengumumanLog.append("Pengumuman hasil akhir selesai.");
        return pengumumanLog.toString();
    }

    public String getLaporanUntukDekan() {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat melihat laporan.";
        
        StringBuilder laporan = new StringBuilder();
        laporan.append("===== LAPORAN KELULUSAN ANGGOTA BARU =====\n");
        laporan.append("Programmer Association of Battuta\n");
        laporan.append("==========================================\n");

        List<Mahasiswa> daftarMahasiswa = getDaftarMahasiswa(); // Gunakan metode yang sudah ada

        if (daftarMahasiswa.isEmpty()) {
            laporan.append("Tidak ada data mahasiswa untuk dilaporkan.\n");
        } else {
            for (Mahasiswa m : daftarMahasiswa) {
                laporan.append("\nNIM: ").append(m.getNim()).append(", Nama: ").append(m.getNamaMahasiswa()).append("\n");
                laporan.append("  Prodi: ").append(m.getProdi()).append("\n");
                laporan.append("  Status Tahap 1: ").append(m.getStatusKelulusan().getStatusTahap1()).append("\n");
                laporan.append("  Status Tahap 2: ").append(m.getStatusKelulusan().getStatusTahap2()).append("\n");
                laporan.append("  Status Akhir Penerimaan: ").append(m.getStatusKelulusan().getStatusAkhir()).append("\n");
            }
        }
        laporan.append("\n==========================================\n");
        laporan.append("Laporan ini diserahkan kepada Dekan Fakultas Teknologi.\n");
        return laporan.toString();
    }
}
