package model;

public class Kelulusan {
    private String nimMahasiswa;
    private String statusTahap1; // "Belum Ujian", "Lulus", "Tidak Lulus" [cite: 93]
    private String statusTahap2; // "Belum Ujian", "Lulus", "Tidak Lulus" [cite: 93]
    private String statusAkhir;  // "Belum Ditentukan", "Diterima", "Tidak Diterima"

    public Kelulusan(String nimMahasiswa) {
        this.nimMahasiswa = nimMahasiswa;
        this.statusTahap1 = "Belum Ujian";
        this.statusTahap2 = "Belum Ujian";
        this.statusAkhir = "Belum Ditentukan";
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