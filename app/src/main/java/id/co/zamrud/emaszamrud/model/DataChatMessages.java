package id.co.zamrud.emaszamrud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataChatMessages {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("image_url")
    @Expose
    private String imageURL;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("last_update")
    @Expose
    private String lastUpdate;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("rating")
    @Expose
    private int rating;

    public Long getId() {
        return id;
    }

    public void setIdBBM(Long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
