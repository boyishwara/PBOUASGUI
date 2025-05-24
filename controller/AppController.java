package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane; // Untuk menampilkan pesan sederhana
import model.*;

public class AppController {
    // Tidak lagi menggunakan MainView console
    // private MainView view; 
    private List<Mahasiswa> daftarMahasiswa;
    private List<Soal> bankSoalTahap1;
    private Mahasiswa loggedInMahasiswa;
    private boolean koordinatorLoggedIn;
    private List<Ujian> daftarUjian; // Untuk menyimpan data ujian mahasiswa
    private Koordinator dummyKoordinator;
    private Koordinator loggedInKoordinator;

    public AppController() {
        // this.view = new MainView(); // Dihapus
        this.daftarMahasiswa = new ArrayList<>();
        this.bankSoalTahap1 = new ArrayList<>();
        this.daftarUjian = new ArrayList<>();
        this.loggedInMahasiswa = null;
        this.koordinatorLoggedIn = false;
        this.dummyKoordinator = new Koordinator("admin", "Admin Koordinator", "admin");
        this.loggedInKoordinator = null;
        inisialisasiDataDummy();
    }

    private void inisialisasiDataDummy() {
        // Dummy Soal
        bankSoalTahap1.add(new Soal("Apa kepanjangan dari UML?", new String[]{"Unified Modeling Language", "Universal Markup Language", "United Machine Learning", "Understood Model Logic"}, 0, "Tahap1"));
        bankSoalTahap1.add(new Soal("Diagram UML yang menggambarkan interaksi antar objek berdasarkan urutan waktu adalah...", new String[]{"Class Diagram", "Use Case Diagram", "Sequence Diagram", "Activity Diagram"}, 2, "Tahap1"));
        bankSoalTahap1.add(new Soal("Yang BUKAN merupakan aktor dalam sistem di jurnal adalah...", new String[]{"Mahasiswa", "Koordinator UKM", "Dosen Wali", "Dekan Fakultas Teknologi"}, 2, "Tahap1"));

        // Dummy Mahasiswa
        Mahasiswa m1 = new Mahasiswa("111", "Budi", "Laki-laki", "SI", "budi@email.com", "pass1", "081", "Jl. ABC");
        m1.tambahDokumen(new Dokumen(m1.getNim(), "KTM_Budi.pdf", "/path/ktm_budi.pdf"));
        daftarMahasiswa.add(m1);
        
        Mahasiswa m2 = new Mahasiswa("222", "Ani", "Perempuan", "Inf", "ani@email.com", "pass2", "082", "Jl. DEF");
        daftarMahasiswa.add(m2);
    }

    // --- Metode untuk Login ---
    public boolean loginMahasiswa(String nim, String password) {
        Optional<Mahasiswa> mhs = daftarMahasiswa.stream()
                                    .filter(m -> m.getNim().equals(nim) && m.getPassword().equals(password))
                                    .findFirst();
        if (mhs.isPresent()) {
            loggedInMahasiswa = mhs.get();
            koordinatorLoggedIn = false;
            return true;
        } else {
            loggedInMahasiswa = null;
            // Cek apakah NIM ada tapi password salah, atau NIM tidak ada sama sekali
            boolean nimExists = daftarMahasiswa.stream().anyMatch(m -> m.getNim().equals(nim));
            if (nimExists) {
                 JOptionPane.showMessageDialog(null, "Login gagal. Password salah.", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Login gagal. NIM tidak ditemukan.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    public boolean loginKoordinator(String id, String password) {
        if (dummyKoordinator.getId().equals(id) && dummyKoordinator.getPassword().equals(password)) {
            koordinatorLoggedIn = true;
            loggedInKoordinator = dummyKoordinator;
            loggedInMahasiswa = null;
            return true;
        } else {
            koordinatorLoggedIn = false;
            loggedInKoordinator = null;
            JOptionPane.showMessageDialog(null, "ID atau Password Koordinator salah.", "Login Error", JOptionPane.ERROR_MESSAGE);
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
        boolean nimExists = daftarMahasiswa.stream().anyMatch(m -> m.getNim().equals(nim));
        if (nimExists) {
            JOptionPane.showMessageDialog(null, "Registrasi gagal. NIM " + nim + " sudah terdaftar.", "Registrasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Mahasiswa newMahasiswa = new Mahasiswa(nim, nama, jenisKelamin, prodi, email, password, nomorTelp, alamat);
        daftarMahasiswa.add(newMahasiswa);
        JOptionPane.showMessageDialog(null, "Registrasi berhasil untuk NIM: " + nim + ". Silakan login.", "Registrasi Sukses", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    // --- Getter untuk status login dan data ---
    public Mahasiswa getLoggedInMahasiswa() {
        return loggedInMahasiswa;
    }

    public Koordinator getLoggedInKoordinator() {
        return loggedInKoordinator;
    }

    public boolean isKoordinatorLoggedIn() {
        return koordinatorLoggedIn;
    }
    
    public List<Mahasiswa> getDaftarMahasiswa() {
        return daftarMahasiswa;
    }

    public List<Soal> getBankSoalTahap1() {
        return bankSoalTahap1;
    }

    // --- Metode untuk Mahasiswa ---
    public boolean uploadDokumen(String namaDokumen, String pathFile) {
        if (loggedInMahasiswa == null) {
            JOptionPane.showMessageDialog(null, "Harap login terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Simulasi validasi dan penyimpanan
        Dokumen doc = new Dokumen(loggedInMahasiswa.getNim(), namaDokumen, pathFile);
        loggedInMahasiswa.tambahDokumen(doc);
        // view.displayMessage("Dokumen '" + namaDokumen + "' berhasil diupload. Sistem akan mengirim notifikasi (simulasi).");
        // view.displayMessage("Silakan menunggu jadwal ujian tahap I.");
        return true; // Anggap selalu berhasil untuk GUI
    }

    public Ujian mulaiUjianTahap1() {
        if (loggedInMahasiswa == null) {
            JOptionPane.showMessageDialog(null, "Harap login terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (bankSoalTahap1.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada soal ujian tahap 1 yang tersedia. Hubungi koordinator.", "Info Ujian", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        // Cek apakah sudah pernah ikut ujian tahap 1 atau sedang menunggu pengumuman
        String statusTahap1 = loggedInMahasiswa.getStatusKelulusan().getStatusTahap1();
        if (!"Belum Ujian".equals(statusTahap1)) {
             JOptionPane.showMessageDialog(null, "Anda sudah mengikuti Ujian Tahap 1 atau status Anda adalah '" + statusTahap1 + "'.", "Info Ujian", JOptionPane.WARNING_MESSAGE);
            return null; // Tidak bisa memulai ujian lagi jika sudah atau sedang diproses
        }

        Ujian ujian = new Ujian(loggedInMahasiswa.getNim(), "Tahap1");
        // Daftar ujian yang sedang dikerjakan bisa disimpan di controller atau langsung dihandle oleh GUI dialog ujian
        return ujian;
    }

    public void submitUjianTahap1(Ujian ujianSelesai) {
        if (loggedInMahasiswa == null || ujianSelesai == null) return;

        ujianSelesai.hitungSkor(bankSoalTahap1); // Sistem memeriksa jawaban
        daftarUjian.add(ujianSelesai); // Simpan ujian yang sudah selesai
        
        loggedInMahasiswa.getStatusKelulusan().setStatusTahap1("Menunggu Pengumuman");
        // view.displayMessage("Ujian Tahap 1 selesai. Jawaban Anda telah tersimpan.");
        // view.displayMessage("Hasil akan diumumkan oleh Koordinator.");
    }
    
    public Kelulusan getStatusKelulusanMahasiswa() {
        if (loggedInMahasiswa != null) {
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
        Soal soalBaru = new Soal(pertanyaan, opsiJawaban, kunciJawabanIndex, "Tahap1");
        bankSoalTahap1.add(soalBaru);
        return true;
    }

    public String prosesHasilUjianTahap1() {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat memproses hasil.";
        
        StringBuilder prosesLog = new StringBuilder("--- Proses Hasil Ujian Tahap 1 ---\n");
        boolean adaYangDiproses = false;

        for (Mahasiswa mhs : daftarMahasiswa) {
            // Cari ujian yang sesuai untuk mahasiswa ini dari daftarUjian
            Optional<Ujian> ujianOpt = daftarUjian.stream()
                .filter(u -> u.getNimMahasiswa().equals(mhs.getNim()) && "Tahap1".equals(u.getTipeUjian()))
                .findFirst(); // Ambil yang terbaru jika ada multiple, atau perlu logika lebih lanjut

            if ("Menunggu Pengumuman".equals(mhs.getStatusKelulusan().getStatusTahap1())) {
                 adaYangDiproses = true;
                if (ujianOpt.isPresent()) {
                    Ujian ujianMhs = ujianOpt.get();
                    // Skor sudah dihitung saat submit, sekarang tentukan lulus/tidak berdasarkan skor
                    // Misal, lulus jika skor > 50% dari total soal
                    boolean lulusTahap1 = false;
                    if (!bankSoalTahap1.isEmpty()) {
                        lulusTahap1 = (double) ujianMhs.getSkor() / bankSoalTahap1.size() > 0.5;
                    }
                    mhs.getStatusKelulusan().setStatusTahap1(lulusTahap1 ? "Lulus" : "Tidak Lulus");
                    prosesLog.append("NIM ").append(mhs.getNim()).append(" (").append(mhs.getNamaMahasiswa()).append("): Skor ").append(ujianMhs.getSkor()).append(" -> ").append(mhs.getStatusKelulusan().getStatusTahap1()).append("\n");
                } else {
                    // Jika status "Menunggu Pengumuman" tapi tidak ada record ujian, mungkin ada kesalahan data
                    // Atau mahasiswa belum submit ujian dengan benar. Untuk amannya, set Tidak Lulus atau biarkan.
                    // mhs.getStatusKelulusan().setStatusTahap1("Tidak Lulus (Data Ujian Tidak Ditemukan)");
                    prosesLog.append("NIM ").append(mhs.getNim()).append(" (").append(mhs.getNamaMahasiswa()).append("): Data ujian tidak ditemukan, status tidak diubah.\n");
                }
            }
        }
        if (!adaYangDiproses) {
            prosesLog.append("Tidak ada mahasiswa dengan status 'Menunggu Pengumuman' untuk diproses.\n");
        }
        prosesLog.append("Pemrosesan hasil Ujian Tahap 1 selesai.");
        return prosesLog.toString();
    }
    
    public Mahasiswa findMahasiswaByNim(String nim) {
        return daftarMahasiswa.stream().filter(m -> m.getNim().equals(nim)).findFirst().orElse(null);
    }

    public String inputHasilUjianTahap2(String nim, String hasilWawancara) {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat input hasil.";
        Mahasiswa mhs = findMahasiswaByNim(nim);

        if (mhs != null) {
            if (!"Lulus".equals(mhs.getStatusKelulusan().getStatusTahap1())) {
                return "Mahasiswa dengan NIM " + nim + " belum lulus Tahap 1.";
            }
            // Cek apakah sudah pernah diinput atau belum
            String statusTahap2 = mhs.getStatusKelulusan().getStatusTahap2();
            if (!"Belum Ujian".equals(statusTahap2) && !"Menunggu Pengumuman".equals(statusTahap2)) {
                 return "Hasil Tahap 2 untuk NIM " + nim + " sudah diinput sebelumnya: " + statusTahap2;
            }

            if (hasilWawancara.equalsIgnoreCase("Lulus") || hasilWawancara.equalsIgnoreCase("Tidak Lulus")) {
                mhs.getStatusKelulusan().setStatusTahap2(hasilWawancara.substring(0,1).toUpperCase() + hasilWawancara.substring(1).toLowerCase());
                return "Hasil Ujian Tahap 2 untuk NIM " + nim + " berhasil disimpan sebagai: " + mhs.getStatusKelulusan().getStatusTahap2();
            } else {
                return "Input tidak valid. Gunakan 'Lulus' atau 'Tidak Lulus'.";
            }
        } else {
            return "Mahasiswa dengan NIM " + nim + " tidak ditemukan.";
        }
    }

    public String umumkanHasilAkhir() {
        if (!koordinatorLoggedIn) return "Hanya koordinator yang dapat mengumumkan hasil.";
        StringBuilder pengumumanLog = new StringBuilder("--- Pengumuman Hasil Akhir Kelulusan ---\n");
        boolean adaYangDiumumkan = false;

        for (Mahasiswa mhs : daftarMahasiswa) {
            Kelulusan status = mhs.getStatusKelulusan();
            // Hanya proses jika status akhir masih "Belum Ditentukan" dan kedua tahap sudah ada hasilnya (bukan "Belum Ujian" atau "Menunggu Pengumuman")
            if ("Belum Ditentukan".equals(status.getStatusAkhir()) &&
                (!"Belum Ujian".equals(status.getStatusTahap1()) && !"Menunggu Pengumuman".equals(status.getStatusTahap1())) &&
                (!"Belum Ujian".equals(status.getStatusTahap2()) && !"Menunggu Pengumuman".equals(status.getStatusTahap2())) ) {
                
                adaYangDiumumkan = true;
                if ("Lulus".equals(status.getStatusTahap1()) && "Lulus".equals(status.getStatusTahap2())) {
                    status.setStatusAkhir("Diterima");
                } else { // Jika salah satu atau kedua tahap tidak lulus
                    status.setStatusAkhir("Tidak Diterima");
                }
                pengumumanLog.append("NIM ").append(mhs.getNim()).append(" (").append(mhs.getNamaMahasiswa()).append("): Hasil Akhir - ").append(status.getStatusAkhir()).append("\n");
            } else if ("Belum Ditentukan".equals(status.getStatusAkhir())) {
                 pengumumanLog.append("NIM ").append(mhs.getNim()).append(": Hasil akhir belum dapat ditentukan (cek status tahap 1 & 2 belum final).\n");
            }
        }
         if (!adaYangDiumumkan && daftarMahasiswa.stream().noneMatch(m -> "Belum Ditentukan".equals(m.getStatusKelulusan().getStatusAkhir()))) {
            pengumumanLog.append("Semua hasil akhir mahasiswa sudah ditentukan sebelumnya atau tidak ada yang memenuhi syarat untuk diumumkan saat ini.\n");
        } else if (!adaYangDiumumkan) {
            pengumumanLog.append("Tidak ada mahasiswa yang status akhirnya dapat diumumkan saat ini (pastikan tahap 1 & 2 sudah selesai diproses).\n");
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
