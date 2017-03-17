package com.e_swipe.e_swipe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set Theme to AppTheme after SplashScreen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isAuth()){

        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }

    /**
     *
     * @return true if user is authentified by Mail/Pass , FB , Twitter
     */
    public boolean isAuth(){
        return false;
    }
}
