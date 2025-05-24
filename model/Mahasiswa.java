package model;

import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    private String nim;
    private String namaMahasiswa;
    private String jenisKelamin; // Laki-laki, Perempuan
    private String prodi; // Inf, SI, TI [cite: 93]
    private String email;
    private String password;
    private String nomorTelp;
    private String alamat;
    private List<Dokumen> dokumenList;
    private Kelulusan statusKelulusan;

    public Mahasiswa(String nim, String namaMahasiswa, String jenisKelamin, String prodi, String email, String password, String nomorTelp, String alamat) {
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.jenisKelamin = jenisKelamin;
        this.prodi = prodi;
        this.email = email;
        this.password = password; // Dalam aplikasi nyata, password harus di-hash
        this.nomorTelp = nomorTelp;
        this.alamat = alamat;
        this.dokumenList = new ArrayList<>();
        this.statusKelulusan = new Kelulusan(nim); // Setiap mahasiswa punya status kelulusan
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

    // Setters
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setProdi(String prodi) { this.prodi = prodi; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNomorTelp(String nomorTelp) { this.nomorTelp = nomorTelp; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public void tambahDokumen(Dokumen dokumen) {
        this.dokumenList.add(dokumen);
        System.out.println("Dokumen " + dokumen.getNamaDokumen() + " berhasil ditambahkan untuk NIM " + this.nim);
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