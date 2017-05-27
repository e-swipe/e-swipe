package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 27/05/2017.
 */

public class Interest {

    String uuid;
    String name;

    public Interest(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
