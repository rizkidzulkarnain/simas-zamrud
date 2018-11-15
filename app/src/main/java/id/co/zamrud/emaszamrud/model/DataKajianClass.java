package id.co.zamrud.emaszamrud.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKajianClass {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data_kajian")
    @Expose
    private List<DataKajian> dataKajian = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DataKajian> getDataKajian() {
        return dataKajian;
    }

    public void setDataKajian(List<DataKajian> dataKajian) {
        this.dataKajian = dataKajian;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
