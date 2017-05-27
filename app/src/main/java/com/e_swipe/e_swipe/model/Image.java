package com.e_swipe.e_swipe.model;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class Image {

    String uuid;
    String url;
    String order;

    public Image(String uuid, String url, String order){
        this.uuid = uuid;
        this.url = url;
        this.order = order;
    }

    public String getUuid(){
        return uuid;
    }

    public String getUrl(){
        return url;
    }

    public String getOrder(){
        return order;
    }
}
