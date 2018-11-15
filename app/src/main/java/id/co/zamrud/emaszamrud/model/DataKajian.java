package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKajian {
    @SerializedName("id_kajian")
    @Expose
    private String idKajian;
    @SerializedName("penceramah")
    @Expose
    private String penceramah;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("tempat")
    @Expose
    private String tempat;
    @SerializedName("kitab")
    @Expose
    private String kitab;
    @SerializedName("tema")
    @Expose
    private String tema;
    private int fotoPenceramah;
    private int colorBG;

    public DataKajian(int fotoPenceramah, String penceramah, String kitab, String tema, String waktu, String tempat, int colorBG) {
        this.fotoPenceramah = fotoPenceramah;
        this.penceramah = penceramah;
        this.kitab = kitab;
        this.tema = tema;
        this.waktu = waktu;
        this.tempat = tempat;
        this.colorBG = colorBG;
    }

    public String getIdKajian() {
        return idKajian;
    }

    public void setIdKajian(String idKajian) {
        this.idKajian = idKajian;
    }

    public String getPenceramah() {
        return penceramah;
    }

    public void setPenceramah(String penceramah) {
        this.penceramah = penceramah;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getKitab() {
        return kitab;
    }

    public void setKitab(String kitab) {
        this.kitab = kitab;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public int getFotoPenceramah() {
        return fotoPenceramah;
    }

    public void setFotoPenceramah(int fotoPenceramah) {
        this.fotoPenceramah = fotoPenceramah;
    }

    public int getColorBG() {
        return colorBG;
    }

    public void setColorBG(int colorBG) {
        this.colorBG = colorBG;
    }
}
