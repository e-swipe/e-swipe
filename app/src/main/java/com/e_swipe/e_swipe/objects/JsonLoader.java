package com.e_swipe.e_swipe.objects;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

/**
 * Class related to Json loading, it will load profiles from "profiles.json", "events.json"
 */
public class JsonLoader {

    private static final String TAG = "Utils";
    static LocationManager locationManager;

    /**
     * Load profiles.json file to extract the profiles
     * @param context Context of the App
     * @return the list of profiles in profiles.json
     */
    public static List<ProfilTinderCard> loadProfiles(Context context) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "profiles.json"));
            List<ProfilTinderCard> profilTinderCardList = new ArrayList<>();
            Log.d("Array", String.valueOf(array.length()));
            for (int i = 0; i < array.length(); i++) {
                ProfilTinderCard profilTinderCard = gson.fromJson(array.getString(i), ProfilTinderCard.class);
                profilTinderCardList.add(profilTinderCard);
            }
            return profilTinderCardList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load events.json file to extract the profiles
     * @param context Context of the App
     * @return the list of events in events.json
     */
    public static List<Event> loadEvents(Context context) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "events.json"));
            List<Event> eventList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Event event = gson.fromJson(array.getString(i), Event.class);
                eventList.add(event);
            }
            return eventList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read in assets folder the file at jsonFileName and return the array of objects related
     * @param context Application Context
     * @param jsonFileName the name if the file to read
     * @return String that represent the JsonArray
     */
    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is = null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG, "path " + jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    /**
     * Load profiles.json file to extract the profiles
     * @param context Context of the App
     * @return the list of profiles in profiles.json
     */
    public static List<ProfilTinderCard> loadProfilesFromFile(Context context) {
        locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromFile(context, "profiles.json"));
            List<ProfilTinderCard> profilTinderCardList = new ArrayList<>();
            Log.d("Array", String.valueOf(array.length()));
            for (int i = 0; i < array.length(); i++) {
                Log.d("Array", array.getString(i));
                JSONObject jsonObject = new JSONObject(array.getString(i));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location locationEvent = new Location("pointUser");

                    JSONObject position = jsonObject.getJSONObject("position");
                    Log.d("ProfilTinderCard", "TinderCard " + position.toString());
                    locationEvent.setLatitude((Double) position.get("latitude"));
                    locationEvent.setLongitude((Double) position.get("longitude"));

                    Log.d("ProfilTinderCard","Me" + location.toString());
                    float meters = location.distanceTo(locationEvent);
                    Log.d("ProfilTinderCard", String.valueOf(meters));
                    int distance = (int) Math.ceil(meters/1000);
                    jsonObject.put("location",distance +"km");
                    Log.d("ProfilTinderCard", String.valueOf(jsonObject.get("location")));
                    ProfilTinderCard profilTinderCard = gson.fromJson(jsonObject.toString(), ProfilTinderCard.class);
                    Log.d("ProfilTinderCard",profilTinderCard.toString());
                    profilTinderCardList.add(profilTinderCard);
                }
            }
            Log.d("ProfilTinderCard", String.valueOf(profilTinderCardList.size()));
            return profilTinderCardList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String loadJSONFromFile(Context context, String fileName){

        String json = null;
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream in = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        json = sb.toString();
        return json;
    }
}
