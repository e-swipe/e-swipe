package com.e_swipe.e_swipe.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.adapter.PictureSelectionAdapter;
import com.e_swipe.e_swipe.objects.Picture;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    //TODO : need to add listener to selection image

    ImageView swapButton;
    GridView gridView;
    ArrayList<Integer> positionsToSwap;
    ArrayList<String> imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        positionsToSwap = new ArrayList<>(2);
        imageUrl = new ArrayList<>();

        //Get Images from server
        gridView = (GridView) findViewById(R.id.grid_pictures);
        final ArrayList<Picture> pictures = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.com_facebook_button_icon_blue);
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));

        final PictureSelectionAdapter pictureSelectionAdapter = new PictureSelectionAdapter(getApplicationContext(),pictures, new PictureSelectionAdapter.PictureSelection() {
            @Override
            public void pictureSelected(Bitmap bitmap, int position) {
                if(positionsToSwap.size() < 2){
                    positionsToSwap.add(position);
                }
                else if(positionsToSwap.size() == 2) positionsToSwap.set(0,position);
            }

            @Override
            public void pictureUnselected(Bitmap bitmap, int position) {
                positionsToSwap.remove(position);
            }
        });

        gridView.setAdapter(pictureSelectionAdapter);

        swapButton = (ImageView) findViewById(R.id.swap_pictures);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(positionsToSwap.size() == 1){
                    Picture picture = pictures.get(positionsToSwap.get(0)); //1,2,3,4
                    Picture firstPicture = pictures.get(0); // 0

                    pictures.set(0,picture);
                    pictures.set(positionsToSwap.get(0),firstPicture);

                    pictureSelectionAdapter.notifyDataSetChanged();
                }
                else if(positionsToSwap.size() == 2){

                    Picture picture = pictures.get(positionsToSwap.get(1)); //1,2,3,4
                    Picture firstPicture = pictures.get(positionsToSwap.get(0)); // 0

                    pictures.set(positionsToSwap.get(1),firstPicture);
                    pictures.set(positionsToSwap.get(0),picture);

                    pictureSelectionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Needed for initialisation of gridview subviews
     */
    public class DownloadPicturesUrl extends AsyncTask<Void,Void,ArrayList<String>>{

        public DownloadPicturesUrl(){

        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            //Get ImagesUrl from server in order / Sort if not
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            super.onPostExecute(arrayList);
            //Notify adapter data change
        }
    }
}
