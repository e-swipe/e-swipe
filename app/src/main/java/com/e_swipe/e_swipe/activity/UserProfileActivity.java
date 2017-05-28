package com.e_swipe.e_swipe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.ImageAdapter;
import com.e_swipe.e_swipe.layout.FlowLayout;
import com.e_swipe.e_swipe.model.EventCard;
import com.e_swipe.e_swipe.model.Image;
import com.e_swipe.e_swipe.model.Profil;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserProfileActivity extends Activity {

    ViewPager viewPager;
    ImageAdapter imageAdapter;
    Profil profil;
    ArrayList<Image> images;
    FlowLayout flowLayout;
    //Get User profil from Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        images = new ArrayList<>();
        flowLayout = (FlowLayout) findViewById(R.id.flow_container);


        for (int i=0;i<15;i++){
            flowLayout.addView(inflateTextview("TEST : " + i),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        try {
            getProfil();
            //getPhotos();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageAdapter = new ImageAdapter(getApplicationContext(),images);
        Log.d("Debug", imageAdapter.toString());

        viewPager = (ViewPager) findViewById(R.id.image_view_pager);
        viewPager.setAdapter(imageAdapter);
    }

    public void getProfil() throws IOException {
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        ProfilServer.getProfil(sharedPref.getString("auth", ""), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                profil = new Gson().fromJson(response.body().toString(),Profil.class);
                for (EventCard eventCard : profil.getEventCards()){
                    flowLayout.addView(inflateTextview(eventCard.getName()),
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        });
    }
    public void getPhotos() throws IOException {

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        ProfilServer.getUserPhotos(sharedPref.getString("auth",""), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Image [] imageList = new Gson().fromJson(response.body().toString(),Image[].class);
                images = new ArrayList<>(Arrays.asList(imageList));
                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(),images);
                viewPager.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }

    public LinearLayout inflateTextview(String eventName){
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.textview_custom, null);
        TextView tv = (TextView) linearLayout.findViewById(R.id.textview_event);
        tv.setText(eventName);
        return linearLayout;
    }
}
