package com.e_swipe.e_swipe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.e_swipe.e_swipe.R;
import com.e_swipe.e_swipe.services.gps.LocalisationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


/**
 * Activity related to the SplashScreen
 */
public class SplashActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Animation anim;
    ImageView imageView;
    //Needs for alertDialog (GPS)
    private GoogleApiClient googleApiClient;

    //Check permissions
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG = "Debug";
    /**
     * onCreate Method that call LoginActivity when the app is fully charged
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initGoogleApi();
        imageView=(ImageView)findViewById(R.id.imageView2); //declaring imageView to show animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); //creating animation
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        requestPermissions();
        startService(new Intent(getApplicationContext(), LocalisationListener.class));
    }

    private void initGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SplashActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
        else {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }

    private void requestPermissions(){

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {
            return;
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSIONS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult");
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //If gps is enabled start animation and go to the next activity
                        imageView.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                        getString(R.string.user_file_key), Context.MODE_PRIVATE);
                                if(!sharedPref.getString("auth","").equals("")){
                                    startActivity(new Intent(getApplicationContext(),TabbedActivity.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d(TAG,"resultCancelled");
                        finish();
                        break;
                    default:
                        Log.d(TAG,"default");
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnexionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //On Connection failed
        Log.d(TAG,"onConnexionFailed");
    }
}