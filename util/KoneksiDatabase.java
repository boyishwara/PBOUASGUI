package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Kelas utilitas untuk mengelola koneksi ke database MySQL.
 * Kredensial sekarang diatur secara dinamis saat runtime.
 */
public class KoneksiDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/db_pbo_uas";
    
    // Kredensial sekarang disimpan sebagai variabel statis setelah diinisialisasi.
    private static String dbUser;
    private static String dbPassword;
    private static Connection koneksi;

    /**
     * Menginisialisasi dan menguji koneksi database dengan kredensial yang diberikan.
     * Metode ini harus dipanggil dengan sukses satu kali sebelum getKoneksi() digunakan.
     * @param user username database
     * @param password password database
     * @return true jika koneksi berhasil, false jika gagal.
     */
    public static boolean init(String user, String password) {
        dbUser = user;
        dbPassword = password;
        
        try {
            // Menutup koneksi yang ada (jika ada) sebelum membuat yang baru
            if (koneksi != null && !koneksi.isClosed()) {
                koneksi.close();
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection(URL, dbUser, dbPassword);
            return true; // Koneksi berhasil
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal terhubung ke database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false; // Koneksi gagal
        }
    }

    /**
     * Mendapatkan koneksi database yang aktif.
     * @return Objek Connection yang aktif.
     */
    public static Connection getKoneksi() {
        try {
            // Jika koneksi null atau tertutup, coba hubungkan kembali dengan kredensial yang tersimpan.
            if (koneksi == null || koneksi.isClosed()) {
                if (dbUser == null || dbPassword == null) {
                    // Kasus ini seharusnya tidak terjadi jika init() dipanggil terlebih dahulu.
                    JOptionPane.showMessageDialog(null, "Kredensial database belum diinisialisasi.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                koneksi = DriverManager.getConnection(URL, dbUser, dbPassword);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi database terputus, mencoba menghubungkan kembali: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            // Anda dapat menangani error ini secara berbeda, misalnya dengan mencoba menghubungkan kembali.
        }
        return koneksi;
    }
}
