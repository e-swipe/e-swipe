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
import com.e_swipe.e_swipe.utils.DateUtils;
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
    TextView description;
    TextView nameAge;
    //Get User profil from Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        images = new ArrayList<>();
        flowLayout = (FlowLayout) findViewById(R.id.flow_container);

        description = (TextView) findViewById(R.id.textViewDescribing);
        /*for (int i=0;i<15;i++){
            flowLayout.addView(inflateTextview("TEST : " + i),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }*/
        nameAge = (TextView) findViewById(R.id.textView_user);

        getProfil();
        //getPhotos();
        Image image = new Image("1","http://i.imgur.com/hES7D98.jpg","1");
        Image image2 = new Image("2","http://i.imgur.com/giL1wvz.jpg","2");
        Image image3 = new Image("3","http://i.imgur.com/9ULpnjR.jpg","3");

        images.add(image);
        images.add(image2);
        images.add(image3);

        imageAdapter = new ImageAdapter(getApplicationContext(),images);
        Log.d("Debug", imageAdapter.toString());

        viewPager = (ViewPager) findViewById(R.id.image_view_pager);
        viewPager.setAdapter(imageAdapter);
    }

    public void getProfil(){
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        Log.d("me", "getProfil");
        try {
            ProfilServer.getProfil(sharedPref.getString("auth", ""), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final Response newResponse = response;
                    UserProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String body = null;
                            try {
                                body = newResponse.body().string();
                                Log.d("me", "Code : " + String.valueOf(newResponse.code()));
                                profil = new Gson().fromJson(body,Profil.class);
                                description.setText(profil.getDescription());
                                Log.d("me",profil.getAge());
                                Log.d("me", String.valueOf(profil.getEventCards()));
                                nameAge.setText(profil.getFirst_name()+","+ profil.getAge());
                                for (EventCard eventCard : profil.getEventCards()){
                                    flowLayout.addView(inflateTextview(eventCard.getName()),
                                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("me",body);
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getPhotos() throws IOException {

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);

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
