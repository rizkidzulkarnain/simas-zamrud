package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataPenjualanItemClass implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("produk")
    @Expose
    private String produk;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("isSudahBayar")
    @Expose
    private String isSudahBayar;
    @SerializedName("tgl_input")
    @Expose
    private String tglInput;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getIsSudahBayar() {
        return isSudahBayar;
    }

    public void setIsSudahBayar(String isSudahBayar) {
        this.isSudahBayar = isSudahBayar;
    }

    public String getTglInput() {
        return tglInput;
    }

    public void setTglInput(String tglInput) {
        this.tglInput = tglInput;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}