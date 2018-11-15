package id.co.zamrud.emaszamrud.model.keuangan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataKeuanganItemClass implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String produk) {
        this.jenis = produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}