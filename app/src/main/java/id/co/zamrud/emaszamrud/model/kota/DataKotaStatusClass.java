package id.co.zamrud.emaszamrud.model.kota;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKotaStatusClass {

    @SerializedName("query")
    @Expose
    private List<Object> query = null;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("results")
    @Expose
    private List<DataKotaItemClass> results = null;

    public List<Object> getQuery() {
        return query;
    }

    public void setQuery(List<Object> query) {
        this.query = query;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<DataKotaItemClass> getResults() {
        return results;
    }

    public void setResults(List<DataKotaItemClass> results) {
        this.results = results;
    }

}