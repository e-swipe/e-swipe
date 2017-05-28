package com.e_swipe.e_swipe.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.PictureSelectionAdapter;
import com.e_swipe.e_swipe.model.Image;
import com.e_swipe.e_swipe.model.UserPatch;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.server.login.LoginServer;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.android.gms.nearby.messages.EddystoneUid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {

    //TODO : need to add listener to selection image

    ImageView swapButton;
    GridView gridView;
    ArrayList<Integer> positionsToSwap;
    ArrayList<String> imageUrl;
    PictureSelectionAdapter pictureSelectionAdapter;
    Switch gender;
    EditText description;
    ArrayList<Image> pictures;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        gender = (Switch) findViewById(R.id.switchSex);
        description = (EditText) findViewById(R.id.edit_description);
        positionsToSwap = new ArrayList<>(2);
        imageUrl = new ArrayList<>();

        //Get Images from server
        gridView = (GridView) findViewById(R.id.grid_pictures);
        pictures = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.com_facebook_button_icon_blue);
        Bitmap test = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_accept);

        final Image image = new Image("1","http://cdn.cavemancircus.com//wp-content/uploads/images/2015/march/pretty_girls_2/pretty_girls_12.jpg","1");
        Image image2 = new Image("2","http://cdn.cavemancircus.com//wp-content/uploads/images/2015/june/pretty_girls_3/pretty_girls_20.jpg","2");
        Image image3 = new Image("3","http://i.imgur.com/wqsvWT4.jpg","2");

        pictures.add(image);
        pictures.add(image2);
        pictures.add(image3);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.user_file_key), Context.MODE_PRIVATE);
        pictureSelectionAdapter = new PictureSelectionAdapter(getApplicationContext(), pictures, new PictureSelectionAdapter.PictureSelection() {
            @Override
            public void pictureSelected(Bitmap bitmap, int position) {
                    pictures.get(position).setSelected(true);
                    Log.d("PictureSelected", String.valueOf(position));
                    if (positionsToSwap.size() < 2) {
                    positionsToSwap.add(position);
                } else if (positionsToSwap.size() == 2) positionsToSwap.set(0, position);
            }

            @Override
            public void pictureUnselected(Bitmap bitmap, int position) {
                pictures.get(position).setSelected(false);
            }
        });

        gridView.setAdapter(pictureSelectionAdapter);

        swapButton = (ImageView) findViewById(R.id.swap_pictures);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PictureSelection", String.valueOf(positionsToSwap));
                if (positionsToSwap.size() == 1) {
                    /*Image picture = pictures.get(positionsToSwap.get(0)); //1,2,3,4
                    Image firstPicture = pictures.get(0); // 0

                    pictures.set(0, picture);
                    pictures.set(positionsToSwap.get(0), firstPicture);

                    pictures.get(0).setOrder(picture.getOrder());
                    pictures.get(positionsToSwap.get(0)).setOrder(firstPicture.getOrder());
                    positionsToSwap.clear();

                    Log.d("Debug", String.valueOf(positionsToSwap) + "2");
                    pictureSelectionAdapter.setPictures(pictures);
                    Log.d("Pictures", String.valueOf(pictures));
                    gridView.setAdapter(pictureSelectionAdapter);*/
                    Collections.swap(pictures,0,positionsToSwap.get(0));
                    pictures.get(positionsToSwap.get(0)).setOrder("0");
                    pictures.get(0).setOrder(pictures.get(positionsToSwap.get(0)).getOrder());
                    positionsToSwap.clear();
                    pictureSelectionAdapter.notifyDataSetChanged();
                } else if (positionsToSwap.size() == 2) {

                    /*Image picture = pictures.get(positionsToSwap.get(1)); //1,2,3,4
                    Image firstPicture = pictures.get(positionsToSwap.get(0)); // 0

                    pictures.set(positionsToSwap.get(1), firstPicture);
                    pictures.set(positionsToSwap.get(0), picture);

                    pictures.get(positionsToSwap.get(1)).setOrder(firstPicture.getOrder());
                    pictures.get(positionsToSwap.get(0)).setOrder(picture.getOrder());

                    positionsToSwap.clear();

                    //pictureSelectionAdapter.setPictures(pictures);
                    Log.d("Debug", String.valueOf(positionsToSwap));
                    Log.d("Pictures", String.valueOf(pictures));
                    pictures.add(image);
                    //gridView.setAdapter(pictureSelectionAdapter);*/
                    Collections.swap(pictures,positionsToSwap.get(0),positionsToSwap.get(1));
                    pictures.get(positionsToSwap.get(0)).setOrder(pictures.get(positionsToSwap.get(1)).getOrder());
                    pictures.get(positionsToSwap.get(1)).setOrder(pictures.get(positionsToSwap.get(0)).getOrder());
                    positionsToSwap.clear();
                    pictureSelectionAdapter.notifyDataSetChanged();
                }
            }
        });


        try {
            ProfilServer.getUserPhotos(sharedPreferences.getString("auth", ""), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (ResponseCode.checkResponseCode(response.code())) {
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        Image[] images = gson.fromJson(response.body().string(), Image[].class);
                        //TODO
                        for (Image image : images) {
                            DownloadPicturesUrl downloadPicturesUrl = new DownloadPicturesUrl(image);
                            downloadPicturesUrl.execute();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        patch();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                patch();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void patch(){
        UserPatch userPatch = new UserPatch();
        if(gender.isChecked()) userPatch.setGender("male");
        else userPatch.setGender("female");
        userPatch.setDescription(description.getText().toString());
        try {
            ProfilServer.patch(sharedPref.getString("auth", ""), userPatch, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("PATCH", String.valueOf(response.code()));

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Needed for initialisation of gridview subviews
     */
    public class DownloadPicturesUrl extends AsyncTask<Void, Void, Bitmap> {

        Image image;

        public DownloadPicturesUrl(Image image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            //Get ImagesUrl from server in order / Sort if not
            Bitmap bitmap = getBitmapFromURL(image.getUrl());
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //Notify adapter data change
            image.setBitmap(bitmap);
            image.setSelected(false);
            pictures.add(image);
            pictureSelectionAdapter.notifyDataSetChanged();

        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }
    }
}