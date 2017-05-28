package com.e_swipe.e_swipe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.model.Image;

import java.util.ArrayList;

/**
 * Created by Anthonny on 25/05/2017.
 */
public class PictureSelectionAdapter extends BaseAdapter{

    private static LayoutInflater inflater = null;
    ArrayList<Image> pictures;
    Context context;
    PictureSelection pictureSelection = null;

    public interface PictureSelection{
        public void pictureSelected(Bitmap bitmap, int position);
        public void pictureUnselected(Bitmap bitmap, int position);
    }

    public PictureSelectionAdapter(Context context, ArrayList<Image> pictures, PictureSelection pictureSelection){
        Log.d("TAG","getView");
        this.pictures = pictures;
        this.context = context;
        this.pictureSelection = pictureSelection;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public Object getItem(int i) {
        return pictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("TAG","getView");
        final Image picture = (Image) getItem(i);
        final int position = i;
        View vi = view;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        vi = inflater.inflate(R.layout.row_grid_small_image, null);
        ImageView imageView = (ImageView)vi.findViewById(R.id.small_image);
        imageView.setImageBitmap(picture.getBitmap());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picture.isSelected()){
                    pictureSelection.pictureUnselected(picture.getBitmap(),position);
                    picture.setSelected(false);
                }
                else {
                    pictureSelection.pictureSelected(picture.getBitmap(),position);
                }
            }
        });

        return vi;

    }

    public void setPictures(ArrayList<Image> pictures){
        this.pictures = pictures;
    }
}
