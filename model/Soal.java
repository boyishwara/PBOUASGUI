package model;

public class Soal {
    private int idSoal;
    private String pertanyaan;
    private String[] opsiJawaban;
    private int kunciJawabanIndex;
    private String tipeUjian;

    // Konstruktor disesuaikan untuk menerima idSoal dari database
    public Soal(int idSoal, String pertanyaan, String[] opsiJawaban, int kunciJawabanIndex, String tipeUjian) {
        this.idSoal = idSoal;
        this.pertanyaan = pertanyaan;
        this.opsiJawaban = opsiJawaban;
        this.kunciJawabanIndex = kunciJawabanIndex;
        this.tipeUjian = tipeUjian;
    }

    // Getters
    public int getIdSoal() { return idSoal; }
    public String getPertanyaan() { return pertanyaan; }
    public String[] getOpsiJawaban() { return opsiJawaban; }
    public int getKunciJawabanIndex() { return kunciJawabanIndex; }
    public String getTipeUjian() { return tipeUjian; }

    public boolean cekJawaban(int jawabanIndex) {
        return jawabanIndex == kunciJawabanIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID Soal: ").append(idSoal).append("\n");
        sb.append("Pertanyaan: ").append(pertanyaan).append("\n");
        char optionChar = 'A';
        for (String opsi : opsiJawaban) {
            sb.append(optionChar++).append(". ").append(opsi).append("\n");
        }
        return sb.toString();
    }
}
