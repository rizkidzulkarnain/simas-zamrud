package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataLiqo implements Serializable {
    @SerializedName("id_liqo")
    @Expose
    private String id_liqo;
    @SerializedName("ayat")
    @Expose
    private String ayat;
    @SerializedName("surat")
    @Expose
    private String surat;
    @SerializedName("nama_surat")
    @Expose
    private String nama_surat;
    @SerializedName("juz")
    @Expose
    private String juz;
    @SerializedName("tema")
    @Expose
    private String tema;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("saldoAwal")
    @Expose
    private String saldoAwal;
    @SerializedName("pemasukan")
    @Expose
    private String pemasukan;
    @SerializedName("pj")
    @Expose
    private String pj;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public String getId_liqo() {
        return id_liqo;
    }

    public void setId_liqo(String id_liqo) {
        this.id_liqo = id_liqo;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public String getSurat() {
        return surat;
    }

    public void setSurat(String surat) {
        this.surat = surat;
    }

    public String getNama_surat() {
        return nama_surat;
    }

    public void setNama_surat(String nama_surat) {
        this.nama_surat = nama_surat;
    }

    public String getJuz() {
        return juz;
    }

    public void setJuz(String juz) {
        this.juz = juz;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getSaldoAwal() {
        return saldoAwal;
    }

    public void setSaldoAwal(String saldoAwal) {
        this.saldoAwal = saldoAwal;
    }

    public String getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(String pemasukan) {
        this.pemasukan = pemasukan;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
