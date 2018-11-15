package id.co.zamrud.emaszamrud.model.kota;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKotaParentClass {
    @SerializedName("rajaongkir")
    @Expose
    private DataKotaStatusClass rajaongkir;

    public DataKotaStatusClass getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(DataKotaStatusClass rajaongkir) {
        this.rajaongkir = rajaongkir;
    }
}
