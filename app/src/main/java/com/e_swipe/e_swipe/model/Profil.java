package com.e_swipe.e_swipe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthonny on 01/05/2017.
 */
public class Profil implements Serializable {

    String uuid;
    String first_name;
    String last_name;
    String date_of_birth;
    String description;
    String picture_url;
    String gender;

    List<String> looking_for;
    int looking_for_age_min;
    int looking_for_age_max;
    boolean is_visible;
    position position;
    ArrayList<Image> pictures;
    ArrayList<EventCard> events;

    public Profil(String uuid, String first_name, String last_name,
                  String date_of_birth, String description,
                  String picture_url, String gender, ArrayList<String> looking_for,
                  int looking_for_age_min, int looking_for_age_max,
                  boolean isVisible, position position, ArrayList<Image> pictures, ArrayList<EventCard> eventCards){

        this.uuid = uuid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.description = description;
        this.picture_url = picture_url;
        this.gender = gender;
        this.looking_for = looking_for;
        this.looking_for_age_min = looking_for_age_min;
        this.looking_for_age_max = looking_for_age_max;
        this.is_visible = isVisible;
        this.position = position;
        this.pictures = pictures;
        this.events = eventCards;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(List<String> looking_for) {
        this.looking_for = looking_for;
    }

    public int getLooking_for_age_min() {
        return looking_for_age_min;
    }

    public void setLooking_for_age_min(int looking_for_age_min) {
        this.looking_for_age_min = looking_for_age_min;
    }

    public int getLooking_for_age_max() {
        return looking_for_age_max;
    }

    public void setLooking_for_age_max(int looking_for_age_max) {
        this.looking_for_age_max = looking_for_age_max;
    }

    public boolean isVisible() {
        return is_visible;
    }

    public void setVisible(boolean visible) {
        is_visible = visible;
    }

    public Profil.position getPosition() {
        return position;
    }

    public void setPosition(Profil.position position) {
        this.position = position;
    }


    public ArrayList<Image> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Image> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<EventCard> getEventCards() {
        return events;
    }

    public void setEventCards(ArrayList<EventCard> eventCards) {
        this.events = eventCards;
    }


    /*public Profil(Parcel in){

        profilesPicsUrls = new ArrayList<>();
        String[] data = new String[4];
        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.userId = data[0];
        this.name = data[1];
        this.surname = data[2];
        this.birthDay = data[3];
        in.readStringList(profilesPicsUrls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {userId,name,surname,birthDay});
        parcel.writeStringList(profilesPicsUrls);
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getBirthday(){
        return birthDay;
    }

    public ArrayList<String> getProfilesPicsUrls(){ return profilesPicsUrls;}


    public static final Creator<Profil> CREATOR = new Creator<Profil>() {
        @Override
        public Profil createFromParcel(Parcel in) {
            return new Profil(in);
        }

        @Override
        public Profil[] newArray(int size) {
            return new Profil[size];
        }
    };*/

    public class position {
        float latitude;
        float longitude;

        position(float latitude,float longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

}
