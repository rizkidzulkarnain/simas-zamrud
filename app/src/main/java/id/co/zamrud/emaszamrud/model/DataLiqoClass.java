package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataLiqoClass {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data_liqo")
    @Expose
    private List<DataLiqo> dataLiqoList;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DataLiqo> getDataLiqo() {
        return dataLiqoList;
    }

    public void setDataLiqo(List<DataLiqo> dataLiqo) {
        this.dataLiqoList = dataLiqo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}