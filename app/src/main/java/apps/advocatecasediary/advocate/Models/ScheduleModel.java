package apps.advocatecasediary.advocate.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("image_url")
    @Expose
    private String imageUrl;


}
