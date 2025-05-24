package model;

public class Koordinator {
    private String id;
    private String nama;
    private String password;

    public Koordinator(String id, String nama, String password) {
        this.id = id;
        this.nama = nama;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }
}