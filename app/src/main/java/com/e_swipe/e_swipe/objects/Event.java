package com.e_swipe.e_swipe.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class related to Events, expose attributs to gson Parsing
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

    /**
     *Getters And Setters
     */
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
