package com.example.pklapps;

public class User {

    private String nim , nama, fakultas, prodi;

    public User(String nim, String nama, String fakultas, String prodi) {
        this.nim = nim;
        this.nama = nama;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getFakultas() {
        return fakultas;
    }

    public String getProdi() {
        return prodi;
    }
}
