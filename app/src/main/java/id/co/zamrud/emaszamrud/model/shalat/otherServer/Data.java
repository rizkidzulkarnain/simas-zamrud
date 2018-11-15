
package id.co.zamrud.emaszamrud.model.shalat.otherServer;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Fajr")
    @Expose
    private String fajr;
    @SerializedName("Sunrise")
    @Expose
    private String sunrise;
    @SerializedName("Dhuhr")
    @Expose
    private String dhuhr;
    @SerializedName("Asr")
    @Expose
    private String asr;
    @SerializedName("Sunset")
    @Expose
    private String sunset;
    @SerializedName("Maghrib")
    @Expose
    private String maghrib;
    @SerializedName("Isha")
    @Expose
    private String isha;
    @SerializedName("SepertigaMalam")
    @Expose
    private String sepertigaMalam;
    @SerializedName("TengahMalam")
    @Expose
    private String tengahMalam;
    @SerializedName("DuapertigaMalam")
    @Expose
    private String duapertigaMalam;
    @SerializedName("method")
    @Expose
    private List<String> method = null;

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        this.dhuhr = dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public String getSepertigaMalam() {
        return sepertigaMalam;
    }

    public void setSepertigaMalam(String sepertigaMalam) {
        this.sepertigaMalam = sepertigaMalam;
    }

    public String getTengahMalam() {
        return tengahMalam;
    }

    public void setTengahMalam(String tengahMalam) {
        this.tengahMalam = tengahMalam;
    }

    public String getDuapertigaMalam() {
        return duapertigaMalam;
    }

    public void setDuapertigaMalam(String duapertigaMalam) {
        this.duapertigaMalam = duapertigaMalam;
    }

    public List<String> getMethod() {
        return method;
    }

    public void setMethod(List<String> method) {
        this.method = method;
    }

}
