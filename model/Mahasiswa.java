package model;

import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    private String nim;
    private String namaMahasiswa;
    private String jenisKelamin;
    private String prodi;
    private String email;
    private String password;
    private String nomorTelp;
    private String alamat;
    private List<Dokumen> dokumenList; // List ini tidak lagi diisi otomatis, tapi bisa digunakan jika ada fitur lihat dokumen
    private Kelulusan statusKelulusan;

    // Konstruktor utama yang digunakan saat registrasi dari GUI
    public Mahasiswa(String nim, String namaMahasiswa, String jenisKelamin, String prodi, String email, String password, String nomorTelp, String alamat) {
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.jenisKelamin = jenisKelamin;
        this.prodi = prodi;
        this.email = email;
        this.password = password;
        this.nomorTelp = nomorTelp;
        this.alamat = alamat;
        this.dokumenList = new ArrayList<>();
        this.statusKelulusan = new Kelulusan(nim); // Status awal saat registrasi
    }

    // Konstruktor baru untuk membuat objek dari data database
    public Mahasiswa(String nim, String namaMahasiswa, String jenisKelamin, String prodi, String email, String password, String nomorTelp, String alamat, Kelulusan statusKelulusan) {
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.jenisKelamin = jenisKelamin;
        this.prodi = prodi;
        this.email = email;
        this.password = password;
        this.nomorTelp = nomorTelp;
        this.alamat = alamat;
        this.dokumenList = new ArrayList<>(); // Biarkan kosong, isi jika diperlukan
        this.statusKelulusan = statusKelulusan; // Gunakan status dari database
    }

    // Getters
    public String getNim() { return nim; }
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public String getJenisKelamin() { return jenisKelamin; }
    public String getProdi() { return prodi; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNomorTelp() { return nomorTelp; }
    public String getAlamat() { return alamat; }
    public List<Dokumen> getDokumenList() { return dokumenList; }
    public Kelulusan getStatusKelulusan() { return statusKelulusan; }

    // Setters (bisa disesuaikan jika perlu)
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setProdi(String prodi) { this.prodi = prodi; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNomorTelp(String nomorTelp) { this.nomorTelp = nomorTelp; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    // Metode ini tetap bisa digunakan, tapi controller yang akan menyimpannya ke DB
    public void tambahDokumen(Dokumen dokumen) {
        this.dokumenList.add(dokumen);
    }

    @Override
    public String toString() {
        return "Mahasiswa {" +
            "NIM='" + nim + '\'' +
            ", Nama='" + namaMahasiswa + '\'' +
            ", Prodi='" + prodi + '\'' +
            ", Email='" + email + '\'' +
            '}';
    }
}
