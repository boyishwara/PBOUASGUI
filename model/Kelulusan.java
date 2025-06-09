package model;

public class Kelulusan {
    private String nimMahasiswa;
    private String statusTahap1;
    private String statusTahap2;
    private String statusAkhir;

    // Konstruktor untuk mahasiswa baru
    public Kelulusan(String nimMahasiswa) {
        this.nimMahasiswa = nimMahasiswa;
        this.statusTahap1 = "Belum Ujian";
        this.statusTahap2 = "Belum Ujian";
        this.statusAkhir = "Belum Ditentukan";
    }
    
    // Konstruktor baru untuk memuat data dari database
    public Kelulusan(String nimMahasiswa, String statusTahap1, String statusTahap2, String statusAkhir) {
        this.nimMahasiswa = nimMahasiswa;
        this.statusTahap1 = statusTahap1;
        this.statusTahap2 = statusTahap2;
        this.statusAkhir = statusAkhir;
    }

    // Getters
    public String getNimMahasiswa() { return nimMahasiswa; }
    public String getStatusTahap1() { return statusTahap1; }
    public String getStatusTahap2() { return statusTahap2; }
    public String getStatusAkhir() { return statusAkhir; }

    // Setters
    public void setStatusTahap1(String statusTahap1) { this.statusTahap1 = statusTahap1; }
    public void setStatusTahap2(String statusTahap2) { this.statusTahap2 = statusTahap2; }
    public void setStatusAkhir(String statusAkhir) { this.statusAkhir = statusAkhir; }

    @Override
    public String toString() {
        return "Status Kelulusan NIM: " + nimMahasiswa + "\n" +
               "  Tahap 1: " + statusTahap1 + "\n" +
               "  Tahap 2: " + statusTahap2 + "\n" +
               "  Hasil Akhir: " + statusAkhir;
    }
}
