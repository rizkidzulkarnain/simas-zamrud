package id.co.zamrud.emaszamrud.model.shalat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataShalatWeather{

    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("temperature")
    @Expose
    private String temperature;

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}