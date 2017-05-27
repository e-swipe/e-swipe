package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.Image;

import java.util.ArrayList;

/**
 * Created by Anthonny on 07/05/2017.
 */
public class ImageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<Image> images;

    public ImageAdapter(Context context, ArrayList<Image> images){
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_pager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_pager);
        Glide.with(mContext).load(images.get(position)).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
