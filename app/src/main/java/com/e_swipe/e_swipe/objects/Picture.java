package com.e_swipe.e_swipe.objects;

import android.graphics.Bitmap;

/**
 * Created by Anthonny on 25/05/2017.
 */
public class Picture {

    Bitmap bitmap;
    boolean isSelected;

    public Picture(Bitmap bitmap, boolean isSelected){
        this.bitmap = bitmap;
        this.isSelected = isSelected;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    public Bitmap getImage(){
        return bitmap;
    }
}
