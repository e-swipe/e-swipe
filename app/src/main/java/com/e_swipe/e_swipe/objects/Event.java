package com.e_swipe.e_swipe.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthonny on 25/03/2017.
 */
public class Event {

    @SerializedName("nameEvent")
    @Expose
    private String nameEvent;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("location")
    @Expose
    private String location;

    public String getName() {
        return nameEvent;
    }

    public void setName(String name) {
        this.nameEvent = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
