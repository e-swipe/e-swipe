package com.e_swipe.e_swipe.server.event;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anthonny on 26/05/2017.
 */

public class EventServer {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String BASE_URL =  "api.stardis.blue";

    public static void getAllEvents(String auth, double latitude, double longitude, int radius,
                                    int offset, int limit, Callback callback) throws IOException, JSONException {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("events")
                .addQueryParameter("latitude",Double.toString(latitude))
                .addQueryParameter("longitude",Double.toString(longitude))
                .addQueryParameter("radius",Integer.toString(radius))
                .addQueryParameter("offset",Integer.toString(offset))
                .addQueryParameter("limit",Integer.toString(limit))
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void getEventInfo(String uuid, String auth, Callback callback){

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegment("v1")
                .addPathSegment("events")
                .addPathSegment(uuid)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("auth",auth)
                .url(httpUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }
}
