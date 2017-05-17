package com.e_swipe.e_swipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.e_swipe.e_swipe.adapter.ImageAdapter;
import com.e_swipe.e_swipe.objects.Profil;

public class UserProfileActivity extends Activity {

    ViewPager viewPager;
    ImageAdapter imageAdapter;
    Profil profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        Intent intent = getIntent();
        profil = intent.getExtras().getParcelable("profil");

        imageAdapter = new ImageAdapter(getApplicationContext(),profil.getProfilesPicsUrls());
        Log.d("Debug", imageAdapter.toString());
        viewPager = (ViewPager) findViewById(R.id.image_view_pager);
        viewPager.setAdapter(imageAdapter);
    }
}
