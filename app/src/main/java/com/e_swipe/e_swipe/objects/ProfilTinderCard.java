package com.e_swipe.e_swipe.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class related to ProfilTinderCard in tinderCard, expose attributs to gson Parsing
 */
public class ProfilTinderCard {

    @SerializedName("first_name")
    @Expose
    private String name;

    @SerializedName("picture_url")
    @Expose
    private String imageUrl;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    /**
     *Getters And Setters
     */
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}