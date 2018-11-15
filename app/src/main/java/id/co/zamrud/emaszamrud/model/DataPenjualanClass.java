package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataPenjualanClass {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data_penjualan")
    @Expose
    private List<DataPenjualanItemClass> dataPenjualan = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DataPenjualanItemClass> getDataPenjualan() {
        return dataPenjualan;
    }

    public void setDataPenjualan(List<DataPenjualanItemClass> dataPenjualan) {
        this.dataPenjualan = dataPenjualan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}