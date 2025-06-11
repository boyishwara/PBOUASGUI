package model;

public class Dokumen {
    private static int nextId = 1;
    private int id;
    private String nimMahasiswa; // Untuk menghubungkan dengan mahasiswa
    private String namaDokumen; // Misal: KTM, Sertifikat
    private String pathFile; // Simulasi path file

    public Dokumen(String nimMahasiswa, String namaDokumen, String pathFile) {
        this.id = nextId++;
        this.nimMahasiswa = nimMahasiswa;
        this.namaDokumen = namaDokumen;
        this.pathFile = pathFile;
    }

    // Getters
    public int getId() { return id; }
    public String getNimMahasiswa() { return nimMahasiswa; }
    public String getNamaDokumen() { return namaDokumen; }
    public String getPathFile() { return pathFile; }

    // Setters
    public void setNamaDokumen(String namaDokumen) { this.namaDokumen = namaDokumen; }
    public void setPathFile(String pathFile) { this.pathFile = pathFile; }

    @Override
    public String toString() {
        return "Dokumen {" +
            "ID=" + id +
            ", NIM Mahasiswa='" + nimMahasiswa + '\'' +
            ", Nama Dokumen='" + namaDokumen + '\'' +
            ", Path File='" + pathFile + '\'' +
            '}';
    }
}