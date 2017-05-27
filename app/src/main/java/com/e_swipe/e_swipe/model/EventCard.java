package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class EventCard {

    String uuid;
    String name;
    String picture_url;
    position position;

    EventCard(String uuid, String name, String picture_url, position position){
        this.uuid = uuid;
        this.name = name;
        this.picture_url = picture_url;
        this.position = position;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public EventCard.position getPosition() {
        return position;
    }

    public void setPosition(EventCard.position position) {
        this.position = position;
    }

    public class position{
        int latitude;
        int longitude;

        position(int latitude, int longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
