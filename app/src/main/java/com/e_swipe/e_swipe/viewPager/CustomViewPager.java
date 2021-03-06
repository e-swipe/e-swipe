package com.e_swipe.e_swipe.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.e_swipe.e_swipe.layout.TinderCard;

/**
 * Created by Anthonny on 30/04/2017.
 */
public class CustomViewPager extends ViewPager{

    private boolean swipeable;

    CustomViewPager(Context context){
        super(context);
        swipeable = true;
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }


    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(swipeable){
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setSwipeable(boolean swipeable){
        this.swipeable = swipeable;
    }
}
