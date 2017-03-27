package com.e_swipe.e_swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Activity related to the SplashScreen
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    /**
     * onCreate Method that call LoginActivity when the app is fully charged
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}