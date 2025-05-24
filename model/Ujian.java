package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ujian {
    private String nimMahasiswa;
    private String tipeUjian; // Tahap1, Tahap2
    private Map<Integer, Integer> jawabanMahasiswa; // Key: idSoal, Value: indexJawaban
    private int skor;
    private boolean lulus;

    public Ujian(String nimMahasiswa, String tipeUjian) {
        this.nimMahasiswa = nimMahasiswa;
        this.tipeUjian = tipeUjian;
        this.jawabanMahasiswa = new HashMap<>();
        this.skor = 0;
        this.lulus = false;
    }

    // Getters
    public String getNimMahasiswa() { return nimMahasiswa; }
    public String getTipeUjian() { return tipeUjian; }
    public Map<Integer, Integer> getJawabanMahasiswa() { return jawabanMahasiswa; }
    public int getSkor() { return skor; }
    public boolean isLulus() { return lulus; }

    // Setters
    public void setLulus(boolean lulus) { this.lulus = lulus; }

    public void tambahJawaban(int idSoal, int indexJawaban) {
        jawabanMahasiswa.put(idSoal, indexJawaban);
    }

    public void hitungSkor(List<Soal> daftarSoalUjian) {
        this.skor = 0;
        for (Soal soal : daftarSoalUjian) {
            if (jawabanMahasiswa.containsKey(soal.getIdSoal())) {
                if (soal.cekJawaban(jawabanMahasiswa.get(soal.getIdSoal()))) {
                    this.skor++; // Misal skor +1 untuk setiap jawaban benar
                }
            }
        }
        // Tentukan kelulusan berdasarkan skor, misal > 50% soal benar
        if (!daftarSoalUjian.isEmpty()) {
            this.lulus = (double)this.skor / daftarSoalUjian.size() > 0.5;
        } else {
            this.lulus = false;
        }
    }

    @Override
    public String toString() {
        return "Ujian {" +
               "NIM Mahasiswa='" + nimMahasiswa + '\'' +
               ", Tipe Ujian='" + tipeUjian + '\'' +
               ", Skor=" + skor +
               ", Lulus=" + lulus +
               '}';
    }
}