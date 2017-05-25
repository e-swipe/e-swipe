package com.e_swipe.e_swipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.e_swipe.e_swipe.adapter.PictureSelectionAdapter;
import com.e_swipe.e_swipe.objects.Picture;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    //TODO : need to add listener to selection image

    ImageButton swapButton;
    GridView gridView;
    int [] positionsToSwap;
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

        positionsToSwap = new int[2];
        imageUrl = new ArrayList<>();

        //Get Images from server
        gridView = (GridView) findViewById(R.id.grid_pictures);
        ArrayList<Picture> pictures = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.com_facebook_button_icon_blue);
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));
        pictures.add(new Picture(icon,false));


        gridView.setAdapter(new PictureSelectionAdapter(getApplicationContext(),pictures, new PictureSelectionAdapter.PictureSelection() {
            @Override
            public void pictureSelected(Bitmap bitmap, int position) {

            }

            @Override
            public void pictureUnselected(Bitmap bitmap, int position) {

            }
        })); //Create new adapter on selection

        swapButton = (ImageButton) findViewById(R.id.swap_pictures);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
