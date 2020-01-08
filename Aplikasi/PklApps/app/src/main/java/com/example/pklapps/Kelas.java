package com.example.pklapps;

import java.io.Serializable;

class Kelas {
    private int id;
    private String nama, tanggal,jamMulai,jamAkhir,nim,stat,keterangan;

    public Kelas(int id, String nama, String tanggal, String jamMulai, String jamAkhir,String nim,String stat,String keterangan) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamAkhir = jamAkhir;
        this.nim = nim;
        this.stat = stat;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getjamMulai() {
        return jamMulai;
    }

    public String getJamAkhir() {
        return jamAkhir;
    }

    public String getNim() {
        return nim;
    }

    public String getStat() {
        return stat;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
