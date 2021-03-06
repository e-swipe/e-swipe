package com.e_swipe.e_swipe.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.e_swipe.e_swipe.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventProfilActivity extends AppCompatActivity {

    private GoogleMap mMap;
    TextView nameEvent;
    TextView date;
    TextView heure;
    TextView nbParticipants;
    Button participateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        nameEvent = (TextView) findViewById(R.id.textViewNameEvent);
        date = (TextView) findViewById(R.id.textView_date);
        heure = (TextView) findViewById(R.id.textView_heure);
        nbParticipants = (TextView) findViewById(R.id.textView_nb);
        participateButton = (Button) findViewById(R.id.joinInOut);
    }
}
