package com.e_swipe.e_swipe.objects;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class related to Json loading, it will load profiles from "profiles.json", "events.json"
 */
public class JsonLoader {

    private static final String TAG = "Utils";

    /**
     * Load profiles.json file to extract the profiles
     * @param context Context of the App
     * @return the list of profiles in profiles.json
     */
    public static List<Profile> loadProfiles(Context context){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "profiles.json"));
            List<Profile> profileList = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                Profile profile = gson.fromJson(array.getString(i), Profile.class);
                profileList.add(profile);
            }
            return profileList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Load events.json file to extract the profiles
     * @param context Context of the App
     * @return the list of events in events.json
     */
    public static List<Event> loadEvents(Context context){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "events.json"));
            List<Event> eventList = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                Event event = gson.fromJson(array.getString(i), Event.class);
                eventList.add(event);
            }
            return eventList;
        }catch (Exception e){
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
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+jsonFileName);
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
}
