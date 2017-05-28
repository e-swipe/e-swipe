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
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.PictureSelectionAdapter;
import com.e_swipe.e_swipe.model.Image;
import com.e_swipe.e_swipe.server.Profil.ProfilServer;
import com.e_swipe.e_swipe.utils.ResponseCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

        positionsToSwap = new ArrayList<>(2);
        imageUrl = new ArrayList<>();

        //Get Images from server
        gridView = (GridView) findViewById(R.id.grid_pictures);
        final ArrayList<Image> pictures = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.com_facebook_button_icon_blue);
        Bitmap test = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_accept);

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
                    Image picture = pictures.get(positionsToSwap.get(0)); //1,2,3,4
                    Image firstPicture = pictures.get(0); // 0

                    pictures.set(0, picture);
                    pictures.set(positionsToSwap.get(0), firstPicture);

                    pictures.get(0).setOrder(picture.getOrder());
                    pictures.get(positionsToSwap.get(0)).setOrder(firstPicture.getOrder());
                    positionsToSwap.clear();

                    Log.d("Debug", String.valueOf(positionsToSwap) + "2");
                    pictureSelectionAdapter.setPictures(pictures);

                    pictureSelectionAdapter.notifyDataSetChanged();
                } else if (positionsToSwap.size() == 2) {

                    Image picture = pictures.get(positionsToSwap.get(1)); //1,2,3,4
                    Image firstPicture = pictures.get(positionsToSwap.get(0)); // 0

                    pictures.set(positionsToSwap.get(1), firstPicture);
                    pictures.set(positionsToSwap.get(0), picture);

                    pictures.get(positionsToSwap.get(1)).setOrder(firstPicture.getOrder());
                    pictures.get(positionsToSwap.get(0)).setOrder(picture.getOrder());

                    positionsToSwap.clear();

                    pictureSelectionAdapter.setPictures(pictures);
                    Log.d("Debug", String.valueOf(positionsToSwap));
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