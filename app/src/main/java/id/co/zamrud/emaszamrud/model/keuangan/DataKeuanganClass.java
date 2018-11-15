package id.co.zamrud.emaszamrud.model.keuangan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataKeuanganClass {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data_keuangan")
    @Expose
    private List<DataKeuanganItemClass> dataKeuangan = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DataKeuanganItemClass> getDataKeuangan() {
        return dataKeuangan;
    }

    public void setDataKeuangan(List<DataKeuanganItemClass> dataPenjualan) {
        this.dataKeuangan = dataPenjualan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}