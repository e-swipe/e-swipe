package com.e_swipe.e_swipe.model;

import java.util.List;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class Event {

    int participants;
    position position;
    List<Interest> interests;
    String date_end;
    String name;
    String date_begin;
    List<Image> images;
    boolean participating;
    String uuid;
    String description;

    public Event(int participants, Event.position position, List<Interest> interests,
                 String date_end, String name, String date_begin, List<Image> images,
                 boolean participating, String uuid, String description) {

        this.participants = participants;
        this.position = position;
        this.interests = interests;
        this.date_end = date_end;
        this.name = name;
        this.date_begin = date_begin;
        this.images = images;
        this.participating = participating;
        this.uuid = uuid;
        this.description = description;
    }

    public int getParticipants() {
        return participants;
    }

    public Event.position getPosition() {
        return position;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public String getDate_end() {
        return date_end;
    }

    public String getName() {
        return name;
    }

    public String getDate_begin() {
        return date_begin;
    }

    public List<Image> getImages() {
        return images;
    }

    public boolean isParticipating() {
        return participating;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDescription() {
        return description;
    }

    public class position {
        int latitude;
        int longitude;

        position(int latitude,int longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
