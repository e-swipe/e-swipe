package com.e_swipe.e_swipe.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class Image {

    @Expose String uuid;
    @Expose String url;
    @Expose String order;
    Bitmap bitmap;
    boolean isSelected;

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

    public void setOrder(String order){
        this.order = order;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }
}
