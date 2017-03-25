package com.e_swipe.e_swipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set Theme to AppTheme after SplashScreen
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "Refreshed token: " + refreshedToken);

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
