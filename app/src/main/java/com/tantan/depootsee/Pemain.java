package com.tantan.depootsee;

public class Pemain {
    private String nama;
    private String nomor;

    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public String getnomor() {
        return nomor;
    }

    public void setnomor(String nomor) {
        this.nomor = nomor;
    }

    @Override
    public String toString() {
        return this.nama;
    }

}
