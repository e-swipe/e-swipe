package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class UserCard {

    String uuid;
    String first_name;
    String last_name;
    String date_og_birth;
    String picture_url;
    position position;

    public UserCard(String uuid, String first_name, String last_name, String date_og_birth,
                    String picture_url, UserCard.position position) {

        this.uuid = uuid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_og_birth = date_og_birth;
        this.picture_url = picture_url;
        this.position = position;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDate_og_birth() {
        return date_og_birth;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public UserCard.position getPosition() {
        return position;
    }

    public class position {
        float latitude;
        float longitude;

        position(float latitude,float longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

}
